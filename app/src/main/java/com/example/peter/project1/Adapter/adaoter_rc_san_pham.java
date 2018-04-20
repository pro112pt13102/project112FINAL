package com.example.peter.project1.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.project1.ChiTietActivity;
import com.example.peter.project1.Interface.ILoadMore;
import com.example.peter.project1.Model.SanPham;
import com.example.peter.project1.R;
import com.eyalbira.loadingdots.LoadingDots;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import static android.support.v4.content.ContextCompat.startActivity;
import static com.example.peter.project1.SanPhamActivity.Addgiohang;
import static com.example.peter.project1.SanPhamActivity.getArrayListGiohang;

/**
 * Created by daovip on 3/22/2018.
 */
class LoadingViewHoder extends RecyclerView.ViewHolder{
    public LoadingDots progressBar;
    public LoadingViewHoder(View itemView) {
        super(itemView);
        progressBar=itemView.findViewById(R.id.progressBar);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder{
    public ImageButton btn_giohang_sanpham;
    public ImageView img;
    public TextView txt_ten,txt_gia;
    public ItemViewHolder(View itemView) {
        super(itemView);
        btn_giohang_sanpham=itemView.findViewById(R.id.btn_giohang_sanpham);
        img=itemView.findViewById(R.id.imgV_hinhsp_sanpham);
        txt_ten=itemView.findViewById(R.id.tv_tensp_sanpham);
        txt_gia=itemView.findViewById(R.id.tv_giasp_sanpham);


    }
}

public class adaoter_rc_san_pham extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int VIEW_TYPE_ITEM=0, VIEW_TYPE_LOADING=1;
    ILoadMore iLoadMore;
    boolean isLoading;
    Activity activity;
    ArrayList<SanPham> arrayList;
    int visibleThreshold =5;
    int lastVisibleItem,totlItemCout;
    int size;
    public  void updateArralist(ArrayList<SanPham> arrayList){
        this.arrayList=arrayList;
    }
    public adaoter_rc_san_pham(RecyclerView recyclerView, final Activity activity, ArrayList<SanPham> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;


        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totlItemCout=linearLayoutManager.getItemCount();
                lastVisibleItem=linearLayoutManager.findLastVisibleItemPosition();
                if(!isLoading && totlItemCout<=(lastVisibleItem+visibleThreshold)){
                    if(iLoadMore !=null)
                        iLoadMore.onLoadMore();
                    isLoading=true;
                }

            }
        });
    }
    //Press ctrl+0


    @Override
    public int getItemViewType(int position) {
        return arrayList.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    public void setLoadMore (ILoadMore loadMore){
        this.iLoadMore=loadMore;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType== VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.view1o_sanpham,parent,false);
            return new ItemViewHolder(view);
        }else if (viewType == VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHoder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        size=getItemCount();
        if(holder instanceof ItemViewHolder){
            final SanPham sp;
             sp = arrayList.get(position);
             String tenSp =sp.getTenSanPha();
             //
            //
             int giaSp=sp.getDongia();
             String hinhSp=sp.getHinh();
            ((ItemViewHolder) holder).txt_gia.setText(DinhDangTien(String.valueOf(giaSp))+" VNĐ");
            ((ItemViewHolder) holder).txt_ten.setText(LongNameWithDot(tenSp));
            //load hinh
//            ((ItemViewHolder) holder).img.setImageResource(R.drawable.garan);
            loadhinh(((ItemViewHolder) holder).img,arrayList.get(position).getHinh());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(activity.getApplicationContext(),ChiTietActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("SanPham",sp);
                    startActivity(activity.getApplicationContext(),i,null);
                }
            });
            ((ItemViewHolder) holder).btn_giohang_sanpham.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Add item to arraylisGioHang
                   Addgiohang(sp);
                   //
                   ArrayList<SanPham> arrayListGiohang= getArrayListGiohang();
                   // Save gio hang
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(((ItemViewHolder) holder).btn_giohang_sanpham.getContext());
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(arrayListGiohang);
                    editor.putString("arrayGioHang", json);
                    editor.commit();

               }
          });
        }else if (holder instanceof LoadingViewHoder){
            LoadingViewHoder loadingViewHoder = (LoadingViewHoder)holder;
            loadingViewHoder.progressBar.setAutoPlay(true);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }
    public void loadhinh(ImageView img ,String hinh){
        String url="https://drive.google.com/uc?id=1FtZdHDo2O58feOotQPNYtqcDMZOthaSU";
        String url1="http://immense-scrubland-98497.herokuapp.com/public/images/"+hinh;
        Picasso.get()
                .load(url1)
                .centerCrop()
                .resize(200,200)
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
