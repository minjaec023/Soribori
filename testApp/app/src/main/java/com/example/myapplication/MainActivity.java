package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
        Button btnSend = (Button) findViewById(R.id.button);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }
    public static void fileUpload (File file) {

        Log.d(TAG, "file===" + file.getName());

        final RequestBody requestBody;
        MultipartBody.Part body;
        LinkedHashMap<String, RequestBody> mapRequestBody = new LinkedHashMap<String, RequestBody>();
        List<MultipartBody.Part> arrBody = new ArrayList<>();


        requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        mapRequestBody.put("file\"; filename=\"" + file.getName(), requestBody);
        mapRequestBody.put("test", RequestBody.create(MediaType.parse("text/plain"), "gogogogogogogog"));


        body = MultipartBody.Part.createFormData("fileName", file.getName(), requestBody);
        arrBody.add(body);
        Log.d(TAG, "checkcheck");

        Call<JsonObject> call =
                RetrofitImg.getInstance().getService().uploadFile(mapRequestBody, arrBody);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {
                    Log.e(TAG, response.body().getAsString());
                    //Toast.showTo(response.body().getAsString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG + "Err", t.getMessage());
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode != 1 || resultCode != RESULT_OK){
            return;
        }
        Uri dataUri = data.getData();
        File file = new File(getPathFromUri(dataUri));
        fileUpload(file);
    }
    public String getPathFromUri(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();
        return path;
    }
}
