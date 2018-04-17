package com.example.nhomcuong.applockv3.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nhomcuong.applockv3.R;
import com.example.nhomcuong.applockv3.model.DataUse;

public class IntroSettingActivity extends AppCompatActivity {
    DataUse dataUse;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_setting);
        dataUse=new DataUse(getApplicationContext());
        btn=(Button)findViewById(R.id.btn_intro_change) ;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataUse.setLockMode("enterfingerpass");
                dataUse.setPassFinger("aaa");
                Toast.makeText(getApplicationContext(),"Kiểu fun finger được set",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
