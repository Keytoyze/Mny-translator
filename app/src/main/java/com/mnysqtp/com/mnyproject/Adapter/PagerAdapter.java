package com.mnysqtp.com.mnyproject.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mnysqtp.com.mnyproject.Fragment.AboutFragment;
import com.mnysqtp.com.mnyproject.Fragment.DictionaryFragment;
import com.mnysqtp.com.mnyproject.Fragment.DiscoveryFragment;
import com.mnysqtp.com.mnyproject.Fragment.TranslationFragment;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>(4);

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(new DictionaryFragment());
        fragments.add(new TranslationFragment());
        fragments.add(new DiscoveryFragment());
        fragments.add(new AboutFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
