package com.breair_sb.apps.killerchair.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.breair_sb.apps.killerchair.R;
import com.breair_sb.apps.killerchair.util.NotificationUtil;

public class BreakTimeAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String contentText = context.getResources().getString(R.string.break_time_finished);
        final String actionTitle = context.getString(R.string.start);
        NotificationUtil notification = new NotificationUtil(context, NotificationUtil.BREAK_TIME_FINISHED_NOTIFICATION_ID) {
            @Override
            public void setBuilder() {
                builder.setContentText(contentText)
                        .addAction(R.attr.start_button_src, actionTitle, getStartAction());
            }
        };
        notification.showNotification();
    }
}
