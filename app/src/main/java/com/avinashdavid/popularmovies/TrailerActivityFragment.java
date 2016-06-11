package com.avinashdavid.popularmovies;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by avinashdavid on 5/30/16.
 */
public class TrailerActivityFragment extends Fragment {


    private TrailerAdapter mTrailerAdapter;
    private ListView mListView;
    private class FinalTrailerTask extends FetchTrailerTask {
        @Override
        protected void onPostExecute(ArrayList<TrailerResult> trailerResults) {
            super.onPostExecute(trailerResults);
            if (mTrailerAdapter!=null && trailerResults!=null){
                mTrailerAdapter.clear();
                mTrailerAdapter.addAll(trailerResults);
                mListView.setAdapter(mTrailerAdapter);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        String movieId = getArguments().getString("movieId");
        FinalTrailerTask finalTrailerTask = new FinalTrailerTask();
        startMyTask(finalTrailerTask, movieId);
    }

    public static TrailerActivityFragment newInstance(String text) {

        TrailerActivityFragment f = new TrailerActivityFragment();
        Bundle b = new Bundle();
        b.putString("movieId", text);

        f.setArguments(b);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//        Intent intent = getActivity().getIntent();
        View rootView=inflater.inflate(R.layout.fragment_reviews, container, false);
        mListView = (ListView)rootView.findViewById(R.id.review_listView);
//        ArrayList<ReviewResult> emptySet = new ArrayList<ReviewResult>();
//        mReviewAdapter = new ReviewAdapter(getActivity(), emptySet);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<TrailerResult> emptySet = new ArrayList<TrailerResult>();
        mTrailerAdapter = new TrailerAdapter(getActivity(), emptySet);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    void startMyTask(FinalTrailerTask asyncTask, String params) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        else
            asyncTask.execute(params);
    }


    public class TrailerAdapter extends ArrayAdapter<TrailerResult> {
        public TrailerAdapter(Context context, ArrayList<TrailerResult> reviewResults){
            super(context,0,reviewResults);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final TrailerResult reviewResult = getItem(position);
            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_trailer,parent,false);
            }
            TextView trailer_textView = (TextView) convertView.findViewById(R.id.trailerNameTextView);
//            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w342/"+reviewResult).into(movie_poster);
//            TextView content_textView = (TextView) convertView.findViewById(R.id.content_textView);
            trailer_textView.setText(reviewResult.getVideoName());
//            content_textView.setText(reviewResult.getReviewText());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reviewResult.getVideoSite().equals("YouTube")) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+reviewResult.getVideoKey())));
                    }
                }
            });

            return convertView;
        }
    }
}
