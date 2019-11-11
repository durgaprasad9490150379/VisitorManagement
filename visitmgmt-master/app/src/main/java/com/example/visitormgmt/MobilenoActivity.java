package com.example.visitormgmt;

import android.content.Context;
import java.lang.String;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MobilenoActivity extends AppCompatActivity {


    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;


    EditText mobileNumber;
    String UserNumber;
    SharedPreferences sharedpreferences;

    TextView validation;

    public String VerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor("#828ffc");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in_mobno);

        sharedpreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        Button otp_send = (Button) findViewById(R.id.sendotp);
        mobileNumber = (EditText) findViewById(R.id.mob_no);
        validation = (TextView) findViewById(R.id.validationform);

        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        otp_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedpreferences.edit();

                UserNumber =  mobileNumber.getText().toString();
                if(UserNumber.isEmpty() || UserNumber.length() < 10 || UserNumber.length() > 10){
                    validation.setText("Enter Valid mobile number");
                    return;
                }
                VerificationId = new String(OTP(6));

                editor.putString("Phone", UserNumber);
                editor.putString("SendOTP", VerificationId);
                editor.apply();


                Log.d("myTag", "send otp " + VerificationId);
                SendOtp();


                Intent otpIntent = new Intent(MobilenoActivity.this, OTPActivity.class);

                // Start the new activity
                startActivity(otpIntent);

            }

        });


            auth = FirebaseAuth.getInstance();
            mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    Toast.makeText(MobilenoActivity.this,"verification completed",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(MobilenoActivity.this,"verification fialed",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    VerificationId = s;
                    Toast.makeText(MobilenoActivity.this,"Code sent",Toast.LENGTH_SHORT).show();
                }
            };




    }



   public void SendOtp(){


       PhoneAuthProvider.getInstance().verifyPhoneNumber(
               "+918899665544",                    // Phone number to verify
               60,                           // Timeout duration
               TimeUnit.SECONDS,                // Unit of timeout
               MobilenoActivity.this,        // Activity (for callback binding)
               mCallback);



         /*try {
                    // Construct data

MobilenoActivity
                    String apiKey = "apikey=" + "MFItv2MtIQU-lcgjIiCSY9rUz5rZX8Sg6M3EYSS1yR";
                    String message = "&message=" + " <#>" + VerificationId + " is your otp ";
                    String sender = "&sender=" + "Visitor management";
                    String numbers = "&numbers=" + "91"+UserNumber;

                    // Send data
                    HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
                    String data = apiKey + numbers + message + sender;
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                    conn.getOutputStream().write(data.getBytes("UTF-8"));
                    final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    final StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    rd.close();

                    Toast.makeText(getApplicationContext(),"OTP send successfully", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Error sms" + e, Toast.LENGTH_LONG).show();
                    Log.d("myApp",e.getMessage());

                }*/

    }


    public static char[] OTP(int len)
    {


        String numbers= "0123456789";


        Random rndm_method = new Random();

        char[] otp = new char[len];

        for (int i = 0; i < len; i++)
        {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i] =
                    numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return otp;
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
