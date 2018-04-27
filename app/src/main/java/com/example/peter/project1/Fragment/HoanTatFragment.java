package com.example.peter.project1.Fragment;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.peter.project1.Adapter.adapter_rc_fragment_hoan_tat;
import com.example.peter.project1.Model.DonHang;
import com.example.peter.project1.Model.SanPham;
import com.example.peter.project1.Model.User;
import com.example.peter.project1.Model.UserData;
import com.example.peter.project1.R;
import com.facebook.FacebookSdk;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.peter.project1.Fragment.ThongTinFragment.MY_PREFS_NAME;
import static com.example.peter.project1.GioHangActivity.RemoveArraylistGiohang;
import static com.facebook.FacebookSdk.getApplicationContext;


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
    User userinfo;

    //DucNguyen Component

    DatabaseReference databaseReference;
    String idDonHang;

    //end DucNguyen Component
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
        tv_tongtien.setText(DinhDangTien(String.valueOf(CountTotalMoney()))+" VNĐ");

        rc_giohang_hoantat=v.findViewById(R.id.rc_giohang_fragment_hoan_tat);
        btn_hoantat=v.findViewById(R.id.btn_hoantat);
    }
    public void ClickEvent(){
        //btn_tiep_tuc_thong_tin CLick
        btn_hoantat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDonHang();

                sendDonHangToFirebase(); //Send To Firebase

                Toast.makeText(getContext(), "Cảm ơn quý khách đã đặt hàng", Toast.LENGTH_SHORT).show();
                RemoveArraylistGiohang();
                SavegioHang();
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
    {   userinfo = new User(user.getHoTen(),user.getSdt(),user.getDiachi(),user.getEmail(),user.getGhichu());
//        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
        txt_hoten.setText(user.getHoTen());
        txt_email.setText(user.getEmail());
        txt_diachi.setText(user.getDiachi());
        txt_ghichu.setText(user.getGhichu());
        txt_sdt.setText(user.getSdt());
    }
    public   String DinhDangTien(String chuoi){
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
    public void setDonHang(){
        DonHang donHang = new DonHang(arrayListGiohang,userinfo,userinfo.getGhichu());
        String jsonDonHang= new Gson().toJson(donHang);
        // Send Donhang
//        sendDonHangtoSever(jsonDonHang);
//        Toast.makeText(getContext(), ""+jsonDonHang, Toast.LENGTH_SHORT).show();
        Log.d("DucNguyen", jsonDonHang);
    }
    public void sendDonHangtoSever(final String jsonDonHang){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial
        String url = "https://app-1510482319.000webhostapp.com/datatoserver/json.php";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.d("AAA",response+"");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                _response.setText("That didn't work!");
                Log.d("AAA","That didn't work!");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Gửi JsonDOnhang voi key là donhang trùng với key get tren sever
                params.put("donhang",jsonDonHang);
                return params;

            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void SavegioHang(){
        arrayListGiohang.clear();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

            String json = gson.toJson(arrayListGiohang);
            editor.putString("arrayGioHang", json);
            editor.commit();

    }

    //DucNguyen Send DonHang to Firebase
    public void sendDonHangToFirebase(){
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("donhang");

            idDonHang = databaseReference.push().getKey();

//            DonHang donHang = new DonHang(idDonHang, arrayListGiohang,userinfo,userinfo.getGhichu());

            databaseReference.child(idDonHang).setValue(arrayListGiohang);

            databaseReference.child(idDonHang).child("khachhang").setValue(userinfo);

//            Toast.makeText(getApplicationContext(), "Data Don Hang Added", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e + "", Toast.LENGTH_SHORT).show();
        }
    }

}
