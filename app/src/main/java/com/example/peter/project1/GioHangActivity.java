package com.example.peter.project1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.peter.project1.Adapter.adapter_rc_gio_hang;
import com.example.peter.project1.Model.SanPham;
import com.example.peter.project1.Service.OnClearFromRecentService;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.peter.project1.Adapter.adapter_rc_gio_hang.getEditSoluong;


public class GioHangActivity extends AppCompatActivity {
    Button btn_thanh_toan;
    ImageButton img_btn_back_gio_hang;
    static RecyclerView rc_giohang;
    static ArrayList<SanPham> arrayList_giohang;
    static TextView tv_giasp_giohang;
    static LinearLayout v_thanh_toan_1;
    static ConstraintLayout v_thanh_toan_2;
    static com.example.peter.project1.Adapter.adapter_rc_gio_hang adapter_rc_gio_hang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        Anhxa();
        getDataFromSanPhamActivity();
//        setData();
        tinhtong();
        setUpRcGiohang();
        img_btn_back_gio_hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBackArrayGioHangEditSanPhamActivity();
            }
        });
        //Go to MuaNgay
        btn_thanh_toan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGiohangToMuaNgayActivity();
            }
        });
    }

    public void Anhxa() {
        v_thanh_toan_1=findViewById(R.id.v_thanh_toan_1);
        v_thanh_toan_2=findViewById(R.id.v_thanh_toan_2);
        btn_thanh_toan=findViewById(R.id.btn_thanh_toan);
        tv_giasp_giohang = findViewById(R.id.tv_giasp_giohang);
        img_btn_back_gio_hang = findViewById(R.id.img_btn_back_gio_hang);
        rc_giohang = findViewById(R.id.rc_giohang);
    }

    public void setUpRcGiohang() {
        adapter_rc_gio_hang = new adapter_rc_gio_hang(arrayList_giohang, GioHangActivity.this, GioHangActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rc_giohang.setLayoutManager(layoutManager);
        rc_giohang.setAdapter(adapter_rc_gio_hang);
    }

    public  static void  tinhtong() {
        int tong=0;
        for (int i = 0; i < arrayList_giohang.size(); i++) {
            int dongia=arrayList_giohang.get(i).getDongia();
            int soluong=arrayList_giohang.get(i).getSoluong();
            tong = tong + (dongia*soluong);
        }
        setTongTien(tong);
    }

       public void getDataFromSanPhamActivity() {
        arrayList_giohang = (ArrayList<SanPham>) getIntent().getSerializableExtra("arrayGioHang");
        showHideThanhToan(arrayList_giohang.size());
    }

    public static ArrayList<SanPham> getArryGioHang() {
        return arrayList_giohang;
    }
    public static void setTongTien(int tong){
        tv_giasp_giohang.setText( DinhDangTien(String.valueOf(tong))+ " VNĐ");
    }
    public  static void updateArraylistgiohang(ArrayList<SanPham> arrayList){
            arrayList_giohang=arrayList;
    }

    public void sendBackArrayGioHangEditSanPhamActivity(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("arrayListEdit",arrayList_giohang);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void sendGiohangToMuaNgayActivity(){
        Intent i = new Intent(GioHangActivity.this,MuaNgayActivity.class);
        i.putExtra("arrayListgiohang",arrayList_giohang);
        startActivity(i);
    }
    public static void RemoveArraylistGiohang(){
        arrayList_giohang.clear();
        adapter_rc_gio_hang.UpdateAraylistGiohang(arrayList_giohang);
        adapter_rc_gio_hang.notifyDataSetChanged();

    }
    public static void showHideThanhToan(int size){
        if(size!=0){
            v_thanh_toan_1.setVisibility(View.VISIBLE);
            v_thanh_toan_2.setVisibility(View.VISIBLE);
        }if(size==0){
            v_thanh_toan_1.setVisibility(View.INVISIBLE);
            v_thanh_toan_2.setVisibility(View.INVISIBLE);
        }

    }
    public  static String DinhDangTien(String chuoi){
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

