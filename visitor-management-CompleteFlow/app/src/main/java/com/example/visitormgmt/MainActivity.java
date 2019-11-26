package com.example.visitormgmt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        changeStatusBarColor("#40a7e5");
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button chk_in = (Button) findViewById(R.id.check_in);
        Button CheckInWithPnr = (Button) findViewById(R.id.check_in_pnr);
        Button CheckOut = (Button) findViewById(R.id.check_out);

        chk_in.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CheckInIntent = new Intent(MainActivity.this, CheckInActivity.class);

                // Start the new activity
                startActivity(CheckInIntent);
            }
        });

        CheckInWithPnr.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent CheckInPNRIntent = new Intent(MainActivity.this, CheckInWithPnr.class);
                startActivity(CheckInPNRIntent);

            }
        });

        CheckOut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent CheckOut = new Intent(MainActivity.this, CheckOutActivity.class);
                startActivity(CheckOut);

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
