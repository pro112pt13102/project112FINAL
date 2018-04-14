package com.example.peter.project1.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.peter.project1.Fragment.SlideshowFragment;
import com.example.peter.project1.Model.SanPham;

import java.util.ArrayList;

/**
 * Created by daovip on 3/27/2018.
 */

public class AdapterSlideShow extends FragmentPagerAdapter {
    ArrayList<SanPham> arrayList;
    public AdapterSlideShow(FragmentManager fm, ArrayList<SanPham> arrayList) {
        super(fm);
        this.arrayList = arrayList;
    }

    public AdapterSlideShow(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return SlideshowFragment.newInstance(arrayList.get(0));
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return SlideshowFragment.newInstance(arrayList.get(1));
            case 2: // Fragment # 1 - This will show SecondFragment
                return SlideshowFragment.newInstance(arrayList.get(2));
            case 3: // Fragment # 1 - This will show SecondFragment
                return SlideshowFragment.newInstance(arrayList.get(3));
            case 4: // Fragment # 1 - This will show SecondFragment
                return SlideshowFragment.newInstance(arrayList.get(4));
            case 5: // Fragment # 1 - This will show SecondFragment
                return SlideshowFragment.newInstance(arrayList.get(5));
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 6;
    }


}
