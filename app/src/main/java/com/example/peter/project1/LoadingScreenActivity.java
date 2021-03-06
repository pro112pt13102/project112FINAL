package com.example.peter.project1;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.peter.project1.Model.SanPham;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadingScreenActivity extends AppCompatActivity {
    ImageView img_logo;
    TextView txt_app_name;
    ArrayList<SanPham> arrayListSanPhamSlideShow;
    ArrayList<SanPham> arrayListMonChinh;
    ArrayList<SanPham> arrayListMonAnVat;
    ArrayList<SanPham> arrayListThucUong;
    ArrayList<SanPham> arrayListComVanPhong;
    ArrayList<SanPham> arrayListCafe;
    ArrayList<SanPham> arrayListTraSua;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        Anhxa();
        //Animation
        loadData();
        Animation();
        Thread trangchu = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(500);
                    while (true){
//                        if (arrayListMonChinh.size()!=0 && arrayListMonAnVat.size()!=0 && arrayListThucUong.size()!=0 &&arrayListComVanPhong.size()!=0&& arrayListCafe.size()!=0 && arrayListTraSua.size()!=0 && arrayListSanPhamSlideShow.size()==6){
//                            Intent i = new Intent(LoadingScreenActivity.this,
//                                    TrangChuActivity.class);
//                            i.putExtra("MonChinh",arrayListMonChinh);
//                            i.putExtra("MonVat",arrayListMonAnVat);
//                            i.putExtra("ThucUong",arrayListThucUong);
//                            i.putExtra("SlideShow",arrayListSanPhamSlideShow);
//                            i.putExtra("ComVanPhong",arrayListComVanPhong);
//                            i.putExtra("Cafe",arrayListCafe);
//                            i.putExtra("TraSua",arrayListTraSua);
//                            startActivity(i);
//                            finish();
////                           Log.d("eee",arrayListSanPhamSlideShow.size()+"");
//                            break;
//                        }
                        if ( arrayListSanPhamSlideShow.size()==6){
                            Intent i = new Intent(LoadingScreenActivity.this,
                                    TrangChuActivity.class);
                            i.putExtra("MonChinh",arrayListMonChinh);
                            i.putExtra("MonVat",arrayListMonAnVat);
                            i.putExtra("ThucUong",arrayListThucUong);
                            i.putExtra("SlideShow",arrayListSanPhamSlideShow);
                            i.putExtra("ComVanPhong",arrayListComVanPhong);
                            i.putExtra("Cafe",arrayListCafe);
                            i.putExtra("TraSua",arrayListTraSua);
                            startActivity(i);
                            finish();
//                           Log.d("eee",arrayListSanPhamSlideShow.size()+"");
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
//                    Log.d("eee",arrayListSanPhamSlideShow.size()+"");
//                    Intent i = new Intent(LoadingScreenActivity.this,
//                            TrangChuActivity.class);
//                    i.putExtra("MonChinh",arrayListMonChinh);
//                    i.putExtra("MonVat",arrayListMonAnVat);
//                    i.putExtra("ThucUong",arrayListThucUong);
//                    i.putExtra("SlideShow",arrayListSanPhamSlideShow);
//                    i.putExtra("ComVanPhong",arrayListComVanPhong);
//                    i.putExtra("Cafe",arrayListCafe);
//                    i.putExtra("TraSua",arrayListTraSua);
//                    startActivity(i);
//                    finish();
                }
            }
        };
        trangchu.start();

    }

    public void Anhxa(){
        img_logo=findViewById(R.id.img_logo);
        txt_app_name=findViewById(R.id.txt_app_name);
        arrayListThucUong=new ArrayList<>();
        arrayListMonAnVat=new ArrayList<>();
        arrayListMonChinh =new ArrayList<>();
        arrayListSanPhamSlideShow=new ArrayList<>();
        arrayListCafe=new ArrayList<>();
        arrayListComVanPhong=new ArrayList<>();
        arrayListTraSua =new ArrayList<>();
    }
    public void changeMarginTopWithanimation1(final View v, int differentMargin) {
        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) v.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(params.topMargin, params.topMargin+differentMargin);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                params.topMargin = (Integer) valueAnimator.getAnimatedValue();
                v.requestLayout();
            }
        });
        animator.setDuration(500);
        animator.start();
    }
    public  void ChangeAnphaAnimation(final View view, float value){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,value);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });

        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }
    public void ScaleView(final View view, float value){
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", value);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", value);
        scaleDownX.setDuration(500);
        scaleDownY.setDuration(500);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);

        scaleDownX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                View p = (View) view.getParent();
                p.invalidate();
            }
        });
        scaleDown.start();

    }
    // LoadData
    public class ThreadLoadMonAn extends Thread{
        @Override
        public void run() {
            super.run();
            // Load dataMonChinh
            // Danh sách giảm dần
            String url="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-mon-an-theo-ma-loai&maloai=1&soluong=10";
            loadDataDoAn(url,arrayListMonChinh);
            // Load ComVanPhong
            // Danh sách giảm dần
            String url1="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-mon-an-theo-ma-loai&maloai=3&soluong=10";
            loadDataDoAn(url1,arrayListComVanPhong);
            // Load dataMonVat
            // Danh sách giảm dần
            String url2="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-mon-an-theo-ma-loai&maloai=2&soluong=10";
            loadDataDoAn(url2,arrayListMonAnVat);
            try {
                while (true){
//                    Thread.sleep(1500);
                    if(arrayListMonChinh.size()!=0 && arrayListComVanPhong.size()!=0 && arrayListMonAnVat.size()!=0){
                        arrayListSanPhamSlideShow.add(arrayListMonChinh.get(0));
                        arrayListSanPhamSlideShow.add(arrayListMonAnVat.get(0));
                        arrayListSanPhamSlideShow.add(arrayListComVanPhong.get(0));
//                        Log.d("eee",arrayListSanPhamSlideShow.size()+"");

                        break;
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public class ThreadLoadThucUong extends Thread{
        @Override
        public void run() {
            super.run();
            // Load dataThucUong
            // Danh sách giảm dần
            String url="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-thuc-uong-theo-ma-loai&maloai=3&soluong=10";
            loadDataDoUong(url,arrayListThucUong);
            // Load dataCafe
            // Danh sách giảm dần
            String url1="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-thuc-uong-theo-ma-loai&maloai=2&soluong=10";
            loadDataDoUong(url1,arrayListCafe);
            // Load dataTraSua
            // Danh sách giảm dần
            String url2="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-thuc-uong-theo-ma-loai&maloai=1&soluong=10";
            loadDataDoUong(url2,arrayListTraSua);
            try {
                while (true){
//                    Thread.sleep(1500);
                    if(arrayListThucUong.size()!=0 && arrayListCafe.size()!=0 && arrayListTraSua.size()!=0){
                        arrayListSanPhamSlideShow.add(arrayListCafe.get(0));
                        arrayListSanPhamSlideShow.add(arrayListTraSua.get(0));
                        arrayListSanPhamSlideShow.add(arrayListThucUong.get(0));
//                        Log.d("eee",arrayListSanPhamSlideShow.size()+"");
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    public void loadDataDoAn(String url, final ArrayList<SanPham> arrayList){
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(LoadingScreenActivity.this);

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
                            count ++;
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
    public void loadDataDoUong(String url, final ArrayList<SanPham> arrayList){
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(LoadingScreenActivity.this);

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
                            count ++;
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
    public void loadData(){
//        ThreadLoadMonAn threadLoadMonAn = new ThreadLoadMonAn();
//        threadLoadMonAn.start();
//
//        Thread threadDataThucuong = new ThreadLoadThucUong();
//        threadDataThucuong.start();
        threadLoadData threadLoadData = new threadLoadData();
        threadLoadData.start();
    }
    public void Animation(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // Scale size logo
                ScaleView(img_logo, (float) 2.2);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Change margin top of logo
//                    changeMarginWithanimation();
                changeMarginTopWithanimation1(img_logo,-40);
            }
        },800);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                changeMarginWithanimation();
                ChangeAnphaAnimation(txt_app_name,1);
//                changeMarginTopWithanimation();
                changeMarginTopWithanimation1(txt_app_name, 95);
            }
        },1000);
    }
    public class threadLoadData extends Thread  {
        @Override
        public void run() {
            super.run();
            // Load dataMonChinh
            // Danh sách giảm dần
            String url="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-mon-an-theo-ma-loai&maloai=1&soluong=10";
            loadDataDoAn(url,arrayListMonChinh);
            // Load ComVanPhong
            // Danh sách giảm dần
            String url1="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-mon-an-theo-ma-loai&maloai=3&soluong=10";
            loadDataDoAn(url1,arrayListComVanPhong);
            // Load dataMonVat
            // Danh sách giảm dần
            String url2="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-mon-an-theo-ma-loai&maloai=2&soluong=10";
            loadDataDoAn(url2,arrayListMonAnVat);
            // Load dataThucUong
            // Danh sách giảm dần
            String url3="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-thuc-uong-theo-ma-loai&maloai=3&soluong=10";
            loadDataDoUong(url3,arrayListThucUong);
            // Load dataCafe
            // Danh sách giảm dần
            String url4="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-thuc-uong-theo-ma-loai&maloai=2&soluong=10";
            loadDataDoUong(url4,arrayListCafe);
            // Load dataTraSua
            // Danh sách giảm dần
            String url5="https://immense-scrubland-98497.herokuapp.com/app.php?kihieu=danh-sach-thuc-uong-theo-ma-loai&maloai=1&soluong=10";
            loadDataDoUong(url5,arrayListTraSua);
            while (true){
                if(count==6){
                    //Món ăn
                    arrayListSanPhamSlideShow.add(arrayListMonChinh.get(0));
                    arrayListSanPhamSlideShow.add(arrayListMonAnVat.get(0));
                    arrayListSanPhamSlideShow.add(arrayListComVanPhong.get(0));
                    // Thức uống
                    arrayListSanPhamSlideShow.add(arrayListCafe.get(0));
                    arrayListSanPhamSlideShow.add(arrayListTraSua.get(0));
                    arrayListSanPhamSlideShow.add(arrayListThucUong.get(0));
                    break;
                }
            }
        }


    }
}
