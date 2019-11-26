package com.example.visitormgmt;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("id")
    @Expose
    private String id;


    public String getid() {
        return id;
    }


}