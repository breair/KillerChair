package com.breair_sb.apps.killerchair.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.breair_sb.apps.killerchair.R;

public class ThemeUtil {
    public static String ThemeKey = "is_dark_theme";

    public static void checkTheme(Context activityContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activityContext);
        boolean isDarkTheme = sharedPreferences.getBoolean(ThemeKey, false);
        if (isDarkTheme) {
            activityContext.setTheme(R.style.AppDarkTheme);
        } else {
            activityContext.setTheme(R.style.AppLightTheme);
        }

    }
}
