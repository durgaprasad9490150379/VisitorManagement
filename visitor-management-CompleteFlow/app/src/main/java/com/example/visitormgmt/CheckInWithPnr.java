package com.example.visitormgmt;

import android.app.AlertDialog;
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

//import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.Result;

public class CheckInWithPnr extends AppCompatActivity {


    private String PNRNumber, contents;
    EditText pnrText;
    SharedPreferences sharedpreferences;
//    private ZXingScannerView mScannerView;
    public String status, f_name, email, contactPerson, contactno, organiation, purpose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor("#40a7e5");
        super.onCreate(savedInstanceState);
//        mScannerView = new ZXingScannerView(CheckInWithPnr.this);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.checkin_with_visitor_id);

        Button search = (Button) findViewById(R.id.proceed);
        Button QRCODE = (Button) findViewById(R.id.qrcode);
        pnrText = (EditText) findViewById(R.id.visitorid_txt);

        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PNRNumber = pnrText.getText().toString();
                if (PNRNumber.isEmpty()) {
                    TextView Error = (TextView) findViewById(R.id.validateVisId);
                    Error.setText("Please enter PNR");
                    return;
                }

                CheckPNRInAPI();

            }
        });

        QRCODE.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        "com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
//
            }
        });
    }


    public void CheckPNRInAPI() {

        loadJSON();

    }


    private void loadJSON() {

        Log.e("MYAPP", "pnr " + PNRNumber);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RetrofitInterface request = retrofit.create(RetrofitInterface.class);
        Call<String> call = request.getPNRDetails(PNRNumber);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    String jsonresponse = response.body().toString();
                    Log.e("MYAPP", "reponse" + jsonresponse);


                    JSONObject dataobj = new JSONObject(jsonresponse);

                    JSONArray dataArray = dataobj.getJSONArray("pnrdetails");

                    final int numberOfItemsInResp = dataArray.length();

                    if (dataArray.length() > 0) {

                        JSONObject originalData = dataArray.getJSONObject(0);
                        status = originalData.getString("status");
                        f_name = originalData.getString("name");
                        contactno = originalData.getString("contactno");
                        organiation = originalData.getString("organization");
                        purpose = originalData.getString("purpose");
                        contactPerson = originalData.getString("contactPerson");
                        email = originalData.getString("emailId");

                        int stat = dataobj.getInt("status");
                        Log.e("MYAPP", "reponse" + f_name);


                        sharedpreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString("status", status);
                        editor.putString("FirstName", f_name);
                        editor.putString("Phone", contactno);
                        editor.putString("Email", email);
                        editor.putString("Company", organiation);
                        editor.putString("Purpose", purpose);
                        editor.putString("MeetWoom", contactPerson);
                        Log.e("MYAPP", "I am getting contact person" + contactPerson);
                        editor.putInt("ExistingUser", 0);
                        editor.apply();


                        Intent InvitedVisitor = new Intent(CheckInWithPnr.this, OTPActivity.class);

                        // Start the new activity
                        startActivity(InvitedVisitor);
                    } else {
                        TextView Error = (TextView) findViewById(R.id.validateVisId);
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


    private void changeStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
            //window.setNavigationBarColor(Color.parseColor(color));
        }
    }

//    @Override
//    public void handleResult(Result result) {
//
//        // Do something with the result here
//
//        Log.v("TAG", result.getText()); // Prints scan results
//        // Prints the scan format (qrcode, pdf417 etc.)
//        Log.v("TAG", result.getBarcodeFormat().toString());
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Scan Result");
//        builder.setMessage(result.getText());
//        AlertDialog alert1 = builder.create();
//        alert1.show();
//
//        // If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(CheckInWithPnr.this);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        // Register ourselves as a handler for scan results.
//        mScannerView.setResultHandler(CheckInWithPnr.this);
//        // Start camera on resume
//        mScannerView.startCamera();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        // Stop camera on pause
//        mScannerView.stopCamera();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//
//        super.onActivityResult(requestCode, resultCode, intent);
//        if (requestCode == 0) {
//            if (resultCode == RESULT_OK) {
//
//
//                contents = intent.getStringExtra("SCAN_RESULT"); // This will contain your scan result
//                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
//
//
//            }
//        }
//
//    }

}

