package com.example.visitormgmt;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
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
import android.os.StrictMode;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import androidx.core.content.FileProvider;

import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
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
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.provider.SettingsSlicesContract.AUTHORITY;


public class PreviewActivity extends AppCompatActivity {

    public String fname, lname, mobile, email1, image, idproof,company,image_path,idproof_path
            , purpose, meet_whom, visitor_id, address, city, state,country, status  ;
    public int userExistingOrNot;
    Bitmap img_src, id_src;
    TextView status_code;
    ImageView img1;
    CircleImageView img2;

    private RelativeLayout llPdf;
    private Bitmap bitmap;
    private String dir;

    private static final int REQUEST = 112;
    private String mCurrentPhotoPath;

    public String token = "Bearer "+"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaXNBZG1pbiI6dHJ1ZSwiaWF0IjoxNTcxMjA4MjkyLCJleHAiOjE1NzM4MDAyOTJ9.fxBbFQ29gqQ-vPRDws0zHKIw3l0tEdB0rEfBvaJSVfs";



    public RetrofitInterface ApiInterfaceObject;

    protected void onCreate(Bundle savedInstanceState) {
        changeStatusBarColor("#828ffc");
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.preview1);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        img1 = (ImageView) findViewById(R.id.img_src);
        img2 = (CircleImageView) findViewById(R.id.id_src);
        status_code = (TextView)findViewById(R.id.code_status);

        //[Geting data which is entered by user using shared preferences which is stored previous page]

        SharedPreferences sharedpreferences;
        sharedpreferences =  getSharedPreferences("MyPrefs", MODE_PRIVATE);
        fname = sharedpreferences.getString("FirstName", "");
        lname = sharedpreferences.getString("LastName", "");
        mobile = sharedpreferences.getString("Phone", "");
        email1 = sharedpreferences.getString("Email", "");
        image = sharedpreferences.getString("Image", "");
        idproof = sharedpreferences.getString("IdProof", "");
        city = sharedpreferences.getString("City", "");
        address = sharedpreferences.getString("Address", "");
        state = sharedpreferences.getString("State", "");
        country = sharedpreferences.getString("Country", "");

        company = sharedpreferences.getString("Company", "");
        purpose = sharedpreferences.getString("Purpose", "");
        meet_whom = sharedpreferences.getString("MeetWhom", "");
        status =  sharedpreferences.getString("status", "");
        image_path = sharedpreferences.getString("ImagePath", "");
        idproof_path = sharedpreferences.getString("IdProofPath", "");
        visitor_id = sharedpreferences.getString("VisitorId", "");



        System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>> Enter in function calling createPost");
        Log.e("MYAPP", "response I am in function" );


        if(sharedpreferences.getString("Existing", "").equals("0")){
            updateVisitorDetails();
        } else {
            createPost();
        }
//        if(userExistingOrNot == 1){
//           // createPostVisitInfo(visitor_id);
//        } else {
//
//        }
        Log.e("MYAPP", "response I am after function" );
        System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>> Enter in function updateQRcode");

        //[updating QR code function] //[Geting data which is entered by user using shared preferences which is stored previous page]
        UpdateQRCode();

         img_src = StringToBitMap(image);
        //id_src = StringToBitMap(idproof);


        img2.setImageBitmap(id_src);

        Button print = (Button) findViewById(R.id.print);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("MYAPP", "response I am in pdf creation function" );

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

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 Intent VisitorThankyou = new Intent(PreviewActivity.this, ThankYou.class);
                 startActivity(VisitorThankyou);
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


        File PdfFile = null;
        try {

            PdfFile = createPdfFile();
        } catch (IOException ex) {
            ex.printStackTrace();
            // Error occurred while creating the File
        }

