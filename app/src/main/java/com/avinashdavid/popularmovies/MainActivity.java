package com.avinashdavid.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {
    public static boolean mTwoPane;
    public final static String RESULTPAGER_FRAGMENTTAG = "resultPagerFrag";
    public static final String MAIN_FRAGMENTTAG = "mainFrag";

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.movie_pager_fragment)!=null){
            mTwoPane = true;
            Log.d("twopane",String.valueOf(mTwoPane));
        } else {
            mTwoPane = false;
            Log.d("twopane","nope");
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(MovieResult movieResult) {
        if (mTwoPane){
            Log.d("ArgsResultPager_send",movieResult.title);
            FragmentManager fm = getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            fm.beginTransaction()
                    .replace(R.id.movie_pager_fragment, DetailActivityFragment.newInstance(movieResult), RESULTPAGER_FRAGMENTTAG)
                    .commit();

        } else {
            Intent intent = new Intent(this,DetailActivity.class).putExtra(Intent.EXTRA_RETURN_RESULT,movieResult);
            Log.d("testIntentPhone",movieResult.title);
            startActivity(intent);
        }
    }
}
