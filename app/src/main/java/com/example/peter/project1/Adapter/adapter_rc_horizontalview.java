package com.example.peter.project1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.project1.ChiTietActivity;
import com.example.peter.project1.Model.SanPham;
import com.example.peter.project1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by daovip on 4/3/2018.
 */

public class adapter_rc_horizontalview extends RecyclerView.Adapter<adapter_rc_horizontalview.View10> {
    ArrayList<SanPham> arrayList;
    Context c;
    public adapter_rc_horizontalview(ArrayList<SanPham> arrayList, Context c) {
        this.arrayList = arrayList;
        this.c=c;
    }

    @Override
    public View10 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view1o_trangchu,parent,false);
        return new View10(view);
    }

    @Override
    public void onBindViewHolder(View10 holder, final int position) {
        loadhinh(holder.imgV_hinhsp,arrayList.get(position).getHinh());
        holder.imgV_hinhsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c,ChiTietActivity.class);
                i.putExtra("SanPham",arrayList.get(position));
                i.putExtra("key",1);
                startActivity(c,i,null);
                Toast.makeText(c, ""+arrayList.get(position).getMaSP(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.tv_tensp.setText(LongNameWithDot(arrayList.get(position).getTenSanPha()));
        holder.tv_giasp.setText(DinhDangTien(String.valueOf(arrayList.get(position).getDongia()))+" VNĐ");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class View10 extends RecyclerView.ViewHolder{
        ImageView imgV_hinhsp;
        TextView tv_tensp,tv_giasp;
        public View10(View itemView) {
            super(itemView);
            imgV_hinhsp=itemView.findViewById(R.id.imgV_hinhsp);
            tv_tensp=itemView.findViewById(R.id.tv_tensp);
            tv_giasp=itemView.findViewById(R.id.tv_giasp);
        }
    }
    public void loadhinh(ImageView img,String hinh){
        String url1="http://immense-scrubland-98497.herokuapp.com/public/images/"+hinh;
        String url2="https://firebasestorage.googleapis.com/v0/b/finalloginproject112.appspot.com/o/dogImages%2Fga-tay-nuong.jpg?alt=media&token=437441a5-b248-406d-9a48-7f072e83bd17";//        Picasso.get()
//                .load(url1)
//                .centerCrop()
//                .resize(200,200)
//                .into(img);
//        Picasso.get()
//                .load(url)
//                .centerCrop()
//                .resize(200,200)
//                .into(img);
        Picasso.get()
                .load(url2)
                .resize(200,200)
                .error(R.drawable.ic_launcher_background)
                .into(img);

    }
    public String LongNameWithDot(String tenSp){
        if(tenSp.trim().length()>18){
            String aferCutsString=tenSp.substring(0,15)+"...";
            tenSp=aferCutsString;
        }
        return tenSp;
    }
    public  String DinhDangTien(String chuoi){
        String chuoiso="";
        //khoản ký tự
        int size=0;
        // Tạo mảng chứa chuỗi con
        ArrayList<String> arrChuoi = new ArrayList();
        // đão ký tự chuổi
        String chuoidao= new StringBuilder(chuoi).reverse().toString();
        // định dạng chuỗi
        if(chuoi.length()>=4){
            while(true){
                size+=3;
                if(chuoi.length()-size<1){
                    String chuoicuoi="";
                    if(size-chuoi.length()==2){
                        chuoicuoi=String.valueOf(chuoidao.charAt(chuoi.length()-1));
                    }if(size-chuoi.length()==1){
                        chuoicuoi=String.valueOf(chuoidao.charAt(chuoi.length()-2))+String.valueOf(chuoidao.charAt(chuoi.length()-1));
                    }if(size-chuoi.length()==0){
                        chuoicuoi=String.valueOf(chuoidao.charAt(chuoi.length()-3))+String.valueOf(chuoidao.charAt(chuoi.length()-2))+String.valueOf(chuoidao.charAt(chuoi.length()-1));
                    }
                    arrChuoi.add(new StringBuilder(chuoicuoi).reverse().toString());
                    break;
                }else{
                    String chuoicon=chuoidao.substring(size-3, size)+".";
                    arrChuoi.add(new StringBuilder(chuoicon).reverse().toString());
                }

            }
        }
        // Nối chuỗi
        Collections.reverse(arrChuoi);
        for(int i=0;i<arrChuoi.size();i++){
            chuoiso=chuoiso+arrChuoi.get(i);

        }
        return chuoiso;
    }
}
