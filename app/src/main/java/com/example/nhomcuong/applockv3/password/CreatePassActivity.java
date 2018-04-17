package com.example.nhomcuong.applockv3.password;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhomcuong.applockv3.R;
import com.example.nhomcuong.applockv3.activities.MainActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CreatePassActivity extends AppCompatActivity {
    Button button;
    EditText editText1, editText2;
    TextView textView1, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pass);
        button = (Button) findViewById(R.id.button);
        editText1 = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text1 = editText1.getText().toString();
                String text2 = editText2.getText().toString();
                if ((text1.equals("") == true) || (text2.equals("") == true)) {
                    Toast.makeText(CreatePassActivity.this, "Không có pass, hãy nhập pass", Toast.LENGTH_SHORT).show();
                } else if (text1.equals(text2)) {

                    // file luu mat khau
                    SharedPreferences settings = getSharedPreferences("CUONG", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("password", text1);
                    editor.putString("lockmode","enterstringpass");
                    //tao ra file luu danh sach packet bi khoa
                    Set<String> set = new HashSet<String>();
                    set.addAll(new ArrayList<String>());
                    editor.putStringSet("applock", set);


                    // vao app
                    //khoi tao isappLock=true
                    editor.putString("isapplock","true");
                    //Toast.makeText(getBaseContext(),settings.getString("isapplock",""),Toast.LENGTH_SHORT).show();

                    editor.commit();


                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(CreatePassActivity.this, "Nhập lại mật khẩu không đúng", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
