package com.example.amr.spotifystreamer;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amr.spotifystreamer.data.AppContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.ErrorDetails;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistTopTenFragment extends Fragment {
    private Map<String, Object> spotifyTopTrackQueryParams;
    Intent newintent;
    SpotifyApi api=new SpotifyApi();
    SpotifyService spotify = api.getService();
    ArrayList<Track> tracks =new ArrayList<Track>();
    CustomListTopTen dataAdabter;
    TopTenTask toptenTask;
    ListView resultList;
    String[] trackUrls;
    static String artistID;
    private static final String PARAM_QUERY_COUNTRY = "country";


    public ArtistTopTenFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview=inflater.inflate(R.layout.fragment_artist_top_ten, container, false);
        Intent intent = getActivity().getIntent();
        spotifyTopTrackQueryParams = new Hashtable<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String countryCode = preferences.getString(getString(R.string.country_pref_key), getString(R.string.country_pref_default_value));
        resultList= (ListView) rootview.findViewById(R.id.topTenlistView);
        toptenTask=new TopTenTask();
        toptenTask.execute(artistID);
        int i=0;
        for(Track x : tracks){

            try{
                trackUrls[i]=(x.id);
                i++;

            }catch (Exception e){

            }
        }
        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Track me = (Track) dataAdabter.getItem(position);
                    MediaPlayerFragment.pos = position;
                    if (getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
                        DialogFragment newdialog = new MediaPlayerFragment();
                        newdialog.show(getActivity().getFragmentManager(), "Media Player");
                    } else {

                        newintent = new Intent(getActivity(), MediaPlayer.class);

                        startActivity(newintent);
                    }
                } catch (Exception e) {

                }

            }
        });
setRetainInstance(true);
        return rootview;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setRetainInstance(true);

        outState.putSerializable("track",tracks);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRetainInstance(true);
    }

    class TopTenTask extends AsyncTask<String, Void, List<Track>>{
        String artistID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List doInBackground(String... params)
        {
            try
            {
                artistID = params[0];
//                Log.v("Passed Id",artistID);
                Map<String, Object> options = new Hashtable<String, Object>();
                options.put("country", "US"); //Replace here
                Tracks tracksPager = spotify.getArtistTopTrack(this.artistID, options);
                tracks = new ArrayList<>();
                for (Track track : tracksPager.tracks)
                {
                    Track localTrack = track;
                    tracks.add(localTrack);
                }
                return tracks;
            }catch (RetrofitError error)
            {
                ErrorDetails details = SpotifyError.fromRetrofitError(error).getErrorDetails();
//                Log.i("deatils", "status " + details.status + ", message " + details.message);
//                System.out.println(details.status + ":" + details.message);
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<Track> tracks) {
            super.onPostExecute(tracks);
            if(tracks == null || tracks.isEmpty()){
                Toast.makeText(getActivity(),"No tracks found",Toast.LENGTH_LONG).show();
            }
            try {
                Log.v("Tracks List", tracks.toString());
 for(Track x:tracks){
     addArtist(x.id,x.name,x.album.name,x.album.images.get(0).url);
 }
                dataAdabter = new CustomListTopTen(getActivity(), (ArrayList<Track>) tracks);
                MediaPlayerFragment.trackID=tracks;
                resultList.setAdapter(dataAdabter);
                Log.v("1", "0");
            }catch (Exception e){
                Log.v("onpostexecute error",e.toString());
            }
        }
    }
    void addArtist(String trackID,String trackName, String albumName, String albumImage) {
        long locationId;

        // First, check if the location with this city name exists in the db
        Cursor trackCursor = getActivity().getContentResolver().query(
                AppContract.TrackEntry.CONTENT_URI,
                new String[]{AppContract.TrackEntry._ID},
                AppContract.TrackEntry.COLUMN_TRACK_ID + " = ?",
                new String[]{trackID},
                null);

        if (trackCursor.moveToFirst()) {
            int locationIdIndex = trackCursor.getColumnIndex(AppContract.TrackEntry._ID);
            locationId = trackCursor.getLong(locationIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues trackValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            trackValues.put(AppContract.TrackEntry.COLUMN_TRACK_ID, trackID);
            trackValues.put(AppContract.TrackEntry.COLUMN_TRACK_NAME, trackName);
            trackValues.put(AppContract.TrackEntry.COLUMN_ALBUM_NAME, albumName);
            trackValues.put(AppContract.TrackEntry.COLUMN_ALBUM_IMAGE, albumImage);
            // Finally, insert location data into the database.

            Uri insertedUri = getActivity().getContentResolver().insert(
                    AppContract.TrackEntry.CONTENT_URI,
                    trackValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
        }

        trackCursor.close();
        // Wait, that worked?  Yes!

    }

}


class CustomListTopTen extends ArrayAdapter{

    private final Activity context;
    private ArrayList<Track> mylist=new ArrayList<Track>();

    public CustomListTopTen(Activity context1,ArrayList<Track> track1) {
        super(context1, R.layout.list_item_view, track1);
        this.context = context1;
        this.mylist = track1;
        Log.v("10","1");

    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public View getView(int position,View view,ViewGroup parent){
        Log.v("1","2");
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.toptenitemview, null, true);
        TextView songname=(TextView)rowView.findViewById(R.id.songTextView);
        TextView albumname=(TextView)rowView.findViewById(R.id.albumTextView);
        ImageView image=(ImageView)rowView.findViewById(R.id.albumArtImageView);
        Track local= mylist.get(position);
        Log.v("First one", local.name);
        songname.setText(local.name);
        albumname.setText(local.album.name);
        try {
            Picasso.with(context).load(local.album.images.get(0).url).into(image);
        }catch (Exception
                e){
            Log.v("Exception song image",e.toString());
        }

        return rowView;
    }
}