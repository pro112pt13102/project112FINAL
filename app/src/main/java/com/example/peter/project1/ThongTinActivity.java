package com.example.peter.project1;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ThongTinActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trangchu);
//        viewPager = findViewById(R.id.viewpager_thanhtoan);
//        tabLayout = findViewById(R.id.tabLayout_thanhtoan);
//
//        tabLayout.addTab(tabLayout.newTab().setText("Thông tin"));
//        tabLayout.addTab(tabLayout.newTab().setText("Thanh Toán"));
//        tabLayout.addTab(tabLayout.newTab().setText("Hoàn Tất"));
//        tabLayout.setTabGravity(TabLayout.MODE_SCROLLABLE);
//
//        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(adapter);
//        tabLayout.setTabsFromPagerAdapter(adapter);
//
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

}
