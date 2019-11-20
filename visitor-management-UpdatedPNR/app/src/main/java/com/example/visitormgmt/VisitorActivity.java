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

import com.rilixtech.widget.countrycodepicker.Country;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VisitorActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    EditText first_name_object, last_name_object, email_id_object, state_object, country_object, Address_object
            , City_object;
    public  String  f_name, l_name, email, state, CountryName, Address, City, status;

    TextView EmailErrormessage, FirstNameErrorMessage, LastNameErrorMessage,CityErrorMesage, AddressErrorMesage, StateErrorMessage
            ,CountryErrorMessage;
    SharedPreferences sharedpreferences;


    String[] SPINNER_DATA = {"Mr.","Mrs.","Ms."};
    MaterialBetterSpinner materialBetterSpinner ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor("#828ffc");
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.visitor_info);

        first_name_object = (EditText) findViewById(R.id.f_name_txt);
        last_name_object  = (EditText) findViewById(R.id.l_name_txt);
        email_id_object = (EditText) findViewById(R.id.email_txt);
        City_object  = (EditText) findViewById(R.id.city_txt);
        state_object = (EditText) findViewById(R.id.state_txt);
        Address_object = (EditText) findViewById(R.id.address_txt);
        country_object = (EditText)findViewById(R.id.country_txt);



        EmailErrormessage = (TextView) findViewById(R.id.validateEmail);
        FirstNameErrorMessage = (TextView) findViewById(R.id.validateFirstName);
        LastNameErrorMessage = (TextView) findViewById(R.id.validateLastName);
        CityErrorMesage = (TextView) findViewById(R.id.validateCity);
        AddressErrorMesage = (TextView) findViewById(R.id.validateAddress);
        CountryErrorMessage = (TextView) findViewById(R.id.validateCountry);
        StateErrorMessage = (TextView) findViewById(R.id.validateState);


        materialBetterSpinner = (MaterialBetterSpinner)findViewById(R.id.material_spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(VisitorActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

        materialBetterSpinner.setAdapter(adapter);




        Button button = (Button) findViewById(R.id.next);

        first_name_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirstNameErrorMessage.setVisibility(View.GONE);
            }
        });
        last_name_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide the keyboard
                LastNameErrorMessage.setVisibility(View.GONE);
                // show own keyboard or buttons
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

        City_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide the keyboard
                CityErrorMesage.setVisibility(View.GONE);
                // show own keyboard or buttons
            }
        });

        Address_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide the keyboard
                AddressErrorMesage.setVisibility(View.GONE);
                // show own keyboard or buttons
            }
        });

        country_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide the keyboard
                CountryErrorMessage.setVisibility(View.GONE);
                // show own keyboard or buttons
            }
        });

        state_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide the keyboard
                StateErrorMessage.setVisibility(View.GONE);
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

                f_name = first_name_object.getText().toString();
                l_name =  last_name_object.getText().toString();
                email = email_id_object.getText().toString().trim();
                City = City_object.getText().toString();
                CountryName = country_object.getText().toString();
                Address = Address_object.getText().toString();
                state = state_object.getText().toString();





                if(f_name.isEmpty() || !f_name.matches("^[a-zA-Z ]+$")){
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
                if(state.isEmpty() || !f_name.matches("^[a-zA-Z ]+$")){
                    StateErrorMessage.setVisibility(View.VISIBLE);
                    StateErrorMessage.setText("Field cannot be empty");
                }

                if(Address.isEmpty()){
                    AddressErrorMesage.setVisibility(View.VISIBLE);
                    AddressErrorMesage.setText("Field cannot be empty");
                }

                if(City.isEmpty() || !f_name.matches("^[a-zA-Z ]+$")){
                    CityErrorMesage.setVisibility(View.VISIBLE);
                    CityErrorMesage.setText("Field cannot be empty");
                }

                if(CountryName.isEmpty() || !f_name.matches("^[a-zA-Z ]+$")){
                    CityErrorMesage.setVisibility(View.VISIBLE);
                    CityErrorMesage.setText("Field cannot be empty");
                }



                    sharedpreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("FirstName", f_name);
                    editor.putString("LastName", l_name);
                    editor.putString("Email", email);
                    editor.putString("State", state);
                    editor.putString("Country", CountryName);
                    editor.putString("City", City);
                    editor.putString("Address", Address);
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
