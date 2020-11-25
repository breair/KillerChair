package com.breair_sb.apps.killerchair.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import com.breair_sb.apps.killerchair.R;

public class SittingTimerUtil extends CountDownTimer {

    public static final String KC_TIMER_ACTION_Time_CHANGED = "com.breair_sb.apps.killerchair.actiontimechanged";
    public static final String KC_TIMER_ACTION_FINISHED = "com.breair_sb.apps.killerchair.actiontimerfinished";
    private boolean istimerStarted;
    public long millisInFuture;
    final Context context;


    public SittingTimerUtil(long millisInFuture, long millisPassed, long countDownInterval, Context context) {
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
        Intent finishedIntent = new Intent();
        finishedIntent.setAction(KC_TIMER_ACTION_FINISHED);
        context.sendBroadcast(finishedIntent, null);
    }

    //sends broadcast to update (UI | notification)
    @SuppressLint("DefaultLocale")
    public void updateTimer(long millisUntilFinished) {
        //TODO check lint locale
        int progressInPercent = (int) ((millisInFuture - millisUntilFinished) * 100 / (millisInFuture));

        String currentTimeleft = formatTimeText(millisUntilFinished, progressInPercent, context);
        Intent updateintent = new Intent();
        updateintent.setAction(KC_TIMER_ACTION_Time_CHANGED)
                .putExtra("progressInPercent", progressInPercent)
                .putExtra("currentTimeleft", currentTimeleft);
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

    public static String formatTimeText(long millisUntilFinished, int progressInPercent, Context context) {
        String currentTimeleft;
        if (progressInPercent == 100) {
            currentTimeleft = context.getString(R.string.zero_time);

        } else {
            currentTimeleft = String.format("%02d:%02d",/*format time in mins:seconds*/
                    millisUntilFinished / 60000, (millisUntilFinished / 1000) % 60);
        }

        return currentTimeleft;
    }

}
