package com.breair_sb.apps.killerchair.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.breair_sb.apps.killerchair.R;

public class NotificationUtil {
    private NotificationManagerCompat notificationManager;
    public NotificationCompat.Builder builder;
    private final int notificationId;
    private Context context;
    private final String KC_timer_channelId = "KC_Timer";

    public NotificationUtil(Context context, int id) {
        this.context = context;
        this.notificationId = id;
        builder = new NotificationCompat.Builder(context, KC_timer_channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("not Started")
                .setNotificationSilent()
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        createBNotificationChannel();
        notificationManager = NotificationManagerCompat.from(context);
    }


    private void createBNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.background_notification_channel_name);
            String description = context.getString(R.string.background_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(KC_timer_channelId, name, importance);
            notificationChannel.setDescription(description);

            // you can't change the importance or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }

    public void updateNotification(String currentTime) {
        builder.setContentText(currentTime);
        notificationManager.notify(notificationId, builder.build());
    }
}
