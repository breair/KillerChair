package com.breair_sb.apps.killerchair.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

public class SimpleSittingTimerUtil extends CountDownTimer {

    public static final String KC_TIMER_ACTION_Time_CHANGED = "com.breair_sb.apps.killerchair.actiontimechanged";
    private boolean istimerStarted;
    public long millisInFuture = 25;
    Context context;


    public SimpleSittingTimerUtil(long millisInFuture, long millisPassed, long countDownInterval, Context context) {
        super(millisInFuture - millisPassed/*to be able to start the timer from when it stopped*/, countDownInterval);
        this.millisInFuture = millisInFuture;
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        updateTimer(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        updateTimer(millisInFuture);
        istimerStarted = false;
        //TODO send notification || alarm
        start();
    }

    //sends broadcast to update (UI | notification)
    @SuppressLint("DefaultLocale")
    public void updateTimer(long millisUntilFinished) {
        //TODO check lint locale
        int progressInPercent = (int) ((millisInFuture - millisUntilFinished) * 100 / (millisInFuture));
        String currentTimeleft = formatTimeText(millisUntilFinished, progressInPercent);
        Intent updateintent = new Intent();
        updateintent.setAction(KC_TIMER_ACTION_Time_CHANGED);
        updateintent.putExtra("progressInPercent", progressInPercent);
        updateintent.putExtra("currentTimeleft", currentTimeleft);
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
        cancel();
        millisInFuture = nextMillisInFuture;
        updateTimer(nextMillisInFuture);
        istimerStarted = false;
    }

    @SuppressLint("DefaultLocale")
    public static String formatTimeText(long millisUntilFinished, int progressInPercent) {
        String currentTimeleft;
        if (progressInPercent == 100) {
            currentTimeleft = "START";
        } else {
            currentTimeleft = String.format("%02d:%02d",/*format time in mins:seconds*/
                    millisUntilFinished / 60000, (millisUntilFinished / 1000) % 60);
        }

        return currentTimeleft;
    }

}
