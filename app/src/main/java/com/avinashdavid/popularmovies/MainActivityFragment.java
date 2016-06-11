package com.avinashdavid.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private PosterAdapter mPosterAdapter;
    public static String apiKey;
    private FavoritesDbHelper mDbHelper;
    private int height;
    private int width;
    private String mMovieId;
    private GridView movie_listing_grid;


    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        String sortString;
        GetMovieInfo doIt = new GetMovieInfo();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        try {
            ApplicationInfo applicationInfo = getActivity().getPackageManager()
                    .getApplicationInfo(getActivity().getPackageName(),
                            PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            apiKey = bundle.getString("api-key");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();  //Do something more useful here!
        }

        String chosenSorting = sp.getString(getString(R.string.pref_key_choose_sorting),getString(R.string.pref_default_sorting));
        if (!(chosenSorting.equals("fav.all"))) {
            doIt.execute(chosenSorting);
        }
        else {
            mDbHelper = FavoritesDbHelper.getInstance(getActivity().getApplicationContext());
            ArrayList<MovieResult> favoritesList = new ArrayList<MovieResult>();
            Cursor cursor = mDbHelper.getReadableDatabase().query(FavoritesContract.FavoritesEntry.TABLE_NAME,
                    FavoritesContract.favoritesProjection,null,null,null,null,null);
            cursor.moveToFirst();
            if (cursor.isBeforeFirst()||cursor.isAfterLast()){
                Log.d("testCur","cursor not valid");
            } else {

                try {
                    favoritesList.addAll(FavoritesContract.convertCursorToMovieResults(cursor));
                } finally {
                    cursor.close();
                    if (mPosterAdapter != null && favoritesList != null) {
                        mPosterAdapter.clear();
                        mPosterAdapter.addAll(favoritesList);
                    }
                }
            }
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main, container, false);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int f = metrics.densityDpi;


        //  getWindowManager().getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;
        height = metrics.heightPixels;
        ArrayList<MovieResult> blankSet = new ArrayList<>();
        movie_listing_grid = (GridView) rootView.findViewById(R.id.movie_listing_grid);
//        if (!MainActivity.mTwoPane){
//            if (width>height) {
//                movie_listing_grid.setColumnWidth(width / 7);
//            } else {movie_listing_grid.setColumnWidth(width/4);}
//        }
//        else {
//            if (width>height) {
//                movie_listing_grid.setColumnWidth(width / 7);
//            } else {movie_listing_grid.setColumnWidth(width/4);}
//        }
        if (width>height) {
            movie_listing_grid.setColumnWidth(width / 5);
        } else {
            movie_listing_grid.setColumnWidth(width/3);
        }
//        if (width>height) {
//            movie_listing_grid.setColumnWidth(width / 5);
//        } else {movie_listing_grid.setColumnWidth(width/4);}

        mPosterAdapter = new PosterAdapter(getActivity().getBaseContext(),blankSet);
        movie_listing_grid.setAdapter(mPosterAdapter);

        movie_listing_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieResult thisOne = mPosterAdapter.getItem(position);
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.onItemSelected(thisOne);
            }
        });

        return rootView;
    }



    public class GetMovieInfo extends AsyncTask<String,Void,ArrayList<MovieResult>>{
        private String LOG_TAG = GetMovieInfo.class.getSimpleName();
        protected ArrayList<MovieResult> doInBackground (String... sorting){
            ArrayList<MovieResult> finalList = new ArrayList<MovieResult>();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String returnJsonStr = null;
            String queryParameter = "sort_by";

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());

            SharedPreferences ps = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String voteMin = ps.getString(getString(R.string.pref_key_vote_minimum),getString(R.string.pref_default_vote_minimum));

            Uri.Builder builder = new Uri.Builder()
                    .encodedPath("http://api.themoviedb.org/3/discover/movie")
                    .appendQueryParameter("api_key",apiKey)
                    .appendQueryParameter("release_date.lte",formattedDate)
                    .appendQueryParameter("vote_count.gte",voteMin)
                    .appendQueryParameter(queryParameter, sorting[0]);

            try {
                Log.d("test",builder.toString());
                URL url = new URL(builder.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line=reader.readLine()) != null){
                    buffer.append(line + "\n");
                }
                if (buffer.length()==0){
                    return null;
                }
                returnJsonStr = buffer.toString();;
            }
            catch (IOException e) {
                Log.e(LOG_TAG,"ERROR!",e);
                return null;
            }
            finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader != null){
                    try {
                        reader.close();
                    }
                    catch (IOException e){
                        Log.e(LOG_TAG,"Reader ERROR",e);
                    }
                }
            }

            try {
                JSONObject returnJson = new JSONObject(returnJsonStr);
                JSONArray resultsArray = returnJson.getJSONArray("results");
                for (int i=0;i<resultsArray.length();i++){
                    JSONObject thisResult = resultsArray.getJSONObject(i);
                    MovieResult toBeAdded = new MovieResult(thisResult.getString("title"),thisResult.getString("overview"),thisResult.getString("release_date").substring(0,4),thisResult.getString("poster_path"),thisResult.getDouble("vote_average"));
                    toBeAdded.setGenreList(thisResult.getJSONArray("genre_ids"));
                    toBeAdded.translateGenreList();
                    toBeAdded.setMovieId(thisResult.getLong("id"));
                    finalList.add(toBeAdded);
                }
            }
            catch (JSONException e){
                Log.e(LOG_TAG,"JSON error",e);
            }

            return finalList;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieResult> movieResults) {
            super.onPostExecute(movieResults);
            if (mPosterAdapter!=null && movieResults!=null) {
                mPosterAdapter.clear();
                mPosterAdapter.addAll(movieResults);
            }
        }
    }

    public class PosterAdapter extends ArrayAdapter<MovieResult>{
        public PosterAdapter(Context context, ArrayList<MovieResult> movieResults){
            super(context,0,movieResults);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MovieResult imageUrl = getItem(position);
            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_poster_imageview,parent,false);
            }
            ImageView movie_poster = (ImageView)convertView.findViewById(R.id.movie_poster);
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w500/"+imageUrl.imageUrl).into(movie_poster);
            return convertView;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public interface Callback{

        public void onItemSelected(MovieResult movieResult);
    }
}
