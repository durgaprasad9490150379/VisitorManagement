package com.example.visitormgmt;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.TextView;

import java.lang.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CheckInWithPnr extends AppCompatActivity {


    private String PNRNumber;
    EditText pnrText;
    SharedPreferences sharedpreferences;

    public  String  status, f_name, l_name, email,contactPerson, state, CountryName, Address, city, contactno, organiation, purpose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
            changeStatusBarColor("#828ffc");
            super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setContentView(R.layout.pnrsearch_layout);

        Button search = (Button) findViewById(R.id.proceed);
        Button QRCODE = (Button) findViewById(R.id.qrcode);
        pnrText = (EditText) findViewById(R.id.pnr_no_txt);

        search.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                PNRNumber = pnrText.getText().toString();
                if (PNRNumber.isEmpty()){
                    TextView Error = (TextView) findViewById(R.id.validatePnr);
                    Error.setText("Please enter PNR");
                    return;
                }

                CheckPNRInAPI();

            }
        });

        QRCODE.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

               // Intent invited = new Intent(CheckInWithPnr.this, Invited_visitor_activity.class);
                //startActivity(invited);

            }
        });
    }


    public void CheckPNRInAPI(){

        loadJSON();

    }



    private void loadJSON() {

        Log.e("MYAPP", "pnr " + PNRNumber);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.100.187:3001/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RetrofitInterface request = retrofit.create(RetrofitInterface.class);
        Call<String> call = request.getPNRDetails(PNRNumber);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    String jsonresponse = response.body().toString();

                    JSONObject dataobj = new JSONObject(jsonresponse);

                    JSONArray dataArray = dataobj.getJSONArray("pnrdetails");

                    final int numberOfItemsInResp = dataArray.length();

                    if(dataArray.length() > 0) {

                        JSONObject originalData = dataArray.getJSONObject(0);
                        status = originalData.getString("status");
                        f_name = originalData.getString("name");
                        contactno = originalData.getString("contactno");
                        Address = originalData.getString("address");
                        organiation = originalData.getString("organization");
                        purpose = originalData.getString("purpose");
                        contactPerson = originalData.getString("contactPerson");
                        email = originalData.getString("emailId");
                        CountryName = originalData.getString("country");
                        city = originalData.getString("city");
                        state = originalData.getString("state");


                        int stat = dataobj.getInt("status");
                        Log.e("MYAPP", "reponse" + f_name);


                        sharedpreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString("status", status);
                        editor.putString("FirstName", f_name);
                        editor.putString("Phone", contactno);
                        editor.putString("Email", email);
                        editor.putString("State", state);
                        editor.putString("Country", CountryName);
                        editor.putString("City", city);
                        editor.putString("Address", Address);
                        editor.putString("Company", organiation);
                        editor.putString("Purpose", purpose);
                        editor.putString("MeetWhom", contactPerson);
                        editor.commit();


                        Intent InvitedVisitor = new Intent(CheckInWithPnr.this, Invited_visitor_activity.class);

                        // Start the new activity
                        startActivity(InvitedVisitor);
                    }

                    else {
                        TextView Error = (TextView) findViewById(R.id.validatePnr);
                        Error.setText("PNR Does not exist");
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("MYAPP", "response " + response.body().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e("MYAPP", "exception: " + t.toString());

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

