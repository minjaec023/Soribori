package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class register_hm extends toolbar_hm {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_hm);

        Toolbar toolbar_hm = (Toolbar) findViewById(R.id.toolbar_hm);
        toolbar_hm.setBackgroundColor(Color.parseColor("#1B1B2F"));
        TextView toolbar_title = (TextView) toolbar_hm.findViewById(R.id.toolbar_title);
        toolbar_title.setTextColor(Color.WHITE);
        toolbar_title.setText("Register");

        ImageView custom_imgview = (ImageView) findViewById(R.id.Custom_img_view);
        custom_imgview.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(register_hm.this, register_custom_hm.class);
                startActivity(intent1);
            }
        });

        ImageView name_imgview = (ImageView) findViewById(R.id.Name_img_view);
        name_imgview.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(register_hm.this, register_name_hm.class);
                startActivity(intent2);
            }
        });

    }
}
