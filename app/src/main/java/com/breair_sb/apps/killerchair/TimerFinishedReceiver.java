package com.breair_sb.apps.killerchair;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

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
        String timer_channelId = "KC_Timerre";

        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//TODO WTH is this
        PendingIntent activityPendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);


        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(context, timer_channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("not Started")
                .setOngoing(true)
                .setContentIntent(activityPendingIntent);
        builder.build();
//        TimerNotificationUtil TimerNotification = new TimerNotificationUtil(context, 11);
//        TimerNotification.builder.build();
//        if (TimerNotification.builder != null){Log.e("alarm", "ALARM!!");}
//        TimerNotification.builder.notify();

    }
}


