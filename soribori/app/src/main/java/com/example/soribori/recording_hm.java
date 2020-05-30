package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
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
    ImageView stop_imageview;
    ImageView storage_imageview;
    private int mBorderColor = Color.parseColor("#6BDBD9DF");
    private int mBorderWidth = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_hm);

        Toolbar toolbar_hm = (Toolbar) findViewById(R.id.toolbar_hm);
        toolbar_hm.setBackgroundColor(Color.parseColor("#150024"));
        TextView toolbar_title = (TextView) toolbar_hm.findViewById(R.id.toolbar_title);
        toolbar_title.setTextColor(Color.WHITE);
        toolbar_title.setText("Record");

        TextView soribori_textView = (TextView) findViewById(R.id.soribori_name);
        soribori_textView.setPaintFlags( soribori_textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        ////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////
        ////////////////////////wave veiw//////////////////////////
        final WaveView waveView;
        waveView = (WaveView) findViewById(R.id.waveView1);
        waveView.setBorder(mBorderWidth, mBorderColor);

        mWaveHelper = new WaveHelper(waveView);

        waveView.setShapeType(WaveView.ShapeType.CIRCLE);
        waveView.setWaveColor(
                Color.parseColor("#DBD9DF"),
                Color.parseColor("#6BDBD9DF")
        );

        mBorderColor = Color.parseColor("#6BDBD9DF");
        waveView.setBorder(mBorderWidth, mBorderColor);

        ////////////////////////wave veiw//////////////////////////
        ////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////

        //btn
        mic_imageview = (ImageView) findViewById(R.id.mic_imageview);
        mic_imageview.setBackgroundResource(R.drawable.myshaperound_mic);
        mic_imageview.setClipToOutline(true);
        mic_imageview.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("click", "hi");
                mic_imageview.setSelected(!mic_imageview.isSelected());
                if(mic_imageview.isSelected() == true) {
                    mic_imageview.setBackgroundResource(R.drawable.myshaperound_pause);
                    mic_imageview.setClipToOutline(true);
                    //일시정지버튼 눌렀을 때
                    if(waveView.isShowWave() == true) {
                        mWaveHelper.cancel();
                    }
                }
                else if(mic_imageview.isSelected() == false){
                    mic_imageview.setBackgroundResource(R.drawable.myshaperound_mic);
                    mic_imageview.setClipToOutline(true);
                    //마이크 버튼 눌렀을 때
                    mWaveHelper.start();
                }
            }
        });

        stop_imageview = (ImageView) findViewById(R.id.stop_imageview);
        stop_imageview.setBackgroundResource(R.drawable.myshaperound_stop_on);
        stop_imageview.setClipToOutline(true);
        stop_imageview.setOnTouchListener(new ImageView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    stop_imageview.setBackgroundResource(R.drawable.myshaperound_stop_pressed);
                    stop_imageview.setClipToOutline(true);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stop_imageview.setBackgroundResource(R.drawable.myshaperound_stop_on);
                    stop_imageview.setClipToOutline(true);
                }
                return false;
            }
        });

        storage_imageview = (ImageView) findViewById(R.id.storage_imageview);
        storage_imageview.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("click", "hi3");
                Uri selectedUri = Uri.parse("/data/user/0/com.example.soribori/files/");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(selectedUri, "resource/folder");
                startActivity(Intent.createChooser(intent, "Open Folder"));
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
