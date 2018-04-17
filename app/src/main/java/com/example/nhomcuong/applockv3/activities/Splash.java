package com.example.nhomcuong.applockv3.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.nhomcuong.applockv3.R;
import com.example.nhomcuong.applockv3.model.DataUse;
import com.example.nhomcuong.applockv3.password.CreatePassActivity;
import com.example.nhomcuong.applockv3.password.CreatePassPatternActivity;
import com.example.nhomcuong.applockv3.password.EnterPassActivity;
import com.example.nhomcuong.applockv3.password.EnterPassPatternActivity;
import com.example.nhomcuong.applockv3.password.FingerPassActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Splash extends AppCompatActivity {
    String password;
    String lockmode;
    DataUse dataUse;
    CharSequence itemget;
    Context mContext ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dataUse= new DataUse(getApplicationContext());
        mContext = this;itemget=null;

        //lay ra kieu khoa
        lockmode=dataUse.getLockMode();
        //lay mat khau

        if(lockmode.equals("enterstringpass")){
            password=dataUse.getPass();
        }
        else
        if(lockmode.equals("")){
            password="";
        }
        else
        if(lockmode.equals("enterpatternpass")){
            password=dataUse.getPassPattern();
        }
//        else
//        if(lockmode.equals("enterintpass")){
//            password=dataUse.getPassInt();
//
//        }
        else
        if(lockmode.equals("enterfingerpass")){
            password=dataUse.getPassFinger();

        }


        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(password.equals("")){
                    //neu chua co mat khau tuc la vao ung dung lan dau tien
                    final AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        builder = new AlertDialog.Builder(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    }
                    else {
                        builder = new AlertDialog.Builder(mContext);
                    }

// Lan dau tien vao dung dung se phai set kieu khoa
                    final CharSequence[] array = {"Kí Tự","Vẽ Hình","Fake Finger"};
                    builder.setTitle("Chọn Kiểu Khóa").setCancelable(false).setSingleChoiceItems(array, 1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface,int i) {
                            itemget=array[i];
                        }
                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            //neu nguoi dung khong chon gi thi mac dinh la kieu ve hinh
                            if(itemget==null){
                                Intent intent= new Intent(getApplicationContext(), CreatePassPatternActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            if(itemget.equals("Kí Tự")){
                                Intent intent= new Intent(getApplicationContext(), CreatePassActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            if(itemget.equals("Vẽ Hình")){
                                Intent intent= new Intent(getApplicationContext(), CreatePassPatternActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else if(itemget.equals("Fake Finger")){
                                Intent intent= new Intent(getApplicationContext(), IntroActivity.class);
                                startActivity(intent);
                                finish();


//                                //tao pass finger
//                                dataUse.setPassFinger("aaa");
//                                dataUse.setLockMode("enterfingerpass");
//                                dataUse.setIsAppLock("true");
//                                SharedPreferences settings = getSharedPreferences("CUONG", 0);
//                                SharedPreferences.Editor editor = settings.edit();
//                                Set<String> set = new HashSet<String>();
//                                set.addAll(new ArrayList<String>());
//                                editor.putStringSet("applock", set);
//                                editor.commit();
//
//                                //chuyen sang main
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(intent);
//                                finish();

                            }

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Splash.this.finish();

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                }
                // neu co mat khau tuc la da vao ung dung roi
                else {
                    //if there is a pass
                    if(lockmode.equals("enterstringpass")){
                        Intent intent= new Intent(getApplicationContext(), EnterPassActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    else
                    if(lockmode.equals("enterpatternpass")){
                        Intent intent= new Intent(getApplicationContext(), EnterPassPatternActivity.class);
                        startActivity(intent);
                        finish();

                    }

                    else
                    if(lockmode.equals("enterfingerpass")){
                        Intent intent= new Intent(getApplicationContext(), FingerPassActivity.class);
                        startActivity(intent);
                        finish();

                    }


                }

            }
        },2000);

    }
}
