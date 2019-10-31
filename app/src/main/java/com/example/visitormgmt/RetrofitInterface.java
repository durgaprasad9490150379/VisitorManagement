package com.example.visitormgmt;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitInterface {



    String BASEURL = "http://192.168.100.122:1337/";

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("visitors")
    Call<String> getString(@Header("Authorization") String auth);


    /* @Headers({ "Content-Type: application/json;charset=UTF-8"})
     @FormUrlEncoded
     @POST("visitors")
     Call<String>  getPostData(
             @Field("FirstName") String FirstName,
             @Field("LastName") String LastName,
             @Field("email") String email,
             @Field("Address") String Address,
             @Field("organization") String organization
     );*/
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @FormUrlEncoded
    @POST("visitors")
    Call<String> getPostData(@Header("Authorization") String auth,@FieldMap Map<String,String> options);

}