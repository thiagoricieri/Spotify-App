package com.example.amr.spotifystreamer;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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

import com.example.amr.spotifystreamer.data.AppContract.*;
import com.example.amr.spotifystreamer.data.AppDbHelper;
import com.example.amr.spotifystreamer.data.AppProvider;
import com.squareup.picasso.Picasso;
import  com.example.amr.spotifystreamer.ArtistAdapter.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment  {
    static final int COL_ARTIST_ID = 0;
    static final int COL_ARTIST_NAME = 1;
    static final int COL_ARTIST_IMAGE = 2;
    static final int COL_TRACK_ID = 0;
    static final int COL_TRACK_NAME = 1;
    static final int COL_ALBUM_NAME = 2;
    static final int COL_ALBUM_IMAGE = 3;

     static boolean mTwoPane;
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    static ArrayList<Artist> savedArtist;

    SpotifyApi api=new SpotifyApi();
    SpotifyService spotify = api.getService();
    ArrayList<Artist> artistList=new ArrayList<Artist>();
    CustomList dataAdabter;


    searchArtist searchTask;
    ListView resultList;
    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        final View rootview=inflater.inflate(R.layout.fragment_main, container, false);

        resultList= (ListView) rootview.findViewById(R.id.resultList);
        resultList.setAdapter(dataAdabter);
        final EditText searchText=((EditText)rootview.findViewById(R.id.searchBar));
            resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    Artist me = (Artist) dataAdabter.getItem(position);
                    ArtistTopTenFragment.artistID=me.id;

                    if (mTwoPane) {
                        // In two-pane mode, show the detail view in this activity by
                        // adding or replacing the detail fragment using a
                        // fragment transaction.
                        Bundle arguments = new Bundle();
                        arguments.putString(ArtistTopTenFragment.artistID, me.id);
                        ArtistTopTenFragment fragment = new ArtistTopTenFragment();
                        fragment.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.artist_detail_container, fragment)
                                .commit();

                    } else {
                        // In single-pane mode, simply start the detail activity
                        // for the selected item ID.
                        Intent detailIntent = new Intent(getActivity(), ArtistTopTen.class);
                        detailIntent.putExtra(ArtistTopTenFragment.artistID, id);
                        startActivity(detailIntent);
                    }


            }catch (Exception e){
                    Log.v("Starting new intent ",e.toString());
                }

            }
        });
    searchText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            searchTask = new searchArtist();

            searchTask.execute(searchText.getText().toString());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });
        setRetainInstance(true);


        return rootview;

    }

    class searchArtist extends AsyncTask<String, Void, List<Artist>>{
        String search;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Artist> doInBackground(String... params) {
            try {
                search = params[0];
                ArtistsPager artistsPager = spotify.searchArtists(search);
                List<Artist> artists = new ArrayList<>();
                for (Artist artist : artistsPager.artists.items) {
                    Artist localartist = artist;
                    artists.add(localartist);
                }

                return artists;
            }catch (Exception e){
                Log.v("Execute issue",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            super.onPostExecute(artists);
            if(artists.isEmpty()){
                Toast.makeText(getActivity(),"No artist found",Toast.LENGTH_LONG).show();
            }

try {
    savedArtist=(ArrayList<Artist>) artists;
    dataAdabter = new CustomList(getActivity(), (savedArtist));
    resultList.setAdapter(dataAdabter);
    Log.v("1", "0");
}catch (Exception e){
    Log.v("array not yet ",e.toString());
}
        }
    }
    void addArtist(String artistID, String artistName, String artistImage) {
        long locationId;


        Cursor artistCursor = getActivity().getContentResolver().query(
                ArtistEntry.CONTENT_URI,
                new String[]{ArtistEntry._ID},
                ArtistEntry.COLUMN_ARTIST_ID + " = ?",
                new String[]{artistID},
                null);

        if (artistCursor.moveToFirst()) {
            int locationIdIndex = artistCursor.getColumnIndex(ArtistEntry._ID);
            locationId = artistCursor.getLong(locationIdIndex);
        } else {

            ContentValues artistValues = new ContentValues();


            artistValues.put(ArtistEntry.COLUMN_ARTIST_ID,artistID);
            artistValues.put(ArtistEntry.COLUMN_ARTIST_NAME, artistName);
            artistValues.put(ArtistEntry.COLUMN_ARTIST_IMAGE, artistImage);


            Uri insertedUri = getActivity().getContentResolver().insert(
                    ArtistEntry.CONTENT_URI,
                    artistValues
            );

             }

        artistCursor.close();

    }

}
class CustomList extends ArrayAdapter{

    private final Activity context;
    private ArrayList<Artist> mylist=new ArrayList<Artist>();

    public CustomList(Activity context1,ArrayList<Artist> artist1) {
        super(context1, R.layout.list_item_view, artist1);
        this.context = context1;
        this.mylist = artist1;
        Log.v("1","1");

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
        View rowView=inflater.inflate(R.layout.list_item_view, null, true);
        TextView text=(TextView)rowView.findViewById(R.id.artistTextView);

        ImageView image=(ImageView)rowView.findViewById(R.id.artistImageView);
        Artist local= mylist.get(position);
        Log.v("First one", local.name);
        text.setText(local.name);
try {
    Picasso.with(context).load(local.images.get(0).url).into(image);
}catch (Exception
        e){
    Log.v("The Exception",e.toString());
        }

        return rowView;
    }
}