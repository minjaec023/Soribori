package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class register_custom_hm extends toolbar_hm {

    ImageView rec_mic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_custom_hm);

        Toolbar toolbar_hm = (Toolbar) findViewById(R.id.toolbar_hm);
        toolbar_hm.setBackgroundColor(Color.parseColor("#1B1B2F"));
        TextView toolbar_title = (TextView) toolbar_hm.findViewById(R.id.toolbar_title);
        toolbar_title.setTextColor(Color.WHITE);
        toolbar_title.setText("Register");

        final TextView textView1 = findViewById(R.id.loading_text);

        rec_mic = (ImageView) findViewById(R.id.rec_mic);
        rec_mic.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("click", "hi");
                rec_mic.setSelected(!rec_mic.isSelected());
                if(rec_mic.isSelected() == true) {
                    rec_mic.setBackgroundResource(R.drawable.new_new_rec_press_mic);
                    Log.i("click", "hi2");
                    textView1.setText("Recording...");

                }
                else if(rec_mic.isSelected() == false){
                    rec_mic.setBackgroundResource(R.drawable.new_new_rec_mic);
                    Log.i("click", "hi3");
                    textView1.setText("");
                }
            }
        });
    }
}
