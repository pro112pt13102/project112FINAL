package com.example.peter.project1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.example.peter.project1.Adapter.adaoter_rc_san_pham;
import com.example.peter.project1.CustomView.Badge;
import com.example.peter.project1.Interface.ILoadMore;
import com.example.peter.project1.Model.SanPham;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;


public class SanPhamActivity extends AppCompatActivity {
    static Badge badge;
    RecyclerView rc_san_pham;
    ArrayList<SanPham> arrayListSanPham;
    ImageButton img_btn_back;
    ImageButton img_btn_giohang;
    adaoter_rc_san_pham adapter;
    PullRefreshLayout swipe_refresh_layout;
    static ArrayList<SanPham> arrayList_giohang;
    TextView txt_loai;
    String title;
    int maSPCuoi;
    int maSpDau;
    int sizeOfArrayrespone=1;
    int Madm;
    int isEmpty=0;
    ArrayList<SanPham> arrayResponse;
    mHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham);
        Anhxa();
        getDataSanPham();
        loadGioHang();
        setNumberBadge(CountSizeArray(arrayList_giohang));

        //set up recycleView
//        rc_san_pham.setLayoutManager(new GridLayoutManager(SanPhamActivity.this,2));
        // Sữa lỗi java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder
        // https://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in
        rc_san_pham.setLayoutManager(new WrapContentGridLayoutManager(SanPhamActivity.this,2));
        adapter = new adaoter_rc_san_pham(rc_san_pham, this, arrayListSanPham);
        rc_san_pham.setAdapter(adapter);
        pulldownToRefresh();
        PullUptoRefresh();


        //onclick btn back
        img_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // go to giohang
        img_btn_giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendGioHangtoGiohangActivity();
            }
        });
        //
        mHandler =new mHandler();

    }

    public void Anhxa() {
        txt_loai=findViewById(R.id.txt_loai);
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        arrayListSanPham = new ArrayList<>();
        arrayResponse = new ArrayList<>();
        img_btn_giohang = findViewById(R.id.img_btn_giohang);
        rc_san_pham = findViewById(R.id.rc_san_pham);
        img_btn_back = findViewById(R.id.img_btn_back);
        badge = findViewById(R.id.badge);
    }

    public void getDataSanPham() {
        // Set Title
         title = getIntent().getStringExtra("LOAI").trim();
        txt_loai.setText(title);
        if(title.equalsIgnoreCase("Món chính")){
            arrayListSanPham= (ArrayList<SanPham>) getIntent().getSerializableExtra("DsMonChinh");
            Madm=1;
        }
        if(title.equalsIgnoreCase("Món Ăn Vặt")){
            arrayListSanPham= (ArrayList<SanPham>) getIntent().getSerializableExtra("DsMonVat");
            Madm=2;
        }
        if(title.equalsIgnoreCase("Thức uống")){
            arrayListSanPham= (ArrayList<SanPham>) getIntent().getSerializableExtra("DsThucUong");
            Madm=3;
        }
        if(title.equalsIgnoreCase("Các Loại Khác")){
            arrayListSanPham= (ArrayList<SanPham>) getIntent().getSerializableExtra("ThucUong");
            Madm=3;
        }
        if(title.equalsIgnoreCase("Trà Sữa")){
            arrayListSanPham= (ArrayList<SanPham>) getIntent().getSerializableExtra("TraSua");
            Madm=1;
        }
        if(title.equalsIgnoreCase("Cafe")){
            arrayListSanPham= (ArrayList<SanPham>) getIntent().getSerializableExtra("Cafe");
            Madm=2;
        }
        if(title.equalsIgnoreCase("Cơm văn phòng")){
            arrayListSanPham= (ArrayList<SanPham>) getIntent().getSerializableExtra("ComVanPhong");
            Madm=3;
        }
        Collections.reverse(arrayListSanPham);
        maSPCuoi=arrayListSanPham.get(arrayListSanPham.size()-1).getMaSP();
        maSpDau=arrayListSanPham.get(0).getMaSP();
    }

    public void pulldownToRefresh() {
        swipe_refresh_layout.setColor(ContextCompat.getColor(SanPhamActivity.this, android.R.color.holo_red_dark));
        swipe_refresh_layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Thread t = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        setUrlRequestPullDown();
                        while (true){
                            if(arrayResponse.size()!=0){
                                mHandler.sendEmptyMessage(1);
                                break;
                            }
                        }
                    }
                };
                t.start();

            }
        });
    }

    public void PullUptoRefresh() {
        adapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                Log.d("AAA","emty"+isEmpty+"Ma dm"+Madm);
                    if(isEmpty!=-1){
                        arrayListSanPham.add(null);
                        arrayListSanPham.add(null);
                        adapter.notifyItemInserted(arrayListSanPham.size() - 2);
                        adapter.notifyItemInserted(arrayListSanPham.size() - 1);
                        Thread t = new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                setUrlRequestPullUp();
                                while (true){
                                    if(arrayResponse.size()!=0){
                                        mHandler.sendEmptyMessage(0);
                                        break;
                                    }
                                }
                            }
                        };
                        t.start();
                    }
            }
        });
    }
    public void SavegioHang(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(arrayList_giohang);

        editor.putString("arrayGioHang", json);
        editor.commit();
    }

    public void loadGioHang() {
        arrayList_giohang = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            SanPham sp = new SanPham("sp" + i, 5 + i * 10, url, 1, i + 1);
//            arrayList_giohang.add(sp);
//        }
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SanPhamActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("arrayGioHang", null);
        if(json!=null){
            Type type = new TypeToken<ArrayList<SanPham>>() {}.getType();
            arrayList_giohang = gson.fromJson(json, type);
        }

    }

    public static void setNumberBadge(int size) {

        Log.d("BADGE", size + "");
        badge.setNumber(size);
    }

    public static void Addgiohang(SanPham sp) {
        int index = CheckSanphamTrung(sp);
        Log.d("222",""+ sp.getTenSanPha());
        if (index == -1) {
            SanPham sp1=new SanPham(sp.getTenSanPha(),sp.getDongia(),sp.getHinh(),sp.getSoluong(),sp.getMaSP());
            arrayList_giohang.add(sp1);
            Log.d("111","deo trung"+sp.getTenSanPha());
            setNumberBadge(CountSizeArray(arrayList_giohang));
        } else {
            SanPham spInArrayList  =arrayList_giohang.get(index);
            int newSoluong = spInArrayList.getSoluong()+1;
            spInArrayList.setSoluong(newSoluong);
            setNumberBadge(CountSizeArray(arrayList_giohang));
            Log.d("AAA","true");
        }
    }

    public void sendGioHangtoGiohangActivity() {
        Intent i = new Intent(SanPhamActivity.this, GioHangActivity.class);
        i.putExtra("arrayGioHang", arrayList_giohang);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                arrayList_giohang = (ArrayList<SanPham>) data.getSerializableExtra("arrayListEdit");
                setNumberBadge(CountSizeArray(arrayList_giohang));
                SavegioHang();
            }
            SavegioHang();
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    public static int CheckSanphamTrung(SanPham sp) {

        for (int i = 0; i < arrayList_giohang.size(); i++) {
            if (sp.getMaSP() == arrayList_giohang.get(i).getMaSP()) {
                return i;
            }
        }
        return -1;
    }

    public static int CountSizeArray(ArrayList<SanPham> arrayList) {
        int size=0;
        for (int i = 0; i < arrayList.size(); i++) {
            size=size+arrayList.get(i).getSoluong();
        }
     return size;
    }
    public void PullUploadDataDoAn(String url){
        // Initialize a new RequestQueue instance
        final RequestQueue requestQueue = Volley.newRequestQueue(SanPhamActivity.this);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
//                        sizeOfArrayrespone=response.length();
                        try{
                            // Loop through the array elements
                            arrayResponse.clear();
                            SanPham monAn=null;
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject MonAn = response.getJSONObject(i);
                                // Get the current student (json object) data
                                int MaMa = MonAn.getInt("MaMA");
                                 if(MaMa==-1){
                                    isEmpty=MaMa;
                                     monAn = new SanPham("",0,"",0,MaMa,0,"","");
                                 }if(MaMa!=-1){
                                    String TenMA = MonAn.getString("TenMA");
                                    String GioiThieu = MonAn.getString("GioiThieu");
                                    int Dongia= MonAn.getInt("Dongia");
                                    String Anh = MonAn.getString("Anh");
                                    int maDM = MonAn.getInt("MaDM");
                                   monAn = new SanPham(TenMA,Dongia,Anh,1,MaMa,maDM,GioiThieu,"DoAn");
                                }

                                arrayResponse.add(monAn);


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
    public void PullUploadDataDoUong(String url){
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(SanPhamActivity.this);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        arrayResponse.clear();
                        try{
                            SanPham monAn=null;
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject MonAn = response.getJSONObject(i);
                                // Get the current student (json object) data
                                int MaMa = MonAn.getInt("MaDU");
                               if (MaMa==-1){
                                    isEmpty=MaMa;
                                   monAn = new SanPham("",0,"",0,MaMa,0,"","");
                                }if (MaMa!=-1){
                                    String TenMA = MonAn.getString("TenDU");
                                    String GioiThieu = MonAn.getString("GioiThieu");
                                    int Dongia= MonAn.getInt("Dongia");
                                    String Anh = MonAn.getString("Anh");
                                    int maDM = MonAn.getInt("MaDM");
                                     monAn = new SanPham(TenMA,Dongia,Anh,1,MaMa,maDM,GioiThieu,"NuocUong");
                                }
                                arrayResponse.add(monAn);
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
    public void PullDownloadDataDoAn(String url){
        // Initialize a new RequestQueue instance
        final RequestQueue requestQueue = Volley.newRequestQueue(SanPhamActivity.this);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        arrayResponse.clear();
                        try{
                            SanPham monAn = null;
                            // Loop through the array elements
                            arrayResponse.clear();
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject MonAn = response.getJSONObject(i);
                                // Get the current student (json object) data
                                int MaMa = MonAn.getInt("MaMA");
                                if(MaMa==-1){
                                    monAn = new SanPham("",0,"",0,MaMa,0,"","");
                                }if(MaMa!=-1){
                                    String TenMA = MonAn.getString("TenMA");
                                    String GioiThieu = MonAn.getString("GioiThieu");
                                    int Dongia= MonAn.getInt("Dongia");
                                    String Anh = MonAn.getString("Anh");
                                    int maDM = MonAn.getInt("MaDM");
                                     monAn = new SanPham(TenMA,Dongia,Anh,1,MaMa,maDM,GioiThieu,"DoAn");
                                }


                                arrayResponse.add(monAn);

                            }
                            Collections.reverse(arrayResponse);
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
    public void PullDownloadDataDoUong(String url){
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(SanPhamActivity.this);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        arrayResponse.clear();
                        try{
                            SanPham monAn = null;
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject MonAn = response.getJSONObject(i);
                                // Get the current student (json object) data
                                int MaMa = MonAn.getInt("MaDU");
                                if(MaMa ==-1){
                                     monAn = new SanPham("",0,"",0,MaMa,0,"","");

                                }if(MaMa !=-1){
                                    String TenMA = MonAn.getString("TenDU");
                                    String GioiThieu = MonAn.getString("GioiThieu");
                                    int Dongia= MonAn.getInt("Dongia");
                                    String Anh = MonAn.getString("Anh");
                                    int maDM = MonAn.getInt("MaDM");
                                     monAn = new SanPham(TenMA,Dongia,Anh,1,MaMa,maDM,GioiThieu,"NuocUong");
                                }

                                arrayResponse.add(monAn);

                            }
                            Collections.reverse(arrayResponse);
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
    public void setUrlRequestPullUp(){
        if(title.equalsIgnoreCase("Món chính") || title.equalsIgnoreCase("Món Ăn Vặt")||title.equalsIgnoreCase("Cơm văn phòng")){
//            String url ="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-mon-an-co-ma-nho-hon&ma="+maSPCuoi+"&soluong=6";
            String url="http://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-mon-an-theo-ma-loai-co-ma-nho-hon&maloai="+Madm+"&ma="+maSPCuoi+"&soluong=6";
            PullUploadDataDoAn(url);
        }
        if(title.equalsIgnoreCase("Các Loại Khác")|| title.equalsIgnoreCase("Trà Sữa")||title.equalsIgnoreCase("Cafe")){
//            String url ="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-thuc-uong-co-ma-nho-hon&ma="+maSPCuoi+"&soluong=6";
            String url="http://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-thuc-uong-theo-ma-loai-co-ma-nho-hon&maloai="+Madm+"&ma="+maSPCuoi+"&soluong=6";

            PullUploadDataDoUong(url);
        }
    }
    public void setUrlRequestPullDown(){
        if(title.equalsIgnoreCase("Món chính") || title.equalsIgnoreCase("Món Ăn Vặt")||title.equalsIgnoreCase("Cơm văn phòng")){
            String url="http://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-mon-an-theo-ma-loai-co-ma-lon-hon&maloai="+Madm+"&ma="+maSpDau+"&soluong=6";
            PullDownloadDataDoAn(url);
        }
        if(title.equalsIgnoreCase("Các Loại Khác")|| title.equalsIgnoreCase("Trà Sữa")||title.equalsIgnoreCase("Cafe")){
            String url="http://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-thuc-uong-theo-ma-loai-co-ma-lon-hon&maloai="+Madm+"&ma="+maSpDau+"&soluong=6";

            PullDownloadDataDoUong(url);
        }
    }
    public static ArrayList<SanPham> getArrayListGiohang(){
        return arrayList_giohang;
    }

    public void searchView(){

    }
    public  class mHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    arrayListSanPham.remove(arrayListSanPham.size() - 2);
                    arrayListSanPham.remove(arrayListSanPham.size() - 1);
                    adapter.notifyItemRemoved(arrayListSanPham.size());
//                               Add more data
                    if(arrayResponse.get(0).getMaSP()!=-1){
                        arrayListSanPham.addAll(arrayResponse);
                        maSPCuoi=arrayListSanPham.get(arrayListSanPham.size()-1).getMaSP();
                        adapter.notifyDataSetChanged();
                    }
                    adapter.setLoaded();
                case 1:
//                        Add more data
                    if(arrayResponse.get(0).getMaSP()!=-1){
                        for (int i = 0; i < arrayResponse.size(); i++) {
                            arrayListSanPham.add(i, arrayResponse.get(i));
                        }
                        maSpDau=arrayListSanPham.get(0).getMaSP();
                        adapter.notifyDataSetChanged();
                    }
                    swipe_refresh_layout.setRefreshing(false);
            }
        }
    }
}

