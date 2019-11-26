package com.example.visitormgmt;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class checkOutStatus {

    @SerializedName("status")
    @Expose
    private int status;


    public int getStatus() {
        return status;
    }

}
