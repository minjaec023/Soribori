package com.example.soribori;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegistrationSelect extends ToolbarActivity {
    public static final int sub = 1001;
    public static final int sub2 = 1002;
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
}
