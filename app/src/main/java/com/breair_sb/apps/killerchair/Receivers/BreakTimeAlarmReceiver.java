package com.breair_sb.apps.killerchair.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.breair_sb.apps.killerchair.R;
import com.breair_sb.apps.killerchair.util.NotificationUtil;

public class BreakTimeAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String contentText = context.getResources().getString(R.string.break_time_finished);
        NotificationUtil notification = new NotificationUtil(context, NotificationUtil.BREAK_TIME_FINISHED_NOTIFICATION_ID) {
            @Override
            public void setBuilder() {
                builder.setContentText(contentText)
                        .addAction(R.drawable.ic_start_play_arrow_24, "START", getStartAction());
            }
        };
        notification.showNotification();
    }
}
