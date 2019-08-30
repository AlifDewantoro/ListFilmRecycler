package com.example.alifd.listfilmrecycler.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.alifd.listfilmrecycler.model.FilmModel;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import timber.log.Timber;

public class FavFilmProvider extends ContentProvider {
    private static final int TASKS = 100;
    private static final int TASKS_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        // content://com.example.rgher.realmtodo/tasks
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_FAV,
                TASKS);

        // content://com.example.rgher.realmtodo/tasks/id
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.TABLE_FAV + "/#",
                TASKS_WITH_ID);
    }
    @Override
    public boolean onCreate() {
        Realm.init(getContext());
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("fav_catalogue.db")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(configuration);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @NonNull String[] projection, @NonNull String selection, @NonNull String[] selectionArgs,
                        @NonNull String sortOrder) {

        int match = sUriMatcher.match(uri);

        Realm realm = Realm.getDefaultInstance();
        MatrixCursor cursorFav =new MatrixCursor(new String[] {DatabaseContract.FavColumns._ID,
                DatabaseContract.FavColumns.VOTE_AVERAGE, DatabaseContract.FavColumns.TITLE, DatabaseContract.FavColumns.POSTER_PATH,
                DatabaseContract.FavColumns.OVERVIEW, DatabaseContract.FavColumns.RELEASE_DATE});
        try{

            switch (match) {

            case TASKS:
                RealmResults<FilmModel> realmResults = realm.where(FilmModel.class).findAll();
                for (FilmModel fav : realmResults) {
                    Object[] rowData = new Object[]{fav.getId(), fav.getVoteAverage(), fav.getTitle()
                            , fav.getPosterPath(), fav.getOverview(), fav.getReleaseDate()};
                    cursorFav.addRow(rowData);
                    Timber.e(fav.toString());
                }
                break;

            case TASKS_WITH_ID:
                Integer id = Integer.parseInt(uri.getPathSegments().get(1));
                FilmModel fav = realm.where(FilmModel.class).equalTo("id", id).findFirst();
                cursorFav.addRow(new Object[]{fav.getId(), fav.getVoteAverage(), fav.getTitle()
                        , fav.getPosterPath(), fav.getOverview(), fav.getReleaseDate()});
                Timber.e(fav.toString());
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        }finally {
            realm.close();
        }

        return cursorFav;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @NonNull final ContentValues contentValues) {

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        //Get Realm Instance
        Realm realm = Realm.getDefaultInstance();
        try {
            switch (match) {
                case TASKS:
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            FilmModel newFilm = realm.createObject(FilmModel.class, contentValues.get(DatabaseContract.FavColumns._ID));
                            newFilm.setVoteAverage((Double) contentValues.get(DatabaseContract.FavColumns.VOTE_AVERAGE));
                            newFilm.setTitle(contentValues.get(DatabaseContract.FavColumns.TITLE).toString());
                            newFilm.setReleaseDate(contentValues.get(DatabaseContract.FavColumns.RELEASE_DATE).toString());
                            newFilm.setPosterPath( contentValues.get(DatabaseContract.FavColumns.POSTER_PATH).toString());
                            newFilm.setOverview( contentValues.get(DatabaseContract.FavColumns.OVERVIEW).toString());
                        }
                    });
                    returnUri = ContentUris.withAppendedId(DatabaseContract.CONTENT_URI, '1');
                    break;

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            getContext().getContentResolver().notifyChange(uri, null);
        }finally {
            realm.close();
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @NonNull String selection, @NonNull String[] selectionArgs) {
        int count = 0;
        Realm realm = Realm.getDefaultInstance();
        try {
            switch (sUriMatcher.match(uri)) {
                case TASKS:
                    selection = (selection == null) ? "1" : selection;
                    RealmResults<FilmModel> tasksRealmResults = realm.where(FilmModel.class).equalTo(selection, Integer.parseInt(selectionArgs[0])).findAll();
                    realm.beginTransaction();
                    tasksRealmResults.deleteAllFromRealm();
                    count++;
                    realm.commitTransaction();
                    break;
                case TASKS_WITH_ID:
                    Integer id = Integer.parseInt(String.valueOf(ContentUris.parseId(uri)));
                    FilmModel myTask = realm.where(FilmModel.class).equalTo("id", id).findFirst();
                    realm.beginTransaction();
                    myTask.deleteFromRealm();
                    count++;
                    realm.commitTransaction();
                    break;
                default:
                    throw new IllegalArgumentException("Illegal delete URI");
            }
        } finally {
            realm.close();
        }
        if (count > 0) {
            //Notify observers of the change
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @NonNull ContentValues values, @NonNull String selection,
                      @NonNull String[] selectionArgs) {
        return 0;
    }
}
