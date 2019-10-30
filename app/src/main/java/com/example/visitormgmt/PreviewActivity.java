package com.example.visitormgmt;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PreviewActivity extends AppCompatActivity {

    public String fname, lname, mobile, email1, image, idproof, company, visitor_id, image_name;
    Bitmap img_src, id_src;
    TextView f_name, l_name, mobile_no, email_id, post;
    ImageView img1, img2;

    private RelativeLayout llPdf;
    private Bitmap bitmap;

    private static final int REQUEST = 112;
    private ApiInterface ApiInterfaceObject;
    public String token = "Bearer "+"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNTcxMjA4MjkyLCJleHAiOjE1NzM4MDAyOTJ9.fxBbFQ29gqQ-vPRDws0zHKIw3l0tEdB0rEfBvaJSVfs";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.preview_activity);

        //[creating objects for view in xml file]

        f_name = (TextView) findViewById(R.id.f_name);
        l_name = (TextView) findViewById(R.id.l_name);
        mobile_no = (TextView) findViewById(R.id.mobile);
        email_id = (TextView) findViewById(R.id.email);

        img1 = (ImageView) findViewById(R.id.img_src);
        img2 = (ImageView) findViewById(R.id.id_src);

        //[Geting data which is entered by user using shared preferences which is stored previous page]

        SharedPreferences sharedpreferences;
        sharedpreferences =  getSharedPreferences("MyPrefs", MODE_PRIVATE);
        fname = sharedpreferences.getString("FirstName", "");
        lname = sharedpreferences.getString("LastName", "");
        mobile = sharedpreferences.getString("Phone", "");
        email1 = sharedpreferences.getString("Email", "");
        image = sharedpreferences.getString("Image", "");
        company = sharedpreferences.getString("Company", "");
        idproof = sharedpreferences.getString("IdProof", "");
        image_name = sharedpreferences.getString("ImageName", "");

        createPost();
        //[updating QR code function] //[Geting data which is entered by user using shared preferences which is stored previous page]
        UpdateQRCode();

        img_src = StringToBitMap(image);
        id_src = StringToBitMap(idproof);

        f_name.setText(fname);
        l_name.setText(lname);
        mobile_no.setText(mobile);
        email_id.setText(email1);

        img2.setImageBitmap(id_src);

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent VisitorIntent = new Intent(PreviewActivity.this, ThankYou.class);

                // Start the new activity
                startActivity(VisitorIntent);*/

                llPdf = (RelativeLayout) findViewById(R.id.preview_activity);

                Log.d("size"," "+llPdf.getWidth() +"  "+llPdf.getWidth());
                bitmap = loadBitmapFromView(llPdf, llPdf.getWidth(), llPdf.getHeight());

                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if (ContextCompat.checkSelfPermission(PreviewActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) PreviewActivity.this, PERMISSIONS, REQUEST );
                    } else {
                        //do here
                        createPdf();
                    }
                }else{
                    createPdf();
                }

            }
        });


    }

    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap1= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap1;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }


    private void UpdateQRCode() {
        String dataInQRCode = fname + "\n" + lname + "\n" + mobile;
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(dataInQRCode, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            img1.setImageBitmap(bitmap);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);
        System.out.println("Haii");

        File sdcard = Environment.getExternalStorageDirectory();
        System.out.print(sdcard);

// to this path add a new directory path
        File dir = new File(sdcard.getAbsolutePath() + "PDF_FILE");
        System.out.print(sdcard);

// create this directory if not already created
        if (!dir.getParentFile().exists()) {
            dir.getParentFile().mkdirs();
            System.out.print("Creating direcory");
        }
// create te file in which we will write the contents

         File file = new File(dir,"PDF123.pdf");

        // write the document content
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            document.writeTo(new FileOutputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(this, "PDF is created!!!", Toast.LENGTH_SHORT).show();

        openGeneratedPDF();

    }

    private void openGeneratedPDF(){
        File file = new File("/sdcard/pdffromlayout.pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(PreviewActivity.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void createPost() {
        post = (TextView) findViewById(R.id.post);


        OkHttpClient client1 = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000,TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client1)
                .baseUrl("http://192.168.100.122:1337/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterfaceObject = retrofit.create(ApiInterface.class);


        JsonObject fields = new JsonObject();
        fields.addProperty("FirstName", fname);
        fields.addProperty("LastName", lname);
        fields.addProperty("email", email1 );
        fields.addProperty("phone", mobile);
        fields.addProperty("Address", "Bangalore");
        fields.addProperty("blacklisted", false);
        fields.addProperty("organization", company);


        Call<Object> call = ApiInterfaceObject.createPost( token, fields);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // textViewResult.setText();
                if (!response.isSuccessful()) {
                    post.setText("" + response.toString());
                    return;
                }else{

                    post.setText("In onResponse" + response.body());
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                post.setText("Failure >>>>>>>>>>>>>>>" + t.toString());
            }
        });



                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofitUploader = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://192.168.100.122:1337/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface uploadInterface = retrofitUploader.create(ApiInterface.class);



//        String root = Environment.getExternalStorageDirectory().toString();
//        File file = new File(root + "/Test/test.jpg");
//
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File file  = new File("/mnt/sdcard/Android/data/com.example.visitormgmt/files/Pictures/96404231276176629463060110249.jpg");
        Log.d("Upload", file.getAbsolutePath());

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("files", file.getName(),
                requestBody

        );
        RequestBody ref = RequestBody.create(MediaType.parse("text/plain"), "visitor");
        RequestBody refId = RequestBody.create(MediaType.parse("text/plain"), "196");
        RequestBody field = RequestBody.create(MediaType.parse("text/plain"), "Photo");




        Call<Object> call_img = uploadInterface.uploadImagePost(
                token,
                fileToUpload,
                ref,
                refId,
                field
                );

        call_img.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (!response.isSuccessful()) {
//                    textViewResult2.setText(">>" + response.raw().toString());
                    System.out.println(response.raw().toString());
                    return;
                }else{

//                    textViewResult2.setText(">>>>>>>>>>>>>>>>>>>" + response.toString());
                    System.out.println(response.toString());

                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
//                textViewResult2.setText("onFailure " + t.toString());
            }
        });






        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(PreviewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) PreviewActivity.this, PERMISSIONS, REQUEST);
            }
        }

    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                    createPdf();
                } else {
                    Toast.makeText(PreviewActivity.this, "The app was not allowed to read your store.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
