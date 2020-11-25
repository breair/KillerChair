package com.breair_sb.apps.killerchair.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.breair_sb.apps.killerchair.AlarmActivity;
import com.breair_sb.apps.killerchair.R;
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
                    sendAlarm();
                } else {
                    sendNotification();
                }
            }
        }
    }

    //TODO implement alarm functionality
    void sendAlarm() {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    void sendNotification() {
        final String contentText = context.getString(R.string.sitting_time_finished);
        final String breakActionText = context.getString(R.string.take_break);
        NotificationUtil finishNotification = new NotificationUtil(context, NotificationUtil.TIMER_FINISHED_NOTIFICATION_ID) {
            @Override
            public void setBuilder() {
                builder.setContentText(contentText)
                        .addAction(R.drawable.ic_baseline_arrow_back_24, breakActionText, getBreakAction());
            }
        };
        finishNotification.showNotification();

    }
}


