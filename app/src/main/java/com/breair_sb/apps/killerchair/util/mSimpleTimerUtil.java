package com.breair_sb.apps.killerchair.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

public class mSimpleTimerUtil extends CountDownTimer {

    public static final String KC_TIMER_ACTION_Time_CHANGED = "com.breair_sb.apps.killerchair.actiontimechanged";
    private boolean timerStarted;
    Context context;


    public mSimpleTimerUtil(long millisInFuture, long millisPassed, long countDownInterval, Context context) {
        super(millisInFuture - millisPassed, countDownInterval);
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        updateTimer(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        updateTimer(0);
        timerStarted = false;
        //TODO send notification || alarm
        start();
    }

    @SuppressLint("DefaultLocale")
    public void updateTimer(long millisUntilFinished) {
        Intent updateintent = new Intent();
        updateintent.setAction(KC_TIMER_ACTION_Time_CHANGED);
        updateintent.putExtra("progresspercent", (int) (240000 - millisUntilFinished) / 2400);
        //TODO check lint locale
        updateintent.putExtra("currentTimeleft", String.format("%02d:%02d",
                millisUntilFinished / 60000, (millisUntilFinished / 1000) % 60));
        context.sendOrderedBroadcast(updateintent, null);


    }

    public boolean isRunning() {
        return timerStarted;
    }

    public void startTimer() {
        start();
        timerStarted = true;

    }

    public void resetTimer() {
        cancel();
        start();
        timerStarted = true;

    }

    public void stopTimer() {
        cancel();
        updateTimer(0);
        timerStarted = false;
    }
}
