package com.breair_sb.apps.killerchair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.breair_sb.apps.killerchair.util.NotificationUtil;

import static com.breair_sb.apps.killerchair.util.mSimpleTimerUtil.KC_TIMER_ACTION_Time_CHANGED;

public class KC_TimerNotificationReceiver extends BroadcastReceiver {

    private boolean notBuilded = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("rcvr", "notification reciver fired");
        if (intent.getAction() != null) {
            if (intent.getAction().equals(KC_TIMER_ACTION_Time_CHANGED)) {
                String currentTime = intent.getStringExtra("currentTimeleft");
                NotificationUtil TimerNotification = new NotificationUtil(context, 0);//timer notification id is 0
                if (notBuilded) {
                    TimerNotification.builder.build();
                    notBuilded = false;
                }
                TimerNotification.updateNotification(context.getResources().getString(R.string.time_remaining) + currentTime);
            }
        }
    }

}
