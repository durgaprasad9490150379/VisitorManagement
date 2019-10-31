package com.example.visitormgmt;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiInterface {

    // @Headers({"Content-Type: application/json;charset=Utf-8"})
    @Multipart
    @POST("upload")
    Call<Object> uploadImagePost(
            @Header("Authorization") String Authorization,
            @Part MultipartBody.Part file,
            @Part("ref") RequestBody ref,
            @Part("refId") RequestBody refId,
            @Part("field") RequestBody Photo
    );
    @Headers({"Content-Type: application/json;charset=Utf-8"})
    @POST("visitors")
    Call<Post> createPost( @Header("Authorization") String Authorization, @Body JsonObject fields);

}