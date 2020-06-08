package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class register_custom_hm extends toolbar_hm {


    String filepath20;

    MediaRecorder recorder;
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

        /////////////녹음 관련/////////////
        recorder = new MediaRecorder();
        //////////////////////////////////
        final File myrecordingfile20 = new File( this.getFilesDir() ,"customfile20.wav");
        filepath20 = myrecordingfile20.getAbsolutePath();
        Log.d("Recording Service","녹음된 파일 저장된 위치 : " + filepath20);

        final EditText edittext=(EditText)findViewById(R.id.edittext);
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
                    settingAudio();
                    recordAudio();

                }
                else if(rec_mic.isSelected() == false){
                    rec_mic.setBackgroundResource(R.drawable.new_new_rec_mic);
                    Log.i("click", "hi3");
                    textView1.setText("");
                    stopRecording();
                    String custom_sound_name = edittext.getText().toString();
                    //여기서 서버로 보내면 될듯
                    fileUpload(myrecordingfile20,2,custom_sound_name);

                }
            }
        });
    }


    private class ExampleThread2 extends Thread {
        private static final String TAG2 = "ExampleThread2";

        public void run() {
            Log.i(TAG2, "THREAD RUN2");
            try {
                Log.e("Debug", "im hear333");
                settingAudio();
                Log.e("Debug", "im hear444");
                recordAudio();
                sleep(3000);
                Log.e("Debug", "im hear555");
                stopRecording();
                sleep(200);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
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
        recorder.setOutputFile(filepath20);
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
