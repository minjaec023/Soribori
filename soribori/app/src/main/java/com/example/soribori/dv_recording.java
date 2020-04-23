package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class dv_recording extends AppCompatActivity {
    boolean isRecording = false;
    boolean isPlaying = false;
    MediaRecorder recorder;
    String filepath;
    MediaPlayer player;
    int position = 0; // 다시 시작 기능을 위한 현재 재생 위치 확인 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dv_recording);

        /////////////녹음 진행/////////////
        recorder = new MediaRecorder();
        File myrecordingfile = new File( this.getFilesDir() ,"recordefile.wav");
        filepath = myrecordingfile.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath);


        //녹음 시작 버튼//
        findViewById(R.id.start_recording).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRecording = true;
                recordAudio();

            }
        });

        //녹음 중지 버튼//
        findViewById(R.id.stop_recording).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRecording = false;
                stopRecording();
            }
        });


        //녹음된 소리 재생 버튼//
        findViewById(R.id.playaudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying == false) {
                    try {
                        playAudio();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    isPlaying = true;
                    player.start();
                }
            }
        });


        //녹음된 소리 재생 중지 버튼//
        findViewById(R.id.stopaudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying == true) {
                    player.stop();
                    isPlaying = false;
                }
            }
        });

        //녹음 시작 자동 버튼//
        findViewById(R.id.auto_recording_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //녹음 자동 중지 버튼//
        findViewById(R.id.auto_recording_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
            }
        });

    }

    ///녹음 시작 함수///
    private void recordAudio(){


        /* 그대로 저장하면 용량이 크다.
         * 프레임 : 한 순간의 음성이 들어오면, 음성을 바이트 단위로 전부 저장하는 것
         * 초당 15프레임 이라면 보통 8K(8000바이트) 정도가 한순간에 저장됨
         * 따라서 용량이 크므로, 압축할 필요가 있음 */

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // Microphone audio source 로 부터 음성 데이터를 받음
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 압축 형식 설정
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(filepath);

        try{
            recorder.prepare();
            recorder.start();

            Toast.makeText(this,"녹음이 진행중입니다.",Toast.LENGTH_SHORT).show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///녹음 중지(완료) 함수///
    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            Toast.makeText(this, "녹음 중지됨.", Toast.LENGTH_SHORT).show();
        }
    }

    //녹음된 소리 파일 재생 중지 함수//
    public void closePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    //녹음된 소리 파일 재생 함수//
    private void playAudio() {
        try {
            closePlayer();

            player = new MediaPlayer();
            player.setDataSource(filepath);
            player.prepare();
            player.start();

            Toast.makeText(this, "재생 시작됨.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
