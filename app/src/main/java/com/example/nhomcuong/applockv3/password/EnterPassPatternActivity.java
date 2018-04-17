package com.example.nhomcuong.applockv3.password;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.rxpatternlockview.RxPatternLockView;
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent;
import com.example.nhomcuong.applockv3.R;
import com.example.nhomcuong.applockv3.activities.MainActivity;
import com.example.nhomcuong.applockv3.model.DataUse;

import java.util.List;

import io.reactivex.functions.Consumer;

public class EnterPassPatternActivity extends AppCompatActivity {

    PatternLockView patternLockView;
    boolean fromService=false;
    String blockedPackageName;
    DataUse dataUse;
    String password;
    TextView tvNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pass_pattern);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataUse = new DataUse(getApplicationContext());
        password = dataUse.getPassPattern();
        tvNotification = (TextView) findViewById(R.id.tv_notification_en_pass);

        //Nhan data tu intent cua service
        Intent i = getIntent();

        if(i.hasExtra("packet")  ){
            fromService = true;
            blockedPackageName = i.getStringExtra("packet");
            // blockedActivityName = i.getStringExtra("activity");
        }

        patternLockView = (PatternLockView) findViewById(R.id.pattern_lock_check);
        patternLockView.addPatternLockListener(mPatternLockViewListener);

        RxPatternLockView.patternChanges(patternLockView)
                .subscribe(new Consumer<PatternLockCompoundEvent>() {
                    @Override
                    public void accept(final PatternLockCompoundEvent event) throws Exception {
                        if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_STARTED) {
                            Log.d(getClass().getName(), "Pattern drawing started");
                            tvNotification.setText("Thả ngón tay để hoàn tất");
                            tvNotification.setTextColor(Color.RED);
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_PROGRESS) {
                            Log.d(getClass().getName(), "Pattern progress: " +
                                    PatternLockUtils.patternToString(patternLockView, event.getPattern()));
                            tvNotification.setText("Bạn phải ít nhất 4 ký tự. Xin hãy vẽ lại");
                            tvNotification.setTextColor(Color.RED);

                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_COMPLETE) {
                            if(PatternLockUtils.patternToString(patternLockView,event.getPattern()).equals(password)){
                                tvNotification.setText("Mật khẩu đúng. Đang vào ứng dụng");
                                tvNotification.setTextColor(Color.YELLOW);
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
                                tvNotification.setText("Mật khẩu sai. Vui lòng nhập lại");
                                tvNotification.setTextColor(Color.RED);
                                patternLockView.clearPattern();
                            }
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_CLEARED) {
                            Log.d(getClass().getName(), "Pattern has been cleared");
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

    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(patternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(patternLockView, pattern));
        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }

    };
}
