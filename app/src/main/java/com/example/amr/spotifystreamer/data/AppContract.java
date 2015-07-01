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
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database.
 */
public class AppContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.amr.spotifystreamer";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_ARTIST = "artist";
    public static final String PATH_TRACKS = "tracks";



    /* Inner class that defines the table contents of the location table */
    public static final class ArtistEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTIST).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTIST;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_ARTIST;

        // Table name
        public static final String TABLE_NAME = "artists";

        // The location setting string is what will be sent to openweathermap
        // as the location query.
        public static final String COLUMN_ARTIST_ID = "artist_id";

        // Human readable location string, provided by the API.  Because for styling,
        // "Mountain View" is more recognizable than 94043.
        public static final String COLUMN_ARTIST_NAME = "artist_name";

        // In order to uniquely pinpoint the location on the map when we launch the
        // map intent, we store the latitude and longitude as returned by openweathermap.
        public static final String COLUMN_ARTIST_IMAGE = "artist_image";

        public static Uri buildArtistUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the weather table */
    public static final class TrackEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRACKS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRACKS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRACKS;

        public static final String TABLE_NAME = "tracks";

        // Column with the foreign key into the location table.
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_TRACK_ID = "track_id";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_TRACK_NAME = "track_name";

        public static final String COLUMN_ALBUM_NAME = "album_name";
        public static final String COLUMN_ALBUM_IMAGE = "album_image";


        public static Uri buildTrackUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
//
//        /*
//            Student: This is the buildArtist function you filled in.
////         */
//        public static Uri buildArtist(String artistID) {
//            return CONTENT_URI.buildUpon().appendPath(artistID).build();
//        }
//
//
//
        public static String getArtistIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
