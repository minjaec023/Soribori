package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;

public class Name_registration extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = Name_registration.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_registration);

        //이름을 받아서
        TextView textView1 = (TextView)findViewById(R.id.textView4);
        Button button1 = (Button)findViewById(R.id.Name_registration1);
        findViewById(R.id.Name_registration1).setOnClickListener(this);


        //setButtonsStatus(true);
    }

    //버튼이 클릭되면
    @Override
    public void onClick(View v) {
        TextView textView2 = (TextView)findViewById(R.id.textView4);

        try {
            // textview 에 있는  string 을 UserName 에 넣어
            String UserName = textView2.getText().toString();
            System.out.println("!!! R NAME IS " + UserName);
            // 인텐트를 만들어서 string 을 username 이라는 이름으로 던져줘
            Intent intent = new Intent();
            intent.putExtra("UserName", UserName);
            setResult(RESULT_OK,intent);




            // User에게 토스트메세지로 알림
            Toast.makeText(getApplicationContext(), "이름 등록이 완료 되었습니다.", Toast.LENGTH_LONG).show();



        }catch (Exception e){
            Log.d(TAG,"Name Registration Error!!\n",e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }
}
