package com.example.soribori;

import androidx.annotation.NonNull;
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
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
import java.util.Timer;
import java.util.TimerTask;

public class recording_hm extends toolbar_hm {

    private WaveHelper mWaveHelper;
    ImageView mic_imageview;
    ImageView stop_imageview;
    ImageView storage_imageview;
    TextView timer_text;
    private int mBorderColor = Color.parseColor("#6BDBD9DF");
    private int mBorderWidth = 10;


    /////////////////////timer count var///////////////////
    String m_display_string;

    // //////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_hm);

        Toolbar toolbar_hm = (Toolbar) findViewById(R.id.toolbar_hm);
        toolbar_hm.setBackgroundColor(Color.parseColor("#1B1B2F"));
        TextView toolbar_title = (TextView) toolbar_hm.findViewById(R.id.toolbar_title);
        toolbar_title.setTextColor(Color.WHITE);
        toolbar_title.setText("Record");

        TextView timer_text = (TextView)findViewById(R.id.timer_textview);
        TextView soribori_textView = (TextView) findViewById(R.id.soribori_name);


        ////////////////////////////////////////////////////////////



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
        ////////////////////////wave veiw///////////////////////////
        ////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////


        //////////////////////btn/////////////////////////////////
        mic_imageview = (ImageView) findViewById(R.id.mic_imageview);
        mic_imageview.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("click", "hi");
                mic_imageview.setSelected(!mic_imageview.isSelected());
                if(mic_imageview.isSelected() == true) {

                    //일시정지버튼 눌렀을 때
                    if(waveView.isShowWave() == true) {

                        mWaveHelper.cancel();
                    }
                }
                else if(mic_imageview.isSelected() == false){

                    //마이크 버튼 눌렀을 때

                    mWaveHelper.start();
                }
            }
        });

        stop_imageview = (ImageView) findViewById(R.id.stop_imageview);
        stop_imageview.setOnTouchListener(new ImageView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                } else if (event.getAction() == MotionEvent.ACTION_UP) {

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


    /*
    Runnable m_display_run = new Runnable() {
        public void run()
        {
            // 텍스트뷰는 시간을 출력한다.
            timer_text.setText(m_display_string);
        }
    };
    
    class UpdateTimeTask extends TimerTask
    {
        public void run()
        {
            // 현재 시간을 밀리초 단위로 얻어온다.
            long m_current_time = System.currentTimeMillis();
            // 현재 시간에서 시작 시간을 빼서 경과시간을 얻는다.
            long millis = m_current_time - m_start_time;
            // 밀리초에 1000을 나누어 초단위로 변경한다.
            int seconds = (int) (millis / 1000);
            // 초단위의 시간정보를 60으로 나누어 분단위로 변경한다.
            int minutes = seconds / 60;
            // 초단위의 시간정보를 60으로 나머지 연산하여 초 값을 얻는다.
            seconds = seconds % 60;
            // 밀리초 단위의 시간정보를 10으로 나눈 몫에 100으로 나머지 연산을 하여
            // 100의 자리와 10의 자리만 출력될 수 있도록 한다.
            millis = (millis / 10) % 100;
            // 출력할 문자열을 구성한다.
            m_display_string = String.format("%02d : %02d : %02d", minutes, seconds, millis);
            // 해당 문자열을 출력하도록 메인 쓰레드에 전달한다.
            timer_text.post(m_display_run);
        }
    }

    public void startStopWatch()
    {
        if(m_timer == null) {
            // 현재 시간에서 이전에 중지했던 시간(경과시간 - 시작시간)을 뺀다.
            // 5초에서 중지를 누른 후 다시 시작을 누르면 1초후에 6초가 될 수 있도록 연산한다.
            m_start_time = System.currentTimeMillis() - (m_current_time - m_start_time);
            // 타이머 객체를 생성한다.
            m_timer = new Timer();
            // 사용자정의 TimerTask 객체를 넘겨주고, 100 밀리초 후에 수행되며,
            // 200 밀리마다 반복 수행되도록 설정한다.
            m_timer.schedule(new UpdateTimeTask(), 100, 200);
        }
    }

    public void stopStopWatch()
    {
        if(m_timer != null) {
            // 해당 타이머가 수행할 모든 행위들을 정지시킵니다.
            m_timer.cancel();
            // 대기중이던 취소된 행위가 있는 경우 모두 제거한다.
            m_timer.purge();
            m_timer = null;
        }
    }
    */
}
