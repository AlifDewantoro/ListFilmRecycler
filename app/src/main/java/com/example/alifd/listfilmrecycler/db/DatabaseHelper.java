package com.example.alifd.listfilmrecycler.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "dbfavmov";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_TABLE_FAV = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_FAV,
            DatabaseContract.FavColumns._ID,
            DatabaseContract.FavColumns.ID,
            DatabaseContract.FavColumns.VOTE_AVERAGE,
            DatabaseContract.FavColumns.TITLE,
            DatabaseContract.FavColumns.POSTER_PATH,
            DatabaseContract.FavColumns.OVERVIEW,
            DatabaseContract.FavColumns.RELEASE_DATE
    );

    private static final String SQL_CREATE_TABLE_FAV_SHOW = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_FAV_SHOW,
            DatabaseContract.ShowFavColumns._ID,
            DatabaseContract.ShowFavColumns.ID,
            DatabaseContract.ShowFavColumns.NAME,
            DatabaseContract.ShowFavColumns.VOTE_AVERAGE,
            DatabaseContract.ShowFavColumns.FIRST_AIR_DATE,
            DatabaseContract.ShowFavColumns.POSTER_PATH,
            DatabaseContract.ShowFavColumns.OVERVIEW
    );

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAV);
        db.execSQL(SQL_CREATE_TABLE_FAV_SHOW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_FAV);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_FAV_SHOW);
        onCreate(db);
    }
}
