package com.finalproject.idlegame;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class BackgroundMusicService extends Service {

    private static final float mStartVolume = (float)1.0;

    private static final String RES_LOCALE = "raw";
    private static final String MAIN_SONG_NAME = "main_song";

    MediaPlayer gameMusic;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        Toast.makeText(this, "Music service created", Toast.LENGTH_SHORT).show();

        int resID = getResources().getIdentifier(MAIN_SONG_NAME, RES_LOCALE, getPackageName());
        gameMusic = MediaPlayer.create(this, resID);
        gameMusic.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(this, "Playing music via service", Toast.LENGTH_LONG).show();
        try {
            gameMusic.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        gameMusic.setVolume(mStartVolume, mStartVolume);

        //Restart if killed. We need the vibes
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        Toast.makeText(this, "Stopping music service", Toast.LENGTH_LONG).show();

        gameMusic.stop();
    }
}
