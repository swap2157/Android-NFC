package com.swapnil.nfccardread.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AudioModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("amount")
    @Expose
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}