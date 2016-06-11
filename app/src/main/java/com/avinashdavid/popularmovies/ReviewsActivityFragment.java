package com.avinashdavid.popularmovies;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by avinashdavid on 5/29/16.
 */
public class ReviewsActivityFragment extends Fragment {
    public static final String REVIEWSFRAG_TAG = "reviewsFrag";
    private ReviewAdapter mReviewAdapter;
    private ListView mListView;

    private class FinalReviewsTask extends FetchReviewsTask {
        @Override
        protected void onPostExecute(ArrayList<ReviewResult> reviewResults) {
            super.onPostExecute(reviewResults);
            if (mReviewAdapter != null && reviewResults!=null){
                mReviewAdapter.clear();
                mReviewAdapter.addAll(reviewResults);
                mListView.setAdapter(mReviewAdapter);
//                Log.d("testPost",Integer.toString(reviewResults.size()));
            }
            else {
//                Log.d("testPost",Integer.toString(reviewResults.size()));
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        String movieId;
        Intent intent = getActivity().getIntent();
        if (intent!=null && !MainActivity.mTwoPane){
            movieId = intent.getStringExtra(Intent.EXTRA_RETURN_RESULT);
        }
        else {
            movieId = getArguments().getString("movieId");
        }

//        Log.d("testIdRec",movieId);
        FinalReviewsTask finalReviewsTask = new FinalReviewsTask();
        startMyTask(finalReviewsTask, movieId);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    void startMyTask(FinalReviewsTask asyncTask, String params) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        else
            asyncTask.execute(params);
    }

    public static ReviewsActivityFragment newInstance(String text) {

        ReviewsActivityFragment f = new ReviewsActivityFragment();
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
//        ArrayList<ReviewResult> emptySet = new ArrayList<ReviewResult>();
//        mReviewAdapter = new ReviewAdapter(getActivity(), emptySet);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<ReviewResult> emptySet = new ArrayList<ReviewResult>();
        mReviewAdapter = new ReviewAdapter(getActivity(), emptySet);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.review_listView);
        mListView.setAdapter(mReviewAdapter);
    }



    public class ReviewAdapter extends ArrayAdapter<ReviewResult> {
        public ReviewAdapter(Context context, ArrayList<ReviewResult> reviewResults){
            super(context,0,reviewResults);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ReviewResult reviewResult = getItem(position);
            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_reviews,parent,false);
            }
            TextView author_textView = (TextView) convertView.findViewById(R.id.author_textView);
//            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w342/"+reviewResult).into(movie_poster);
            TextView content_textView = (TextView) convertView.findViewById(R.id.content_textView);
            author_textView.setText(reviewResult.getAuthor());
            content_textView.setText(reviewResult.getReviewText());
            return convertView;
        }
    }
}
