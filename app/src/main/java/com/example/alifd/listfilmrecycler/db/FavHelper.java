package com.example.alifd.listfilmrecycler.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.alifd.listfilmrecycler.model.FilmModel;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.ID;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.OVERVIEW;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.POSTER_PATH;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.RELEASE_DATE;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.TITLE;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.VOTE_AVERAGE;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.TABLE_FAV;

public class FavHelper {
    private static final String DATABASE_TABLE = TABLE_FAV;
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

    public ArrayList<FilmModel> getAllNotes() {
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
                filmModel.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(TITLE)));
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

    public long insertNote(FilmModel filmModel) {
        ContentValues args = new ContentValues();
        args.put(ID, filmModel.getId());
        args.put(VOTE_AVERAGE, filmModel.getVoteAverage());
        args.put(TITLE, filmModel.getTitle());
        args.put(POSTER_PATH, filmModel.getPosterPath());
        args.put(OVERVIEW, filmModel.getOverview());
        args.put(RELEASE_DATE, filmModel.getReleaseDate());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int updateNote(FilmModel filmModel) {
        ContentValues args = new ContentValues();
        args.put(ID, filmModel.getId());
        args.put(VOTE_AVERAGE, filmModel.getVoteAverage());
        args.put(TITLE, filmModel.getTitle());
        args.put(POSTER_PATH, filmModel.getPosterPath());
        args.put(OVERVIEW, filmModel.getOverview());
        args.put(RELEASE_DATE, filmModel.getReleaseDate());
        return database.update(DATABASE_TABLE, args, ID + "= '" + filmModel.getId() + "'", null);
    }

    public int deleteNote(int id) {
        return database.delete(TABLE_FAV, ID + " = '" + id + "'", null);
    }
}
