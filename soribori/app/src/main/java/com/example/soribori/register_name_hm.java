package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class register_name_hm extends toolbar_hm {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name_hm);

        Toolbar toolbar_hm = (Toolbar) findViewById(R.id.toolbar_hm);
        toolbar_hm.setBackgroundColor(Color.parseColor("#1B1B2F"));
        TextView toolbar_title = (TextView) toolbar_hm.findViewById(R.id.toolbar_title);
        toolbar_title.setTextColor(Color.WHITE);
        toolbar_title.setText("Register");

    }
}
