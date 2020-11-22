package com.breair_sb.apps.killerchair.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.breair_sb.apps.killerchair.R;

public class AlarmUtil {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    public AlarmUtil(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.timer_beep);
        mediaPlayer.setLooping(true);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

    }

    public void start() {
        mediaPlayer.start();
        long[] pattren = {0, 50, 1000};
        vibrator.vibrate(pattren, 0);
    }

    public void stop() {
        mediaPlayer.stop();
        vibrator.cancel();
    }

}
