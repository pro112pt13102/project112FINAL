package com.example.peter.project1.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.peter.project1.ChiTietActivity;
import com.example.peter.project1.Model.SanPham;
import com.example.peter.project1.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlideshowFragment extends android.support.v4.app.Fragment {
    View v;
    ImageView view_slideshow;
    SanPham sanPham;

    public static SlideshowFragment newInstance(SanPham sp) {
        SlideshowFragment fragmentFirst = new SlideshowFragment();
        Bundle args = new Bundle();
        args.putSerializable("sanpham",sp);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get Data
        sanPham= (SanPham) getArguments().getSerializable("sanpham");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       v=inflater.inflate(R.layout.fragment_slideshow, container, false);
       Anhxa();
       SetData();
       onClick();
        return v;
    }
    public void Anhxa(){
        view_slideshow=v.findViewById(R.id.view_slide_show);
    }
    public void SetData(){
        loadhinh(view_slideshow,sanPham.getHinh());
    }
    public void onClick(){
        view_slideshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getActivity(), ChiTietActivity.class);
                i.putExtra("SanPham",sanPham);
                i.putExtra("key",1);
                startActivity(i);
            }
        });
    }
    public void loadhinh(ImageView img ,String hinh){
        Picasso.get()
                .load(hinh)
                .resize(500,300)
                .error(R.drawable.error404)
                .into(img);
    }
}
