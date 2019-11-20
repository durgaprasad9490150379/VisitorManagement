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

    public String pnr;

    TextInputEditText pnr_no;
    Button checkout;
    TextView result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor("#828ffc");
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        pnr_no = (TextInputEditText) findViewById(R.id.pnr_no_txt);

        checkout = (Button) findViewById(R.id.proceed);

        result = (TextView) findViewById(R.id.validatePnr);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pnr = pnr_no.getText().toString();
                if(pnr.isEmpty()){
                    result.setText("Please enter yout pnr");
                    return;
                }
                create_checkout();
            }
        });


    }


    private void   create_checkout() {


        OkHttpClient client1 = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000,TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client1)
                .baseUrl("http://192.168.100.187:3001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterfaceObject = retrofit.create(RetrofitInterface.class);



        JsonObject fields = new JsonObject();
        fields.addProperty("pnr", pnr);



        System.out.println(">>>>>>>>>>>> >>>  In Checkout  pnr = prastr"+ pnr);




//        Call<Object> call = ApiInterfaceObject.createPost( token, fields);
        Call<checkOutStatus> call = ApiInterfaceObject.createCheckOut(pnr);


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

                            result.setText("PNR does not existing");
                        }
                        else{
                            Intent thankyouActivity = new Intent(CheckOutActivity.this, ThankYou.class);
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



