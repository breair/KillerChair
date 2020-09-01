package com.breair_sb.apps.killerchair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;
    private boolean timerStarted = false;
    public mSimpleCountDownTimer sittingTimer;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        constraintLayout = findViewById(R.id.constraint_layout);
        ConstraintLayout timerLayout = findViewById(R.id.timer_Layout);
        sittingTimer = new mSimpleCountDownTimer(240000, 0, 1000, constraintLayout, this, timerStarted, true);


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

    @Override
    protected void onPause() {
        long millisPassed = (long) (sittingTimer.toBackground()) * 1000;
        sittingTimer = new mSimpleCountDownTimer(240000, millisPassed, 1000, constraintLayout, context.getApplicationContext(), timerStarted, false);
        super.onPause();
    }


}