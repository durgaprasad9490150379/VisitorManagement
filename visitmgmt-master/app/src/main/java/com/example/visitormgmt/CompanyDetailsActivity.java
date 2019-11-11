package com.example.visitormgmt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class CompanyDetailsActivity extends AppCompatActivity {

    public String company_name;
    public String purpose;
    public String meet_whom;
    public String department;
    private static final String TAG = "MyApp";
    TextInputEditText text1;
    TextInputEditText text2;
    TextInputEditText text3;
    TextInputEditText text4;
    TextView validateCompany, validateMeet;

    private RadioGroup radioPurpose;
    private RadioButton radioPurposeButton;

    private static final String[] paths = {"Offical", "Personal", "Vendor", "Partner"};
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor("#828ffc");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visit_purpose);




        text1 = (TextInputEditText) findViewById(R.id.company_name);

        text3 = (TextInputEditText) findViewById(R.id.meet_whom_txt);

        text4 = (TextInputEditText) findViewById(R.id.dept_txt);

        validateCompany = (TextView) findViewById(R.id.validateCompanyName);
        validateMeet = (TextView) findViewById(R.id.validateMeet);

        radioPurpose = (RadioGroup) findViewById(R.id.radioPurpose);




        Button next_to_details = (Button) this.findViewById(R.id.next);

        next_to_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = radioPurpose.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioPurposeButton = (RadioButton) findViewById(selectedId);

                Toast.makeText(CompanyDetailsActivity.this,
                        radioPurposeButton.getText(), Toast.LENGTH_SHORT).show();

                purpose = String.valueOf(radioPurposeButton.getText());

                company_name = text1.getText().toString();

                meet_whom = text3.getText().toString();
                department = text4.getText().toString();

                if(company_name.isEmpty()){

                    validateCompany.setText("Field cannot be empty");
                    return;
                }

                if(meet_whom.isEmpty()){

                    validateMeet.setText("Field cannot be empty");
                    return;
                }


                SharedPreferences sharedpreferences;

                sharedpreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("Company", company_name);
                editor.putString("Purpose", purpose);
                editor.putString("MeetWhom", meet_whom);
                editor.putString("Department", department);
                editor.commit();



                Intent previewActivity = new Intent(CompanyDetailsActivity.this, PreviewActivity.class);

                startActivity(previewActivity);
            }
        });
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