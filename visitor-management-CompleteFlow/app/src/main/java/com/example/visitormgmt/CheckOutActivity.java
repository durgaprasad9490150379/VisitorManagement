package com.example.visitormgmt;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckOutActivity extends AppCompatActivity {

    public RetrofitInterface ApiInterfaceObject;

    public String visitorID, mobileNumber, visitorCheckout;

    TextInputEditText visitorID_object, mobileNumber_object;
    Button checkout;
    TextView result ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor("#40a7e5");
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        visitorID_object = (TextInputEditText) findViewById(R.id.visitorid_txt);
        mobileNumber_object = (TextInputEditText) findViewById(R.id.mobile_txt);

        checkout = (Button) findViewById(R.id.proceed);

        result = (TextView) findViewById(R.id.validateInput);


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visitorID = visitorID_object.getText().toString();
                mobileNumber = mobileNumber_object.getText().toString();
                if (visitorID.isEmpty() && mobileNumber.isEmpty()) {
                    result.setText("Please enter yout VisitorID");
                    return;
                } else if (!visitorID.isEmpty() && !mobileNumber.isEmpty()) {
                    result.setText("Please enter only one");
                    return;
                } else {

                    if(visitorID.isEmpty()){
                        create_checkout_mobile();
                    } else {

                        create_checkout();
                    }

                }
            }
        });


    }


    private void   create_checkout() {



        OkHttpClient client1 = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000,TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client1)
                .baseUrl(RetrofitInterface.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterfaceObject = retrofit.create(RetrofitInterface.class);



        JsonObject fields = new JsonObject();
        fields.addProperty("pnr", visitorID);



        System.out.println(">>>>>>>>>>>> >>>  In Checkout  pnr = prastr"+ visitorID);




//        Call<Object> call = ApiInterfaceObject.createPost( token, fields);
        Call<checkOutStatus> call = ApiInterfaceObject.createCheckOut(visitorID);


        System.out.println(">>>>>>>>>>>> >>>  after calling Post method");



        call.enqueue(new Callback<checkOutStatus>() {
            @Override
            public void onResponse(Call<checkOutStatus> call, Response<checkOutStatus> response) {
                System.out.println(">>>>>>>>>>>> >>>  before Post method");

                // textViewResult.setText();
                if (!response.isSuccessful()) {
                    result.setText("Response!!! failure");
                    return;
                }else{

                    System.out.println(">>>>>>>>>>>> >>> code " + response.code());

                    try {
                        int status = response.body().getStatus();

                        if(status == 400) {

                            result.setText("VisitorID does not exist");
                        }
                        else{
                            Intent thankyouActivity = new Intent(CheckOutActivity.this, Thankyou.class);
                            // Start the new activity
                            startActivity(thankyouActivity);

                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }



                }

            }

            @Override
            public void onFailure(Call<checkOutStatus> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }


    private void   create_checkout_mobile() {



        OkHttpClient client1 = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000,TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client1)
                .baseUrl(RetrofitInterface.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterfaceObject = retrofit.create(RetrofitInterface.class);



        JsonObject fields = new JsonObject();
        fields.addProperty("pnr", visitorID);



        System.out.println(">>>>>>>>>>>> >>>  In Checkout  pnr = prastr"+ visitorID);




//        Call<Object> call = ApiInterfaceObject.createPost( token, fields);
        Call<checkOutStatus> call = ApiInterfaceObject.createCheckOut_mobileNumber(mobileNumber);


        System.out.println(">>>>>>>>>>>> >>>  after calling Post method");



        call.enqueue(new Callback<checkOutStatus>() {
            @Override
            public void onResponse(Call<checkOutStatus> call, Response<checkOutStatus> response) {
                System.out.println(">>>>>>>>>>>> >>>  before Post method");

                // textViewResult.setText();
                if (!response.isSuccessful()) {
                    result.setText("Response!!! failure");
                    return;
                }else{

                    System.out.println(">>>>>>>>>>>> >>> code " + response.code());

                    try {
                        int status = response.body().getStatus();

                        if(status == 400) {

                            result.setText("Mobile number does not exist");
                        }
                        else{
                            Intent thankyouActivity = new Intent(CheckOutActivity.this, Thankyou.class);
                            // Start the new activity
                            startActivity(thankyouActivity);

                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }



                }

            }

            @Override
            public void onFailure(Call<checkOutStatus> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }



    private void changeStatusBarColor(String color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
            //window.setNavigationBarColor(Color.parseColor(color));
        }
    }

}



