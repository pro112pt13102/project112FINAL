package com.example.peter.project1.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.peter.project1.Fragment.HoanTatFragment;
import com.example.peter.project1.Fragment.ThanhToanFragment;
import com.example.peter.project1.Fragment.ThongTinFragment;
import com.example.peter.project1.Model.SanPham;

import java.util.ArrayList;

/**
 * Created by daovip on 3/27/2018.
 */

public class adapterFragmentMuaNgayActivity extends FragmentPagerAdapter {
    ArrayList<SanPham> arrayListGiohang;
    public adapterFragmentMuaNgayActivity(FragmentManager fm, ArrayList<SanPham> arrayListGiohang) {
        super(fm);
        this.arrayListGiohang=arrayListGiohang;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ThongTinFragment();
            case 1:
                return new ThanhToanFragment();
            case 2:
                return new HoanTatFragment(arrayListGiohang);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
