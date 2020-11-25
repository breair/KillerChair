package com.breair_sb.apps.killerchair.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.breair_sb.apps.killerchair.MainActivity;
import com.breair_sb.apps.killerchair.R;
import com.breair_sb.apps.killerchair.SittingTimerService;

import static com.breair_sb.apps.killerchair.SittingTimerService.KC_BREAK_TIME_ACTION_START;
import static com.breair_sb.apps.killerchair.SittingTimerService.KC_TIMER_ACTION_RESET;
import static com.breair_sb.apps.killerchair.SittingTimerService.KC_TIMER_ACTION_START;

//TODO refactor notification system
public abstract class NotificationUtil {
    public static final int TIMER_STATUS_NOTIFICATION_ID = 0;
    public static final int TIMER_FINISHED_NOTIFICATION_ID = 1;
    public static final int BREAK_TIME_FINISHED_NOTIFICATION_ID = 2;
    private NotificationManagerCompat notificationManager;
    protected NotificationCompat.Builder builder;
    private final int NOTIFICATION_ID;
    private Context context;


    public NotificationUtil(Context context, int NOTIFICATION_ID) {
        notificationManager = NotificationManagerCompat.from(context);
        this.NOTIFICATION_ID = NOTIFICATION_ID;
        this.context = context;
        String timer_channelId = "KC_Timers";
        createNotificationChannel(context, timer_channelId);
        builder = new NotificationCompat.Builder(context, timer_channelId)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentText("")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getMainActivityPendingIntent());
        setBuilder();

    }

    protected abstract void setBuilder();

//    public int getNOTIFICATION_ID() {
//        return NOTIFICATION_ID;
//    }

    public void showNotification() {
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void updateNotification(String currentTime) {
        builder.setContentText(currentTime);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    protected PendingIntent getMainActivityPendingIntent() {
        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);//TODO WTH is this
        return PendingIntent.getActivity(context, 0, activityIntent, 0);
    }

    protected PendingIntent getResetAction() {
        Intent resetActionIntent = new Intent(context, SittingTimerService.class).setAction(KC_TIMER_ACTION_RESET);
        return PendingIntent.getService(context, 0, resetActionIntent, 0);
    }

    protected PendingIntent getStartAction() {
        Intent startActionIntent = new Intent(context, SittingTimerService.class).setAction(KC_TIMER_ACTION_START);
        return PendingIntent.getService(context, 0, startActionIntent, 0);
    }

    protected PendingIntent getBreakAction() {
        Intent breakActionIntent = new Intent(context, SittingTimerService.class).setAction(KC_BREAK_TIME_ACTION_START);
        return PendingIntent.getService(context, 0, breakActionIntent, 0);
    }

    private void createNotificationChannel(Context context, String channelId) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.notification_channel_name);
            String description = context.getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, name, importance);
            notificationChannel.setDescription(description);
            // you can't change the importance or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }


}
