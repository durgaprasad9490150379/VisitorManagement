package com.example.visitormgmt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class VisitorActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    TextView first_name, last_name, mob_number, email_id;
    public  String  f_name;
    public  String  l_name;
    public  String  email;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visitor_info);

        first_name = (TextView) findViewById(R.id.f_name_txt);


        last_name  = (TextView) findViewById(R.id.l_name_txt);



        email_id = (TextView) findViewById(R.id.email_txt);


        Button button = (Button) findViewById(R.id.next);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (first_name.getText().toString().isEmpty() ||last_name.getText().toString().isEmpty() || email_id.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Enter the Data", Toast.LENGTH_SHORT).show();
                } else {
                    f_name = first_name.getText().toString();
                    l_name =  last_name.getText().toString();
                    email = email_id.getText().toString();

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

            }

        });

    }

}
