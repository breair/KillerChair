package com.breair_sb.apps.killerchair;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.breair_sb.apps.killerchair.util.mSimpleTimerUtil;

import static com.breair_sb.apps.killerchair.util.mSimpleTimerUtil.KC_TIMER_ACTION_Time_CHANGED;


public class KC_TimerService extends Service {
    public static final String KC_TIMER_ACTION_START = "com.breair_sb.apps.killerchair.actionstart";
    public static final String KC_TIMER_ACTION_STOP = "com.breair_sb.apps.killerchair.actionstop";
    public static final String KC_TIMER_ACTION_RESET = "com.breair_sb.apps.killerchair.actionreset";

    private mSimpleTimerUtil simpleSittingTimer;
    KC_TimerNotificationReceiver kc_NotificationReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        simpleSittingTimer = new mSimpleTimerUtil(240000, 0, 1000, this);
        kc_NotificationReceiver = new KC_TimerNotificationReceiver();
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
                        simpleSittingTimer.stopTimer();

                    }
                    unregisterReceiver(kc_NotificationReceiver);
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
        super.onDestroy();
        unregisterReceiver(kc_NotificationReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
