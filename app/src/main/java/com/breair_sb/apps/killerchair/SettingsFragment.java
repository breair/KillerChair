package com.breair_sb.apps.killerchair;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.breair_sb.apps.killerchair.util.ThemeUtil;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private OnThemeChangeListener onThemeChangeListener;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() != null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onThemeChangeListener = (OnThemeChangeListener) context;
    }

    public interface OnThemeChangeListener {

        void onThemeChange();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(ThemeUtil.ThemeKey)) {
            onThemeChangeListener.onThemeChange();
        }
    }
}
