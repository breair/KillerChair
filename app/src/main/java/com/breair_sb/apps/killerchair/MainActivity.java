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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.breair_sb.apps.killerchair.util.CircularProgressBar;
import com.breair_sb.apps.killerchair.util.NotificationUtil;
import com.breair_sb.apps.killerchair.util.ThemeUtil;

import java.util.Random;

import static com.breair_sb.apps.killerchair.util.SittingTimerUtil.KC_TIMER_ACTION_Time_CHANGED;
import static com.breair_sb.apps.killerchair.util.SittingTimerUtil.formatTimeText;


public class MainActivity extends AppCompatActivity {
    public final int THEME_REQUEST_CODE = 1;
    private CircularProgressBar circularProgressBar;
    private TextView timerTimeTextView;
    private TextView quoteTextView;
    private ImageButton stopActionButton;
    private Context context;
    private TimerUIReceiver timerUIReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.checkTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConstraintLayout timerLayout = findViewById(R.id.timer_Layout);
        timerTimeTextView = timerLayout.findViewById(R.id.timer_time);
        quoteTextView = findViewById(R.id.quote);
        circularProgressBar = timerLayout.findViewById(R.id.custom_progressBar);
        stopActionButton = findViewById(R.id.action_stoptimer);
        ImageButton settingsActivityButton = findViewById(R.id.settings_activity_button);
        context = this;
        NotificationManagerCompat.from(context).cancel(0);
        timerTimeTextView.setText(setDefTimeTextView());
        quoteTextView.setText(pickRandomQuote());
        //open settings when clicked
        settingsActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivityForResult(intent, THEME_REQUEST_CODE);
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
        timerUIReceiver = new TimerUIReceiver();
        IntentFilter intentFilter = new IntentFilter(KC_TIMER_ACTION_Time_CHANGED);
        intentFilter.setPriority(10);
        this.registerReceiver(timerUIReceiver, intentFilter);
        //remove timer notification. TimerNotification id is 0
        NotificationManagerCompat.from(context).cancel(NotificationUtil.TIMER_STATUS_NOTIFICATION_ID);
        quoteTextView.setText(pickRandomQuote());
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(timerUIReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == THEME_REQUEST_CODE) {
            recreate();
        }
    }

    private String pickRandomQuote() {
        String[] quotes = getResources().getStringArray(R.array.qoutes);
        Random random = new Random();
        return quotes[random.nextInt(quotes.length)];
    }

    private String setDefTimeTextView() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        int time = sharedPrefs.getInt("SittingInterval", 0);
        return formatTimeText(time * 60000, 100, context);

    }

    //update UI
    public class TimerUIReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(KC_TIMER_ACTION_Time_CHANGED)) {
                    abortBroadcast();//cancel the broadcast before it reach Notification Broadcastreceiver
                    String currentTimeText = context.getString(R.string.zero_time);
                    int progressInPercent = 0;
                    try {
                        currentTimeText = intent.getStringExtra("currentTimeleft");
                        progressInPercent = intent.getIntExtra("progressInPercent", 0);

                    } catch (Exception e) {
                        Log.e("intent", "onReceive-transferTimeProgress");
                    }
                    timerTimeTextView.setText(currentTimeText);
                    circularProgressBar.setProgress(progressInPercent);
                    circularProgressBar.setColor(progressInPercent);

                }
            }
        }
    }

}
