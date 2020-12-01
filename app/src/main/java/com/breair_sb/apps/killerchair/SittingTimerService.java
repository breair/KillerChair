package com.breair_sb.apps.killerchair;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.breair_sb.apps.killerchair.receivers.TimerFinishedReceiver;
import com.breair_sb.apps.killerchair.receivers.TimerNotificationReceiver;
import com.breair_sb.apps.killerchair.util.BreakTimeUtil;
import com.breair_sb.apps.killerchair.util.NotificationUtil;
import com.breair_sb.apps.killerchair.util.SittingTimerUtil;

import org.jetbrains.annotations.NotNull;

import static com.breair_sb.apps.killerchair.util.SittingTimerUtil.KC_TIMER_ACTION_FINISHED;
import static com.breair_sb.apps.killerchair.util.SittingTimerUtil.KC_TIMER_ACTION_Time_CHANGED;


public class SittingTimerService extends Service {
    public static final String KC_TIMER_ACTION_START = "com.breair_sb.apps.killerchair.actionstart";
    public static final String KC_TIMER_ACTION_STOP = "com.breair_sb.apps.killerchair.actionstop";
    public static final String KC_TIMER_ACTION_RESET = "com.breair_sb.apps.killerchair.actionreset";
    public static final String KC_BREAK_TIME_ACTION_START = "com.breair_sb.apps.killerchair.actionstartbreak";

    private SittingTimerUtil SittingTimer;
    TimerNotificationReceiver timeNotificationReceiver;
    TimerFinishedReceiver timerFinishedReceiver;
    SharedPreferences sharedPrefs;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        long sittingTimeInMins = sharedPrefs.getInt("SittingInterval", 25);
        SittingTimer = new SittingTimerUtil((sittingTimeInMins * 6000), 0, 1000, context);//TODO 60000
        timeNotificationReceiver = new TimerNotificationReceiver();
        timerFinishedReceiver = new TimerFinishedReceiver();
        this.registerReceiver(timeNotificationReceiver, new IntentFilter(KC_TIMER_ACTION_Time_CHANGED));
        this.registerReceiver(timerFinishedReceiver, new IntentFilter(KC_TIMER_ACTION_FINISHED));
    }

    @Override
    public int onStartCommand(@NotNull Intent intent, int flags, int startId) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case KC_TIMER_ACTION_START:
                    if (!SittingTimer.isRunning()) {
                        SittingTimer.startTimer();
                    }
                    break;
                case KC_TIMER_ACTION_RESET:
                    if (SittingTimer.isRunning()) {
                        SittingTimer.resetTimer();
                    }
                    break;
                case KC_TIMER_ACTION_STOP:
                    if (SittingTimer.isRunning()) {
                        long nextIntervalInMins = sharedPrefs.getInt("SittingInterval", 25);
                        SittingTimer.stopTimer(nextIntervalInMins * 60000);

                    }
                    stopSelf();
                    break;
                case KC_BREAK_TIME_ACTION_START:
                    BreakTimeUtil breakTime = new BreakTimeUtil(context);
                    NotificationManagerCompat.from(context).cancel(NotificationUtil.TIMER_FINISHED_NOTIFICATION_ID);
                    breakTime.start();
                default:
                    break;
            }
        }
        return START_STICKY;

    }


    @Override
    public void onDestroy() {
        unregisterReceiver(timeNotificationReceiver);
        unregisterReceiver(timerFinishedReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
