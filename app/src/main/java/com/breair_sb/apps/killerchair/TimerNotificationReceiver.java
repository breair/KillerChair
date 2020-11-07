package com.breair_sb.apps.killerchair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.breair_sb.apps.killerchair.util.TimerNotificationUtil;

import static com.breair_sb.apps.killerchair.util.SimpleSittingTimerUtil.KC_TIMER_ACTION_Time_CHANGED;

public class TimerNotificationReceiver extends BroadcastReceiver {

    private boolean isBuilded = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("broadReceiver", "notification receiver fired");
        if (intent.getAction() != null) {
            if (intent.getAction().equals(KC_TIMER_ACTION_Time_CHANGED)) {
                //format String like: 'Time remaining: 10:04'
                String currentTime = context.getResources().getString(R.string.time_remaining) + intent.getStringExtra("currentTimeleft");
                TimerNotificationUtil TimerNotification = new TimerNotificationUtil(context, 0);//timer notification id is 0
                if (!isBuilded) {
                    TimerNotification.builder.build();
                    isBuilded = true;
                }
                TimerNotification.updateNotification(currentTime);
            }
        }
    }

}
