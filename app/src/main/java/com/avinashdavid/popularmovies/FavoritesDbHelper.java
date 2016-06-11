package com.avinashdavid.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by avinashdavid on 5/29/16.
 */
public class FavoritesDbHelper extends SQLiteOpenHelper {
    private static FavoritesDbHelper mInstance = null;
    private Context mCtx;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FavoriteMovies.db";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FavoritesContract.FavoritesEntry.TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + FavoritesContract.FavoritesEntry.TABLE_NAME +
            " (" + FavoritesContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY," +
            FavoritesContract.FavoritesEntry.COLUMN_NAME_TITLE + " TEXT," +
            FavoritesContract.FavoritesEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
            FavoritesContract.FavoritesEntry.COLUMN_NAME_RELEASE_DATE + " TEXT," +
            FavoritesContract.FavoritesEntry.COLUMN_NAME_IMAGE_URL + " TEXT," +
            FavoritesContract.FavoritesEntry.COLUMN_NAME_AVE_RATING + " DOUBLE(1,2)," +
            FavoritesContract.FavoritesEntry.COLUMN_NAME_TRANSLATED_GENRES + " TEXT)";

//    public FavoritesDbHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }

    public static FavoritesDbHelper getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new FavoritesDbHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    private FavoritesDbHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.mCtx = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }
}
