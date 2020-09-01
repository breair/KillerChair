package com.breair_sb.apps.killerchair;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class mSimpleCountDownTimer extends CountDownTimer {
    public TextView timerTimeTextView;
    private CircularProgressBar circularProgressBar;
    private TextView timePassedTextView;
    private int secPassed = 0;
    int progresspercent;
    boolean timerStarted;
    float[] hsv = {220, 89, 100};
    public NotificationCompat.Builder builder;
    public NotificationManagerCompat notificationManager;
    Context context;
    boolean inForeground;


    public mSimpleCountDownTimer(long millisInFuture, long millisPassed, long countDownInterval, ConstraintLayout timerLayout,
                                 Context context, boolean timerStarted, boolean inForeground) {
        super(millisInFuture - millisPassed, countDownInterval);
        if (inForeground) {
            this.timerStarted = timerStarted;
            this.inForeground = inForeground;
            this.context = context;
            timerTimeTextView = timerLayout.findViewById(R.id.timer_time);
            timePassedTextView = timerLayout.findViewById(R.id.time_passed);
            circularProgressBar = timerLayout.findViewById(R.id.custom_progressBar);
            circularProgressBar.setColor(Color.HSVToColor(hsv));
        } else {
            builder = new NotificationCompat.Builder(context, "Timer")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText("Time remaining: ")
                    .setNotificationSilent()
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            createBNotificationChannel();
            notificationManager = NotificationManagerCompat.from(context);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onTick(long millisUntilFinished) {
        if (inForeground) {
            timerTimeTextView.setText(String.format("%02d:%02d",
                    millisUntilFinished / 60000, (millisUntilFinished / 1000) % 60));
            progresspercent = (int) (240000 - millisUntilFinished) / 2400;
            circularProgressBar.setProgress(progresspercent);
            if ((millisUntilFinished / 1000) % 5 == 0) {
                hsv[0] = ((float) 100 - progresspercent) * 2.2f;
                circularProgressBar.setColor(Color.HSVToColor(hsv));

            }
            secPassed++;
            timePassedTextView.setText(String.format("Time Passed: %d min", secPassed / 60));
        } else {
            builder.setContentText(String.format("Time remaining: %02d:%02d",
                    millisUntilFinished / 60000, (millisUntilFinished / 1000) % 60));
            notificationManager.notify(0, builder.build());
        }
    }

    @Override
    public void onFinish() {
        if (inForeground) {
            timerTimeTextView.setText("done!");
            circularProgressBar.setProgress(100);
        } else {
            builder.setContentText("done!");
            notificationManager.notify(0, builder.build());
        }
        timerStarted = false;
        //TODO send notification || alarm
        start();
    }

    public int toBackground() {
        cancel();
        return secPassed;
    }

    private void createBNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* TODO null pointer refrence from resource
             *   because activity is destoryed*/
            CharSequence name = Resources.getSystem().getString(R.string.background_notification_channel_name);
            String description = Resources.getSystem().getString(R.string.background_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("Timer", name, importance);
            notificationChannel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }

}
