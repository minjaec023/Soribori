package com.example.soribori;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Phrase_frag extends Fragment {


    public Phrase_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //등록한 사용자 이름이 날아가면 안되니까
        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
        SharedPreferences sf = getActivity().getSharedPreferences("sFile",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        String text = sf.getString("UserName","");
        Log.i("Pharse_frag","User Name :"+text);
        FrameLayout frameLayout = (FrameLayout)inflater.inflate(R.layout.fragment_phrase_frag, container, false);
        CheckBox checkBox = (CheckBox)frameLayout.findViewById(R.id.checkBoxName);
        checkBox.setText("      "+text);
        return frameLayout;
    }

}
