package com.example.peter.project1.Fragment;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.project1.Adapter.adapter_rc_fragment_hoan_tat;
import com.example.peter.project1.Model.SanPham;
import com.example.peter.project1.Model.User;
import com.example.peter.project1.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.peter.project1.Fragment.ThongTinFragment.MY_PREFS_NAME;
import static com.example.peter.project1.GioHangActivity.RemoveArraylistGiohang;




/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class HoanTatFragment extends android.support.v4.app.Fragment {
    Button btn_hoantat;
    TextView tv_tongtien,txt_hoten,txt_email,txt_diachi,txt_ghichu,txt_sdt;
    View v;
    ArrayList<SanPham> arrayListGiohang;
    RecyclerView rc_giohang_hoantat;
    @SuppressLint("ValidFragment")
    public HoanTatFragment(ArrayList<SanPham> arrayListGiohang) {
        // Required empty public constructor
        this.arrayListGiohang = arrayListGiohang;
    }

    public HoanTatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_hoan_tat, container, false);
        Anhxa();
        setUpRcGiohang();
        ClickEvent();
        //get DAta Userinfo;
        getUserInfo();
        return v;
    }
    public void Anhxa(){
        txt_hoten=v.findViewById(R.id.tv_tenkhachhang_hoantat);
        txt_email=v.findViewById(R.id.tv_email_hoantat);
        txt_diachi=v.findViewById(R.id.tv_diachi_hoantat);
        txt_ghichu=v.findViewById(R.id.tv_ghichu_hoantat);
        txt_sdt=v.findViewById(R.id.tv_sdt_hoantat);
        // set tổng tiền
        tv_tongtien=v.findViewById(R.id.tv_tongtien);
        tv_tongtien.setText(CountTotalMoney()+"");

        rc_giohang_hoantat=v.findViewById(R.id.rc_giohang_fragment_hoan_tat);
        btn_hoantat=v.findViewById(R.id.btn_hoantat);
    }
    public void ClickEvent(){
        //btn_tiep_tuc_thong_tin CLick
        btn_hoantat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Đã hoàn tất và gửi giỏ hàng lên sever", Toast.LENGTH_SHORT).show();
                RemoveArraylistGiohang();
                getActivity().finish();
            }
        });
    }
    public void setUpRcGiohang(){
        adapter_rc_fragment_hoan_tat adapter = new adapter_rc_fragment_hoan_tat(arrayListGiohang,getContext());
        rc_giohang_hoantat.setLayoutManager(new LinearLayoutManager(getContext()));
        rc_giohang_hoantat.setAdapter(adapter);
    }
    public  int CountTotalMoney() {
        int total=0;
        for (int i = 0; i < arrayListGiohang.size(); i++) {
            total=total+arrayListGiohang.get(i).getSoluong()*arrayListGiohang.get(i).getDongia();
        }
        return total;
    }
    public void getUserInfo(){
        SharedPreferences prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
            int idName = prefs.getInt("idName", 0); //0 is the default value.
            Log.d("demo",name+"-"+idName);
        }
    }
    public void displayReceivedData(User user)
    {
//        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
        txt_hoten.setText(user.getHoTen());
        txt_email.setText(user.getEmail());
        txt_diachi.setText(user.getDiachi());
        txt_ghichu.setText(user.getGhichu());
        txt_sdt.setText(user.getSdt());
    }

}
