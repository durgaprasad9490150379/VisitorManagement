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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class IdProofActivity1 extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private static final String TAG = IdProofActivity1.class.getSimpleName();

    private ImageView imageView;
    private static final int CAMERA_REQUEST_CODE = 1450;
    private static final int CAMERA_PERMISSION_CODE = 1460;
    private String mCurrentPhotoPath;

    private Spinner spinner;
    private static final String[] paths = {"Aadhar", "Driving Licence", "Voter ID"};


    public int userExistingOrNot = 0;

    Button uploadButton;
    Button nextToUser;

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



                //check if app has permission to access the camera.
                if (EasyPermissions.hasPermissions(IdProofActivity1.this, Manifest.permission.CAMERA)) {
                    launchCamera();

                } else {
                    //If permission is not present request for the same.
                    EasyPermissions.requestPermissions(IdProofActivity1.this, getString(R.string.permission_text), CAMERA_PERMISSION_CODE, Manifest.permission.CAMERA);
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

    private void launchCamera() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        File photoFile = null;
        try {

            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".provider",
                    photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            //Start the camera application
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);

        }
    }

    /**
     * Previews the captured picture on the app
     * Called when the picture is taken
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Preview the image captured by the camera
        if (requestCode == CAMERA_REQUEST_CODE) {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            imageView.setImageBitmap(bitmap);
            uploadButton.setVisibility(View.GONE);
            nextToUser.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, IdProofActivity1.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        launchCamera();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }


    /**
     * Creates the image file in the external directory
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        sharedpreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);




        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */

        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        editor.putString("IdProofPath", mCurrentPhotoPath);
        editor.apply();
        return image;
    }

}
