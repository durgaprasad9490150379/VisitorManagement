package com.example.visitormgmt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;

public class VisitorActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    EditText first_name, last_name, mob_number, email_id;
    public  String  f_name;
    public  String  l_name;
    public  String  email;
    TextView EmailErrormessage, FirstNameErrorMessage, LastNameErrorMessage;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor("#828ffc");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visitor_info);

        first_name = (EditText) findViewById(R.id.f_name_txt);


        last_name  = (EditText) findViewById(R.id.l_name_txt);



        email_id = (EditText) findViewById(R.id.email_txt);

        EmailErrormessage = (TextView) findViewById(R.id.validateEmail);
        FirstNameErrorMessage = (TextView) findViewById(R.id.validateFirstName);
        LastNameErrorMessage = (TextView) findViewById(R.id.validateLastName);


        Button button = (Button) findViewById(R.id.next);

        first_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirstNameErrorMessage.setVisibility(View.GONE);
            }
        });
        last_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide the keyboard
                LastNameErrorMessage.setVisibility(View.GONE);
                // show own keyboard or buttons
            }
        });
        email_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide the keyboard
                EmailErrormessage.setVisibility(View.GONE);
                // show own keyboard or buttons
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f_name = first_name.getText().toString();
                l_name =  last_name.getText().toString();
                email = email_id.getText().toString().trim();




                if(f_name.isEmpty()){
                    FirstNameErrorMessage.setVisibility(View.VISIBLE);
                    FirstNameErrorMessage.setText("Field cannot be empty");
                    return;
                }

                if(l_name.isEmpty()){

                    LastNameErrorMessage.setVisibility(View.VISIBLE);
                    LastNameErrorMessage.setText("Field cannot be empty");
                    return;
                }

                if(!validateEmail()){
                    EmailErrormessage.setVisibility(View.VISIBLE);
                    EmailErrormessage.setText("Invalid Email");
                    return;
                }




                    sharedpreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("FirstName", f_name);
                    editor.putString("LastName", l_name);
                    editor.putString("Email", email);
                    editor.commit();

                    Intent cameraIntent = new Intent(VisitorActivity.this, IdProofActivity1.class);
                    // Start the new activity
                    startActivity(cameraIntent);
                    

            }

        });

    }

    public final boolean validateEmail(){
        String target = email;
        if (TextUtils.isEmpty(target)) {
                return false;
            } else {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
            }
    }
    private void changeStatusBarColor(String color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
           // window.setNavigationBarColor(Color.parseColor(color));
        }
    }

}
