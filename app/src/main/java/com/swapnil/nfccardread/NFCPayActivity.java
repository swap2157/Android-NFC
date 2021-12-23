package com.swapnil.nfccardread;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.swapnil.creditCardNfcReader.CardNfcAsyncTask;
import com.swapnil.nfccardread.model.AudioModel;
import com.swapnil.nfccardread.model.FilterBody;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class NFCPayActivity extends Activity implements View.OnClickListener, PaymentResultWithDataListener {

    public static final String TAG = NFCPayActivity.class.getSimpleName();

    private TextView tvCardNumber;
    private TextView tvEXPDate;
    private EditText edtAmount;
    private Button btnPay;
    private ImageView mCardLogoIcon, ivBack;
    private String card = "", cardType = "", expiredDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_pay);
        initViews();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            card = extras.getString("card");
            cardType = extras.getString("cardType");
            expiredDate = extras.getString("expiredDate");

            tvCardNumber.setText("" + card);
            tvEXPDate.setText("" + expiredDate);

            parseCardType(cardType);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isValid()){
                    generateOrderId();
                }


            }
        });
    }

    private boolean isValid() {

        if(TextUtils.isEmpty(edtAmount.getText().toString().trim())){
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
            return  false;
        }

        return  true;
    }

    public void startPayment(String amount, String orderId) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = NFCPayActivity.this;
        Log.i(getClass().getName(), "getActivity---");

        final Checkout co = new Checkout();
        co.setKeyID("rzp_test_lJnaDPyXQa7I4f");
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Smart Card reader");
            options.put("description", "App Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            Log.i(getClass().getName(),"total====="+amount);

            double total = Double.parseDouble(amount);
            total = total * 100;

            Log.i(getClass().getName(),"total==after==="+total);

            options.put("amount", amount);
            options.put("theme.color", "#4426A8");
            options.put("order_id", orderId);


            JSONObject preFill = new JSONObject();
            //  preFill.put("email", sharedPreferences.getString(sessionManager.EMAIL,null));
            //  preFill.put("contact", sharedPreferences.getString(sessionManager.PHONE,null));
            preFill.put("email", "mcaswap123@gmail.com");
            preFill.put("contact", "7045085980");
            options.put("prefill", preFill);

            Log.i(getClass().getName(), "error------before-------------");
            co.open(activity, options);

            Log.i(getClass().getName(), "error------after-------------");
        } catch (Exception e) {

            Log.i(getClass().getName(), "open-------------------");

            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }


    private void parseCardType(String cardType) {

        Log.e("CardTyoe", " " + cardType);
        if (cardType.equals(CardNfcAsyncTask.CARD_UNKNOWN)) {
            Toast.makeText(NFCPayActivity.this, getString(R.string.snack_unknown_bank_card), Toast.LENGTH_LONG).show();
        } else if (cardType.equals(CardNfcAsyncTask.CARD_VISA)) {
            mCardLogoIcon.setImageResource(R.mipmap.visa_logo);
        } else if (cardType.equals(CardNfcAsyncTask.CARD_NAB_VISA)) {
            mCardLogoIcon.setImageResource(R.mipmap.visa_logo);
        }
    }

    private void initViews() {


        tvCardNumber = findViewById(R.id.tvCardNumber);
        tvEXPDate = findViewById(R.id.tvEXPDate);
        ivBack = findViewById(R.id.ivBack);
        mCardLogoIcon = findViewById(R.id.ivCardIcon);
        btnPay = findViewById(R.id.btnPay);
        edtAmount = findViewById(R.id.edtAmount);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rlReadNFCTAG:
                intent = new Intent(this, NFCRead.class);
                this.startActivity(intent);
                break;

            case R.id.rlWriteWithNFC:
                intent = new Intent(this, NFCWrite.class);
                this.startActivity(intent);
                break;

            case R.id.rlCreditCard:
                intent = new Intent(this, NFCCardReading.class);
                this.startActivity(intent);
                break;
        }
    }


    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Log.i(getClass().getName(), "onPaymentSuccess-------------------");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.i(getClass().getName(), "onPaymentError-------------------");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void generateOrderId() {
        Log.i(getClass().getName(),"generateOrderId-->");
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://s3.amazon.com/profile-picture/path/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);
        FilterBody filterBody = new FilterBody();
        Double total = Double.parseDouble(edtAmount.getText().toString().trim());
        total = total * 100;
        Log.i(getClass().getName(),"total-generateOrderId->"+total);
        filterBody.setAmount(String.format("%.0f",total));

        filterBody.setCurrency("INR");

        /*final String syr= Base64.getEncoder().encodeToString(
                String.format("%s:%s", "rzp_test_lJnaDPyXQa7I4f","jAkoujlunWk3GAhQFPzx4Dg3")
                        .getBytes());*/
        Log.i(getClass().getName(),"syr"+getAuthToken());


        userApi.getOrderId("https://api.razorpay.com/v1/orders",filterBody,getAuthToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::success, this::getTipFailure);


    }

    private void getTipFailure(Throwable throwable) {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        Log.i(getClass().getName(),"getTipFailure--->"+throwable.toString());
    }

    public static String getAuthToken() {
        byte[] data = new byte[0];
        try {
            data = ("rzp_test_lJnaDPyXQa7I4f" + ":" + "jAkoujlunWk3GAhQFPzx4Dg3").getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);

    }

    private void success(AudioModel responseBody) {
        Log.i(getClass().getName(),"success--->"+responseBody.getId());

        Log.i(getClass().getName(),"getAmount--->"+responseBody.getAmount());
        startPayment(responseBody.getAmount(),responseBody.getId());
    }
}
