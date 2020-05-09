package com.example.soribori;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegistrationSelect extends ToolbarActivity {
    public static final int sub = 1001;
    public static final int sub2 = 1002;
    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_registration_select);

        Button bt_close = (Button) findViewById(R.id.button_close);
        bt_close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        Button bt_custom = (Button) findViewById(R.id.custom_registration);
        bt_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserCustomSound_registration.class);
                startActivityForResult(intent, sub);
            }
        });
        Button bt_name = (Button) findViewById(R.id.name_registration);
        bt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), Name_registration.class);
                startActivityForResult(intent2, sub2);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
        //액티비티 애니메이션 x
    }


    /*
    @Override
    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();
        // 첫 번째 뒤로가기 버튼을 누를때 표시
        final Toast toast;
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }
*/
}
