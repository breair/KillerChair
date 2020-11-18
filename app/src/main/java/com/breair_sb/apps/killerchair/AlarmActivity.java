package com.breair_sb.apps.killerchair;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.breair_sb.apps.killerchair.util.ThemeUtil;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.checkTheme(this);
        setContentView(R.layout.activity_alarm);
    }


}