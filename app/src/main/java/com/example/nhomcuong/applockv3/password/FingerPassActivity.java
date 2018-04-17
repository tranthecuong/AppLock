package com.example.nhomcuong.applockv3.password;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nhomcuong.applockv3.R;
import com.example.nhomcuong.applockv3.activities.MainActivity;
import com.example.nhomcuong.applockv3.model.DataUse;

public class FingerPassActivity extends AppCompatActivity {
    ImageButton img1,img2,img3;
    ImageView img4;
    int data;
    boolean fromService=false;
    String blockedPackageName;
    DataUse dataUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_pass);
        data=0;

        dataUse= new DataUse(getApplicationContext());
        fromService=false;
        Intent i = getIntent();

        if(i.hasExtra("packet")  ){
            fromService = true;
            blockedPackageName = i.getStringExtra("packet");

        }


        img1=(ImageButton)findViewById(R.id.img1);
        img2=(ImageButton)findViewById(R.id.img2);
        img3=(ImageButton)findViewById(R.id.img3);
        img4=(ImageView)findViewById(R.id.imageButton) ;
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data=1;

            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data=0;

            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data=0;

            }
        });
        img4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(data==1){
                    //neu Nhap dung

                    if(!fromService){
                        dataUse.setIsAppLock("false");
                        Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();}
                    else{
                        Log.e("CUONG","Nhan duoc packet la "+blockedPackageName);
                        dataUse.setChoPhep(blockedPackageName);
                        finish();
                    }

                }
                else {
                    data=0;
                    Toast.makeText(FingerPassActivity.this,"Vân tay không khớp",Toast.LENGTH_SHORT).show();
                }


                return false;
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fromService) {
            cancel();
        } else {
            setResult(RESULT_OK);
            finish();
        }

    }

    private void cancel(){
        Log.e("CUONG", "cancel");
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
        startActivity(startMain);
        finish();
    }
}
