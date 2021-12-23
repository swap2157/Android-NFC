package com.swapnil.nfccardread.model;

import com.google.gson.annotations.SerializedName;

public class FilterBody {
    @SerializedName("amount")
    String amount;

    @SerializedName("currency")
    String currency;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
