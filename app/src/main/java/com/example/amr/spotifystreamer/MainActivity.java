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

static  int screenWidth;
   static int screenHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
      screenWidth = displaymetrics.widthPixels;
        screenHeight = displaymetrics.heightPixels;


        if(screenWidth > screenHeight)
        {
            ArtistTopTenFragment topten=new ArtistTopTenFragment();
            MainActivityFragment artist=new MainActivityFragment();
            android.support.v4.app.FragmentManager fm=getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ft=fm.beginTransaction();
            ft.add(R.id.artistfragment,artist,"Artist Search Fragment");
            ft.add(R.id.toptenfragment,topten,"Top Ten Fragment");
            setContentView(R.layout.activity_main_tablet);
        }
        else
        {
            setContentView(R.layout.activity_main);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
