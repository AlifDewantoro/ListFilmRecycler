package com.example.alifd.listfilmrecycler.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.alifd.listfilmrecycler.model.FilmModel;
import com.example.alifd.listfilmrecycler.model.TvShowModel;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.ID;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.OVERVIEW;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.POSTER_PATH;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.RELEASE_DATE;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.TITLE;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.VOTE_AVERAGE;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.TABLE_FAV;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.TABLE_FAV_SHOW;

public class FavHelper {
    private static final String DATABASE_TABLE = TABLE_FAV;
    private static final String DATABASE_TABLE_SHOW = TABLE_FAV_SHOW;
    private static DatabaseHelper dataBaseHelper;
    private static FavHelper INSTANCE;
    private static SQLiteDatabase database;

    private FavHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static FavHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavHelper(context);
                }
            }
        }
        return INSTANCE;
    }
    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }
    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public ArrayList<FilmModel> getAllFilmFavs() {
        ArrayList<FilmModel> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        FilmModel filmModel;
        if (cursor.getCount() > 0) {
            do {
                filmModel = new FilmModel();
                filmModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                filmModel.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                filmModel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                filmModel.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                filmModel.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                filmModel.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                arrayList.add(filmModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public FilmModel getFilmFavorite(int id){
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();

        FilmModel filmModel;
        filmModel = new FilmModel();
        if (cursor.getCount() > 0) {
            do {
                if(cursor.getInt(cursor.getColumnIndexOrThrow(ID))==id) {
                    filmModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                    filmModel.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                    filmModel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                    filmModel.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                    filmModel.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                    filmModel.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                }
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();

        return filmModel;
    }

    public long insertFilm(FilmModel filmModel) {
        ContentValues args = new ContentValues();
        args.put(ID, filmModel.getId());
        args.put(VOTE_AVERAGE, filmModel.getVoteAverage());
        args.put(TITLE, filmModel.getTitle());
        args.put(POSTER_PATH, filmModel.getPosterPath());
        args.put(OVERVIEW, filmModel.getOverview());
        args.put(RELEASE_DATE, filmModel.getReleaseDate());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteFilm(int id) {
        return database.delete(TABLE_FAV, ID + " = '" + id + "'", null);
    }



    public ArrayList<TvShowModel> getAllShowFavs() {
        ArrayList<TvShowModel> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE_SHOW, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        TvShowModel tvShowModel;
        if (cursor.getCount() > 0) {
            do {
                tvShowModel = new TvShowModel();
                tvShowModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ShowFavColumns.ID)));
                tvShowModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ShowFavColumns.NAME)));
                tvShowModel.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.ShowFavColumns.VOTE_AVERAGE)));
                tvShowModel.setFirstAirDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ShowFavColumns.FIRST_AIR_DATE)));
                tvShowModel.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ShowFavColumns.POSTER_PATH)));
                tvShowModel.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ShowFavColumns.OVERVIEW)));
                arrayList.add(tvShowModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public TvShowModel getShowFavorite(int id){
        Cursor cursor = database.query(DATABASE_TABLE_SHOW, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();

        TvShowModel tvShowModel;
        tvShowModel = new TvShowModel();
        if (cursor.getCount() > 0) {
            do {
                if(cursor.getInt(cursor.getColumnIndexOrThrow(ID))==id) {
                    tvShowModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ShowFavColumns.ID)));
                    tvShowModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ShowFavColumns.NAME)));
                    tvShowModel.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.ShowFavColumns.VOTE_AVERAGE)));
                    tvShowModel.setFirstAirDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ShowFavColumns.FIRST_AIR_DATE)));
                    tvShowModel.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ShowFavColumns.POSTER_PATH)));
                    tvShowModel.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ShowFavColumns.OVERVIEW)));
                }
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();

        return tvShowModel;
    }

    public long insertShow(TvShowModel tvShowModel) {
        ContentValues args = new ContentValues();
        args.put(DatabaseContract.ShowFavColumns.ID, tvShowModel.getId());
        args.put(DatabaseContract.ShowFavColumns.NAME, tvShowModel.getName());
        args.put(DatabaseContract.ShowFavColumns.VOTE_AVERAGE, tvShowModel.getVoteAverage());
        args.put(DatabaseContract.ShowFavColumns.FIRST_AIR_DATE, tvShowModel.getFirstAirDate());
        args.put(DatabaseContract.ShowFavColumns.POSTER_PATH, tvShowModel.getPosterPath());
        args.put(DatabaseContract.ShowFavColumns.OVERVIEW, tvShowModel.getOverview());
        return database.insert(DATABASE_TABLE_SHOW, null, args);
    }

    public int deleteShow(int id) {
        return database.delete(TABLE_FAV_SHOW, ID + " = '" + id + "'", null);
    }
}
