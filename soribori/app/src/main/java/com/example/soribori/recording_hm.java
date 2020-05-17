package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.gelitenight.waveview.library.WaveView;

import java.util.AbstractQueue;

public class recording_hm extends toolbar_hm {

    private WaveHelper mWaveHelper;
    ImageView mic_imageview;
    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_hm);

        Toolbar toolbar_hm = (Toolbar) findViewById(R.id.toolbar_hm);
        TextView toolbar_title = (TextView) toolbar_hm.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Record");


        mic_imageview = (ImageView) findViewById(R.id.mic_imageview);
        mic_imageview.setBackgroundResource(R.drawable.myshaperound_mic);
        mic_imageview.setClipToOutline(true);
        mic_imageview.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("click", "hi");
                mic_imageview.setSelected(!mic_imageview.isSelected());
            }
        });

        final WaveView waveView;
        waveView = (WaveView) findViewById(R.id.waveView1);
        waveView.setBorder(mBorderWidth, mBorderColor);

        mWaveHelper = new WaveHelper(waveView);

        waveView.setShapeType(WaveView.ShapeType.CIRCLE);
        waveView.setWaveColor(
                Color.parseColor("#88b8f1ed"),
                Color.parseColor("#b8f1ed")
        );

        mBorderColor = Color.parseColor("#b8f1ed");
        waveView.setBorder(mBorderWidth, mBorderColor);

        Button waveview_btn = (Button) findViewById(R.id.waveview_btn);
        waveview_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWaveHelper.start();
            }
        });

        Button waveview_btn2 = (Button) findViewById(R.id.waveview_btn2);
        waveview_btn2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(waveView.isShowWave() == true) {
                    mWaveHelper.cancel();
                }
            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveHelper.start();
    }

}
