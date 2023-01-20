package com.finalproject.idlegame;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BackgroundMusicService extends Service {

    private static final float mStartVolume = (float)1.0;

    private static final String TAG = "BackgroundMusicService";
    private static final String RES_LOCALE = "raw";
    private static final String MAIN_SONG_NAME = "main_song";
    private static final String INTENT_MUSIC_PAUSE = "com.finalproject.idlegame.BackgroundMusicService.PAUSE";

    MediaPlayer gameMusic;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        //Helps in debugging
        Toast.makeText(this, "Music service created", Toast.LENGTH_SHORT).show();

        //Intent listener to pause music
        IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_MUSIC_PAUSE);
        this.registerReceiver(mReceiver, filter);

        //Get music file ID for MediaPlayer.
        int resID = getResources().getIdentifier(MAIN_SONG_NAME, RES_LOCALE, getPackageName());
        gameMusic = MediaPlayer.create(this, resID);
        gameMusic.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(this, "Playing music via service", Toast.LENGTH_LONG).show();

        //Tries to start the song.
        try {
            gameMusic.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        //Sets volume to max in app, does not change anything on the system side.
        gameMusic.setVolume(mStartVolume, mStartVolume);

        //Restart if killed. We need the vibes
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        Toast.makeText(this, "Stopping music service", Toast.LENGTH_LONG).show();

        gameMusic.stop();
    }

    private void musicPause(){
        gameMusic.pause();
    }

    //Receiver pauses the music
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, action);
            if(action.equals(INTENT_MUSIC_PAUSE)){
            musicPause();
            }
        }
    };
}
