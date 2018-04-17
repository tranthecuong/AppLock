package com.example.nhomcuong.applockv3.password;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhomcuong.applockv3.R;
import com.example.nhomcuong.applockv3.activities.MainActivity;
import com.example.nhomcuong.applockv3.model.DataUse;

public class EnterPassActivity extends AppCompatActivity {
    Button button;
    EditText editText1;
    TextView textView1;
    String password;
    boolean fromService=false;
    String blockedPackageName;
    DataUse dataUse;
    Handler handler;
    //String blockedActivityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pass);
        dataUse= new DataUse(getApplicationContext());
        password=dataUse.getPass();
        fromService=false;

        //Nhan data tu intent cua service
        Intent i = getIntent();

        if(i.hasExtra("packet")  ){
            fromService = true;
            blockedPackageName = i.getStringExtra("packet");

        }


        button=(Button)findViewById(R.id.button2);
        editText1=(EditText)findViewById(R.id.editText3);
        textView1=(TextView)findViewById(R.id.textView3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edit=editText1.getText().toString();
                if(edit.equals(""))
                    Toast.makeText(EnterPassActivity.this, "Không có mật khẩu, hãy nhập mật khẩu", Toast.LENGTH_SHORT).show();
                else
                {
                    if(edit.equals(password)){

                        if(!fromService){
                            dataUse.setIsAppLock("false");
                            Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();}
                        else{
                            Log.e("CUONG","Nhan duoc packet la "+blockedPackageName);
                            dataUse.setChoPhep(blockedPackageName);
                            //android.os.Process.killProcess(android.os.Process.myPid());
                            finish();
                        }

                    }
                    else
                        Toast.makeText(EnterPassActivity.this, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();




                }
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
