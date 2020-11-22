package com.breair_sb.apps.killerchair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.breair_sb.apps.killerchair.util.AlarmUtil;
import com.breair_sb.apps.killerchair.util.ThemeUtil;

public class AlarmActivity extends AppCompatActivity {

    private Button stopButton;
    private Button breakButton;
    AlarmUtil alarm;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.checkTheme(this);
        setContentView(R.layout.activity_alarm);
        stopButton = findViewById(R.id.alarmStop);
        breakButton = findViewById(R.id.alarmbreak);
        alarm = new AlarmUtil(this);
        alarm.start();
        context = this;

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarm.stop();
                onBackPressed();
            }
        });
        breakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarm.stop();
                Intent intent = new Intent(context, SittingTimerService.class);
                intent.setAction(SittingTimerService.KC_BREAK_TIME_ACTION_START);
                context.startService(intent);
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP) || (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            alarm.stop();
        }
        return true;
    }
}