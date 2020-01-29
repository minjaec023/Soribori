package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;

public class Name_registration extends AppCompatActivity implements View.OnClickListener {

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

        // textview 에 있는  string 을 UserName 에 넣어
        String UserName = textView2.getText().toString();

        // 인텐트를 만들어서 string을 username이라는 이름으로 던져줘
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("UserName",UserName);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }
}
