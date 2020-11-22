package com.breair_sb.apps.killerchair.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.breair_sb.apps.killerchair.AlarmActivity;
import com.breair_sb.apps.killerchair.util.NotificationType;
import com.breair_sb.apps.killerchair.util.NotificationUtil;

import static com.breair_sb.apps.killerchair.util.SimpleSittingTimerUtil.KC_TIMER_ACTION_FINISHED;

public class TimerFinishedReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals(KC_TIMER_ACTION_FINISHED)) {
                this.context = context;
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean isAlarm = sharedPreferences.getBoolean("alarm", false);
                if (isAlarm) {
                    sendAlarmActivity();
                } else {
                    sendAlarmNotification();
                }
            }
        }
    }

    //TODO implement alarm functionality
    void sendAlarmActivity() {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    void sendAlarmNotification() {

        NotificationUtil TimerNotification = new NotificationUtil(context, NotificationType.TIMER_FINISHED);
        TimerNotification.builder.build();
        if (TimerNotification.builder != null) {
            Log.e("alarm", "ALARM!!");
        }
        TimerNotification.builder.notify();

    }
}


