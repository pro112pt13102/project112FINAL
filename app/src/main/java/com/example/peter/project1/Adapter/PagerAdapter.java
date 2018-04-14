package com.example.peter.project1.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Peter on 26/03/2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
//        Fragment frag = null;
//        switch (position){
//            case 0:
//                frag = new ThongTinFragment();
//                break;
//            case 1:
//                frag = new ThanhToanFragment();
//                break;
//        }
//
//        return frag;
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Thông tin";
                break;
            case 1:
                title = "Thanh Toán";
                break;
        }
        return title;
    }
}
