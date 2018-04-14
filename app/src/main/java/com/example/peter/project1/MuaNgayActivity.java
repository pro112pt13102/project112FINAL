package com.example.peter.project1;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.peter.project1.Adapter.adapterFragmentMuaNgayActivity;
import com.example.peter.project1.CustomView.OneDirectionWipeViewpager;
import com.example.peter.project1.Enum.DirectionViewpager;
import com.example.peter.project1.Fragment.HoanTatFragment;
import com.example.peter.project1.Fragment.ThongTinFragment;
import com.example.peter.project1.Model.SanPham;
import com.example.peter.project1.Model.User;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;

public class MuaNgayActivity extends AppCompatActivity implements ThongTinFragment.SendData{
    static OneDirectionWipeViewpager vp_mungay_activity;
    static StateProgressBar stateProgressBar;
    ArrayList<SanPham> arrayListgiohang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mua_ngay);
        Anhxa();
        getGiohangFromGiohangActivity();
        setUpViewpager();
    }
    public void Anhxa(){
        arrayListgiohang=new ArrayList<>();
        vp_mungay_activity=findViewById(R.id.vp_mungay_activity);
        stateProgressBar=findViewById(R.id.state_progress_bar);
    }
    public void setUpViewpager(){
        adapterFragmentMuaNgayActivity adapter = new adapterFragmentMuaNgayActivity(getSupportFragmentManager(),arrayListgiohang);
        vp_mungay_activity.setAdapter(adapter);
        vp_mungay_activity.setAllowedSwipeDirection(DirectionViewpager.left);

    }
    public static void setCurrentPage(int item){
        vp_mungay_activity.setCurrentItem(item);
    }

    public static void setCurrentStateTwo(){
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
    }
    public static void setCurrentStateThree(){
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
    }
    public void getGiohangFromGiohangActivity(){
        arrayListgiohang= (ArrayList<SanPham>) getIntent().getSerializableExtra("arrayListgiohang");
//        Toast.makeText(this, ""+arrayListgiohang.size(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void sendData(User user) {
        String tag = "android:switcher:" + R.id.vp_mungay_activity + ":" + 2;
        HoanTatFragment f = (HoanTatFragment) getSupportFragmentManager().findFragmentByTag(tag);
        f.displayReceivedData(user);
//        Toast.makeText(this, ""+tag, Toast.LENGTH_SHORT).show();
    }
}
