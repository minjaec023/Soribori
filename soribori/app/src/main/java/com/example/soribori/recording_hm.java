package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;

public class recording_hm extends toolbar_hm {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_hm);

        Toolbar toolbar_hm = (Toolbar)findViewById(R.id.toolbar_hm);
        TextView toolbar_title = (TextView)toolbar_hm.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Record");

    }
}
