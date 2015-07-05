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
    MediaPlayer player;
    String songURL;

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
    public void onCreate(){
        super.onCreate();
        player=new MediaPlayer();
        try {
            initMusicPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setSong(String url){
        this.songURL=url;
    }
    public class MusicBinder extends Binder {
         MediaPlayBackService getService(){
            return MediaPlayBackService.this;
        }
    }
    public void PlayMusic(){
        player.start();
    }
    public int getDuration(){
     return   player.getDuration();
    }
    public int getCurrentPosition(){
        return player.getCurrentPosition();
    }
    public void pauseMusic(){
        player.pause();
    }
    public void initMusicPlayer() throws IOException {
        player=new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        Log.v("I am here", songURL);
        player.setDataSource(getApplicationContext(), Uri.parse(songURL));

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });
        player.prepareAsync();

    }
}
