package com.example.amr.spotifystreamer;import android.app.Activity;import android.app.FragmentManager;import android.app.FragmentTransaction;import android.app.PendingIntent;import android.content.Intent;import android.graphics.drawable.GradientDrawable;import android.os.PersistableBundle;import android.support.v7.app.ActionBarActivity;import android.os.Bundle;import android.util.DisplayMetrics;import android.view.Display;import android.view.Menu;import android.view.MenuItem;import java.util.ArrayList;import kaaes.spotify.webapi.android.models.Artist;public class MainActivity extends ActionBarActivity   {    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        if(getResources().getConfiguration().orientation==getResources().getConfiguration().ORIENTATION_LANDSCAPE){        setContentView(R.layout.activity_main_tablet);        MainActivityFragment.mTwoPane=true;}else if(getResources().getConfiguration().orientation==getResources().getConfiguration().ORIENTATION_PORTRAIT){            setContentView(R.layout.activity_main);            MainActivityFragment.mTwoPane=false;        }    }    @Override    public boolean onCreateOptionsMenu(Menu menu) {         getMenuInflater().inflate(R.menu.menu_main, menu);        return true;    }    @Override    public boolean onOptionsItemSelected(MenuItem item) {        int id = item.getItemId();        if (id == R.id.action_settings) {            return true;        }        if(id==R.id.nowplaying){ {            Intent intent = new Intent(getApplication(), MediaPlayer.class);            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);startActivity(intent);        }        }        return super.onOptionsItemSelected(item);    }}