        try {
            if (PdfFile != null) {
                //OutputStream out = new FileOutputStream(outputFile);
                document.writeTo(new FileOutputStream(PdfFile));
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(this, "PDF is created!!!", Toast.LENGTH_SHORT).show();


    }

    private void createPost() {

        Log.e("MYAPP", "response I am in createPost function" );
        System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>> Enter in function create post");


        OkHttpClient client1 = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000,TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client1)
                .baseUrl("http://192.168.100.187:3001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterfaceObject = retrofit.create(RetrofitInterface.class);


        JsonObject fields = new JsonObject();
//        fields.addProperty("lastName", lname);
        fields.addProperty("status", status);
        fields.addProperty("name", fname);
        fields.addProperty("contactno",mobile );
        fields.addProperty("address", address);
        fields.addProperty("organization", company);
        fields.addProperty("purpose", purpose);
        fields.addProperty("contactPerson", meet_whom);
        fields.addProperty("emailId", email1 );
        fields.addProperty("idProof", "gggggghg");
        fields.addProperty("country", country);
        fields.addProperty("city", city);
        fields.addProperty("state", state);
        fields.addProperty("pic", "zzaswefwef");
        fields.addProperty("expecteddate", "12:23:34");
        fields.addProperty("expectedtime", "12:23:34");






        Call<Object> call = ApiInterfaceObject.createPost(fields);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


                if (!response.isSuccessful()) {
                    status_code.setText("Failed To create");
                    Log.e("MYAPP", "Failed to create" );
                    return;
                }else{
                    System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>>codeing" + response.code());
                    status_code.setText("Checked in Sucessfully");
                    Log.e("MYAPP", "created" );
                    Log.e("MYAPP", "created" + response.body().toString());
                    //createPostImage(visitorId, image_path, 0);
                    //createPostImage(visitorId, idproof_path, 1);
                    //createPostVisitInfo(visitorId);
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("MYAPP", "failed" );
                System.out.print(">>>>>>>>>>>>>>..Error");
                t.printStackTrace();
                System.out.print(">>>>>>>>>>>>>>..Error");
            }
        });


    }
    private void updateVisitorDetails() {


        System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>> Enter in function update post");


        OkHttpClient client1 = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000,TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client1)
                .baseUrl("http://192.168.100.187:3001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterfaceObject = retrofit.create(RetrofitInterface.class);


        JsonObject fields = new JsonObject();
//        fields.addProperty("lastName", lname);
        fields.addProperty("status", "Mr");
        fields.addProperty("name", fname);
        fields.addProperty("contactno",mobile );
        fields.addProperty("address", address);
        fields.addProperty("organization", company);
        fields.addProperty("purpose", purpose);
        fields.addProperty("contactPerson", meet_whom);
        fields.addProperty("emailId", email1 );
        fields.addProperty("idProof", "gggggghg");
        fields.addProperty("country", country);
        fields.addProperty("city", city);
        fields.addProperty("state", state);
        fields.addProperty("pic", "zzaswefwef");
        fields.addProperty("expecteddate", "12:23:34");
        fields.addProperty("expectedtime", "12:23:34");





        Call<Object> call = ApiInterfaceObject.updateExisting(fields);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


                if (!response.isSuccessful()) {
                    status_code.setText("Failed To create");
                    return;
                }else{
                    System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>>code" + response.code());
                    status_code.setText("Checked in Sucessfully");
                    Log.e("MYAPP", "created" );
                    Log.e("MYAPP", "created" + response.body().toString());
                    //createPostImage(visitorId, image_path, 0);
                    //createPostImage(visitorId, idproof_path, 1);
                    //createPostVisitInfo(visitorId);
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                System.out.print(">>>>>>>>>>>>>>>>>>>. Error");
                t.printStackTrace();

            }
        });


    }



//   private void createPostVisitInfo(String id){
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.100.122:1337/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        ApiInterfaceObject = retrofit.create(RetrofitInterface.class);
//
//        JsonObject fields = new JsonObject();
//        fields.addProperty("id", id);
//        fields.addProperty("Purpose", purpose);
//        fields.addProperty("MeetWhom", meet_whom );
//
//        Call<Post> call = ApiInterfaceObject.createPostVisitInfo( token, fields);
//
//        call.enqueue(new Callback<Post>() {
//            @Override
//            public void onResponse(Call<Post> call, Response<Post> response) {
//                // textViewResult.setText();
//                if (!response.isSuccessful()) {
////                    status_code.setText(response.toString());
//
//                    return;
//                }else{
//                    String visitorLogId = response.body().getid();
//                    createPostImage(visitorLogId, idproof_path, 2);
////                    status_code.setText("In onResponse" + response.body());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Post> call, Throwable t) {
//                t.printStackTrace();
////                status_code.setText("Failure >>>>>>>>>>>>>>>" + t.toString());
//            }
//        });
//    }
//
//    private void createPostImage(String id, String file_path, int fileType) {
//
//
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
//
//        Retrofit retrofitUploader = new Retrofit.Builder()
//                .client(client)
//                .baseUrl("http://192.168.100.122:1337/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RetrofitInterface uploadInterface = retrofitUploader.create(RetrofitInterface.class);
//        File file = new File(file_path);
//        Log.d("Upload", file.getAbsolutePath());
//
//        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
//        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("files", file.getName(),
//                requestBody
//
//        );
//        RequestBody ref;
//        RequestBody refId;
//        RequestBody field;
//
//        if(fileType == 0){
//             field = RequestBody.create(MediaType.parse("text/plain"), "Photo");
//             ref = RequestBody.create(MediaType.parse("text/plain"), "visitor");
//             refId = RequestBody.create(MediaType.parse("text/plain"), id);
//
//        }else if(fileType == 1){
//             field = RequestBody.create(MediaType.parse("text/plain"), "idProof");
//             ref = RequestBody.create(MediaType.parse("text/plain"), "visitor");
//             refId = RequestBody.create(MediaType.parse("text/plain"), id);
//
//        }else{
//             ref = RequestBody.create(MediaType.parse("text/plain"), "visitorlog");
//             refId = RequestBody.create(MediaType.parse("text/plain"), id);
//             field = RequestBody.create(MediaType.parse("text/plain"), "photo");
//
//
//        }
//
//
//
//        Call<Object> call_img = uploadInterface.uploadImagePost(
//                token,
//                fileToUpload,
//                ref,
//                refId,
//                field
//        );
//
//        call_img.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, Response<Object> response) {
//
//                if (!response.isSuccessful()) {
////                    textViewResult2.setText(">>" + response.raw().toString());
//                    System.out.println(response.raw().toString());
//                    return;
//                } else {
//
////                    textViewResult2.setText(">>>>>>>>>>>>>>>>>>>" + response.toString());
//                    System.out.println(response.toString());
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//                t.printStackTrace();
////                textViewResult2.setText("onFailure " + t.toString());
//            }
//        });
//    }
//
//*/



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

    private File createPdfFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String PdfFileName = "PDF";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        //File direct = new File(Environment.getExternalStorageDirectory() + "/Test1");

        File Pdf = File.createTempFile(
                PdfFileName,  /* prefix */
                ".pdf",         /* suffix */
                storageDir      /* directory */

        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = Pdf.getAbsolutePath();
        return Pdf;
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
