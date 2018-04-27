package com.example.peter.project1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
    boolean chkUp=false;
    ArrayList<SanPham> arrayResponse;
    mHandler mHandler;
    int count =0;

    //DucNguyen SearchView Component

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private SimpleCursorAdapter myAdapter;

    //Object SanPhamSearchView
    public SanPham sanPhamSearchView;
    public ArrayList<SanPham> arrayListSanPhamSearchView = new ArrayList<SanPham>();

    SearchView searchView = null;
    private String[] strArrData = {"No Suggestions"};
    public ArrayList<String> listSearch = new ArrayList<String>();

    //test
    private String[] strArrDucNguyen;

    //Toolbar
    private Toolbar toolbar;

    //End DucNguyen SearchView Commponent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham);
        Anhxa();
        runSearchView(); //SearchView Here !!!!
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
//        mHandler =new mHandler();

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
        maSPCuoi=arrayListSanPham.get(arrayListSanPham.size()-1).getMaSP();
        maSpDau=arrayListSanPham.get(0).getMaSP();
//        Toast.makeText(this, "mã sản phẩm cuối: "+maSPCuoi, Toast.LENGTH_SHORT).show();
    }

    public void pulldownToRefresh() {
        arrayResponse.clear();
        swipe_refresh_layout.setColor(ContextCompat.getColor(SanPhamActivity.this, android.R.color.holo_red_dark));
        swipe_refresh_layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                Thread t = new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        setUrlRequestPullDown();
//                        while (true){
//                            if(arrayResponse.size()!=0){
//                                mHandler.sendEmptyMessage(1);
//                                break;
//                            }
//                        }
//                    }
//                };
//                t.start();
                setUrlRequestPullDown();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Add new Data
                        if(arrayResponse.get(0).getMaSP()!=-1){
                            Collections.reverse(arrayResponse);
                            for (int i = 0; i < arrayResponse.size(); i++) {
                                arrayListSanPham.add(i, arrayResponse.get(i));
                            }
                            maSpDau=arrayResponse.get(arrayResponse.size()-1).getMaSP();
                            adapter.notifyDataSetChanged();
                        }
                        swipe_refresh_layout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    public void PullUptoRefresh() {

        adapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                arrayResponse.clear();
//                    if(isEmpty!=-1){
//                        arrayListSanPham.add(null);
//                        arrayListSanPham.add(null);
//                        adapter.notifyItemInserted(arrayListSanPham.size() - 2);
//                        adapter.notifyItemInserted(arrayListSanPham.size() - 1);
//                        Thread t = new Thread(){
//                            @Override
//                            public void run() {
//                                super.run();
//                                setUrlRequestPullUp();
//                                while (true){
//                                    if(arrayResponse.size()!=0){
//                                        mHandler.sendEmptyMessage(0);
//                                        break;
//                                    }
//                                }
//                            }
//                        };
//                        t.start();
//                    }
                if(isEmpty!=-1){
                    setUrlRequestPullUp();
                    arrayListSanPham.add(null);
                    arrayListSanPham.add(null);
                    adapter.notifyItemInserted(arrayListSanPham.size() - 2);
                    adapter.notifyItemInserted(arrayListSanPham.size() - 1);
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           //Remove item loading
                           arrayListSanPham.remove(arrayListSanPham.size() - 2);
                           arrayListSanPham.remove(arrayListSanPham.size() - 1);
                           adapter.notifyItemRemoved(arrayListSanPham.size());
                            //Add new Data
                               if(arrayResponse.size()!=0){
                                   if(arrayResponse.get(0).getMaSP()!=-1){
                                       maSPCuoi=arrayResponse.get(arrayResponse.size()-1).getMaSP();
                                       arrayListSanPham.addAll(arrayResponse);
                                       adapter.notifyDataSetChanged();
//                                       for(int i=0;i<arrayResponse.size();i++){
//                                           Log.d("AAA",""+arrayResponse.get(i).getMaSP());
//                                       }
//                                       for(int i=0;i<arrayListSanPham.size();i++){
//                                           Log.d("111","Array San pham  "+arrayListSanPham.get(i).getMaSP() );
//                                       }
                                   }
                                   adapter.notifyDataSetChanged();
                                   adapter.setLoaded();
                               }
                       }
                   }, 4000);
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

        if (index == -1) {
            SanPham sp1=new SanPham(sp.getTenSanPha(),sp.getDongia(),sp.getHinh(),sp.getSoluong(),sp.getMaSP(),sp.getMaDm(),sp.getGioithieu(),sp.getLoai());
            arrayList_giohang.add(sp1);
//            Log.d("222",""+ sp1.getLoai());
            setNumberBadge(CountSizeArray(arrayList_giohang));
        } else {
            SanPham spInArrayList  =arrayList_giohang.get(index);
            int newSoluong = spInArrayList.getSoluong()+1;
            spInArrayList.setSoluong(newSoluong);
            setNumberBadge(CountSizeArray(arrayList_giohang));
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
//                            arrayResponse.clear();
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
//                        arrayResponse.clear();
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
//                        arrayResponse.clear();
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
//                        arrayResponse.clear();
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
            // Danh sách giảm dần
            String url="http://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-mon-an-theo-ma-loai-co-ma-nho-hon&maloai="+Madm+"&ma="+maSPCuoi+"&soluong=6";
            PullUploadDataDoAn(url);
            Log.d("AAA",""+url);
        }
        if(title.equalsIgnoreCase("Các Loại Khác")|| title.equalsIgnoreCase("Trà Sữa")||title.equalsIgnoreCase("Cafe")||title.equalsIgnoreCase("Thức uống")){
//            String url ="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-thuc-uong-co-ma-nho-hon&ma="+maSPCuoi+"&soluong=6";
            // Danh sách giảm dần
            String url="http://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-thuc-uong-theo-ma-loai-co-ma-nho-hon&maloai="+Madm+"&ma="+maSPCuoi+"&soluong=6";
            PullUploadDataDoUong(url);
        }
    }
    public void setUrlRequestPullDown(){
        if(title.equalsIgnoreCase("Món chính") || title.equalsIgnoreCase("Món Ăn Vặt")||title.equalsIgnoreCase("Cơm văn phòng")){
            // Danh sách tăng dần
            String url="http://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-mon-an-theo-ma-loai-co-ma-lon-hon&maloai="+Madm+"&ma="+maSpDau+"&soluong=6";
            PullDownloadDataDoAn(url);
        }
        if(title.equalsIgnoreCase("Các Loại Khác")|| title.equalsIgnoreCase("Trà Sữa")||title.equalsIgnoreCase("Cafe")||title.equalsIgnoreCase("Thức uống")){
            // Danh sách tăng dần
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
                        maSPCuoi=arrayResponse.get(arrayResponse.size()-1).getMaSP();
                        Collections.reverse(arrayResponse);
                        arrayListSanPham.addAll(arrayResponse);
                        adapter.notifyDataSetChanged();
//                     Toast.makeText(SanPhamActivity.this, ""+arrayResponse.size() +" -"+maSPCuoi, Toast.LENGTH_SHORT).show();
                        for(int i=0;i<arrayResponse.size();i++){
                            Log.d("AAA",""+arrayResponse.get(i).getMaSP());
                        }
                        for(int i=0;i<arrayListSanPham.size();i++){
                            Log.d("111","Array San pham  "+arrayListSanPham.get(i).getMaSP() );
                        }


                    }
//                    if(arrayResponse.get(0).getMaSP()!=-1){
//                        for(int i=0;i<arrayResponse.size();i++){
////                            Toast.makeText(SanPhamActivity.this, ""+arrayResponse.size(), Toast.LENGTH_SHORT).show();
//                            arrayListSanPham.add(arrayResponse.get(i));
//                        }
//                        maSPCuoi=arrayResponse.get(arrayResponse.size()-1).getMaSP();
//                        adapter.notifyDataSetChanged();
//                        for(int i=0;i<arrayResponse.size();i++){
//                            Log.d("AAA","Array Response"+arrayResponse.get(i).getMaSP());
//                        }
//                        for(int i=0;i<arrayResponse.size();i++){
//                            Log.d("111","Array San pham  "+arrayListSanPham.get(i).getMaSP());
//                        }
//                    }
                    adapter.setLoaded();
                case 1:
//                        Add more data
                    if(arrayResponse.get(0).getMaSP()!=-1){
                        Collections.reverse(arrayResponse);
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

    //DucNguyen SearchView Component

    public void runSearchView(){
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String[] from = new String[] {"fishName"};
        final int[] to = new int[] {android.R.id.text1};

        // setup SimpleCursorAdapter
        myAdapter = new SimpleCursorAdapter(SanPhamActivity.this, android.R.layout.simple_spinner_dropdown_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        // Fetch data from mysql table using AsyncTask
        new AsyncFetch().execute();
        new AsyncFetch1().execute();

        //test
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // adds item to action bar
        getMenuInflater().inflate(R.menu.search_main, menu);

        // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) SanPhamActivity.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SanPhamActivity.this.getComponentName()));
            searchView.setIconified(true);
            searchView.setSuggestionsAdapter(myAdapter);
            // Getting selected (clicked) item suggestion
            searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionClick(int position) {

                    // Add clicked text to search box
                    CursorAdapter ca = searchView.getSuggestionsAdapter();
                    Cursor cursor = ca.getCursor();
                    cursor.moveToPosition(position);
//                    searchView.setQuery(cursor.getString(cursor.getColumnIndex("fishName")),false);
                    searchView.setQuery(cursor.getString(cursor.getColumnIndex("fishName")),false);

                    //Toast Data
                    String temp2 = "";
                    String temp = cursor.getString(cursor.getColumnIndex("fishName"));
                    for (int i = 0 ; i < arrayListSanPhamSearchView.size(); i++){
                        if (temp.equalsIgnoreCase(String.valueOf(arrayListSanPhamSearchView.get(i).getTenSanPha()))){
//                            temp2 = arrayListSanPhamSearchView.get(i).getDongia()+"";

                            try {
                                SanPham sp = arrayListSanPhamSearchView.get(i);
                                Intent x = new Intent(SanPhamActivity.this,ChiTietActivity.class);
                                x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                x.putExtra("SanPham", sp);
                                startActivity(x);
                            } catch (Exception e) {
                                Log.d("ducnguyen", e+"");
                            }

                        }
                    }
//                    Toast.makeText(SanPhamActivity.this, temp2+"", Toast.LENGTH_SHORT).show();

                    return true;
                }

                @Override
                public boolean onSuggestionSelect(int position) {
                    return true;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    // Filter data
                    final MatrixCursor mc = new MatrixCursor(new String[]{ BaseColumns._ID, "fishName"});


//                    for (int i=0; i<strArrData.length; i++) {
//                        if (strArrData[i].toLowerCase().startsWith(s.toLowerCase()))
//                            mc.addRow(new Object[] {i, strArrData[i]});
//                    }
//                    myAdapter.changeCursor(mc);

                    for (int i = 0; i < listSearch.size(); i++){

                        if (listSearch.get(i).toString().toLowerCase().startsWith(s.toLowerCase())){
                            mc.addRow(new Object[] {i, listSearch.get(i).toString()});
                        }
                    }
                    myAdapter.changeCursor(mc);
                    return false;
                }
            });

            //onStart()
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    txt_loai.setVisibility(View.INVISIBLE);
                    img_btn_back.setVisibility(View.VISIBLE);
                    img_btn_giohang.setVisibility(View.INVISIBLE);
                    badge.setVisibility(View.INVISIBLE);
                }
            });

            //onClose()
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    txt_loai.setVisibility(View.VISIBLE);
                    img_btn_back.setVisibility(View.VISIBLE);
                    img_btn_giohang.setVisibility(View.VISIBLE);
                    badge.setVisibility(View.VISIBLE);
                    return false;
                }
            });

            //Change Button Search
            ImageView searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
            searchClose.setImageResource(R.drawable.ic_search_ducnguyen);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function
    @Override
    protected void onNewIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }

            // User entered text and pressed search button. Perform task ex: fetching data from database and display

        }
    }

    // Create class AsyncFetch

    private class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(SanPhamActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides or your JSON file address
//                url = new URL("https://app-1510482319.000webhostapp.com/testfish/fetch-all-fish.php");
                url = new URL("http://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-mon-an");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we receive data
                conn.setDoOutput(true);
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            ArrayList<String> dataList = new ArrayList<String>();

            //test Ducnguyen
            ArrayList<String> dataListDucNguyen = new ArrayList<String>();

            pdLoading.dismiss();


            if(result.equals("no rows")) {

                // Do some action if no data from database

            }else{

                try {

                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        dataList.add(json_data.getString("TenMA"));

                        //testDucNguyen
                        dataListDucNguyen.add(json_data.getString("MaMA"));

                        //add to listSearch
                        listSearch.add(json_data.getString("TenMA"));

                        int temp = json_data.getInt("MaMA");
                        String temp2 = json_data.getString("TenMA");
                        String temp3 = json_data.getString("GioiThieu");
                        int temp4 = json_data.getInt("Dongia");
                        String temp5 = json_data.getString("Anh");
                        int temp6 = json_data.getInt("MaDM");
                        sanPhamSearchView = new SanPham(temp2, temp4, temp5, 1, temp, Madm, temp3, "DoAn");
                        arrayListSanPhamSearchView.add(sanPhamSearchView);
                        //end test
                    }

                    strArrData = dataList.toArray(new String[dataList.size()]);

                    //test DucNguyen
                    strArrDucNguyen = dataListDucNguyen.toArray(new String[dataListDucNguyen.size()]);


                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
//                    Toast.makeText(SanPhamActivity.this, e.toString(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }

    private class AsyncFetch1 extends AsyncTask<String, String, String> {

//        ProgressDialog pdLoading = new ProgressDialog(SanPhamActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading...");
//            pdLoading.setCancelable(false);
//            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides or your JSON file address
//                url = new URL("https://app-1510482319.000webhostapp.com/testfish/fetch-all-fish.php");
                url = new URL("https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-thuc-uong");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we receive data
                conn.setDoOutput(true);
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            ArrayList<String> dataList = new ArrayList<String>();

            //test Ducnguyen
            ArrayList<String> dataListDucNguyen = new ArrayList<String>();

//            pdLoading.dismiss();


            if(result.equals("no rows")) {

                // Do some action if no data from database

            }else{

                try {

                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        dataList.add(json_data.getString("TenDU"));

                        //testDucNguyen
                        dataListDucNguyen.add(json_data.getString("MaDU"));

                        //add to listSearch
                        listSearch.add(json_data.getString("TenDU"));

                        int temp = json_data.getInt("MaDU");
                        String temp2 = json_data.getString("TenDU");
                        String temp3 = json_data.getString("GioiThieu");
                        int temp4 = json_data.getInt("Dongia");
                        String temp5 = json_data.getString("Anh");
                        int temp6 = json_data.getInt("MaDM");
                        sanPhamSearchView = new SanPham(temp2, temp4, temp5, 1, temp, Madm, temp3, "NuocUong");
                        arrayListSanPhamSearchView.add(sanPhamSearchView);
                        //end test
                    }

                    strArrData = dataList.toArray(new String[dataList.size()]);

                    //test DucNguyen
                    strArrDucNguyen = dataListDucNguyen.toArray(new String[dataListDucNguyen.size()]);


                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
//                    Toast.makeText(SanPhamActivity.this, e.toString(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }


    //End SearchView Component
}

