package com.example.nhomcuong.applockv3.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nhomcuong.applockv3.R;
import com.example.nhomcuong.applockv3.model.DataUse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class IntroActivity extends AppCompatActivity {
    Button btnGotIt ;
    DataUse dataUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        dataUse= new DataUse(getApplicationContext());
        btnGotIt=(Button)findViewById(R.id.btn_intro);
        btnGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tao pass finger
                dataUse.setPassFinger("aaa");
                dataUse.setLockMode("enterfingerpass");
                dataUse.setIsAppLock("true");
                SharedPreferences settings = getSharedPreferences("CUONG", 0);
                SharedPreferences.Editor editor = settings.edit();
                Set<String> set = new HashSet<String>();
                set.addAll(new ArrayList<String>());
                editor.putStringSet("applock", set);
                editor.commit();

                //chuyen sang main
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
