package com.example.visitormgmt;

import android.content.Context;
import com.example.visitormgmt.MobilenoActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OTPActivity extends AppCompatActivity {


    SharedPreferences sharedpreferences;

    public String VerificationId;
    public String userPhone;
    public String phoneNumber;
    public String Enterotp;


    EditText editText1, editText2, editText3, editText4, editText5, editText6;
    private EditText[] editTexts;
    Button resendOTP, otp_verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verification);

        editText1 = (EditText) findViewById(R.id.digit1);
        editText2 = (EditText) findViewById(R.id.digit2);
        editText3 = (EditText) findViewById(R.id.digit3);
        editText4 = (EditText) findViewById(R.id.digit4);
        editText5 = (EditText) findViewById(R.id.digit5);
        editText6 = (EditText) findViewById(R.id.digit6);
        editTexts = new EditText[]{editText1, editText2, editText3, editText4, editText5, editText6};


        editText1.addTextChangedListener(new PinTextWatcher(0));
        editText2.addTextChangedListener(new PinTextWatcher(1));
        editText3.addTextChangedListener(new PinTextWatcher(2));
        editText4.addTextChangedListener(new PinTextWatcher(3));
        editText5.addTextChangedListener(new PinTextWatcher(4));
        editText6.addTextChangedListener(new PinTextWatcher(5));

        editText1.setOnKeyListener(new PinOnKeyListener(0));
        editText2.setOnKeyListener(new PinOnKeyListener(1));
        editText3.setOnKeyListener(new PinOnKeyListener(2));
        editText4.setOnKeyListener(new PinOnKeyListener(3));
        editText5.setOnKeyListener(new PinOnKeyListener(4));
        editText6.setOnKeyListener(new PinOnKeyListener(5));




        sharedpreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        VerificationId = sharedpreferences.getString("SendOTP","");
        userPhone = sharedpreferences.getString("Phone","");

        TextView showPhone = (TextView) findViewById(R.id.mobilenumberDisplay);
        showPhone.setText("Please enter the verification code that sent to " + userPhone);
        loadJSON();

        ButtonListners();

    }

    private void ButtonListners() {

        otp_verify = (Button) findViewById(R.id.verify);
        otp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Enterotp = (editText1.getText().toString() +editText2.getText().toString() +editText3.getText().toString() +editText4.getText().toString()
                        + editText5.getText().toString() + editText6.getText().toString());

                Log.d("myTag", "phone number " + userPhone + "Empty");

                Log.d("myTag", "verification id " + VerificationId + "Empty");

                Log.d("myTag", "send otp " + Enterotp);
                VerificationId = "111111";

                if(Enterotp.equals(VerificationId)) {

                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    Log.d("myTag", "user phone " + userPhone + "Empty");

                    Log.d("myTag", "phone number " + phoneNumber + "Empty");


                    if (userPhone.equals(phoneNumber)) {
                        editor.putInt("UserExistOrNot", 1);
                        editor.apply();

                        Intent preview = new Intent(OTPActivity.this, IdProofActivity1.class);
                        // Start the new activity
                        startActivity(preview);
                    } else {
                        editor.putInt("UserExistOrNot", 0);
                        editor.apply();

                        Intent VisitorIntent = new Intent(OTPActivity.this, VisitorActivity.class);
                        //tart the new activity
                        startActivity(VisitorIntent);

                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"Invalid OTP", Toast.LENGTH_LONG).show();
                }

            }

        });

        resendOTP = (Button) findViewById(R.id.resend);

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }



        });
    }


    private void loadJSON() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RetrofitInterface request = retrofit.create(RetrofitInterface.class);
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNTcxMjA4MjkyLCJleHAiOjE1NzM4MDAyOTJ9.fxBbFQ29gqQ-vPRDws0zHKIw3l0tEdB0rEfBvaJSVfs";
        Call<String> call = request.getString("Bearer " + token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String str = String.valueOf(response.code());
                Log.i("Responsestring", response.body().toString());

                String jsonresponse = response.body().toString();
                try {
                    JSONArray dataArray = new JSONArray(jsonresponse);
                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject dataobj = dataArray.getJSONObject(i);
                        phoneNumber = String.valueOf(dataobj.getLong("phone"));
                        if (phoneNumber.equals(userPhone)) {
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e("MYAPP", "exception: " + t.toString());

            }
        });

    }

   public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char*/
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0)); // TODO: We can fill out other EditTexts

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }

    }

}




