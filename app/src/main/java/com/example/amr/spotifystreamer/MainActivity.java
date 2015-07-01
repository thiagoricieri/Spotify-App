package com.example.amr.spotifystreamer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
//    Display display = getWindowManager().getDefaultDisplay();
//    int width = display.getWidth();
//    int height = display.getHeight();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;
//
//        if(screenWidth > screenHeight)
//        {
////Landscape
////set your landscape drawable
//            ArtistTopTenFragment topten=new ArtistTopTenFragment();
//            MainActivityFragment artist=new MainActivityFragment();
//            FragmentManager fm=getFragmentManager();
//            FragmentTransaction ft=fm.beginTransaction();
//            ft.add(R.id.artistfragment,artist,"Artist Search Fragment");
//            ft.add(R.id.toptenfragment,topten,"Artist Search Fragment");
//            setContentView(R.layout.activity_main_tablet);
////        }
//        else
//        {
//portrait
//set your portrait drawable
            setContentView(R.layout.activity_main);
//        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent local= new Intent(getApplicationContext(),MediaPlayer.class);
            startActivity(local);
        }

        return super.onOptionsItemSelected(item);
    }
}
