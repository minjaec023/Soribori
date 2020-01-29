package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Name_registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_registration);

        TextView textView1 = (TextView)findViewById(R.id.textView4);
        String UserName = textView1.getText().toString();
    }
}
