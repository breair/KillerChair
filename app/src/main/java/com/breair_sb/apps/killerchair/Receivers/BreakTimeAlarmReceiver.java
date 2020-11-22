package com.breair_sb.apps.killerchair.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.breair_sb.apps.killerchair.util.NotificationType;
import com.breair_sb.apps.killerchair.util.NotificationUtil;

public class BreakTimeAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationUtil notification = new NotificationUtil(context, NotificationType.BREAK_TIME_FINISHED);
        notification.builder.build();
    }
}
