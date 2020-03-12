package com.example.soribori;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.impl.util.PermissionUtils;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 *
 * @author Daum Communications Corp.
 * @since 2013
 *
 */


public class MainActivity extends ToolbarActivity implements View.OnClickListener, SpeechRecognizeListener {

    private static final int REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE = 1;
    private SpeechRecognizerClient client;
    String User_Name;
    boolean AutoRunning = true;
    boolean isRecording = false;
    boolean isPlaying = false;
    MediaRecorder recorder;


    ////////////////////////////
    int recording_counter = 0; //카운터
    ////////file hardcording//////////
    String filepath01;
    String filepath02;
    String filepath03;
    String filepath04;
    String filepath05;
    String filepath06;
    String filepath07;
    String filepath08;
    String filepath09;
    String filepath10;
    //////////////////////////////////


    MediaPlayer player;
    int position = 0; // 다시 시작 기능을 위한 현재 재생 위치 확인 변수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //앱 해시키가 있어야 API 콜이 정상작동하는데 과연 다른 컴퓨터에서도 될지?
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //activity_main을 screen으로 설정
        setContentView(R.layout.activity_main);

        //유저에게 권한 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE);
            } else {
                // 유저가 거부하면서 다시 묻지 않기를 클릭, 권한이 없다고 유저에게 직접 알림.
            }
        } else {
            //startUsingSpeechSDK();
        }

        // SDK library 초기화
        // API를 사용할 시점이 되면, initializeLibrary(Context)를 호출
        // 사용을 마치면 꼭!!!!!!!!!!!!! finalizeLibrary()를 호출해야 함
        SpeechRecognizerManager.getInstance().initializeLibrary(this);



        /////////////녹음 관련/////////////
        recorder = new MediaRecorder();
        //////////////////////////////////
        File myrecordingfile01 = new File( this.getFilesDir() ,"recordefile01.wav");
        filepath01 = myrecordingfile01.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath01);
        //////////////////////////////////
        File myrecordingfile02 = new File( this.getFilesDir() ,"recordefile02.wav");
        filepath02 = myrecordingfile02.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath02);
        //////////////////////////////////
        File myrecordingfile03 = new File( this.getFilesDir() ,"recordefile03.wav");
        filepath03 = myrecordingfile03.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath03);
        //////////////////////////////////
        File myrecordingfile04 = new File( this.getFilesDir() ,"recordefile04.wav");
        filepath04 = myrecordingfile04.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath04);
        //////////////////////////////////
        File myrecordingfile05 = new File( this.getFilesDir() ,"recordefile05.wav");
        filepath05 = myrecordingfile05.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath05);
        //////////////////////////////////
        File myrecordingfile06 = new File( this.getFilesDir() ,"recordefile06.wav");
        filepath06 = myrecordingfile06.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath06);
        //////////////////////////////////
        File myrecordingfile07 = new File( this.getFilesDir() ,"recordefile07.wav");
        filepath07 = myrecordingfile07.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath07);
        //////////////////////////////////
        File myrecordingfile08 = new File( this.getFilesDir() ,"recordefile08.wav");
        filepath08 = myrecordingfile08.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath08);
        //////////////////////////////////
        File myrecordingfile09 = new File( this.getFilesDir() ,"recordefile09.wav");
        filepath09 = myrecordingfile09.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath09);
        //////////////////////////////////
        File myrecordingfile10 = new File( this.getFilesDir() ,"recordefile10.wav");
        filepath10 = myrecordingfile10.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath10);


        // 버튼 클릭 리스너 등록
        findViewById(R.id.Voice_recognition_start).setOnClickListener(this);
        findViewById(R.id.Voice_recognition_stop).setOnClickListener(this);
        findViewById(R.id.Voice_recognition_auto).setOnClickListener(this);
        findViewById(R.id.Voice_recognition_auto_stop).setOnClickListener(this);
        findViewById(R.id.auto_recording_recognition).setOnClickListener(this);
        findViewById(R.id.stop_recording_recognition).setOnClickListener(this);
        findViewById(R.id.name_update).setOnClickListener(this);
        findViewById(R.id.recoding1).setOnClickListener(this);

        findViewById(R.id.recording_play_bt).setOnClickListener(this);
        findViewById(R.id.play_stop).setOnClickListener(this);
        setButtonsStatus(true);

        // 클라이언트 생성
        // ex code ===>> SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WEB).setUserDictionary(userdict);  // optional

        //등록한 사용자 이름이 날아가면 안되니까
        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
        SharedPreferences sf = getSharedPreferences("sFile",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        String text = sf.getString("UserName","");
        User_Name = sf.getString("UserName","");
        TextView usernametext = (TextView)findViewById(R.id.User_name_textview);
        usernametext.setText(text);


    }




    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        //API를 더이상 사용하지 않을 때 finalizeLibrary()를 호출함.
        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }

    private void setButtonsStatus(boolean enabled) {
        findViewById(R.id.Voice_recognition_start).setEnabled(enabled);
        findViewById(R.id.Voice_recognition_stop).setEnabled(!enabled);
    }

    //이건 사용 안할지도?
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //startUsingSpeechSDK();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        String serviceType = SpeechRecognizerClient.SERVICE_TYPE_DICTATION;

        // Voice_recognition_start 버튼을 클릭했을때
        if (id == R.id.Voice_recognition_start) {
            if(PermissionUtils.checkAudioRecordPermission(this)) {

                SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
                        setServiceType(serviceType);

                /**
                고립어용
                 if (serviceType.equals(SpeechRecognizerClient.SERVICE_TYPE_DICTATION)) {


                    Log.i("SpeechSampleActivity", "word list : " + wordList.replace('\n', ','));
                }
                */

                client = builder.build();

                client.setSpeechRecognizeListener(this);
                client.startRecording(true);

                setButtonsStatus(false);
            }
        }

        // 음성 인식 자동시작
        // 자동 시작하면 2초정도로 끊어서 계속 인식하게 된다
        else if (id == R.id.Voice_recognition_auto) {
            AutoRunning = true;
            if(PermissionUtils.checkAudioRecordPermission(this)) {

                //쓰레드 시작
                try {
                    ExampleThread thread = new ExampleThread();
                    thread.setDaemon(true);
                    thread.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        // 자동시작 stop 하는 버튼
        else if (id == R.id.Voice_recognition_auto_stop) {
            if (client != null) {
                client.stopRecording();
            }
            AutoRunning = false;
        }

        // 음성인식 중지버튼 listener
        else if (id == R.id.Voice_recognition_stop) {
            if (client != null) {
                client.stopRecording();
            }
        }

        // 녹음 + api 시작
        else if (id == R.id.auto_recording_recognition){
            //녹음
            //settingAudio();
            //isRecording = true;
            //recordAudio();
            ///////////////////////////////
            //음성인식//////////////////////
            Log.e("Debug", "im hear111");

            AutoRunning = true;
            if(PermissionUtils.checkAudioRecordPermission(this)) {
                //쓰레드 시작
                try {
                    ExampleThread thread = new ExampleThread();
                    ExampleThread2 thread2 = new ExampleThread2();
                    thread.setDaemon(true);
                    thread2.setDaemon(true);
                    thread.start();

                    Log.e("Debug", "im hear222");
                    thread2.start();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }



        }

        // 녹음 + api 중지
        else if (id == R.id.stop_recording_recognition){
            //녹음중지
            isRecording = false;
            try {
                finishRecording();
            }catch (Exception e){
                Log.i("Recording","finish Recording");
            }
            //음성인식중지
            if (client != null) {
                client.stopRecording();
            }
        }

        //이름 업데이트 버튼 클릭했을때
        else if (id == R.id.name_update){
             // 이름 등록 페이지로부터 이름 받아오기

             Intent intent = new Intent(MainActivity.this , Name_registration.class);
             startActivityForResult(intent, 1234);
            // String user_name = intent.getStringExtra("UserName");
            // System.out.println("!!!!!!!!!!!!!!! Name : " + user_name);
            // tx1.setText(user_name);
        }

        //레코딩 버튼 클릭했을 때
        else if (id == R.id.recoding1){
            Intent i3 = new Intent(getApplicationContext(), dv_recording.class);
            startActivity(i3);
        }

        //녹음된거 재생
        else if (id == R.id.recording_play_bt){
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


        //재생하던거 중지
        else if (id == R.id.play_stop){
            if(isPlaying == true) {
                player.stop();
                isPlaying = false;
            }
        }

        //녹음된거 나중에 재생해볼려면
        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
            }
        });



    }

    private class ExampleThread extends Thread {
        private static final String TAG = "ExampleThread";
        public void run() {
            Log.i(TAG, "THREAD RUN");

            final String serviceType = SpeechRecognizerClient.SERVICE_TYPE_DICTATION;

            Looper.prepare();
            try {
                SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().setServiceType(serviceType);
                client = builder.build();
                client.setSpeechRecognizeListener(MainActivity.this);
                client.startRecording(true);
                sleep(3000);
                client.stopRecording();
                sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Looper.loop();
        }
    }

    private class ExampleThread2 extends Thread {
        private static final String TAG2 = "ExampleThread2";
        public void run() {
            Log.i(TAG2, "THREAD RUN2");
            while(AutoRunning) {
                try {
                    Log.e("Debug", "im hear333");
                    settingAudio();
                    isRecording = true;
                    Log.e("Debug", "im hear444");
                    recordAudio();
                    isRecording = false;
                    sleep(3000);
                    Log.e("Debug", "im hear555");
                    stopRecording();
                    sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                    AutoRunning = false;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView tx1 = (TextView)findViewById(R.id.User_name_textview);
        TextView thread01_tx2 = (TextView)findViewById(R.id.Thread01_Textview);


        if(requestCode == 1234 && resultCode == RESULT_OK){
            String user_name = data.getStringExtra("UserName");
            tx1.setText(user_name);
        }
        else if (resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(VoiceRecognizeActivity.EXTRA_KEY_RESULT_ARRAY);

            final StringBuilder builder = new StringBuilder();

            for (String result : results) {
                builder.append(result);
                builder.append("\n");
            }

            thread01_tx2.setText(builder.toString());


        } else if (requestCode == RESULT_CANCELED) {
            // 음성인식의 오류 등이 아니라 activity의 취소가 발생했을 때.
            if (data == null) {
                return;
            }

            int errorCode = data.getIntExtra(VoiceRecognizeActivity.EXTRA_KEY_ERROR_CODE, -1);
            String errorMsg = data.getStringExtra(VoiceRecognizeActivity.EXTRA_KEY_ERROR_MESSAGE);

            if (errorCode != -1 && !TextUtils.isEmpty(errorMsg)) {
                new AlertDialog.Builder(this).
                        setMessage(errorMsg).
                        setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();



            }
        }
    }


    @Override
    public void onReady() {
        //TODO implement interface
    }

    @Override
    public void onBeginningOfSpeech() {
        //TODO implement interface
    }

    @Override
    public void onEndOfSpeech() {
        //TODO implement interface
    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        //TODO implement interface
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setButtonsStatus(true);
            }
        });

        Log.i("onError","on error thread start");
        if(AutoRunning == true) {
            try {
                ExampleThread thread = new ExampleThread();
                thread.setDaemon(true);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("onError", "on error end");
        }

        client = null;
    }

    @Override
    public void onPartialResult(String partialResult) {
        //TODO implement interface
    }

    @Override
    public void onResults(Bundle results) {
        //TODO implement interface
        //써야할듯
        final StringBuilder builder = new StringBuilder();
        Log.i("SpeechSampleActivity", "onResults");
        final TextView thread01_tx1 = (TextView)findViewById(R.id.Thread01_Textview);
        ArrayList<String> texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        ArrayList<Integer> confs = results.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);

        for (int i = 0; i < texts.size(); i++) {
            builder.append(texts.get(i));
            builder.append(" (");
            builder.append(confs.get(i).intValue());
            builder.append(")\n");
        }

        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // finishing일때는 처리하지 않는다.
                if (activity.isFinishing()) return;

                thread01_tx1.setText(builder.toString());

                //이름이 포함되어있으면 알림
                if(builder.toString().contains(User_Name) == true) {
                    Toast.makeText(getApplicationContext(), "누군가가 당신의 이름을 부르고 있습니다!", Toast.LENGTH_LONG).show();
                }
                    //이부분은 테스트용 안녕 들리면 알림줌.
                if(builder.toString().contains("안녕") == true){
                    Toast.makeText(getApplicationContext(),"누군가가 당신에게 인사하고 있어요!!", Toast.LENGTH_LONG).show();
                }
                setButtonsStatus(true);
            }
        });

        client = null;

        Log.i("SpeechSampleActivity", "onResults End");
        if(AutoRunning==true) {
            try {
                ExampleThread thread = new ExampleThread();
                thread.setDaemon(true);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("ExmapleThread", "onresult loop end");
        }

    }

    @Override
    public void onAudioLevel(float audioLevel) {
        //TODO implement interface
    }

    @Override
    public void onFinished() {
        if(AutoRunning == true){
            try {
                ExampleThread thread = new ExampleThread();
                thread.setDaemon(true);
                thread.start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Log.i("SpeechSampleActivity","onFinished");
        //TODO implement interface
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        //TODO implement interface
    }

    //녹음 초기 세팅 함수
    private void settingAudio(){
        /* 그대로 저장하면 용량이 크다.
         * 프레임 : 한 순간의 음성이 들어오면, 음성을 바이트 단위로 전부 저장하는 것
         * 초당 15프레임 이라면 보통 8K(8000바이트) 정도가 한순간에 저장됨
         * 따라서 용량이 크므로, 압축할 필요가 있음 */
        if (recorder == null){
            recorder = new MediaRecorder();
        }
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // Microphone audio source 로 부터 음성 데이터를 받음
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 압축 형식 설정
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        if(recording_counter == 0) {
            recorder.setOutputFile(filepath01);
            Log.i("Recording","filepath01");
            recording_counter++;
        }
        else if(recording_counter == 1){
            recorder.setOutputFile(filepath02);
            Log.i("Recording","filepath02");
            recording_counter++;
        }
        else if(recording_counter == 2){
            recorder.setOutputFile(filepath03);
            Log.i("Recording","filepath03");
            recording_counter++;
        }
        else if(recording_counter == 3){
            recorder.setOutputFile(filepath04);
            Log.i("Recording","filepath04");
            recording_counter++;
        }
        else if(recording_counter == 4){
            recorder.setOutputFile(filepath05);
            Log.i("Recording","filepath05");
            recording_counter++;
        }
        else if(recording_counter == 5){
            recorder.setOutputFile(filepath06);
            Log.i("Recording","filepath06");
            recording_counter++;
        }
        else if(recording_counter == 6){
            recorder.setOutputFile(filepath07);
            Log.i("Recording","filepath07");
            recording_counter++;
        }
        else if(recording_counter == 7){
            recorder.setOutputFile(filepath08);
            Log.i("Recording","filepath08");
            recording_counter++;
        }
        else if(recording_counter == 8){
            recorder.setOutputFile(filepath09);
            Log.i("Recording","filepath09");
            recording_counter++;
        }
        else if(recording_counter == 9){
            recorder.setOutputFile(filepath10);
            Log.i("Recording","filepath10");
            recording_counter = 0;
        }
        try {
            recorder.prepare();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    ///녹음 시작 함수///
    private void recordAudio(){
            recorder.start();
            Log.i("Recording", "녹음 진행중");
    }

    ///녹음 중지(완료) 함수///
    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.reset();
            Log.i("Recording", "녹음 중지됨");

        }
    }

    //녹음 완전 중지 함수//
    private void finishRecording(){
        recorder.stop();
        recorder.release();
        recorder = null;
        AutoRunning = false;
        Log.i("Recording","녹음 종료됨");
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
            player.setDataSource(filepath01);
            player.prepare();
            player.start();

            Toast.makeText(this, "재생 시작됨.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
