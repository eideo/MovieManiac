package com.adithyaupadhya.moviemaniac.tvseries;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.adithyaupadhya.moviemaniac.R;
import com.adithyaupadhya.moviemaniac.base.AbstractTabFragment;
import com.adithyaupadhya.moviemaniac.tvseries.favoritetvseries.FavoriteTVSeriesFragment;
import com.adithyaupadhya.moviemaniac.tvseries.tvseries.TVSeriesFragment;
import com.adithyaupadhya.moviemaniac.tvseries.tvseriessearch.TVSeriesSearchActivity;
import com.adithyaupadhya.newtorkmodule.volley.networkconstants.AppIntentConstants;
import com.adithyaupadhya.newtorkmodule.volley.networkconstants.NetworkConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TVSeriesLaunchFragment extends AbstractTabFragment {

    public TVSeriesLaunchFragment() {
        // Required empty public constructor
    }

    @Override
    public List<Fragment> getViewPagerFragmentList() {
        List<Fragment> mFragmentList = new ArrayList<>();
        Fragment fragment;

        Bundle bundle = new Bundle();
        bundle.putString(AppIntentConstants.BUNDLE_URL, NetworkConstants.TV_SERIES_ON_THE_AIR_BASE_URL);
        fragment = new TVSeriesFragment();
        fragment.setArguments(bundle);
        mFragmentList.add(fragment);

        bundle = new Bundle();
        bundle.putString(AppIntentConstants.BUNDLE_URL, NetworkConstants.TV_SERIES_POPULAR_BASE_URL);
        fragment = new TVSeriesFragment();
        fragment.setArguments(bundle);
        mFragmentList.add(fragment);

        fragment = new FavoriteTVSeriesFragment();
        mFragmentList.add(fragment);
        return mFragmentList;
    }

    @Override
    public String[] getSearchAndToolbarTitle() {
        return new String[]{"Ongoing TV Series", "Popular TV Series", "My Favourites", "Search for TV Series..."};
    }

    @Override
    public int[] getTabDrawableIcon() {
        return new int[]{
                R.drawable.vector_ongoing,
                R.drawable.vector_popular,
                R.drawable.vector_favorite
        };
    }

    @Override
    public void searchQuerySubmission(String searchQuery) {
        startActivity(TVSeriesSearchActivity.getActivityIntent(getContext(), searchQuery));
    }
}
