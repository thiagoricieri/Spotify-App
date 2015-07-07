package com.example.amr.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.media.*;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;

public class MediaPlayBackService extends Service implements android.media.MediaPlayer.OnPreparedListener,
        android.media.MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{
    static MediaPlayer player=new MediaPlayer();
    static String songURL;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player=new MediaPlayer();

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);


        player.setWakeMode(this,
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        try {
            initMusicPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void onCreate(){
        super.onCreate();
        player=new MediaPlayer();

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);



        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        try {
            initMusicPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onStop(){


    }

    @Override
    public void onDestroy() {
        player.release();
        super.onDestroy();
    }

    static public void setSong(String url){
        songURL=url;
    }
    public class MusicBinder extends Binder {
        MediaPlayBackService getService(){
            return MediaPlayBackService.this;
        }
    }
    public static void PlayMusic() throws IOException {

        player.start();
    }
    public static int getDuration(){
        return   player.getDuration();
    }
    public static int getCurrentPosition(){
        return player.getCurrentPosition();
    }
    public static void pauseMusic(){
        player.pause();
    }
    public  void initMusicPlayer() throws IOException {

        player.setDataSource(this, Uri.parse(songURL));
        player.prepareAsync();
        Log.v("I am here", songURL);
//
//        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.start();
//            }
//        });


    }
}

