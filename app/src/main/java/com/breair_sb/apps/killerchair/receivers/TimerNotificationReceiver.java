package com.breair_sb.apps.killerchair.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.breair_sb.apps.killerchair.R;
import com.breair_sb.apps.killerchair.util.NotificationUtil;

import static com.breair_sb.apps.killerchair.util.SittingTimerUtil.KC_TIMER_ACTION_Time_CHANGED;

public class TimerNotificationReceiver extends BroadcastReceiver {
    private String contentText;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("broadReceiver", "notification receiver fired");
        if (intent.getAction() != null) {
            if (intent.getAction().equals(KC_TIMER_ACTION_Time_CHANGED)) {
                //format String like: 'Time remaining: 10:04'
                contentText = context.getResources().getString(R.string.time_remaining) + intent.getStringExtra("currentTimeleft");
                final String actionTitle = context.getString(R.string.reset);
                NotificationUtil TimerNotification = new NotificationUtil(context, NotificationUtil.TIMER_STATUS_NOTIFICATION_ID) {
                    @Override
                    public void setBuilder() {
                        builder.setNotificationSilent()
                                .setOnlyAlertOnce(true)
                                .setOngoing(true)
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .addAction(R.attr.reset_button_src, actionTitle, getResetAction());
                    }
                };
                TimerNotification.updateNotification(contentText);
            }
        }
    }
}


