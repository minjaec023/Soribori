package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try{

            Thread.sleep(1000);


        }catch (InterruptedException e) {

            e.printStackTrace();

        }


        Intent intent= new Intent(this, recording_hm.class);

        startActivity(intent);

        finish();




    }

}


