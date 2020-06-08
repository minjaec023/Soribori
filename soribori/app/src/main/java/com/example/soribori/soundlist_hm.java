package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class soundlist_hm extends toolbar_hm {

    int bt_develop_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundlist_hm);


        Toolbar toolbar_hm = (Toolbar) findViewById(R.id.toolbar_hm);
        toolbar_hm.setBackgroundColor(Color.parseColor("#1B1B2F"));
        TextView toolbar_title = (TextView) toolbar_hm.findViewById(R.id.toolbar_title);
        toolbar_title.setTextColor(Color.WHITE);
        toolbar_title.setText("Sound List");

        //Tablayout
        TabLayout tabs = (TabLayout) findViewById(R.id.tabLayout3);
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //Adapter
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final soundlist_hm.MyPagerAdapter myPagerAdapter = new soundlist_hm.MyPagerAdapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(myPagerAdapter);

        //탭 선택 이벤트
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        Button bt_develop = (Button) findViewById(R.id.develop1);
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

    public class MyPagerAdapter extends FragmentPagerAdapter {

        int mNumOfTabs; //탭의 갯수

        public MyPagerAdapter(FragmentManager fm, int numTabs) {
            super(fm);
            this.mNumOfTabs = numTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 :
                    Normal_sound_frag tab1 = new Normal_sound_frag();
                    return tab1;
                case 1:
                    Custom_frag tab2 = new Custom_frag();
                    return tab2;
                case 2:
                    Phrase_frag tab3 = new Phrase_frag();
                    return tab3;
                default:
                    return null;
            }
            //return null;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }




}
