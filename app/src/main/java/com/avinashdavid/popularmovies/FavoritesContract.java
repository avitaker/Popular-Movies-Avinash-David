package com.avinashdavid.popularmovies;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by avinashdavid on 5/29/16.
 */
public final class FavoritesContract {
    public FavoritesContract() {
    }

    public static abstract class FavoritesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_NAME_ID = "movieId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_NAME_IMAGE_URL = "imgUrl";
        public static final String COLUMN_NAME_AVE_RATING = "aveRating";
        public static final String COLUMN_NAME_TRANSLATED_GENRES = "translatedGenres";
    }

    public static final String[] favoritesProjection = new String[]{
            FavoritesEntry.COLUMN_NAME_TITLE,
            FavoritesEntry._ID,
            FavoritesEntry.COLUMN_NAME_IMAGE_URL,
            FavoritesEntry.COLUMN_NAME_RELEASE_DATE,
            FavoritesEntry.COLUMN_NAME_AVE_RATING,
            FavoritesEntry.COLUMN_NAME_TRANSLATED_GENRES,
            FavoritesEntry.COLUMN_NAME_DESCRIPTION
    };

    public static final int COL_TITLE = 0;
    public static final int COL_ID = 1;
    public static final int COL_IMGURL = 2;
    public static final int COL_RELEASEDATE = 3;
    public static final int COL_AVERATING = 4;
    public static final int COL_GENRES = 5;
    public static final int COL_DESC = 6;

    public final static ArrayList<MovieResult> convertCursorToMovieResults(Cursor cursor){
        ArrayList<MovieResult> returnArray = new ArrayList<MovieResult>();
        if (cursor.isAfterLast() || cursor.isBeforeFirst()){
            Log.d("testConv","convertcursortomovieresults aborted");
            return null;
        }
        else {
            cursor.moveToFirst();
            do {
                MovieResult movieResult = new MovieResult(cursor.getString(COL_TITLE));
                movieResult.setMovieId(cursor.getLong(COL_ID));
                movieResult.setImageUrl(cursor.getString(COL_IMGURL));
                movieResult.setReleaseDate(cursor.getString(COL_RELEASEDATE));
                movieResult.setAveRating(cursor.getDouble(COL_AVERATING));
                movieResult.setTranslatedGenres(cursor.getString(COL_GENRES));
                movieResult.setDescription(cursor.getString(COL_DESC));
                returnArray.add(movieResult);
            } while (cursor.moveToNext());
//            }
//            finally {
//                cursor.close();
//            }
            return returnArray;
        }
    }
}
