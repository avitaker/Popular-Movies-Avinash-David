package com.avinashdavid.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    public static final Integer EXTRA_REVIEWS = 0;
    public static final Integer EXTRA_TRAILERS = 1;

    public MovieResult obtainResult;
    private FavoritesDbHelper mDbHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private boolean isFavorite = false;
    private ImageView movie_poster_detail;
    private TextView movie_title;
    private TextView movie_releaseDate;
    private TextView movie_rating;
    private TextView movie_description;
    private Button button_favorite;
    private String movieId;

//    ConfigurationBuilder cb = new ConfigurationBuilder()
//            .setDebugEnabled(BuildConfig.DEBUG)
//            .setApplicationOnlyAuthEnabled(true)
//            .setOAuthConsumerKey(BuildConfig.MOVIEDB_API_KEY);


    private Intent mIntent;

    private View.OnClickListener isFavoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("testFav","isfavoritelistener");
            deleteFavorite(obtainResult);
            isFavorite = false;
        }
    };

    private View.OnClickListener isNotFavoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("testFav","isnotfavoritelistener");
            insertFavorite(obtainResult);
            isFavorite = true;
        }
    };

//    public String formatGenres(String[] a){
//        StringBuilder builder = new StringBuilder();
//        if (a.length > 0) {
//            for (int i=0; i< a.length-1; i++){
//                builder.append(a[i]).append(", ");
//            }
//            builder.append(a[a.length-1]);
//        }
//        return builder.toString();
//    }

    public static DetailActivityFragment newInstance(Parcelable movie) {

        DetailActivityFragment f = new DetailActivityFragment();
        Bundle b = new Bundle();
        b.putParcelable("movieId", movie);

        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState == null){
            mIntent=getActivity().getIntent();
        }
        else {
            mIntent = null;
            obtainResult = savedInstanceState.getParcelable(Intent.EXTRA_INTENT);
//            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }
    }

    public void updateUI(){
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500/"+obtainResult.imageUrl).into(movie_poster_detail);
//        obtainResult.translateGenreList();
        movie_title.setText(obtainResult.title);
        movie_releaseDate.setText(obtainResult.releaseDate);
        Log.d("testId",Long.toString(obtainResult.getMovieId()));
        movie_rating.setText(getString(R.string.format_rating,Double.toString(obtainResult.aveRating)));
        movie_description.setText(getString(R.string.format_description_genre, obtainResult.translatedGenres, obtainResult.description));
    }

    @Override
    public void onStart() {
        super.onStart();

//        obtainResult = getArguments().getParcelable("movieId");
//        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        if (savedInstanceState == null){
//            mIntent=getActivity().getIntent();
//            obtainResult = mIntent.getParcelableExtra(Intent.EXTRA_RETURN_RESULT);
            obtainResult = getArguments().getParcelable("movieId");
        }
        else {
            mIntent = null;
            obtainResult = savedInstanceState.getParcelable(Intent.EXTRA_INTENT);
//            mContent = getActivity().getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }
//        if (mIntent!=null){
//            obtainResult = mIntent.getParcelableExtra(Intent.EXTRA_RETURN_RESULT);
//        }
//        obtainResult = getArguments().getParcelable("movieId");
        mDbHelper = FavoritesDbHelper.getInstance(getActivity().getApplicationContext());
        mSQLiteDatabase = mDbHelper.getWritableDatabase();
            Cursor cursor = mSQLiteDatabase.query(FavoritesContract.FavoritesEntry.TABLE_NAME, null,
                    FavoritesContract.FavoritesEntry._ID + " = ?",
                    new String[]{Long.toString(obtainResult.getMovieId())},
                    null, null, null);

            if (!cursor.isAfterLast() || !cursor.isBeforeFirst()) {
                isFavorite = true;
            }

            cursor.close();

        movie_poster_detail = (ImageView) rootView.findViewById(R.id.movie_poster_detail);
        movie_title = (TextView) rootView.findViewById(R.id.movie_title);
        movie_releaseDate = (TextView) rootView.findViewById(R.id.movie_releaseDate);
//        TextView movie_genreList = (TextView) rootView.findViewById(R.id.movie_genreList);
        movie_rating = (TextView) rootView.findViewById(R.id.movie_rating);
        movie_description = (TextView) rootView.findViewById(R.id.movie_description);
        button_favorite= (Button) rootView.findViewById(R.id.buttonFavorite);

        movieId = Long.toString(obtainResult.getMovieId());
        updateUI();
        Log.d("testFav",String.valueOf(isFavorite));

//        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w342/"+obtainResult.imageUrl).into(movie_poster_detail);
////        obtainResult.translateGenreList();
//        movie_title.setText(titleIntro + obtainResult.title);
//        movie_releaseDate.setText(dateIntro + obtainResult.releaseDate);
//        Log.d("testId",Long.toString(obtainResult.getMovieId()));
//        View.OnClickListener isFavoriteListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteFavorite(obtainResult);
//                isFavorite = false;
//            }
//        };
//        View.OnClickListener isNotFavoriteListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                insertFavorite(obtainResult);
//                isFavorite = true;
//            }
//        };
        if (isFavorite){
            button_favorite.setText(R.string.button_remove_favorite);
            button_favorite.setOnClickListener(isFavoriteListener);
        } else {
            button_favorite.setText(R.string.button_mark_favorite);
            button_favorite.setOnClickListener(isNotFavoriteListener);
        }


//        movie_rating.setText(ratingIntro + Double.toString(obtainResult.aveRating));
//        movie_description.setText(genreIntro + "\n" + obtainResult.translatedGenres + "\n\n" + descriptionIntro + "\n" + obtainResult.description);
//        obtainResult.translateGenreList();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
        boolean twoPane = MainActivity.mTwoPane;
        switch (item.getItemId()){
            case R.id.action_reviews:{
                if (twoPane){
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.movie_pager_fragment, ReviewsActivityFragment.newInstance(movieId), ReviewsActivityFragment.REVIEWSFRAG_TAG)
                            .commit();
                }
                else {
                    Intent intent = new Intent(getActivity(), ReviewsActivity.class);
                    intent.putExtra(Intent.EXTRA_RETURN_RESULT, movieId);
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, EXTRA_REVIEWS);
                    startActivity(intent);
                }
                return true;
            }
            case R.id.action_trailers:{
                if (twoPane){
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.movie_pager_fragment, TrailerActivityFragment.newInstance(movieId), ReviewsActivityFragment.REVIEWSFRAG_TAG)
                            .commit();
                }
                else {
                    Intent intent = new Intent(getActivity(), ReviewsActivity.class);
                    intent.putExtra(Intent.EXTRA_RETURN_RESULT, movieId);
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, EXTRA_TRAILERS);
                    startActivity(intent);
                }
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Intent.EXTRA_INTENT, obtainResult);
    }



    public long insertFavorite(MovieResult movieResult){
        ContentValues cv = new ContentValues();
        cv.put(FavoritesContract.FavoritesEntry._ID, movieResult.getMovieId());
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_NAME_TITLE, movieResult.title);
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_NAME_AVE_RATING, movieResult.aveRating);
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_NAME_RELEASE_DATE, movieResult.releaseDate);
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_NAME_IMAGE_URL, movieResult.imageUrl);
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_NAME_TRANSLATED_GENRES, movieResult.translatedGenres);
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_NAME_DESCRIPTION, movieResult.description);
        long insertRow = mSQLiteDatabase.insert(FavoritesContract.FavoritesEntry.TABLE_NAME, null, cv);
        button_favorite.setText(R.string.button_remove_favorite);
        button_favorite.setOnClickListener(isFavoriteListener);
        return insertRow;
    }

    public int deleteFavorite(MovieResult movieResult){
        int removedCols = mSQLiteDatabase.delete(FavoritesContract.FavoritesEntry.TABLE_NAME, FavoritesContract.FavoritesEntry._ID + " = ?",
                new String[]{Long.toString(movieResult.getMovieId())});
        button_favorite.setText(R.string.button_mark_favorite);
        button_favorite.setOnClickListener(isNotFavoriteListener);
        return removedCols;
    }
}
