package com.example.visitormgmt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        Button chk_in = (Button) findViewById(R.id.check_in);

        chk_in.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CheckInIntent = new Intent(MainActivity.this, CheckInActivity.class);

                // Start the new activity
                startActivity(CheckInIntent);
            }
        });

    }


}
