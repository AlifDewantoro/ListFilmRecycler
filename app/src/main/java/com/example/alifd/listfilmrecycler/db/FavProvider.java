package com.example.alifd.listfilmrecycler.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.alifd.listfilmrecycler.MainActivity;
import com.example.alifd.listfilmrecycler.adapter.FilmAdapter;

import timber.log.Timber;

import static com.example.alifd.listfilmrecycler.db.DatabaseContract.AUTHORITY;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.CONTENT_URI;
import static com.example.alifd.listfilmrecycler.db.DatabaseContract.FavColumns.TABLE_NAME;

public class FavProvider extends ContentProvider {
    private static final int FAV = 1;
    private static final int FAV_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavHelper favHelper;

    static {
        // content://com.example.alifd.listfilmrecycler/fav_movie
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV);
        // content://com.example.alifd.listfilmrecycler/fav_movie/id
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", FAV_ID);
    }

    @Override
    public boolean onCreate() {
        favHelper = FavHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        favHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FAV:
                cursor = favHelper.queryProvider();
                break;
            case FAV_ID:
                cursor = favHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        favHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case FAV:
                added = favHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, new FilmAdapter.DataObserver(new Handler()));
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        favHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case FAV_ID:
                deleted = favHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, new FilmAdapter.DataObserver(new Handler()));
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
