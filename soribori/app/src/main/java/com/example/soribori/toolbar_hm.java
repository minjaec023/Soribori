package com.example.soribori;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public abstract class toolbar_hm extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    private Context context = this;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_hm);
    }
    @Override
    public void setContentView(int layoutResID){
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_toolbar_hm,null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content_hm);


        //RelativeLayout fullView = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_toolbar_hm,null);
        //FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content_hm);

        TextView toolbar_title = (TextView)findViewById(R.id.toolbar_title);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);

        toolbar = (Toolbar) findViewById(R.id.toolbar_hm);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);
        //툴바 사용여부 결정(기본적으로 사용)
        if(useToolbar()){
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }



        //추가된 소스코드, Toolbar의 왼쪽에 버튼을 추가하고 버튼의 아이콘을 바꾼다.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_name);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //기본 타이틀 보여줄지 말지 설정


        setUpNavView();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {



            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.go_record){
                    Toast.makeText(context, title + ": 계정 정보를 확인합니다.", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.go_register){
                    Toast.makeText(context, title + ": 설정 정보를 확인합니다.", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.go_sound_list){
                    Toast.makeText(context, title + ": 로그아웃 시도중", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.go_setting){
                    Toast.makeText(context, title + ": 로그아웃 시도중", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
    }

    //툴바를 사용할지 말지 정함
    protected boolean useToolbar(){
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean useDrawerToggle()
    {
        return true;
    }

    protected void setUpNavView()
    {
        if( useDrawerToggle()) { // use the hamburger menu
            drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
            mDrawerLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();
        } else if(useToolbar() && getSupportActionBar() != null) {
            // Use home/back button instead
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public abstract void onClick(View v);
}
