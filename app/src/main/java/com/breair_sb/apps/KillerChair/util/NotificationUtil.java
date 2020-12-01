package com.breair_sb.apps.KillerChair.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.breair_sb.apps.KillerChair.MainActivity;
import com.breair_sb.apps.KillerChair.R;
import com.breair_sb.apps.KillerChair.SittingTimerService;

import static com.breair_sb.apps.KillerChair.SittingTimerService.KC_BREAK_TIME_ACTION_START;
import static com.breair_sb.apps.KillerChair.SittingTimerService.KC_TIMER_ACTION_RESET;
import static com.breair_sb.apps.KillerChair.SittingTimerService.KC_TIMER_ACTION_START;

//TODO refactor notification system
public abstract class NotificationUtil {
    public static final int TIMER_STATUS_NOTIFICATION_ID = 0;
    public static final int TIMER_FINISHED_NOTIFICATION_ID = 1;
    public static final int BREAK_TIME_FINISHED_NOTIFICATION_ID = 2;
    private NotificationManagerCompat notificationManager;
    protected NotificationCompat.Builder builder;
    private int NOTIFICATION_ID;
    private Context context;


    public NotificationUtil(Context context, int NOTIFICATION_ID) {
        notificationManager = NotificationManagerCompat.from(context);
        this.NOTIFICATION_ID = NOTIFICATION_ID;
        this.context = context;
        String timer_channelId = "KillerChairNC";
        createNotificationChannel(context, timer_channelId);
        Uri soundURI = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.sound0);
        builder = new NotificationCompat.Builder(context, timer_channelId)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentText("")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(getMainActivityPendingIntent())
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setSound(soundURI, AudioManager.STREAM_NOTIFICATION);
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
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(activityIntent);
        return taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

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
            Uri soundURI = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.sound1);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            NotificationChannel notificationChannel = new NotificationChannel(channelId, name, importance);
            notificationChannel.setDescription(description);
            notificationChannel.setSound(soundURI, audioAttributes);
            // you can't change the importance or other notification behaviors after this
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.deleteNotificationChannel(channelId);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


}
