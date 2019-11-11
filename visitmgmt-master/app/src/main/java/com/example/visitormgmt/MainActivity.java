package com.example.visitormgmt;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;


public class MainActivity extends AppCompatActivity {


    TextInputEditText user_name, email, emp_id, password, confirm_password;
    TextView validate_username, validate_email, validate_emp_id, validate_pwd, validate_confirm_pwd, thankyou;
    public String UserName, Email, Emp_id, Password, Conf_Password;
    Button Register;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        changeStatusBarColor("#828ffc");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main1);



        user_name = (TextInputEditText) findViewById(R.id.username_txt);
        email = (TextInputEditText) findViewById(R.id.email_txt);
        emp_id = (TextInputEditText) findViewById(R.id.emp_id);
        password = (TextInputEditText) findViewById(R.id.pass_word);
        confirm_password = (TextInputEditText) findViewById(R.id.confirm_pass_word);

        UserName = user_name.getText().toString();
        Email = email.getText().toString().trim();
        Emp_id = emp_id.getText().toString();
        Password = password.getText().toString();
        Conf_Password = confirm_password.getText().toString();


        validate_username = (TextView) findViewById(R.id.validateUsername);
        validate_email = (TextView) findViewById(R.id.validateEmail);
        validate_emp_id = (TextView) findViewById(R.id.validateEmpId);
        validate_pwd = (TextView) findViewById(R.id.validatePassword);
        validate_confirm_pwd = (TextView) findViewById(R.id.validateConfirmPwd);
        thankyou = (TextView) findViewById(R.id.Thankyou);


        Register = (Button) findViewById(R.id.Register);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (UserName.isEmpty()) {
                    validate_username.setText("Please enter username");
                    return;
                }

                if (Email.isEmpty()) {
                    validate_email.setText("Please enter email");
                    return;
                }

                if (!validateEmail()) {
                    validate_email.setText("Invalid Email");
                    return;
                }

                if (Emp_id.isEmpty()) {
                    validate_emp_id.setText("Please enter emp_id");
                    return;
                }

                if (Password.isEmpty()) {
                    validate_pwd.setText("Please enter password");
                    return;
                }

                if (Conf_Password.isEmpty()) {
                    validate_confirm_pwd.setText("Please confirm password");
                    return;
                }

                thankyou.setText("Thank you");


            }

        });


    }


    public final boolean validateEmail(){
        String target = Email;
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
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
