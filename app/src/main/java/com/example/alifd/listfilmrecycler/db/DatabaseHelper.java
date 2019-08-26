package com.example.alifd.listfilmrecycler.db;

public class DatabaseHelper {
    public static String DATABASE_NAME = "dbfavmov";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_FAV,
            DatabaseContract.FavColumns.ID,
            DatabaseContract.FavColumns.VOTE_AVERAGE,
            DatabaseContract.FavColumns.TITLE,
            DatabaseContract.FavColumns.POSTER_PATH,
            DatabaseContract.FavColumns.OVERVIEW,
            DatabaseContract.FavColumns.RELEASE_DATE
    );
}
