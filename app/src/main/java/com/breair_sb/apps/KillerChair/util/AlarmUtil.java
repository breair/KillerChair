package com.breair_sb.apps.KillerChair.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.breair_sb.apps.KillerChair.R;

public class AlarmUtil {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    public AlarmUtil(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.sound0);
        mediaPlayer.setLooping(true);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

    }

    private void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(250);
        }
    }

    public void start() {
        mediaPlayer.start();
        vibrate();

    }

    public void stop() {
        mediaPlayer.stop();
        vibrator.cancel();
    }

}
