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

import static com.breair_sb.apps.killerchair.SittingTimerService.KC_TIMER_ACTION_RESET;
//TODO refactor notification system
public class NotificationUtil {
    private NotificationManagerCompat notificationManager;
    public NotificationCompat.Builder builder;


    public NotificationUtil(Context context, NotificationType notificationType) {
        notificationManager = NotificationManagerCompat.from(context);

        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//TODO WTH is this
        PendingIntent activityPendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

        Intent resetActionIntent = new Intent(context, SittingTimerService.class).setAction(KC_TIMER_ACTION_RESET);
        PendingIntent resetActionPendingIntent = PendingIntent.getService(context, 0, resetActionIntent, 0);

        String timer_channelId = "KC_Timers";
        createNotificationChannel(context, timer_channelId);
        builder = new NotificationCompat.Builder(context, timer_channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("not Started")
                .setNotificationSilent()
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(activityPendingIntent)
                .addAction(R.drawable.ic_timer_reset_24, "RESET", resetActionPendingIntent);

    }


    public static void createNotificationChannel(Context context, String channelId) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.notification_channel_name);
            String description = context.getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, name, importance);
            notificationChannel.setDescription(description);

            // you can't change the importance or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }

    public void updateNotification(String currentTime) {
        builder.setContentText(currentTime);
        notificationManager.notify(0, builder.build());
    }

    public void cancelNotification() {
        notificationManager.cancel(0);
    }

}
