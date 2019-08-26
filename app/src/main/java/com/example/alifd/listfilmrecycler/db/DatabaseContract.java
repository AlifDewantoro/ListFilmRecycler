package com.example.alifd.listfilmrecycler.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.example.alifd.listfilmrecycler";
    public static final String SCHEME = "content";
    static String TABLE_FAV = "fav";

    public static final class FavColumns implements BaseColumns{
        public static final String TABLE_NAME = "fav_movie";
        static String ID = "id";
        static String VOTE_AVERAGE = "vote_average";
        static String TITLE = "title";
        static String POSTER_PATH = "poster_path";
        static String OVERVIEW = "overview";
        static String RELEASE_DATE = "release_date";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
