package com.avinashdavid.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by avinashdavid on 10/30/15.
 */
public class MovieResult implements Parcelable {
    public String title;
    public String description;
    public String releaseDate;
    public String imageUrl;
    public double aveRating;
    public JSONArray genreList;
    public String translatedGenres;
    private long movieId;

    public MovieResult(String t, String d, String r, String i, double a){
        this.title = t;
        this.description = d;
        this.releaseDate = r;
        this.imageUrl = i;
        this.aveRating = a;
    }

    public MovieResult(String t, String d, String r, String i, double a, String al){
        this.title = t;
        this.description = d;
        this.releaseDate = r;
        this.imageUrl = i;
        this.aveRating = a;
        this.translatedGenres = al;
    }

    public MovieResult(String t){
        this.title = t;
    }

    private MovieResult(Parcel in){
        movieId = in.readLong();
        title = in.readString();
        description = in.readString();
        releaseDate = in.readString();
        imageUrl = in.readString();
        aveRating = in.readDouble();
        translatedGenres = in.readString();
//        translatedGenres = in.readArrayList(String.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setDescription(String d){
        this.description = d;
    }
    public void setReleaseDate(String r){
        this.releaseDate = r;
    }
    public void setImageUrl(String i){
        this.imageUrl = i;
    }
    public void setGenreList(JSONArray input){
        this.genreList = input;
    }

    public String getTranslatedGenres() {
        return translatedGenres;
    }

    public void setTranslatedGenres(String translatedGenres) {
        this.translatedGenres = translatedGenres;
    }

    public double getAveRating() {
        return aveRating;
    }

    public void setAveRating(double aveRating) {
        this.aveRating = aveRating;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public void translateGenreList(){
        try{
            JSONObject genreKey = new JSONObject("{\n" +
                    "  \"genres\": [\n" +
                    "    {\n" +
                    "      \"id\": 28,\n" +
                    "      \"name\": \"Action\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 12,\n" +
                    "      \"name\": \"Adventure\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 16,\n" +
                    "      \"name\": \"Animation\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 35,\n" +
                    "      \"name\": \"Comedy\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 80,\n" +
                    "      \"name\": \"Crime\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 99,\n" +
                    "      \"name\": \"Documentary\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 18,\n" +
                    "      \"name\": \"Drama\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 10751,\n" +
                    "      \"name\": \"Family\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 14,\n" +
                    "      \"name\": \"Fantasy\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 10769,\n" +
                    "      \"name\": \"Foreign\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 36,\n" +
                    "      \"name\": \"History\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 27,\n" +
                    "      \"name\": \"Horror\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 10402,\n" +
                    "      \"name\": \"Music\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 9648,\n" +
                    "      \"name\": \"Mystery\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 10749,\n" +
                    "      \"name\": \"Romance\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 878,\n" +
                    "      \"name\": \"Science Fiction\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 10770,\n" +
                    "      \"name\": \"TV Movie\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 53,\n" +
                    "      \"name\": \"Thriller\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 10752,\n" +
                    "      \"name\": \"War\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 37,\n" +
                    "      \"name\": \"Western\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}");
            JSONArray genreKeyArr = genreKey.getJSONArray("genres");
            String[] translatedGenreList = new String[this.genreList.length()];
            for (int i=0; i<this.genreList.length(); i++){
                for (int l=0; l<genreKeyArr.length(); l++){
                    if (this.genreList.getInt(i) == genreKeyArr.getJSONObject(l).getInt("id")){
                        translatedGenreList[i] = (genreKeyArr.getJSONObject(l).getString("name"));
                        this.translatedGenres = formatGenres(translatedGenreList);
                    }
                }
            }
        }
        catch (JSONException e){
            Log.e("MovieResult","JSON Creation error",e);
        }
//        this.translatedGenres = formatGenres(translatedGenreList);
    }

    public String formatGenres(String[] a){
        StringBuilder builder = new StringBuilder();
        if (a.length > 0) {
            for (int i=0; i< a.length-1; i++){
                builder.append(a[i]).append(", ");
            }
            builder.append(a[a.length-1]);
        }
        return builder.toString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(movieId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(releaseDate);
        dest.writeString(imageUrl);
        dest.writeDouble(aveRating);
        dest.writeString(translatedGenres);
    }

    public static final Parcelable.Creator<MovieResult> CREATOR = new Parcelable.Creator<MovieResult>(){
        @Override
        public MovieResult createFromParcel(Parcel source) {
            return new MovieResult(source);
        }

        @Override
        public MovieResult[] newArray(int size) {
            return new MovieResult[size];
        }
    };
}