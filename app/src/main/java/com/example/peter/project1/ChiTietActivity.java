package com.example.peter.project1;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.peter.project1.Adapter.adapter_rc_horizontalview;
import com.example.peter.project1.Model.SanPham;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static com.example.peter.project1.SanPhamActivity.Addgiohang;
import static com.example.peter.project1.SanPhamActivity.arrayList_giohang;

public class ChiTietActivity extends AppCompatActivity {
    SanPham sp;
    TextView tv_xemthem;
    LinearLayout linearLayout_xemthem;
    TextView giaSp,tenSp,tv_noidungxemthem;
    ImageView imgHinhSp;
    ImageButton img_back_chitiet;
    ImageView img_giohang_chi_tiet;
    ArrayList<SanPham> arrayListSpTuongTu;
    int key=0;
    RecyclerView rc_horizontal_chitiet;
    Thread t;
    mHandler mHandler;
    boolean isAdd=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        Anhxa();
        getData1Sanpham();
        mHandler =new mHandler();
        // load Data
         t=new Thread(){
            @Override
            public void run() {
                super.run();
                LoadArraylistSanphamTuongtu();
                try {
                    while (true){
                        Thread.sleep(1500);
                        if(arrayList_giohang.size()!=0 ){
                            break;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    mHandler.sendEmptyMessage(0);
                }

            }
        };
        t.start();
        onClickEvent();



    }
    public void getData1Sanpham(){
        key=getIntent().getIntExtra("key",0);
        sp= (SanPham) getIntent().getSerializableExtra("SanPham");
        giaSp.setText(sp.getDongia()+"");
        tenSp.setText(sp.getTenSanPha());
        tv_noidungxemthem.setText(sp.getGioithieu());
//        imgHinhSp.setImageResource(sp.getHinh());
        loadhinh(imgHinhSp,sp.getHinh());
    }
    public  void Anhxa(){
        rc_horizontal_chitiet=findViewById(R.id.rc_horizontal_chitiet);
        arrayListSpTuongTu=new ArrayList<>();
        tv_noidungxemthem=findViewById(R.id.tv_noidungxemthem);
        img_giohang_chi_tiet=findViewById(R.id.img_giohang_chi_tiet);
        img_back_chitiet=findViewById(R.id.img_btn_back_chitiet);
        linearLayout_xemthem = findViewById(R.id.xemthem);
        tv_xemthem = findViewById(R.id.tv_noidungxemthem);
        tv_xemthem.setVisibility(View.INVISIBLE);
        giaSp=findViewById(R.id.tv_giasp_chitiet);
        tenSp=findViewById(R.id.tv_tensp_chitiet);
        imgHinhSp=findViewById(R.id.img_hinhsp_chitiet);
    }
    public void onClickEvent(){
        linearLayout_xemthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_xemthem.getVisibility() == View.VISIBLE){
                    tv_xemthem.setVisibility(View.GONE);
                } else tv_xemthem.setVisibility(View.VISIBLE);
            }
        });

        //onclick img_back_chitiet
        img_back_chitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // onclick img_giohang_chi_tiet
        img_giohang_chi_tiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(key==0){
                    Addgiohang(sp);
                    //Save gio hang
                    loadGioHang();
                    SavegioHang(sp);
                }if (key==2){
                    showToastTy("Sản phẩm đã cò trong giỏ hàng");
                }


            }
        });
    }
    public void showToastTy(String msg){
        Toasty.Config.getInstance()
                .setTextColor(Color.parseColor("#FFF1DD2F"))
                .setSuccessColor(Color.parseColor("#FFF11616"))
                .apply();
        Toasty.success(ChiTietActivity.this,msg).show();
    }
    public void loadhinh(ImageView img ,String hinh){
        Picasso.get()
                .load("http://immense-scrubland-98497.herokuapp.com/public/images/"+hinh)
                .centerCrop()
                .resize(200,200)
                .into(img);
    }
    public void setUpRcHorizontal(){
        // del intance sanpham
        for(int i=0;i<arrayListSpTuongTu.size();i++){
            if(arrayListSpTuongTu.get(i).getMaSP()==sp.getMaSP()){
                arrayListSpTuongTu.remove(i);
                break;
            }
        }

        adapter_rc_horizontalview adapter_rc_horizontalview = new adapter_rc_horizontalview(arrayListSpTuongTu,ChiTietActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        rc_horizontal_chitiet.setLayoutManager(layoutManager);
        rc_horizontal_chitiet.setAdapter(adapter_rc_horizontalview);
    }
    public void LoadArraylistSanphamTuongtu(){
        int maDm =sp.getMaDm();
        String loai=sp.getLoai();
        if(loai.equalsIgnoreCase("NuocUong")){
            String url ="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-thuc-uong-theo-ma-loai&maloai="+maDm+"&soluong=5";
            loadDataDoUong(url,arrayListSpTuongTu);
        }if (loai.equalsIgnoreCase("DoAn")){
            String url ="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-mon-an-theo-ma-loai&maloai="+maDm+"&soluong=5";
            loadDataDoAn(url,arrayListSpTuongTu);
        }



    }
    public void loadDataDoUong(String url, final ArrayList<SanPham> arrayList){
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(ChiTietActivity.this);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject MonAn = response.getJSONObject(i);
                                // Get the current student (json object) data
                                int MaMa = MonAn.getInt("MaDU");
                                String TenMA = MonAn.getString("TenDU");
                                String GioiThieu = MonAn.getString("GioiThieu");
                                int Dongia= MonAn.getInt("Dongia");
                                String Anh = MonAn.getString("Anh");
                                int maDM = MonAn.getInt("MaDM");
                                SanPham monAn = new SanPham(TenMA,Dongia,Anh,1,MaMa,maDM,GioiThieu,"NuocUong");

                                arrayList.add(monAn);

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Log.d("BBB",error+"");
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }
    public void loadDataDoAn(String url, final ArrayList<SanPham> arrayList){
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(ChiTietActivity.this);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject MonAn = response.getJSONObject(i);
                                // Get the current student (json object) data
                                int MaMa = MonAn.getInt("MaMA");
                                String TenMA = MonAn.getString("TenMA");
                                String GioiThieu = MonAn.getString("GioiThieu");
                                int Dongia= MonAn.getInt("Dongia");
                                String Anh = MonAn.getString("Anh");
                                int maDM = MonAn.getInt("MaDM");
                                SanPham monAn = new SanPham(TenMA,Dongia,Anh,1,MaMa,maDM,GioiThieu,"DoAn");

                                arrayList.add(monAn);

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Log.d("BBB",error+"");
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }
    public void SavegioHang(SanPham sp){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        if(arrayList_giohang.size()!=0){
            for(int i=0;i<arrayList_giohang.size();i++){
                if(sp.getMaSP()==arrayList_giohang.get(i).getMaSP()){
                    showToastTy("Sản phẫm đã có trong giỏ hàng");
                    isAdd=false;
                    break;
                }
            }if(isAdd==true){
                arrayList_giohang.add(sp);
                String json = gson.toJson(arrayList_giohang);
                editor.putString("arrayGioHang", json);
                editor.commit();
                showToastTy("Đã thêm sản phẩm vào giỏ hàng");
            }

        }else {
            arrayList_giohang.add(sp);
            String json = gson.toJson(arrayList_giohang);
            editor.putString("arrayGioHang", json);
            editor.commit();
        }

    }

    public void loadGioHang() {
        arrayList_giohang = new ArrayList<>();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ChiTietActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("arrayGioHang", null);
        if(json!=null){
            Type type = new TypeToken<ArrayList<SanPham>>() {}.getType();
            arrayList_giohang = gson.fromJson(json, type);
        }

    }
    public class mHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    setUpRcHorizontal();
            }
        }
    }
}
