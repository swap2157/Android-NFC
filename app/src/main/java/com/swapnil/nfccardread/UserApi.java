package com.swapnil.nfccardread;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.swapnil.nfccardread.model.AudioModel;
import com.swapnil.nfccardread.model.FilterBody;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface UserApi {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Headers({
            "Content-Type:application/json"
    })
    @POST()
    Observable<AudioModel> getOrderId(@Url String url, @Body FilterBody filterBody, @Header("Authorization") String authHeader);

}
