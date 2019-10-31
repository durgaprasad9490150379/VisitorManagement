package com.example.visitormgmt;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("FirstName")
    @Expose
    private String FirstName;

    @SerializedName("LastName")
    @Expose
    private String LastName;

    @SerializedName("email")
    @Expose
    private String email;


    @SerializedName("phone")
    @Expose
    private Long phone;

    @SerializedName("Address")
    @Expose
    private String Address;


    @SerializedName("blacklisted")
    @Expose
    private Boolean blacklisted;



    public String getid() {
        return id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getEmail() {
        return email;
    }

    public Long getPhone() {
        return phone;
    }
    public String getAddress() {
        return Address;
    }

    public Boolean getBlacklisted() {
        return blacklisted;
    }

//    @Override
//    public String toString() {
//        return "Post{" +
//                "title='" + title + '\'' +
//                ", body='" + body + '\'' +
//                ", userId=" + userId +
//                ", id=" + id +
//                '}';
//    }
}