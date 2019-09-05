package com.example.alifd.listfilmrecycler.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.example.alifd.listfilmrecycler";
    public static final String SCHEME = "content";
    static String TABLE_FAV = "fav";
    static String TABLE_FAV_SHOW = "fav_show";

    public static final class FavColumns implements BaseColumns{
        public static final String TABLE_NAME = "fav_movie";
        public static String ID = "id";
        public static String VOTE_AVERAGE = "vote_average";
        public static String TITLE = "title";
        public static String POSTER_PATH = "poster_path";
        public static String OVERVIEW = "overview";
        public static String RELEASE_DATE = "release_date";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static final class ShowFavColumns implements BaseColumns{
        //public static final String TABLE_NAME = "fav_show";
        static String ID = "id";
        static String NAME = "name";
        static String VOTE_AVERAGE = "vote_average";
        static String FIRST_AIR_DATE = "first_air_date";
        static String POSTER_PATH = "poster_path";
        static String OVERVIEW = "overview";
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
    public static double getColumnDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }
}
