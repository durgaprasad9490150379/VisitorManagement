package com.example.visitormgmt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.rilixtech.widget.countrycodepicker.Country;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VisitorActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    EditText first_name_object,  email_id_object, state_object, Company_object, MeetWhoom_object
            , subject_object, mobile_object;
    public  String  name, email, status, mobile_number, company, purpose, meet_whoom ;

    TextView EmailErrormessage, FirstNameErrorMessage, OranizationErrorMessage,MeetWoomErrorMesage, AddressErrorMesage, SubjectErrorMesage
            ;
    TextInputLayout Emiail_layout, first_layout, organization_layout, meet_layout, subject_layout;
    SharedPreferences sharedpreferences;


    String[] SPINNER_DATA = {"Mr.","Mrs.","Ms."};
    MaterialBetterSpinner materialBetterSpinner ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor("#40a7e5");
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.visitor_info_new);



        materialBetterSpinner = (MaterialBetterSpinner)findViewById(R.id.material_spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(VisitorActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

        materialBetterSpinner.setAdapter(adapter);

        sharedpreferences =  getSharedPreferences("MyPrefs", MODE_PRIVATE);

        status = sharedpreferences.getString("status","");
        name = sharedpreferences.getString("FirstName", "");
        email = sharedpreferences.getString("Emial","");
        company = sharedpreferences.getString("company","");
        purpose = sharedpreferences.getString("Purpose","");
        meet_whoom = sharedpreferences.getString("MeetWhom","");
        mobile_number = sharedpreferences.getString("Phone","");


        first_name_object = (EditText) findViewById(R.id.name_txt);
        email_id_object = (EditText) findViewById(R.id.email_txt);
        Company_object  = (EditText) findViewById(R.id.organization_txt);
        MeetWhoom_object = (EditText) findViewById(R.id.meet_whom_txt);
        subject_object = (EditText) findViewById(R.id.subject_txt);
        mobile_object = (EditText) findViewById(R.id.mobile_txt);

        mobile_object.setText(mobile_number);
        materialBetterSpinner.setText("Mr.");




        first_layout = (TextInputLayout) findViewById(R.id.name);
        Emiail_layout = (TextInputLayout) findViewById(R.id.email);
        organization_layout = (TextInputLayout) findViewById(R.id.organization);
        meet_layout = (TextInputLayout) findViewById(R.id.meet_whom);
        subject_layout = (TextInputLayout) findViewById(R.id.subject);





        FirstNameErrorMessage = (TextView) findViewById(R.id.validateName);
        EmailErrormessage = (TextView) findViewById(R.id.validateEmail);
        OranizationErrorMessage = (TextView) findViewById(R.id.validateOrg);
        MeetWoomErrorMesage = (TextView) findViewById(R.id.validateMeetWhom);
        SubjectErrorMesage = (TextView) findViewById(R.id.validateSubject);






        subject_object.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent event) {
                // TODO Auto-generated method stub
                if (view.getId() ==R.id.subject_txt) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()&MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });



        Button button = (Button) findViewById(R.id.next);

        first_name_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirstNameErrorMessage.setVisibility(View.GONE);
            }
        });
        email_id_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide the keyboard
                EmailErrormessage.setVisibility(View.GONE);
                // show own keyboard or buttons
            }
        });

        Company_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide the keyboard
                OranizationErrorMessage.setVisibility(View.GONE);
                // show own keyboard or buttons
            }
        });

        MeetWhoom_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide the keyboard
                MeetWoomErrorMesage.setVisibility(View.GONE);
                // show own keyboard or buttons
            }
        });

        subject_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide the keyboard
                SubjectErrorMesage.setVisibility(View.GONE);
                // show own keyboard or buttons
            }
        });


        materialBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                status= adapterView.getItemAtPosition(position).toString();

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = first_name_object.getText().toString();
                email = email_id_object.getText().toString().trim();
                company = Company_object.getText().toString();
                purpose = subject_object.getText().toString();
                meet_whoom = MeetWhoom_object.getText().toString();
//                City = City_object.getText().toString();
//                CountryName = country_object.getText().toString();
//                Address = Address_object.getText().toString();
//                state = state_object.getText().toString();





                if(name.isEmpty()){
                    FirstNameErrorMessage.setVisibility(View.VISIBLE);
                    FirstNameErrorMessage.setText("Field cannot be empty");
                    return;
                }


                if(!validateEmail()){
                    EmailErrormessage.setVisibility(View.VISIBLE);
                    EmailErrormessage.setText("Invalid Email");
                    return;
                }
                if(company.isEmpty()){
                    OranizationErrorMessage.setVisibility(View.VISIBLE);
                    OranizationErrorMessage.setText("Field cannot be empty");
                    return;
                }

                if(purpose.isEmpty()){
                    SubjectErrorMesage.setVisibility(View.VISIBLE);
                    SubjectErrorMesage.setText("Field cannot be empty");
                    return;
                }

                if(meet_whoom.isEmpty()){
                    MeetWoomErrorMesage.setVisibility(View.VISIBLE);
                    MeetWoomErrorMesage.setText("Field cannot be empty");
                    return;
                }



                    sharedpreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("FirstName", name);
                    editor.putString("Email", email);
                    editor.putString("Company", company);
                    editor.putString("Purpose",purpose);
                    editor.putString("MeetWoom", meet_whoom);
//                    editor.putString("State", state);
//                    editor.putString("Country", CountryName);
//                    editor.putString("City", City);
//                    editor.putString("Address", Address);
                    editor.putString("status", status);
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
