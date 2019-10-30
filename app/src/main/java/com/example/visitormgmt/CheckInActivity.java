package com.example.visitormgmt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CheckInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in);

        Button visitor = (Button) findViewById(R.id.visitor);

        visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent VisitorIntent = new Intent(CheckInActivity.this, MobilenoActivity.class);

                // Start the new activity
                startActivity(VisitorIntent);
            }
        });



    }
}