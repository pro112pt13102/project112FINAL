package com.example.peter.project1.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by daovip on 3/27/2018.
 */

public class adapterFragment_ThanhToan extends FragmentStatePagerAdapter {

    public adapterFragment_ThanhToan(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:

        }
       return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
