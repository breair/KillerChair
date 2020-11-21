package com.breair_sb.apps.killerchair.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;

public class SimpleSittingTimerUtil extends CountDownTimer {

    public static final String KC_TIMER_ACTION_Time_CHANGED = "com.breair_sb.apps.killerchair.actiontimechanged";
    public static final String KC_TIMER_ACTION_FINISHED = "com.breair_sb.apps.killerchair.actiontimerfinished";
    public static final String TIME_PASSED_KEY = "TimePassed";
    private boolean istimerStarted;
    public long millisInFuture = 25;
    private long timePassedInMins;
    private long startTimeMillis;
    private SharedPreferences sharedPrefs;
    Context context;


    public SimpleSittingTimerUtil(long millisInFuture, long millisPassed, long countDownInterval, Context context) {
        super(millisInFuture - millisPassed/*to be able to start the timer from when it stopped*/, countDownInterval);
        this.millisInFuture = millisInFuture;
        this.context = context;
        startTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        updateTimer(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        updatePassedTime(context, getTimePassedInMins());
        updateTimer(millisInFuture);
        istimerStarted = false;
        Intent finishedIntent = new Intent();
        finishedIntent.setAction(KC_TIMER_ACTION_FINISHED);
        context.sendBroadcast(finishedIntent, null);
    }

    //sends broadcast to update (UI | notification)
    @SuppressLint("DefaultLocale")
    public void updateTimer(long millisUntilFinished) {
        //TODO check lint locale
        int progressInPercent = (int) ((millisInFuture - millisUntilFinished) * 100 / (millisInFuture));

        String currentTimeleft = formatTimeText(millisUntilFinished, progressInPercent);
        //change color every 5s and change passed time text view
        boolean colorChanged = false;
        //if (millisUntilFinished % 5000 == 0) {
        colorChanged = true;
        //}
        boolean timePassedChanged = false;
        //if (millisUntilFinished % 60 < 1200 && millisUntilFinished % 60 > 800) {
        timePassedChanged = true;
        //}
        updatePassedTime(context, 65);
        Intent updateintent = new Intent();
        updateintent.setAction(KC_TIMER_ACTION_Time_CHANGED)
                .putExtra("progressInPercent", progressInPercent)
                .putExtra("currentTimeleft", currentTimeleft)
                .putExtra("colorChanged", colorChanged)
                .putExtra("timePassedChanged", timePassedChanged);
        context.sendOrderedBroadcast(updateintent, null);


    }

    public boolean isRunning() {
        return istimerStarted;
    }

    public void startTimer() {
        start();
        istimerStarted = true;

    }

    public void resetTimer() {
        cancel();
        start();
        istimerStarted = true;

    }

    public void stopTimer(long nextMillisInFuture) {
        updatePassedTime(context, getTimePassedInMins());
        cancel();
        millisInFuture = nextMillisInFuture;
        updateTimer(nextMillisInFuture);
        istimerStarted = false;
    }

    @SuppressLint("DefaultLocale")
    public static String formatTimeText(long millisUntilFinished, int progressInPercent) {
        String currentTimeleft;
        if (progressInPercent == 100) {
            currentTimeleft = "00:00";
        } else {
            currentTimeleft = String.format("%02d:%02d",/*format time in mins:seconds*/
                    millisUntilFinished / 60000, (millisUntilFinished / 1000) % 60);
        }

        return currentTimeleft;
    }

    private long getTimePassedInMins() {
        sharedPrefs = context.getSharedPreferences("time_passed", Context.MODE_PRIVATE);
        return sharedPrefs.getLong(TIME_PASSED_KEY, 0);
    }

    private void updatePassedTime(Context context, long timePassed) {
        long stoptimeMillis = System.currentTimeMillis();
        timePassedInMins = timePassed;
        timePassedInMins += (stoptimeMillis - startTimeMillis) / 60000;
        sharedPrefs = context.getSharedPreferences("time_passed", Context.MODE_PRIVATE);
        sharedPrefs.edit().putLong(TIME_PASSED_KEY, timePassedInMins).apply();
    }

}
