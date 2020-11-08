package com.breair_sb.apps.killerchair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

public class TimerFinishedReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("alarm", "ALARM!!");
        this.context = context;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isAlarm = sharedPreferences.getBoolean("alarm", false);
        if (isAlarm) {
            sendAlarmActivity();
        } else {
            sendAlarmNotification();
        }
    }

    //TODO implement alarm functionality
    void sendAlarmActivity() {
    }

    void sendAlarmNotification() {
    }
}
