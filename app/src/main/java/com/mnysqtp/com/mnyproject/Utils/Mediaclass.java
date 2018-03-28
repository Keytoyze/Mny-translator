package com.mnysqtp.com.mnyproject.Utils;

import android.media.MediaPlayer;
import android.widget.ImageButton;

import com.mnysqtp.com.mnyproject.R;

import java.io.IOException;

/**
 * Created by 22428 on 2017/12/10.
 */

public class Mediaclass {
    private static boolean isPlay = false;
    public static void Play(String path, ImageButton IB)throws IOException{
        final ImageButton IB_f = IB;
        if(isPlay){
            return;
        }
        isPlay = true;
        MediaPlayer mp = new MediaPlayer();
        IB_f.setImageResource(R.drawable.ic_volume_up_black_24dp);
        mp.setDataSource(path);
        mp.prepare();
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                IB_f.setImageResource(R.drawable.ic_volume_down_black_24dp);
                isPlay = false;
            }
        });
    }
}
