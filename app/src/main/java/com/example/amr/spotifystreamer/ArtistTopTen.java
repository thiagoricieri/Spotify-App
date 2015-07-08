package com.example.amr.spotifystreamer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class ArtistTopTen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_top_ten);}catch (Exception e){
            Log.v("the error",e.toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist_top_ten, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id==R.id.nowplaying){
            Intent local= new Intent(getApplicationContext(),MediaPlayBackService.class);
            getApplicationContext().startService(local);
        }

        return super.onOptionsItemSelected(item);
    }
}
