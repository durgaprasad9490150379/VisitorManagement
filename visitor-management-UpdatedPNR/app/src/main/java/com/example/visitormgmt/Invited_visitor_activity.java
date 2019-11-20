package com.example.visitormgmt;

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
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Invited_visitor_activity extends AppCompatActivity {



    MaterialBetterSpinner materialBetterSpinner ;
    SharedPreferences sharedpreferences;

    String[] SPINNER_DATA = {"Official","Personal","Partner"};

    TextInputEditText status_object, name_object, mobile_object, email_object, organization_object, address_object,  meetwhom_object, country_object, state_object, city_object;
    public String fname, lname, mobile, email1, image, idproof,company
            , purpose, meet_whom, visitor_id, address, city, state,country, status ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor("#828ffc");
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.visitor_edit_activity);

        materialBetterSpinner = (MaterialBetterSpinner)findViewById(R.id.purpose);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Invited_visitor_activity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

        materialBetterSpinner.setAdapter(adapter);


        status_object = (TextInputEditText) findViewById(R.id.status_txt);

        name_object = (TextInputEditText) findViewById(R.id.name);

        mobile_object = (TextInputEditText) findViewById(R.id.mobile);

        email_object = (TextInputEditText) findViewById(R.id.email_txt);

        organization_object = (TextInputEditText) findViewById(R.id.company_name);

        address_object = (TextInputEditText) findViewById(R.id.address_txt);

        meetwhom_object = (TextInputEditText) findViewById(R.id.meet_whom_txt);


        country_object = (TextInputEditText) findViewById(R.id.country_txt);

        state_object = (TextInputEditText) findViewById(R.id.state_txt);

        city_object = (TextInputEditText) findViewById(R.id.city_txt);




        sharedpreferences =  getSharedPreferences("MyPrefs", MODE_PRIVATE);
        status = sharedpreferences.getString("status", "");
        fname = sharedpreferences.getString("FirstName", "");
        lname = sharedpreferences.getString("LastName", "");
        mobile = sharedpreferences.getString("Phone", "");
        email1 = sharedpreferences.getString("Email", "");
        image = sharedpreferences.getString("Image", "");
        idproof = sharedpreferences.getString("IdProof", "");
        city = sharedpreferences.getString("City", "");
        address = sharedpreferences.getString("Address", "");
        state = sharedpreferences.getString("State", "");
        country = sharedpreferences.getString("Country", "");

        company = sharedpreferences.getString("Company", "");
        purpose = sharedpreferences.getString("Purpose", "");
        meet_whom = sharedpreferences.getString("MeetWhom", "");

        status_object.setText(status);
        name_object.setText(fname);
        mobile_object.setText(mobile);
        email_object.setText(email1);
        organization_object.setText(company);
        address_object.setText(address);
        meetwhom_object.setText(meet_whom);
        country_object.setText(country);
        state_object.setText(state);
        city_object.setText(city);
        status_object.setText(status);


        materialBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                purpose= adapterView.getItemAtPosition(position).toString();
            }
        });


        Button submit = (Button) findViewById(R.id.next);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedpreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();


                editor.putString("status", "Mr");
                editor.putString("FirstName", name_object.getText().toString());
                editor.putString("LastName", "sss");
                editor.putString("Email", email_object.getText().toString());
                editor.putString("State", state_object.getText().toString());
                editor.putString("Country", country_object.getText().toString());
                editor.putString("City", city_object.getText().toString());
                editor.putString("Address", address_object.getText().toString());
                editor.putString("Company", organization_object.getText().toString());
                editor.putString("Purpose", purpose);
                editor.putString("MeetWhom", meetwhom_object.getText().toString());
                editor.putString("Existing", "0");
                editor.commit();

                Intent Camera = new Intent(Invited_visitor_activity.this, CameraActivity.class);
                startActivity(Camera);


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




