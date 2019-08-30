package com.example.alifd.listfilmrecycler.contentprovider;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String TABLE_FAV = "fav";
    public static final String CONTENT_AUTHORITY = "com.example.alifd.listfilmrecycler";

    public static final class FavColumns implements BaseColumns {
        public static final String _ID = "id";
        public static final String VOTE_AVERAGE  = "vote_average";
        public static final String TITLE = "title";
        public static final String POSTER_PATH = "poster_path";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "Release_Date";
    }

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_FAV)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }
}
