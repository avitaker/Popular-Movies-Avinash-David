package com.avinashdavid.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
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
 * Created by avinashdavid on 5/30/16.
 */
public class FetchTrailerTask extends AsyncTask<String,Void,ArrayList<TrailerResult>> {
    private String LOG_TAG = FetchReviewsTask.class.getSimpleName();
    protected ArrayList<TrailerResult> doInBackground (String... movieId){
        ArrayList<TrailerResult> finalList = new ArrayList<TrailerResult>();
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String returnJsonStr = null;

//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        String formattedDate = df.format(c.getTime());

        Uri.Builder builder = new Uri.Builder()
                .encodedPath("http://api.themoviedb.org/3/movie")
                .appendPath(movieId[0])
                .appendPath("videos")
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
                TrailerResult toBeAdded = new TrailerResult(thisResult.getString("id"));
                toBeAdded.setVideoKey(thisResult.getString("key"));
                toBeAdded.setVideoName(thisResult.getString("name"));
                toBeAdded.setVideoSite(thisResult.getString("site"));
                toBeAdded.setVideoType(thisResult.getString("type"));
                finalList.add(toBeAdded);
            }
        }
        catch (JSONException e){
            Log.e(LOG_TAG,"JSON error",e);
        }

        return finalList;
    }
}
