package com.example.amr.spotifystreamer;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Amr on 01/07/2015.
 */
class ArtistAdapter extends CursorAdapter {
    public ArtistAdapter(Context context, Cursor c) {
        super(context, c);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String artistName=cursor.getString(MainActivityFragment.COL_ARTIST_NAME);
        TextView artisttx=(TextView)view.findViewById(R.id.artistTextView);
        artisttx.setText(artistName);
        String artistImage=cursor.getString(MainActivityFragment.COL_ARTIST_IMAGE);
    }
}
class TrackAdapter extends CursorAdapter {
    public TrackAdapter(Context context, Cursor c) {
        super(context, c);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String trackName=cursor.getString(MainActivityFragment.COL_TRACK_NAME);
        String albumName=cursor.getString(MainActivityFragment.COL_ALBUM_NAME);
        TextView tracktx=(TextView)view.findViewById(R.id.songTextView);
        tracktx.setText(trackName);
        TextView albumtx=(TextView)view.findViewById(R.id.albumTextView);
        tracktx.setText(albumName);

        String albumImage=cursor.getString(MainActivityFragment.COL_ALBUM_IMAGE);
    }
}