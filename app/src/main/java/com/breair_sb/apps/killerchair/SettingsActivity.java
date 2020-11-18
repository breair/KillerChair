package com.breair_sb.apps.killerchair;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.breair_sb.apps.killerchair.util.ThemeUtil;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.OnThemeChangeListener {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.checkTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ImageButton returnButton = findViewById(R.id.sittingsReturn);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onThemeChange() {
        setResult(1);
        recreate();
    }

}
