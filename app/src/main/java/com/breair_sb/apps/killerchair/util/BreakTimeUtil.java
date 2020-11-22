package com.breair_sb.apps.killerchair.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.breair_sb.apps.killerchair.Receivers.BreakTimeAlarmReceiver;

import java.util.Calendar;

public class BreakTimeUtil {
    private int breakTimeInMins;
    Context context;

    public BreakTimeUtil(Context context) {
        this.context = context;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        breakTimeInMins = sharedPreferences.getInt("breaktime", 0);

    }

    public void start() {
        if (breakTimeInMins != 0) {
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(System.currentTimeMillis());
            time.add(Calendar.MINUTE, breakTimeInMins);

            Intent intent = new Intent(context, BreakTimeAlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
        }
    }
}
