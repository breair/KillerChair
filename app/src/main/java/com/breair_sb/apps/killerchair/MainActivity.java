package com.breair_sb.apps.killerchair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;

import com.breair_sb.apps.killerchair.util.CircularProgressBar;

import static com.breair_sb.apps.killerchair.util.mSimpleTimerUtil.KC_TIMER_ACTION_Time_CHANGED;


public class MainActivity extends AppCompatActivity {
    private TextView timerTimeTextView;
    private CircularProgressBar circularProgressBar;
    //private TextView timePassedTextView;
    private Context context;
    private KC_TimerUIReceiver kc_timerUIReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ConstraintLayout timerLayout = findViewById(R.id.timer_Layout);
        timerTimeTextView = timerLayout.findViewById(R.id.timer_time);
        //timePassedTextView = timerLayout.findViewById(R.id.time_passed);TODO get passed time from shared prefrences
        circularProgressBar = timerLayout.findViewById(R.id.custom_progressBar);
        context = this;
        //Start the Timer service

        timerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //click to start
                Intent startIntent = new Intent(context, KC_TimerService.class);
                startIntent.setAction(KC_TimerService.KC_TIMER_ACTION_START);
                context.startService(startIntent);

            }
        });
        timerLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //long click to reset
                Intent resetIntent = new Intent(context, KC_TimerService.class);
                resetIntent.setAction(KC_TimerService.KC_TIMER_ACTION_RESET);
                context.startService(resetIntent);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //fire a broadcast receiver to update UI whenever the activity is visible
        //UI broadcastrececiver has higher priority
        kc_timerUIReceiver = new KC_TimerUIReceiver();
        IntentFilter intentFilter = new IntentFilter(KC_TIMER_ACTION_Time_CHANGED);
        intentFilter.setPriority(10);
        this.registerReceiver(kc_timerUIReceiver, intentFilter);
        //remove timer notification. TimerNotification id is 0
        NotificationManagerCompat.from(context).cancel(0);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(kc_timerUIReceiver);
    }

    //update UI
    public class KC_TimerUIReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(KC_TIMER_ACTION_Time_CHANGED)) {
                    abortBroadcast();//cancel the broadcast before it reach Notification Broadcastreceiver
                    String currentTime = "00:00";
                    int progresspercent = 0;
                    //String timePassed = "0 mins";
                    try {
                        currentTime = intent.getStringExtra("currentTimeleft");
                        //timePassed = intent.getStringExtra("passedTime");
                        progresspercent = intent.getIntExtra("progresspercent", 0);
                    } catch (Exception e) {
                        Log.e("intent", "onReceive-transferTimeProgress");
                    }
                    timerTimeTextView.setText(currentTime);
                    circularProgressBar.setProgress(progresspercent);
                    circularProgressBar.setColor(progresspercent);
                    //timePassedTextView.setText(getString(R.string.time_passed) + timePassed);
                }
            }
        }
    }
}
