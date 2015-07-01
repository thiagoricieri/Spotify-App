/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.amr.spotifystreamer.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.amr.spotifystreamer.data.AppContract.ArtistEntry;
import com.example.amr.spotifystreamer.data.AppContract.TrackEntry;
/**
 * Manages a local database for weather data.
 */
public class AppDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "weather.db";

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_ARTIST_TABLE = "CREATE TABLE " + ArtistEntry.TABLE_NAME + " (" +
                ArtistEntry._ID + " INTEGER PRIMARY KEY," +
                ArtistEntry.COLUMN_ARTIST_ID + " TEXT UNIQUE NOT NULL, " +
                ArtistEntry.COLUMN_ARTIST_NAME + " TEXT NOT NULL, " +
                ArtistEntry.COLUMN_ARTIST_IMAGE + " TEXT NOT NULL " +

                " );";

        final String SQL_CREATE_TRACK_TABLE = "CREATE TABLE " + TrackEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                TrackEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                TrackEntry.COLUMN_ALBUM_IMAGE + " TEXT NOT NULL, " +
                TrackEntry.COLUMN_ALBUM_NAME + " TEXT NOT NULL, " +
                TrackEntry.COLUMN_TRACK_NAME + " TEXT NOT NULL, " +
                TrackEntry.COLUMN_TRACK_ID + " TEXT NOT NULL" +

                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_ARTIST_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRACK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ArtistEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrackEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
