package com.breair_sb.apps.killerchair;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.breair_sb.apps.killerchair.util.AlarmUtil;
import com.breair_sb.apps.killerchair.util.ThemeUtil;

public class AlarmActivity extends AppCompatActivity {

    private AlarmUtil alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.checkTheme(this);
        setContentView(R.layout.activity_alarm);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (keyguardManager != null) {
                keyguardManager.requestDismissKeyguard(this, null);
            }
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }

        Button breakButton = findViewById(R.id.alarmbreak);
        ImageButton stopButton = findViewById(R.id.alarmStop);
        alarm = new AlarmUtil(this);
        alarm.start();

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
                Intent intent = new Intent(getApplicationContext(), SittingTimerService.class);
                intent.setAction(SittingTimerService.KC_BREAK_TIME_ACTION_START);
                startService(intent);
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