package com.avinashdavid.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * Created by avinashdavid on 5/30/16.
 */
public class ResultPagerActivity extends AppCompatActivity {
//    private Intent mIntent;
    private MovieResult mMovieResult;
    private Toolbar toolbar;
    private String mMovieId;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, savedInstanceState.getParcelable(Intent.EXTRA_RETURN_RESULT));
        setIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mMovieResult = getIntent().getParcelableExtra(Intent.EXTRA_RETURN_RESULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mMovieResult = getIntent().getParcelableExtra(Intent.EXTRA_RETURN_RESULT);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.movie_pager_fragment, ResultPagerActivityFragment.newInstance(mMovieResult), MainActivity.RESULTPAGER_FRAGMENTTAG)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.setIntent(new Intent().putExtra(Intent.EXTRA_RETURN_RESULT, mMovieResult));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Intent.EXTRA_RETURN_RESULT, mMovieResult);
    }
}
