package com.example.visitormgmt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CheckInActivity extends AppCompatActivity {

    private Animation scale;
    private LinearLayout visitor;
    private TextView visitorTxt ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor("#828ffc");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in1);

         scale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

         visitor = (LinearLayout) findViewById(R.id.visitor);
         visitorTxt = (TextView) findViewById(R.id.visitor_txt);

        visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent VisitorIntent = new Intent(CheckInActivity.this, MobilenoActivity.class);
                // Start the new activity
                visitor.setBackgroundColor(Color.BLUE);
                visitorTxt.setBackgroundColor(Color.BLUE);

                startActivity(VisitorIntent);
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