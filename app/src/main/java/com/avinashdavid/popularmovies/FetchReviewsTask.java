package com.avinashdavid.popularmovies;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by avinashdavid on 5/29/16.
 */
public class FetchReviewsTask extends AsyncTask<String,Void,ArrayList<ReviewResult>> {

    private String LOG_TAG = FetchReviewsTask.class.getSimpleName();
    protected ArrayList<ReviewResult> doInBackground (String... movieId){
        ArrayList<ReviewResult> finalList = new ArrayList<ReviewResult>();
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String returnJsonStr = null;

//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String formattedDate = df.format(c.getTime());

        Uri.Builder builder = new Uri.Builder()
                .encodedPath("http://api.themoviedb.org/3/movie")
                .appendPath((String)movieId[0])
                .appendPath("reviews")
                .appendQueryParameter("api_key",MainActivityFragment.apiKey);

        try {
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
            returnJsonStr = buffer.toString();
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
                ReviewResult toBeAdded = new ReviewResult(thisResult.getString("id"));
                toBeAdded.setAuthor(thisResult.getString("author"));
                toBeAdded.setReviewText(thisResult.getString("content"));
                finalList.add(toBeAdded);
            }
        }
        catch (JSONException e){
            Log.e(LOG_TAG,"JSON error",e);
        }

        return finalList;
    }

//    @Override
//    protected void onPostExecute(ArrayList<MovieResult> movieResults) {
//        super.onPostExecute(movieResults);
//        if (mPosterAdapter!=null && movieResults!=null) {
//            mPosterAdapter.clear();
//            mPosterAdapter.addAll(movieResults);
//        }
//    }
}
