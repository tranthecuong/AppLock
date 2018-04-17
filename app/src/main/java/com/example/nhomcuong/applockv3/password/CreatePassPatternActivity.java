package com.example.nhomcuong.applockv3.password;

import android.content.Intent;
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

public class CreatePassPatternActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_create_password_pattern);
        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);
        final TextView tvNoti = (TextView) findViewById(R.id.tv_notification);
        final Button btnDel = (Button) findViewById(R.id.btn_del);
        final Button btnContinue = (Button) findViewById(R.id.btn_continue);

        RxPatternLockView.patternChanges(mPatternLockView)
                .subscribe(new Consumer<PatternLockCompoundEvent>() {
                    @Override
                    public void accept(final PatternLockCompoundEvent event) throws Exception {
                        if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_STARTED) {
                            Log.d(getClass().getName(), "Pattern drawing started");
                            tvNoti.setText("Thả ngón tay để hoàn tất");
                            tvNoti.setTextColor(Color.RED);
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_PROGRESS) {
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_COMPLETE) {
                            if (PatternLockUtils.patternToString(mPatternLockView, event.getPattern()).length() < 4) {
                                tvNoti.setText("Bạn phải ít nhất 4 ký tự. Xin hãy vẽ lại");
                                tvNoti.setTextColor(Color.RED);
                                mPatternLockView.setCorrectStateColor(Color.RED);
                            } else {
                                tvNoti.setText("Hình đã được lưu lại");
                                tvNoti.setTextColor(Color.RED);
                                Log.d(getClass().getName(), "Pattern complete: " +
                                        PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
                                btnContinue.setEnabled(true);
                                btnContinue.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // vao app
                                        Intent intent = new Intent(getApplicationContext(), RePasswordPatternActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("passPattern", PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                mPatternLockView.setEnabled(false);
                                mPatternLockView.setCorrectStateColor(Color.GRAY);
                            }
                            btnDel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    tvNoti.setText("Vẽ kiểu mở khóa");
                                    tvNoti.setTextColor(Color.RED);
                                    mPatternLockView.setEnabled(true);
                                    btnContinue.setEnabled(false);
                                    mPatternLockView.clearPattern();
                                }
                            });
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_CLEARED) {
                        }
                    }
                });
    }
}

