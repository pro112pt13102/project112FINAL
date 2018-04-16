package com.example.peter.project1.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.project1.ChiTietActivity;
import com.example.peter.project1.GioHangActivity;
import com.example.peter.project1.Model.SanPham;
import com.example.peter.project1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.example.peter.project1.GioHangActivity.showHideThanhToan;
import static com.example.peter.project1.GioHangActivity.tinhtong;
import static com.example.peter.project1.GioHangActivity.updateArraylistgiohang;

/**
 * Created by daovip on 3/27/2018.
 */

public class adapter_rc_gio_hang extends RecyclerView.Adapter<adapter_rc_gio_hang.View1SanPham> {
    ArrayList<SanPham> arrayList;
    Context c;
   Activity activity;
    static String chuoiEdit="";
    public adapter_rc_gio_hang(ArrayList<SanPham> arrayList, Context c, Activity activity) {
        this.arrayList = arrayList;
        this.c=c;
        this.activity=activity;
    }
    public void UpdateAraylistGiohang(ArrayList<SanPham> arrayList){
        this.arrayList=arrayList;
    }

    @Override
    public View1SanPham onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view1o_giohang,parent,false);
        return new View1SanPham(view);
    }

    @Override
    public void onBindViewHolder(final View1SanPham holder, final int position) {
        final String tenSp =arrayList.get(position).getTenSanPha();
        final int giaSp=arrayList.get(position).getDongia();
        final String hinhSp=arrayList.get(position).getHinh();
        final int soluong=arrayList.get(position).getSoluong();
        final int MaSp=arrayList.get(position).getMaSP();
        final String Loaisp=arrayList.get(position).getLoai();
        final int Madm=arrayList.get(position).getMaDm();
        final String Gioithieu=arrayList.get(position).getGioithieu();
//        final SanPham sp = new SanPham(tenSp,giaSp,hinhSp,soluong,MaSp,Loaisp);
        final SanPham sp = new SanPham(tenSp,giaSp,hinhSp,soluong,MaSp,Madm,Gioithieu,Loaisp);
        holder.et_soluong_giohang.setText(soluong+"");
        holder.tv_giasp_giohang.setText(giaSp+"");
        holder.tv_tensp_giohang.setText(tenSp);
        loadhinh(holder.img_hinhsp_giohang,hinhSp);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c,ChiTietActivity.class);
                i.putExtra("SanPham",sp);
                i.putExtra("key",2);
                startActivity(c,i,null);
            }
        });
        //
        holder.img_xoa_giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check Xoa  sản phẩm
                AlertDialogError(position);

            }
        });
        //  tăng giảm sản phẩm
        // Tang sản phẩm
        holder.viewTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=arrayList.get(position).getSoluong();
                    count ++;
                    holder.et_soluong_giohang.setText(count+"");
                    arrayList.get(position).setSoluong(count);
                     tinhtong();

            }
        });
        // Giảm sản phẩm
        holder.viewGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count =arrayList.get(position).getSoluong();
                if(count>1){
                    count--;
                    holder.et_soluong_giohang.setText(count+"");
                    arrayList.get(position).setSoluong(count);
                    tinhtong();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class View1SanPham extends RecyclerView.ViewHolder{
        public ImageView img_hinhsp_giohang,img_xoa_giohang;
        public TextView tv_tensp_giohang,tv_giasp_giohang,et_soluong_giohang;;
        public View viewTang,viewGiam;

        public View1SanPham(View itemView) {
            super(itemView);
            et_soluong_giohang=itemView.findViewById(R.id.et_soluong_giohang);
            img_hinhsp_giohang=itemView.findViewById(R.id.img_hinhsp_giohang);
            tv_tensp_giohang=itemView.findViewById(R.id.tv_tensp_giohang);
            tv_giasp_giohang=itemView.findViewById(R.id.tv_giasp_giohang);
            img_xoa_giohang=itemView.findViewById(R.id.img_xoa);
            viewGiam=itemView.findViewById(R.id.viewGiam);
            viewTang=itemView.findViewById(R.id.viewTang);
        }
    }

    public static String getEditSoluong(){
        return chuoiEdit;
    }
    public void loadhinh(ImageView img ,String hinh){
        Picasso.get()
                .load("http://immense-scrubland-98497.herokuapp.com/public/images/"+hinh)
                .centerCrop()
                .resize(200,200)
                .into(img);
    }
    public void AlertDialogError(final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);

        // set title
        alertDialogBuilder.setTitle("Thông báo");

        // set dialog message
        alertDialogBuilder
                .setMessage("Bạn có chắc chắn muốn xóa?")
                .setCancelable(false)
                .setPositiveButton("Đồng ý",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        arrayList.remove(position);
                        // updateArraylist to GioHangActivity
                        updateArraylistgiohang(arrayList);
                        tinhtong();
                        showHideThanhToan(arrayList.size());
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Hủy bỏ",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
