package com.example.amr.spotifystreamer;

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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ErrorDetails;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

import static android.net.Uri.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class MediaPlayerFragment extends Fragment {
    MediaPlayer mediaPlayer = new MediaPlayer();

    private Button b1, b2, b3, b4;
    private ImageView iv;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    String songID;
    SpotifyApi api=new SpotifyApi();
    SpotifyService spotify = api.getService();

    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1, tx2, tx3;

    public static int oneTimeOnly = 0;

    public MediaPlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);
        Intent intent=getActivity().getIntent();
        songID=intent.getStringExtra("Song");
        Log.v("Song ID", songID);
        getSongTask x=new getSongTask();
        x.execute(songID);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        b1 = (Button) rootView.findViewById(R.id.button);
        b2 = (Button) rootView.findViewById(R.id.button2);
        b3 = (Button) rootView.findViewById(R.id.button3);
        b4 = (Button) rootView.findViewById(R.id.button4);
        iv = (ImageView) rootView.findViewById(R.id.songImage);

        tx1 = (TextView) rootView.findViewById(R.id.artistName);
        tx2 = (TextView) rootView.findViewById(R.id.songName);
        tx3=(TextView) rootView.findViewById(R.id.seekText);


        seekbar = (SeekBar) rootView.findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        b2.setEnabled(false);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Playing sound", Toast.LENGTH_SHORT).show();
                mediaPlayer.start();

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }
//                tx3.setText(String.format("%d min, %d sec",
//                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
//                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
//                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
//                );

                tx3.setText(String.format("%d min, %d sec",
                                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                );

                seekbar.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 100);
                b2.setEnabled(true);
                b3.setEnabled(false);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Pausing sound", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                b2.setEnabled(false);
                b3.setEnabled(true);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getActivity(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;

                if ((temp - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getActivity(), "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView ;
    }

        private Runnable UpdateSongTime = new Runnable() {
            public void run() {
                startTime = mediaPlayer.getCurrentPosition();
                tx1.setText(String.format("%d min, %d sec",

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

                return mytrack;
            }catch (RetrofitError error)
            {
                ErrorDetails details = SpotifyError.fromRetrofitError(error).getErrorDetails();
                Log.i("deatils", "status " + details.status + ", message " + details.message);
//                System.out.println(details.status + ":" + details.message);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Track tracks) {
            super.onPostExecute(tracks);

//            try {
//                Log.v("Tracks List", tracks.toString());
//                dataAdabter = new CustomListTopTen(getActivity(), (ArrayList<Track>) tracks);
//                resultList.setAdapter(dataAdabter);
//                Log.v("1", "0");
//            }catch (Exception e){
//                Log.v("onpostexecute error",e.toString());
            try {
                Picasso.with(getActivity()).load(String.valueOf(tracks.album.images.get(0))).into(iv);
                tx1.setText(tracks.album.name);
                tx2.setText(tracks.name);
                mediaPlayer.setDataSource(getActivity(), Uri.parse(tracks.preview_url));
                mediaPlayer.prepare();
//                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//                        mp.start();
//                    }
//                });
            } catch (IOException e) {
                Log.v("Playback error",e.toString());
            }

//            mediaPlayer.start();
            Log.v("URI Song", tracks.name);
// }
        }
    }

    }

