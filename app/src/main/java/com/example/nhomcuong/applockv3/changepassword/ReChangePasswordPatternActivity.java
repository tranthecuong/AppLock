package com.example.nhomcuong.applockv3.changepassword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.rxpatternlockview.RxPatternLockView;
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent;
import com.example.nhomcuong.applockv3.R;

import java.util.List;

import io.reactivex.functions.Consumer;

public class ReChangePasswordPatternActivity extends AppCompatActivity {
    PatternLockView mPatternLockView;

    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
        }

        @Override
        public void onCleared() {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_re_pass_pattern);
        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view_re_pass);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);
        final TextView tvNoti = (TextView) findViewById(R.id.tv_notification_re_pass);
        final Intent intent = this.getIntent();
        final String rePassPattern = intent.getStringExtra("passPattern");
        final Button btnSubmit = (Button) findViewById(R.id.submit);

        RxPatternLockView.patternChanges(mPatternLockView)
                .subscribe(new Consumer<PatternLockCompoundEvent>() {
                    @Override
                    public void accept(final PatternLockCompoundEvent event) throws Exception {
                        if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_STARTED) {
                            Log.d(getClass().getName(), "Pattern drawing started");
                            tvNoti.setText("Thả ngón tay để hoàn tất");
                            tvNoti.setTextColor(Color.RED);
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_PROGRESS) {

                            Log.d(getClass().getName(), "Pattern progress: " +
                                    PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));

                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_COMPLETE) {
                            if (PatternLockUtils.patternToString(mPatternLockView, event.getPattern()).length() < 4) {
                                tvNoti.setText("Bạn phải ít nhất 4 ký tự. Xin hãy vẽ lại");
                                tvNoti.setTextColor(Color.RED);
                                mPatternLockView.setCorrectStateColor(Color.RED);
                            } else {
                                if (PatternLockUtils.patternToString(mPatternLockView, event.getPattern()).equals(rePassPattern)) {
                                    tvNoti.setText("Mật khẩu thỏa mãn");
                                    tvNoti.setTextColor(Color.RED);

                                    mPatternLockView.setEnabled(false);
                                    mPatternLockView.setCorrectStateColor(Color.GRAY);

                                    SharedPreferences settings = getSharedPreferences("CUONG", 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("passwordpattern", PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
                                    editor.putString("lockmode","enterpatternpass");
                                    //tao ra file luu danh sach packet bi khoa
                                  /*  Set<String> set = new HashSet<String>();
                                    set.addAll(new ArrayList<String>());
                                    editor.putStringSet("applock", set);
                                    editor.putString("isapplock","true");*/
                                    editor.commit();


                                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                        }
                                    });
                                } else {
                                    tvNoti.setText("Mật khẩu không khớp. Vui lòng nhập lại");
                                }
                            }

                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_CLEARED) {
                        }
                    }
                });
    }
}

