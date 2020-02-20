package com.example.soribori;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class ToolbarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    @Override
    public void setContentView(int layoutResID){
        LinearLayout fullView = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_menu, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //툴바 사용여부 결정(기본적으로 사용)
        if(useToolbar()){
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }
    //툴바를 사용할지 말지 정함
    protected boolean useToolbar(){
        return true;
    }
    //메뉴 등록하기
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        setTitle(null);
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    //앱바 메뉴 클릭 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.record:
                Toast.makeText(getApplicationContext(), "녹음 화면입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Recording.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                return true;

            case R.id.registration:
                Toast.makeText(getApplicationContext(), "등록 선택 화면입니다.", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this, RegistrationSelect.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent2.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent2);
                return true;

            case R.id.classificationList:
                Toast.makeText(getApplicationContext(), "등록된 소리 목록 화면입니다.", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(this, SoundClassificationItems.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent3.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent3);
                return true;

            case R.id.setting:
                Toast.makeText(getApplicationContext(), "환경 설정 화면입니다.", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(this, Setting.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent4.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent4);
                return true;

            default:
                //If we got here, the user's action was not recognized.
                //Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}

