package com.avinashdavid.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by avinashdavid on 5/31/16.
 */
public class ResultPagerActivityFragment extends Fragment {
    private MovieResult mMovieResult;
    private String mMovieId;
    public static final String MOVIERESULT_KEY = "movieResult";
    private ViewPager pager;
    private MyPagerAdapter mMyPagerAdapter;

    public static ResultPagerActivityFragment newInstance(MovieResult movieResult){
        ResultPagerActivityFragment resultPagerActivityFragment = new ResultPagerActivityFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(MOVIERESULT_KEY, movieResult);
        resultPagerActivityFragment.setArguments(arguments);
        return resultPagerActivityFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null) {
            mMovieResult = getArguments().getParcelable(MOVIERESULT_KEY);
        }
//        mMovieResult = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_RETURN_RESULT);
        mMovieId = Long.toString(mMovieResult.getMovieId());
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        if (getArguments()!=null) {
//            mMovieResult = getArguments().getParcelable(MOVIERESULT_KEY);
//            Log.d("ArgsResultPager_rec",mMovieResult.title);
//        }
////        mMovieResult = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_RETURN_RESULT);
//        mMovieId = Long.toString(mMovieResult.getMovieId());
//        if (mMyPagerAdapter!=null){
//            if (pager.getAdapter()==mMyPagerAdapter){
//                mMyPagerAdapter.notifyDataSetChanged();
//            }
//            else {
//                mMyPagerAdapter=new MyPagerAdapter(getActivity().getSupportFragmentManager());
//                pager.setAdapter(mMyPagerAdapter);
//            }
//        }
////        pager.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//        Intent intent = getActivity().getIntent();
        View rootView=inflater.inflate(R.layout.fragment_viewpager, container, false);

        PagerTabStrip pagerTabStrip = (PagerTabStrip) rootView.findViewById(R.id.pager_tab_strip);
        pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.background_material_light));
        pagerTabStrip.setTextColor(R.color.colorPrimary);
        pagerTabStrip.setTabIndicatorColor(R.color.colorAccent);

        if (getArguments()!=null) {
            mMovieResult = getArguments().getParcelable(MOVIERESULT_KEY);
            Log.d("ArgsResultPager_recv",mMovieResult.title);
        }
//        mMovieResult = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_RETURN_RESULT);
        mMovieId = Long.toString(mMovieResult.getMovieId());

//        TabLayout tabs = (TabLayout) rootView.findViewById(R.id.tabs);
        pager = (ViewPager) rootView.findViewById(R.id.viewPager);
        if (mMyPagerAdapter==null) {
            mMyPagerAdapter = new MyPagerAdapter((getActivity().getSupportFragmentManager()));
        }
        else {
            mMyPagerAdapter.notifyDataSetChanged();
        }
        pager.setAdapter(mMyPagerAdapter);
//        tabs.setupWithViewPager(pager);

        return rootView;
    }

    @Override
    public void onDestroy() {
        mMyPagerAdapter = null;
        pager = null;
        super.onDestroy();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return DetailActivityFragment.newInstance(mMovieResult);
                case 1: return ReviewsActivityFragment.newInstance(mMovieId);
                case 2: return TrailerActivityFragment.newInstance(mMovieId);
//                case 2: return ThirdFragment.newInstance("ThirdFragment, Instance 1");
//                case 3: return ThirdFragment.newInstance("ThirdFragment, Instance 2");
//                case 4: return ThirdFragment.newInstance("ThirdFragment, Instance 3");
                default: return DetailActivityFragment.newInstance(mMovieResult);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            return super.getPageTitle(position);
            switch (position){
                case 0:
                    return getString(R.string.page_title_info);
                case 1:
                    return getString(R.string.page_title_reviews);
                case 2:
                    return getString(R.string.page_title_trailers);
                default:
                    return super.getPageTitle(position);
            }
        }

        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }


    }
}
