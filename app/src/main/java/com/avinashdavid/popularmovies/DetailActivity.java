package com.avinashdavid.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setMinimumHeight(120);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if (!MainActivity.mTwoPane && intent!=null){
            MovieResult movieResult = (MovieResult)intent.getParcelableExtra(Intent.EXTRA_RETURN_RESULT);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, DetailActivityFragment.newInstance(movieResult), MainActivity.RESULTPAGER_FRAGMENTTAG)
                    .commit();
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}