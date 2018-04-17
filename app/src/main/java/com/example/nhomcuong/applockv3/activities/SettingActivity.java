package com.example.nhomcuong.applockv3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.example.nhomcuong.applockv3.R;
import com.example.nhomcuong.applockv3.changepassword.CreateChangePassActivity;
import com.example.nhomcuong.applockv3.changepassword.CreateChangePassPatternActivity;
import com.example.nhomcuong.applockv3.model.DataUse;

public class SettingActivity extends AppCompatActivity {

    CardView cvPassPattern,cvPassText,cvFinger;
    DataUse dataUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        dataUse = new DataUse(getApplicationContext());

        cvPassPattern = (CardView) findViewById(R.id.type_pass_pattern);
        cvPassText = (CardView) findViewById(R.id.type_pass_text);
        cvFinger = (CardView) findViewById(R.id.type_pass_finger);


        cvPassPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dataUse.setLockMode("enterpatternpass");
                Intent intent = new Intent(SettingActivity.this, CreateChangePassPatternActivity.class);
                intent.putExtra("option","aaa");
                startActivity(intent);
                finish();
            }
        });


        cvPassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dataUse.setLockMode("enterstringpass");
                Intent intent = new Intent(SettingActivity.this, CreateChangePassActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cvFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, IntroSettingActivity.class);
                startActivity(intent);
                finish();

//                dataUse.setLockMode("enterfingerpass");
//                dataUse.setPassFinger("aaa");
//                Toast.makeText(getApplicationContext(),"Kiểu fun finger được set",Toast.LENGTH_SHORT).show();
//                finish();
            }
        });
    }

}

