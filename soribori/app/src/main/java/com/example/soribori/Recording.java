package com.example.soribori;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Recording extends ToolbarActivity{

    int bt_develop_count = 0;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_record);

        Button bt_close = (Button) findViewById(R.id.button_close);
        bt_close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        Button bt_develop = (Button) findViewById(R.id.develop);
        bt_develop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(bt_develop_count < 5){
                    final Toast toast = Toast.makeText(getApplicationContext(), "개발자 페이지에 접근하려면 "+ (5 - bt_develop_count)+"번 더 연속으로 클릭하세요", Toast.LENGTH_SHORT);
                    toast.show();
                    bt_develop_count++;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override public void run() {
                            toast.cancel(); } }, 200);

                }
                else if(bt_develop_count == 5) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
