package com.example.alifd.listfilmrecycler.helper;

import android.database.Cursor;

import com.example.alifd.listfilmrecycler.model.FilmModel;

import java.util.ArrayList;

import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.ID;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.OVERVIEW;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.POSTER_PATH;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.RELEASE_DATE;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.TITLE;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.VOTE_AVERAGE;

public class MappingHelper {
    public static ArrayList<FilmModel> mapCursorToArrayList(Cursor notesCursor) {
        ArrayList<FilmModel> filmModels = new ArrayList<>();
        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(ID));
            double vote = notesCursor.getDouble(notesCursor.getColumnIndexOrThrow(VOTE_AVERAGE));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE));
            String poster = notesCursor.getString(notesCursor.getColumnIndexOrThrow(POSTER_PATH));
            String overview = notesCursor.getString(notesCursor.getColumnIndexOrThrow(OVERVIEW));
            String release = notesCursor.getString(notesCursor.getColumnIndexOrThrow(RELEASE_DATE));
            filmModels.add(new FilmModel(id, vote, title, poster, overview, release));
        }
        return filmModels;
    }
}
