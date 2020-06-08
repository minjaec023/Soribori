package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class register_name_hm extends toolbar_hm {

    private static final String TAG = register_name_hm.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name_hm);

        Toolbar toolbar_hm = (Toolbar) findViewById(R.id.toolbar_hm);
        toolbar_hm.setBackgroundColor(Color.parseColor("#1B1B2F"));
        TextView toolbar_title = (TextView) toolbar_hm.findViewById(R.id.toolbar_title);
        toolbar_title.setTextColor(Color.WHITE);
        toolbar_title.setText("Register");

        final EditText editText = (EditText)findViewById(R.id.edit_text01);
        ImageView register_button = (ImageView)findViewById(R.id.register_btn_img);

        register_button.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = editText.getText().toString();
                Intent name_intent = new Intent();
                name_intent.putExtra("UserName",user_name);
                setResult(RESULT_OK,name_intent);

                // User에게 토스트메세지로 알림
                Toast.makeText(getApplicationContext(), "이름 등록이 완료 되었습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onStop(){
        super.onStop();


        // Name Registration Activity가 종료되기 전에 저장한다.
        //SharedPreferences를 sFile이름, 기본모드로 설정
        SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);
        EditText editText = (EditText)findViewById(R.id.edit_text01);
        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String text = editText.getText().toString(); // 사용자가 입력한 저장할 데이터
        editor.putString("UserName",text); // key, value를 이용하여 저장하는 형태
        //최종 커밋
        editor.commit();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }
}
