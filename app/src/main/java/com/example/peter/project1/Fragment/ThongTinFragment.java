package com.example.peter.project1.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.andreabaccega.widget.FormEditText;
import com.example.peter.project1.Model.User;
import com.example.peter.project1.R;

import static com.example.peter.project1.MuaNgayActivity.setCurrentPage;

import static com.example.peter.project1.MuaNgayActivity.setCurrentStateTwo;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThongTinFragment extends android.support.v4.app.Fragment {
    Button btn_tiep_tuc_thong_tin;
    FormEditText et_hoten_thongtin, et_sdt_thongtin, et_diachi_thongtin, et_email_thongtin;
    EditText  et_ghichu_thongtin;
    View v;
    String user_ten, user_email, user_diachi, user_sdt, user_ghichu;
    SendData sendData;
    User user;
    public static final String MY_PREFS_NAME = "USERINFO";

    public ThongTinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_thong_tin, container, false);
        Anhxa();
        ClickEvent();
        return v;
    }

    public void Anhxa() {

        btn_tiep_tuc_thong_tin=v.findViewById(R.id.btn_tiep_tuc_thong_tin);
        et_hoten_thongtin = v.findViewById(R.id.et_hoten_thongtin);
        et_sdt_thongtin = v.findViewById(R.id.et_sdt_thongtin);
        et_diachi_thongtin = v.findViewById(R.id.et_diachi_thongtin);
        et_email_thongtin = v.findViewById(R.id.et_email_thongtin);
        et_ghichu_thongtin = v.findViewById(R.id.et_ghichu_thongtin);
    }

    public void ClickEvent() {
        //btn_tiep_tuc_thong_tin CLick
        btn_tiep_tuc_thong_tin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });
    }

    public void getUserInput() {
        user_ten = et_hoten_thongtin.getText().toString();
        user_diachi = et_diachi_thongtin.getText().toString();
        user_email = et_email_thongtin.getText().toString();
        user_sdt = et_sdt_thongtin.getText().toString();
        user_ghichu = et_ghichu_thongtin.getText().toString();
        user= new User(user_ten,user_sdt,user_diachi,user_email,user_ghichu);

    }

    public void Validation() {
        FormEditText[] allFields	= { et_hoten_thongtin,et_sdt_thongtin, et_diachi_thongtin,et_email_thongtin};
        boolean allValid = true;
        for (FormEditText field: allFields) {
            allValid = field.testValidity() && allValid;
        }
        if (allValid) {
            setCurrentPage(1);
            setCurrentStateTwo();
            getUserInput();
            sendData.sendData(user);
        } else {

        }
    }

    public interface  SendData{
        void sendData(User user);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sendData= (SendData) getActivity();

        }catch (ClassCastException e){
            throw  new ClassCastException("Must be implment ThongTinFragment");
        }
    }
}


