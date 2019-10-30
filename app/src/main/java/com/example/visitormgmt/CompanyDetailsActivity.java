package com.example.visitormgmt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CompanyDetailsActivity extends AppCompatActivity {

    public String company_name;
    public String purpose;
    public String meet_whom;
    public String department;
    private static final String TAG = "MyApp";
    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visit_purpose);




        text1 = (TextView) findViewById(R.id.company_name);

        text2 = (TextView) findViewById(R.id.purpose_txt);

        text3 = (TextView) findViewById(R.id.meet_whom_txt);

        text4 = (TextView) findViewById(R.id.dept_txt);

        Log.i(TAG, "Haiiii");







        Button next_to_details = (Button) this.findViewById(R.id.submit);

        next_to_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                company_name = text1.getText().toString();
                purpose = text2.getText().toString();
                meet_whom = text3.getText().toString();
                department = text4.getText().toString();


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
}
