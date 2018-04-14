package com.example.peter.project1.Fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.peter.project1.R;

import static com.example.peter.project1.MuaNgayActivity.setCurrentPage;
import static com.example.peter.project1.MuaNgayActivity.setCurrentStateThree;
import static com.example.peter.project1.MuaNgayActivity.setCurrentStateTwo;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThanhToanFragment extends android.support.v4.app.Fragment {
    Button btn_tiep_tuc_thanh_toan;
    View v;
    public ThanhToanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_thanh_toan, container, false);
        Anhxa();
        ClickEvent();
        return v;
    }
    public void Anhxa(){
        btn_tiep_tuc_thanh_toan=v.findViewById(R.id.btn_tiep_tuc_thanh_toan);
    }
    public void ClickEvent(){
        //btn_tiep_tuc_thong_tin CLick
        btn_tiep_tuc_thanh_toan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentPage(2);
                setCurrentStateThree();
            }
        });
    }
}
