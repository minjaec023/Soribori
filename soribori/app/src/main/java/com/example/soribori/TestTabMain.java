package com.example.soribori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class TestTabMain extends AppCompatActivity {
    private long backKeyPressedTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tab_main);

        //Tablayout
        TabLayout tabs = (TabLayout) findViewById(R.id.tabLayout3);
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //Adapter
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager02);
        final TestTabMain.MyPagerAdapter myPagerAdapter = new TestTabMain.MyPagerAdapter(getSupportFragmentManager(), 4);
        viewPager.setAdapter(myPagerAdapter);

        //탭 선택 이벤트
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
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
                    Recording_frag tab1 = new Recording_frag();
                    return tab1;
                case 1:
                    Register_frag tab2 = new Register_frag();
                    return tab2;
                case 2:
                    SoundClassification_frag tab3 = new SoundClassification_frag();
                    return tab3;
                case 3:
                    Setting_frag tab4 = new Setting_frag();
                    return tab4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

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

}
