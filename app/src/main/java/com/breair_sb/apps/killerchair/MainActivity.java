package com.breair_sb.apps.killerchair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.breair_sb.apps.killerchair.util.CircularProgressBar;

import static com.breair_sb.apps.killerchair.util.SimpleSittingTimerUtil.KC_TIMER_ACTION_Time_CHANGED;
import static com.breair_sb.apps.killerchair.util.SimpleSittingTimerUtil.formatTimeText;


public class MainActivity extends AppCompatActivity {
    private TextView timerTimeTextView;
    private CircularProgressBar circularProgressBar;
    //private TextView timePassedTextView;
    private ImageButton stopActionButton;
    private ImageButton settingsActivityButton;
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
        stopActionButton = findViewById(R.id.action_stoptimer);
        settingsActivityButton = findViewById(R.id.settings_activity_button);

        context = this;

        timerTimeTextView.setText(setDefTimeTextView());
        //open settings when clicked
        settingsActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingsActivity.class);
                context.startActivity(intent);
            }
        });
        //listen to when the button clicked ->Start the Timer service
        timerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //click to start
                Intent startIntent = new Intent(context, SittingTimerService.class);
                startIntent.setAction(SittingTimerService.KC_TIMER_ACTION_START);
                context.startService(startIntent);
                stopActionButton.setVisibility(View.VISIBLE);

            }
        });
        timerLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //long click to reset
                Intent resetIntent = new Intent(context, SittingTimerService.class);
                resetIntent.setAction(SittingTimerService.KC_TIMER_ACTION_RESET);
                context.startService(resetIntent);
                return false;
            }
        });
        //stop the timer when clicked
        stopActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stopIntent = new Intent(context, SittingTimerService.class);
                stopIntent.setAction(SittingTimerService.KC_TIMER_ACTION_STOP);
                context.startService(stopIntent);
                stopActionButton.setVisibility(View.INVISIBLE);
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

    String setDefTimeTextView() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        int time = sharedPrefs.getInt("SittingInterval", 0);
        String timeText = formatTimeText(time * 60000, 100);
        return timeText;

    }

    //update UI
    public class KC_TimerUIReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(KC_TIMER_ACTION_Time_CHANGED)) {
                    abortBroadcast();//cancel the broadcast before it reach Notification Broadcastreceiver
                    String currentTimeText = "00:00";
                    int progressInPercent = 0;
                    //String timePassed = "0 mins";
                    try {
                        currentTimeText = intent.getStringExtra("currentTimeleft");
                        //timePassed = intent.getStringExtra("passedTime");
                        progressInPercent = intent.getIntExtra("progressInPercent", 0);
                    } catch (Exception e) {
                        Log.e("intent", "onReceive-transferTimeProgress");
                    }
                    timerTimeTextView.setText(currentTimeText);
                    circularProgressBar.setProgress(progressInPercent);
                    circularProgressBar.setColor(progressInPercent);
                    //timePassedTextView.setText(getString(R.string.time_passed) + timePassed);
                }
            }
        }
    }
}
