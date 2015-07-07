package com.example.amr.spotifystreamer;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ErrorDetails;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

/**
 * A placeholder fragment containing a simple view.
 */
public class MediaPlayerFragment extends Fragment {
//    MediaPlayer mediaPlayer = new MediaPlayer();

    private Button previousButton, pauseButton, playButton, nextButton;
    private ImageView albumartImageView;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    String songID;
String[] trackID;
    SpotifyApi api=new SpotifyApi();
    SpotifyService spotify = api.getService();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1, tx2, tx3;
    Artist myartist;
    Tracks topten;
    int position;
    public static int oneTimeOnly = 0;
    MediaPlayBackService Playservice;
    public MediaPlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final getSongTask x=new getSongTask();


        position=0;
        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);
        Intent intent=getActivity().getIntent();
        songID=intent.getStringExtra("IDs");


         x.execute(songID);


        previousButton = (Button) rootView.findViewById(R.id.previosButton);
        pauseButton = (Button) rootView.findViewById(R.id.pauseButton);
        playButton = (Button) rootView.findViewById(R.id.playButton);
        nextButton = (Button) rootView.findViewById(R.id.nextButton);
        albumartImageView = (ImageView) rootView.findViewById(R.id.songImage);

        tx1 = (TextView) rootView.findViewById(R.id.artistName);
        tx2 = (TextView) rootView.findViewById(R.id.songName);
        tx3=(TextView) rootView.findViewById(R.id.seekText);


        seekbar = (SeekBar) rootView.findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        pauseButton.setEnabled(false);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Playing sound", Toast.LENGTH_SHORT).show();
                try {
                    MediaPlayBackService.PlayMusic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finalTime = MediaPlayBackService.getDuration();
                startTime = MediaPlayBackService.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }

                tx3.setText(String.format("%d min, %d sec",
                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                );

                seekbar.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 100);
                pauseButton.setEnabled(true);
                playButton.setEnabled(false);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Pausing sound", Toast.LENGTH_SHORT).show();
MediaPlayBackService.pauseMusic();
                pauseButton.setEnabled(false);
                playButton.setEnabled(true);
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int localpos=position-1;
                x.execute(trackID[localpos]);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int localpos=position-1;
                x.execute(trackID[localpos]);
            }
        });
        return rootView ;
    }
        private Runnable UpdateSongTime = new Runnable() {
            public void run() {
                startTime = MediaPlayBackService.getCurrentPosition();
                tx3.setText(String.format("%d min, %d sec",

                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                toMinutes((long) startTime)))
                );
                seekbar.setProgress((int)startTime);
                myHandler.postDelayed(this, 100);
            }
        };


    class getSongTask extends AsyncTask<String, Void, Track> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Track doInBackground(String... params)
        {
            try
            {
                songID = params[0];
                Log.v("Passed Somng Id", songID);
                Track mytrack = spotify.getTrack(songID);
                Intent newintent=new Intent(getActivity(),MediaPlayBackService.class);
                getActivity().stopService(newintent);

                return mytrack;
            }catch (RetrofitError error)
            {
                ErrorDetails details = SpotifyError.fromRetrofitError(error).getErrorDetails();
                Log.i("deatils", "status " + details.status + ", message " + details.message);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Track tracks) {
            super.onPostExecute(tracks);

            try {
                Picasso.with(getActivity()).load(tracks.album.images.get(0).url).into(albumartImageView);
                tx1.setText(tracks.album.name);
                tx2.setText(tracks.name);
                MediaPlayBackService.setSong(tracks.preview_url);
                Intent newintent=new Intent(getActivity(),MediaPlayBackService.class);
                getActivity().startService(newintent);
            } catch (Exception e) {
                Log.v("Playback error",e.toString());
            }
        }
    }

    }

