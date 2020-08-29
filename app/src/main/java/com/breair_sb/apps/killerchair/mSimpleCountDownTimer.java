package com.breair_sb.apps.killerchair;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class mSimpleCountDownTimer extends CountDownTimer {
    private TextView timerTimeTextView;
    private CircularProgressBar circularProgressBar;
    private TextView timePassedTextView;
    private int minsPassed = 0;
    int progresspercent;
    boolean timerStarted = false;
    float[] hsv = {220, 89, 100};


    public mSimpleCountDownTimer(long millisInFuture, long countDownInterval, ConstraintLayout timerLayout, boolean timerStarted) {
        super(millisInFuture, countDownInterval);
        this.timerStarted = timerStarted;
        timerTimeTextView = timerLayout.findViewById(R.id.timer_time);
        timePassedTextView = timerLayout.findViewById(R.id.time_passed);
        circularProgressBar = (CircularProgressBar) timerLayout.findViewById(R.id.custom_progressBar);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timerTimeTextView.setText(String.format("%02d:%02d", millisUntilFinished / 60000, (millisUntilFinished / 1000) % 60));
        progresspercent = (int) (240000 - millisUntilFinished) / 2400;
        circularProgressBar.setProgress(progresspercent);
        if ((millisUntilFinished / 1000) % 5 == 0) {
            hsv[0] = ((float) 100 - progresspercent) * 2.2f;
            circularProgressBar.setColor(Color.HSVToColor(hsv));

        }
        if ((millisUntilFinished / 1000) % 60 == 0) {
            minsPassed++;
            timePassedTextView.setText(String.format("Time Passed: %d min", minsPassed));
        }
    }

    @Override
    public void onFinish() {
        timerTimeTextView.setText("done!");
        circularProgressBar.setProgress(100);
        timerStarted = false;
        //send notification||alarm
        start();

    }
}
