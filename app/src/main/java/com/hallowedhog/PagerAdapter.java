package com.hallowedhog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Jay on 2/26/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int mNumOfTabs){
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TopFragment topFragment = new TopFragment();
                return topFragment;
            case 1:
                CarletonFragment carletonFragment = new CarletonFragment();
                return carletonFragment;
            case 2:
                SportsFragment sportsFragment = new SportsFragment();
                return sportsFragment;
            case 3:
                WorldwideAffairsFragment worldwideAffairsFragment = new WorldwideAffairsFragment();
                return worldwideAffairsFragment;
            case 4:
                ComicsFragment comicsFragment = new ComicsFragment();
                return comicsFragment;
            case 5:
                PopCultureFragment popCultureFragment = new PopCultureFragment();
                return popCultureFragment;
            case 6:
                PoliticsFragment politicsFragment = new PoliticsFragment();
                return politicsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
