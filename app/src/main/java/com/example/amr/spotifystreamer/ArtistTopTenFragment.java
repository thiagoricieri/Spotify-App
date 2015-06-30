package com.example.amr.spotifystreamer;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
    ArrayList<Track> trackList =new ArrayList<Track>();
    CustomListTopTen dataAdabter;
    TopTenTask toptenTask;
    ListView resultList;
    List tracks;
    private static final String PARAM_QUERY_COUNTRY = "country";

    public ArtistTopTenFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootview=inflater.inflate(R.layout.fragment_artist_top_ten, container, false);
        Intent intent = getActivity().getIntent();
        String artistID=intent.getStringExtra("ar");
        Log.v("artist ID",artistID.toString());
        spotifyTopTrackQueryParams = new Hashtable<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String countryCode = preferences.getString(getString(R.string.country_pref_key), getString(R.string.country_pref_default_value));

        resultList= (ListView) rootview.findViewById(R.id.topTenlistView);
        toptenTask=new TopTenTask();
        toptenTask.execute(artistID);
        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Track me=(Track) dataAdabter.getItem(position);
                    newintent=new Intent(getActivity(),MediaPlayer.class);
                    newintent.putExtra("Song", me.id);

                    Log.v("Song ID before send",me.id);
                    startActivity(newintent);

                } catch (Exception e) {

                }

            }
        });

        return rootview;

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
                Log.v("Passed Id",artistID);
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
                Log.i("deatils", "status " + details.status + ", message " + details.message);
//                System.out.println(details.status + ":" + details.message);
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<Track> tracks) {
            super.onPostExecute(tracks);
            try {
                Log.v("Tracks List", tracks.toString());
                dataAdabter = new CustomListTopTen(getActivity(), (ArrayList<Track>) tracks);
                resultList.setAdapter(dataAdabter);
                Log.v("1", "0");
            }catch (Exception e){
                Log.v("onpostexecute error",e.toString());
            }
        }
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