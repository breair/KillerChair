package com.breair_sb.apps.killerchair;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.preference.PreferenceManager;

import com.breair_sb.apps.killerchair.util.SimpleSittingTimerUtil;

import static com.breair_sb.apps.killerchair.util.SimpleSittingTimerUtil.KC_TIMER_ACTION_Time_CHANGED;


public class SittingTimerService extends Service {
    public static final String KC_TIMER_ACTION_START = "com.breair_sb.apps.killerchair.actionstart";
    public static final String KC_TIMER_ACTION_STOP = "com.breair_sb.apps.killerchair.actionstop";
    public static final String KC_TIMER_ACTION_RESET = "com.breair_sb.apps.killerchair.actionreset";

    private SimpleSittingTimerUtil simpleSittingTimer;
    TimerNotificationReceiver kc_NotificationReceiver;
    SharedPreferences sharedPrefs;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        long sittingTimeInMins = sharedPrefs.getInt("SittingInterval", 25);
        simpleSittingTimer = new SimpleSittingTimerUtil((sittingTimeInMins * 60000), 0, 1000, context);
        kc_NotificationReceiver = new TimerNotificationReceiver();
        this.registerReceiver(kc_NotificationReceiver, new IntentFilter(KC_TIMER_ACTION_Time_CHANGED));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case KC_TIMER_ACTION_START:
                    if (!simpleSittingTimer.isRunning()) {
                        simpleSittingTimer.startTimer();
                    }
                    break;
                case KC_TIMER_ACTION_RESET:
                    if (simpleSittingTimer.isRunning()) {
                        simpleSittingTimer.resetTimer();
                    }
                    break;
                case KC_TIMER_ACTION_STOP:
                    if (simpleSittingTimer.isRunning()) {
                        long nextIntervalInMins = sharedPrefs.getInt("SittingInterval", 25);
                        simpleSittingTimer.stopTimer(nextIntervalInMins * 60000);

                    }
                    stopSelf();
                    break;
                default:
                    break;
            }
        }
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(kc_NotificationReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
