package com.avinashdavid.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by avinashdavid on 5/29/16.
 */
public class ReviewsActivity extends AppCompatActivity {
    public static final String REVIEWSFRAG_TAG = "reviewsFrag";
    public static final String TRAILERSFRAG_TAG = "trailersFrag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        Intent intent = getIntent();
        int typeFrag = intent.getIntExtra(Intent.EXTRA_MIME_TYPES, 1);
        String movieId = intent.getStringExtra(Intent.EXTRA_RETURN_RESULT);
        if (movieId!=null) {
            if (typeFrag == DetailActivityFragment.EXTRA_REVIEWS){
                setTitle(R.string.page_title_reviews);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.reviews_fragment, ReviewsActivityFragment.newInstance(movieId), REVIEWSFRAG_TAG)
                        .commit();
            } else if (typeFrag == DetailActivityFragment.EXTRA_TRAILERS){
                setTitle(R.string.page_title_trailers);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.reviews_fragment, TrailerActivityFragment.newInstance(movieId), TRAILERSFRAG_TAG)
                        .commit();
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
////        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
////            }
////        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
