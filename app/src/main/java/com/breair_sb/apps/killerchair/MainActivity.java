package com.breair_sb.apps.killerchair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {
    private CircularProgressBar circularProgressBar;
    private ConstraintLayout constraintLayout;
    private TextView timerTimeTextView;
    private TextView timePassedTextView;
    private int minsPassed = 0;
    int progresspercent;
    float[] hsv = {230, 89, 100};
    boolean timerStarted = false;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.timer_Layout);
        timerTimeTextView = findViewById(R.id.timer_time);
        timePassedTextView = findViewById(R.id.time_passed);
        circularProgressBar = (CircularProgressBar) constraintLayout.findViewById(R.id.custom_progressBar);
        countDownTimer = new CountDownTimer(240000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTimeTextView.setText(String.format("%02d:%02d", millisUntilFinished / 60000, (millisUntilFinished / 1000) % 60));
                progresspercent = (int) (240000 - millisUntilFinished) / 2400;
                circularProgressBar.setProgress(progresspercent);
                if ((millisUntilFinished / 1000) % 5 == 0) {
                    hsv[0] = ((float) 100 - progresspercent) * 2.3f;
                    circularProgressBar.setColor(Color.HSVToColor(hsv));

                }
                if ((millisUntilFinished / 1000) % 60 == 0) {
                    minsPassed++;
                    timePassedTextView.setText(String.format("Time Passed: %d min",minsPassed));
                }
            }

            public void onFinish() {
                timerTimeTextView.setText("done!");
                circularProgressBar.setProgress(100);
                timerStarted = false;
                //send notification||alarm
                countDownTimer.start();

            }
        };
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!timerStarted) {
                    timerStarted = true;
                    countDownTimer.start();
                }
            }
        });
        constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                countDownTimer.cancel();
                timerStarted = false;
                return false;
            }
        });
    }


}