package com.example.peter.project1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peter.project1.Model.SanPham;
import com.example.peter.project1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by daovip on 4/1/2018.
 */

public class adapter_rc_fragment_hoan_tat extends RecyclerView.Adapter<adapter_rc_fragment_hoan_tat.View1SanPham> {
    ArrayList<SanPham> arrayList;
    Context c;

    public adapter_rc_fragment_hoan_tat(ArrayList<SanPham> arrayList, Context c) {
        this.arrayList = arrayList;
        this.c = c;
    }

    @Override
    public View1SanPham onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view1o_fragment_hoantat,parent,false);
        return new View1SanPham(view);
    }

    @Override
    public void onBindViewHolder(View1SanPham holder, int position) {
        SanPham sp=arrayList.get(position);
//        holder.img_hinh_sp.setImageResource(sp.getHinh());
        loadhinh(holder.img_hinh_sp,arrayList.get(position).getHinh());
        holder.txt_gia_sp.setText(sp.getDongia()+"");
        holder.txt_soluong_sp.setText(sp.getSoluong()+"");
        holder.txt_ten_sp.setText(sp.getTenSanPha());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class View1SanPham extends RecyclerView.ViewHolder{
        public ImageView img_hinh_sp;
        public TextView txt_ten_sp,txt_gia_sp,txt_soluong_sp;
        public View1SanPham(View itemView) {
            super(itemView);
            img_hinh_sp=itemView.findViewById(R.id.View_hinhsp_giohang);
            txt_ten_sp=itemView.findViewById(R.id.tv_tensp_giohang);
            txt_gia_sp=itemView.findViewById(R.id.tv_dongiasp_hoantat);
            txt_soluong_sp=itemView.findViewById(R.id.tv_soluong_hoantat);
        }
    }
    public void loadhinh(ImageView img ,String hinh){
        Picasso.get()
                .load("http://immense-scrubland-98497.herokuapp.com/public/images/"+hinh)
                .centerCrop()
                .resize(200,200)
                .into(img);
    }
}
