package com.breair_sb.apps.killerchair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;
    private boolean timerStarted = false;
    public mSimpleCountDownTimer sittingTimer;
    public NotificationCompat.Builder builder;
    public NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.constraint_layout);
        ConstraintLayout timerLayout = findViewById(R.id.timer_Layout);
        builder = new NotificationCompat.Builder(this, "Timer")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Time remaining: ")
                .setNotificationSilent()
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel();
        notificationManager = NotificationManagerCompat.from(this);
        sittingTimer = new mSimpleCountDownTimer(240000, 1000, constraintLayout, timerStarted) {
            @Override
            public void onTick(long millisUntilFinished) {
                super.onTick(millisUntilFinished);
                builder.setContentText("Time remaining: " + sittingTimer.timerTimeTextView.getText().toString());
                notificationManager.notify(0, builder.build());
            }

        };
        timerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timerStarted) {
                    timerStarted = true;
                    sittingTimer.start();
                }
            }
        });
        timerLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                sittingTimer.cancel();
                timerStarted = false;
                return false;
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("Timer", name, importance);
            notificationChannel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }


}