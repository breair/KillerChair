package com.breair_sb.apps.KillerChair.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.breair_sb.apps.KillerChair.R;
import com.breair_sb.apps.KillerChair.util.NotificationUtil;

import static com.breair_sb.apps.KillerChair.util.SittingTimerUtil.KC_TIMER_ACTION_Time_CHANGED;

public class TimerNotificationReceiver extends BroadcastReceiver {
    private NotificationUtil TimerNotification;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals(KC_TIMER_ACTION_Time_CHANGED)) {
                //format String like: 'Time remaining: 10:04'
                String contentText = context.getResources().getString(R.string.time_remaining) + intent.getStringExtra("currentTimeleft");
                final String actionTitle = context.getString(R.string.reset);
                if (TimerNotification == null) {
                    TimerNotification = new NotificationUtil(context, NotificationUtil.TIMER_STATUS_NOTIFICATION_ID) {
                        @Override
                        public void setBuilder() {
                            builder.setNotificationSilent()
                                    .setOnlyAlertOnce(true)
                                    .setOngoing(true)
                                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                    .addAction(R.attr.reset_button_src, actionTitle, getResetAction());
                        }
                    };
                }
                TimerNotification.updateNotification(contentText);
            }
        }
    }
}


