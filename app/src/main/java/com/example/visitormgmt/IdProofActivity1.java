package com.example.visitormgmt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class IdProofActivity1 extends AppCompatActivity{

    private Spinner spinner;
    private static final String[] paths = {"Aadhar", "Driving Licence", "Voter ID"};

    private static final int CAMERA_REQUEST_GOVT = 1888;

    private static final int MY_CAMERA_PERMISSION_GOVT = 100;

    public int userExistingOrNot = 0;

    Button uploadButton;
    Button nextToUser;
    ImageView imageView;

    SharedPreferences sharedpreferences;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_proof_activity1);

        imageView = (ImageView) findViewById(R.id.image_capture_govt);

        spinner = (Spinner)findViewById(R.id.spinner);
        // Creating adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(IdProofActivity1.this, android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        uploadButton = (Button) findViewById(R.id.Take_picture_of_ID);

        nextToUser = (Button) findViewById(R.id.next_to_API);
        nextToUser.setVisibility(View.GONE);
        sharedpreferences =  getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userExistingOrNot = sharedpreferences.getInt("UserExistOrNot", 0);

        ButtonClickListener();


    }

    private void ButtonClickListener(){

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {



                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_GOVT);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_GOVT);
                }

            }
        });

        nextToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userExistingOrNot == 0) {
                    Intent CameraActivity = new Intent(IdProofActivity1.this, CameraActivity.class);
                    startActivity(CameraActivity);
                } else{
                    Intent PreviewActivity = new Intent(IdProofActivity1.this, PreviewActivity.class);
                    startActivity(PreviewActivity);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_GOVT)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_GOVT);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST_GOVT && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            String idString = BitMapToString(photo);
            imageView.setImageBitmap(photo);

            sharedpreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("IdProof", idString);
            editor.commit();
            uploadButton.setVisibility(View.GONE);
            nextToUser.setVisibility(View.VISIBLE);


        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] arr=baos.toByteArray();
        String result= Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }


}
