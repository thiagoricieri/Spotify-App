package com.example.amr.spotifystreamer;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.amr.spotifystreamer.data.AppContract.*;
import com.example.amr.spotifystreamer.data.AppDbHelper;
import com.example.amr.spotifystreamer.data.AppProvider;
import com.squareup.picasso.Picasso;

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

    SpotifyApi api=new SpotifyApi();
    SpotifyService spotify = api.getService();
    ArrayList<Artist> artistList=new ArrayList<Artist>();
    CustomList dataAdabter;


    searchArtist searchTask;
    ListView resultList;
    public MainActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootview=inflater.inflate(R.layout.fragment_main, container, false);
        resultList= (ListView) rootview.findViewById(R.id.resultList);
        resultList.setAdapter(dataAdabter);
        final EditText searchText=((EditText)rootview.findViewById(R.id.searchBar));
            resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    Artist me = (Artist) dataAdabter.getItem(position);
                                   Intent intent=new Intent(getActivity(),ArtistTopTen.class);
                intent.putExtra("ar",me.id );
                Log.v("Artist",me.id);
                startActivity(intent);

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
            searchTask=new searchArtist();

            searchTask.execute(searchText.getText().toString());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });


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
            Log.v("Artists List", artists.toString());
            Log.v("Artist", artists.get(0).name);
            Vector<ContentValues> cv = new Vector<ContentValues>(artists.size());
            for(Artist local:artists) {

                ContentValues artistValues = new ContentValues();

                artistValues.put(ArtistEntry.COLUMN_ARTIST_ID, local.id);
                artistValues.put(ArtistEntry.COLUMN_ARTIST_NAME, local.name);
//                artistValues.put(ArtistEntry.COLUMN_ARTIST_IMAGE, local.images.get(0).url);
                cv.add(artistValues);
                  }
            int inserted =0 ;
            if(cv.size()>0){
                ContentValues[] cvArray= new ContentValues[cv.size()];
                cv.toArray();
                inserted=getActivity().getContentResolver().bulkInsert(ArtistEntry.CONTENT_URI,cvArray);

            }
            dataAdabter= new CustomList(getActivity(),(ArrayList<Artist>)artists);
            resultList.setAdapter(dataAdabter);
            Log.v("1", "0");

        }
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