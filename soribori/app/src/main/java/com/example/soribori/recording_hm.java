package com.example.soribori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.gelitenight.waveview.library.WaveView;
import com.google.gson.JsonObject;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.impl.util.PermissionUtils;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class recording_hm extends toolbar_hm implements SpeechRecognizeListener {




    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// Wave view var //////////////////////////////////////////////////////////
    private WaveHelper mWaveHelper;
    ImageView mic_imageview;
    ImageView stop_imageview;
    ImageView storage_imageview;
    TextView timer_text;
    private int mBorderColor = Color.parseColor("#6BDBD9DF");
    private int mBorderWidth = 10;
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// Recording + Recognize var //////////////////////////////////////////////
    private static final int REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE = 1;
    private SpeechRecognizerClient client;
    String User_Name;
    boolean AutoRunning = true;
    boolean isRecording = false;
    boolean isPlaying = false;
    MediaRecorder recorder;
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////// Media Recorder File var ////////////////////////////////////////////////
    int recording_counter = 0; //카운터
    int file_select_num = 1; //파일 고르는 카운터
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
    MediaPlayer player;
    int position = 0; // 다시 시작 기능을 위한 현재 재생 위치 확인 변수
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_hm);


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////API call should work normally only if app hashing////////////////////////////////////
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

        // SDK library 초기화
        // API를 사용할 시점이 되면, initializeLibrary(Context)를 호출
        // 사용을 마치면 꼭!!!!!!!!!!!!! finalizeLibrary()를 호출해야 함
        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////Request permission from user/////////////////////////////////////////////////
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                // 유저가 거부하면서 다시 묻지 않기를 클릭, 권한이 없다고 유저에게 직접 알림.
            }
        } else {
            //startUsingSpeechSDK();
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////Toolbar Setting/////////////////////////////////////////////////////
        Toolbar toolbar_hm = (Toolbar) findViewById(R.id.toolbar_hm);
        toolbar_hm.setBackgroundColor(Color.parseColor("#1B1B2F"));
        TextView toolbar_title = (TextView) toolbar_hm.findViewById(R.id.toolbar_title);
        toolbar_title.setTextColor(Color.WHITE);
        toolbar_title.setText("Record");
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////TextView ///////////////////////////////////////////////////////
        TextView timer_text = (TextView)findViewById(R.id.timer_textview);
        TextView soribori_textView = (TextView) findViewById(R.id.soribori_name);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////Recording Path Setting////////////////////////////////////////////////////////
        recorder = new MediaRecorder();
        File myrecordingfile01 = new File( this.getFilesDir() ,"recordefile01.wav");
        filepath01 = myrecordingfile01.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath01);
        File myrecordingfile02 = new File( this.getFilesDir() ,"recordefile02.wav");
        filepath02 = myrecordingfile02.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath02);
        File myrecordingfile03 = new File( this.getFilesDir() ,"recordefile03.wav");
        filepath03 = myrecordingfile03.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath03);
        File myrecordingfile04 = new File( this.getFilesDir() ,"recordefile04.wav");
        filepath04 = myrecordingfile04.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath04);
        File myrecordingfile05 = new File( this.getFilesDir() ,"recordefile05.wav");
        filepath05 = myrecordingfile05.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath05);
        File myrecordingfile06 = new File( this.getFilesDir() ,"recordefile06.wav");
        filepath06 = myrecordingfile06.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath06);
        File myrecordingfile07 = new File( this.getFilesDir() ,"recordefile07.wav");
        filepath07 = myrecordingfile07.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath07);
        File myrecordingfile08 = new File( this.getFilesDir() ,"recordefile08.wav");
        filepath08 = myrecordingfile08.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath08);
        File myrecordingfile09 = new File( this.getFilesDir() ,"recordefile09.wav");
        filepath09 = myrecordingfile09.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath09);
        File myrecordingfile10 = new File( this.getFilesDir() ,"recordefile10.wav");
        filepath10 = myrecordingfile10.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath10);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////User Name Recognize Var////////////////////////////////////////////////
        //등록한 사용자 이름이 날아가면 안되니까
        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
        SharedPreferences sf = getSharedPreferences("sFile",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        String text = sf.getString("UserName","");
        User_Name = sf.getString("UserName","");
        //TextView usernametext = (TextView)findViewById(R.id.User_name_textview);
        //usernametext.setText(text);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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
        mWaveHelper.cancel();
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
                    //재생 버튼 눌렀을 때
                    isRecording = true;
                    AutoRunning = true;
                    // 녹음 + api 시작
                    Log.e("Debug", "im hear111");

                    AutoRunning = true;
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


                    //wave view motion start
                    mWaveHelper.start();

                }
                else if(mic_imageview.isSelected() == false){
                    //일시 정지 버튼 눌렀을 때

                    //녹음 중지
                    isRecording = false;
                    AutoRunning = false;
                    try {
                        finishRecording();
                    }catch (Exception e){
                        Log.i("Recording","finish Recording");
                    }
                    //음성 인식 중지
                    if (client != null) {
                        client.stopRecording();
                    }

                    mWaveHelper.cancel();
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


    private class ExampleThread extends Thread {
        private static final String TAG = "ExampleThread";
        public void run() {
            Log.i(TAG, "THREAD RUN");

            final String serviceType = SpeechRecognizerClient.SERVICE_TYPE_DICTATION;

            Looper.prepare();
            try {
                SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().setServiceType(serviceType);
                client = builder.build();
                client.setSpeechRecognizeListener(recording_hm.this);
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


                    ////// 여기에 post 코드 추가할것!
                    if(recording_counter == 0) {
                        File record_file10 = new File(recording_hm.this.getFilesDir(), "recordefile10.wav");
                        fileUpload(record_file10,1,null);
                    }
                    else if(recording_counter == 1) {
                        File record_file01 = new File(recording_hm.this.getFilesDir(), "recordefile01.wav");
                        fileUpload(record_file01,1,null);
                    }
                    else if(recording_counter == 2) {
                        File record_file02 = new File(recording_hm.this.getFilesDir(), "recordefile02.wav");
                        fileUpload(record_file02,1,null);
                    }
                    else if(recording_counter == 3) {
                        File record_file03 = new File(recording_hm.this.getFilesDir(), "recordefile03.wav");
                        fileUpload(record_file03,1,null);
                    }
                    else if(recording_counter == 4) {
                        File record_file04 = new File(recording_hm.this.getFilesDir(), "recordefile04.wav");
                        fileUpload(record_file04,1,null);
                    }
                    else if(recording_counter == 5) {
                        File record_file05 = new File(recording_hm.this.getFilesDir(), "recordefile05.wav");
                        fileUpload(record_file05,1,null);
                    }
                    else if(recording_counter == 6) {
                        File record_file06 = new File(recording_hm.this.getFilesDir(), "recordefile06.wav");
                        fileUpload(record_file06,1,null);
                    }
                    else if(recording_counter == 7) {
                        File record_file07 = new File(recording_hm.this.getFilesDir(), "recordefile07.wav");
                        fileUpload(record_file07,1,null);
                    }
                    else if(recording_counter == 8) {
                        File record_file08 = new File(recording_hm.this.getFilesDir(), "recordefile08.wav");
                        fileUpload(record_file08,1,null);
                    }
                    else if(recording_counter == 9) {
                        File record_file09 = new File(recording_hm.this.getFilesDir(), "recordefile09.wav");
                        fileUpload(record_file09,1,null);
                    }
                    ///////////////////////////////
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
        //TextView tx1 = (TextView)findViewById(R.id.User_name_textview);
        //TextView thread01_tx2 = (TextView)findViewById(R.id.Thread01_Textview);


        if(requestCode == 1234 && resultCode == RESULT_OK){
            String user_name = data.getStringExtra("UserName");
            //tx1.setText(user_name);
        }
        else if (requestCode != 1 || resultCode != RESULT_OK){
            Log.e("requestcode_resultcode","Error occur");
            return;
        }
        /*else if ( requestCode == 1 && resultCode == RESULT_OK ){
            Uri dataUri = data.getData();
            File file = new File(getPathFromUri(dataUri));
            fileUpload(file);
        }*/
        else if (resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(VoiceRecognizeActivity.EXTRA_KEY_RESULT_ARRAY);

            final StringBuilder builder = new StringBuilder();

            for (String result : results) {
                builder.append(result);
                builder.append("\n");
            }

            Log.i("kakao voice result","ff");
            //thread01_tx2.setText(builder.toString());

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
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveHelper.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //API를 더이상 사용하지 않을 때 finalizeLibrary()를 호출함.
        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int errorCode, String errorMsg) {

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

    }

    @Override
    public void onResults(Bundle results) {
        final StringBuilder builder = new StringBuilder();
        Log.i("SpeechSampleActivity", "onResults");
        //final TextView thread01_tx1 = (TextView)findViewById(R.id.Thread01_Textview);
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
                Log.i("name alarm test2","hi hi2");
                // thread01_tx1.setText(builder.toString());
                Log.i("kakao name test",builder.toString());

                Log.i("name alarm test","hi hi");
                //이름이 포함되어있으면 알림
                if (builder.toString().contains(User_Name) == true) {
                    Toast.makeText(getApplicationContext(), "누군가가 당신의 이름을 부르고 있습니다!", Toast.LENGTH_LONG).show();
                }//이부분은 테스트용 안녕 들리면 알림줌.

                if(builder.toString().contains("안녕") == true){
                    Toast.makeText(getApplicationContext(),"누군가가 당신에게 인사하고 있어요!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        client = null;

        Log.i("SpeechSampleActivity", "onResults End");
        if(AutoRunning == true) {
            try {
                ExampleThread thread = new ExampleThread();
                thread.setDaemon(true);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("ExmapleThread", "onResult loop end");
        }
    }

    @Override
    public void onAudioLevel(float audioLevel) {

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
        Log.e("Recording","recording counter value : " + recording_counter);
        if(recording_counter == 9) {
            recorder.setOutputFile(filepath10);
            Log.i("Recording","filepath10 : " + filepath10);
            recording_counter = 0;
        }
        else if(recording_counter == 8){
            recorder.setOutputFile(filepath09);
            Log.i("Recording","filepath09"+ filepath09);
            recording_counter++;
        }
        else if(recording_counter == 7){
            recorder.setOutputFile(filepath08);
            Log.i("Recording","filepath08"+ filepath08);
            recording_counter++;
        }
        else if(recording_counter == 6){
            recorder.setOutputFile(filepath07);
            Log.i("Recording","filepath07"+ filepath07);
            recording_counter++;
        }
        else if(recording_counter == 5){
            recorder.setOutputFile(filepath06);
            Log.i("Recording","filepath06"+ filepath06);
            recording_counter++;
        }
        else if(recording_counter == 4){
            recorder.setOutputFile(filepath05);
            Log.i("Recording","filepath05"+ filepath05);
            recording_counter++;
        }
        else if(recording_counter == 3){
            recorder.setOutputFile(filepath04);
            Log.i("Recording","filepath04"+ filepath04);
            recording_counter++;
        }
        else if(recording_counter == 2){
            recorder.setOutputFile(filepath03);
            Log.i("Recording","filepath03"+ filepath03);
            recording_counter++;
        }
        else if(recording_counter == 1){
            recorder.setOutputFile(filepath02);
            Log.i("Recording","filepath02"+ filepath02);
            recording_counter++;
        }
        else if(recording_counter == 0){
            recorder.setOutputFile(filepath01);
            Log.i("Recording","filepath01"+ filepath01);
            recording_counter++;
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


    ////파일 send 하는 함수 관련////
    public void fileUpload (File file, int post_type, String custom_sound_name) {

        Log.d("Send Test", "file===" + file.getName());

        RequestBody requestBody;
        MultipartBody.Part body;
        LinkedHashMap<String, RequestBody> mapRequestBody = new LinkedHashMap<String, RequestBody>();
        List<MultipartBody.Part> arrBody = new ArrayList<>();


        requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        if(post_type==1) {
            mapRequestBody.put("file\"; filename=\"" + file.getName(), requestBody);
            mapRequestBody.put("post_type", RequestBody.create(MediaType.parse("text/plain"), "prediction"));
        }else if(post_type==2){
            mapRequestBody.put("post_type", RequestBody.create(MediaType.parse("text/plain"), "user"));
            mapRequestBody.put("sound_name", RequestBody.create(MediaType.parse("text/plain"), custom_sound_name));
        }

        body = MultipartBody.Part.createFormData("fileName", file.getName(), requestBody);
        arrBody.add(body);
        Log.d("Send Test", "checkcheck");

        Call<JsonObject> call =
                RetrofitImg.getInstance().getService().uploadFile(mapRequestBody, arrBody);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.body() != null) {
                    String prediction_result = response.body().get("result").getAsString();
                    Log.d("Send Test", prediction_result);
                    if(!prediction_result.equals("nothing")){
                        Toast.makeText(getApplicationContext(), prediction_result, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Send Test" + "Err", t.getMessage());
            }
        });
    }

}
