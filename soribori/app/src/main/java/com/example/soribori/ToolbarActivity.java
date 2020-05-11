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
        getSupportActionBar().setTitle("");
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
                Intent intent = new Intent(this, Recording.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP | intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                overridePendingTransition(0,0);
                return true;

            case R.id.registration:
                Intent intent2 = new Intent(this, RegistrationSelect.class);
                intent2.setFlags(intent2.FLAG_ACTIVITY_SINGLE_TOP | intent2.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent2);
                overridePendingTransition(0, 0);
                return true;
            case R.id.classificationList:
                Intent intent3 = new Intent(this, SoundClassificationItems.class);
                intent3.setFlags(intent3.FLAG_ACTIVITY_SINGLE_TOP | intent3.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent3);
                overridePendingTransition(0, 0);
                return true;

            case R.id.setting:
                Intent intent4 = new Intent(this, Setting.class);
                intent4.setFlags(intent4.FLAG_ACTIVITY_SINGLE_TOP | intent4.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent4);
                overridePendingTransition(0, 0);
                return true;

            default:
                //If we got here, the user's action was not recognized.
                //Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}

