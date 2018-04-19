package com.example.peter.project1.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.bumptech.glide.Glide;
import com.example.peter.project1.LoginActivity;
import com.example.peter.project1.Model.User;
import com.example.peter.project1.Model.UserData;
import com.example.peter.project1.R;
import com.example.peter.project1.TrangChuActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.peter.project1.MuaNgayActivity.setCurrentPage;

import static com.example.peter.project1.MuaNgayActivity.setCurrentStateTwo;
import static com.example.peter.project1.TrangChuActivity.USER_DATABASE;


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

    //DucNguyen Login Component

    public FirebaseUser userFireBase;
    public GoogleApiClient mGoogleApiClient;
    FirebaseAuth mAuth;

    String dataIdUser, dataNameUser, dataEmailUser;
    String emailToCheck;

    DatabaseReference databaseReference;
    boolean checkUserDuplicate = false;
    ArrayList<UserData> listData = new ArrayList<UserData>();

    SharedPreferences sharedPreferences;

    //End DucNguyen Component

    public ThongTinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_thong_tin, container, false);
        Anhxa();
        ClickEvent();

        //DucNguyen Component
        initGoogle();
        addDataToForm();

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

                //DucNguyen Component
                updateUserdataToFirebase();
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

    //DucNguyen Component

    private void initGoogle() {
        mAuth = FirebaseAuth.getInstance();
        //------------GSO----------------

        try {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mGoogleApiClient.connect();
        } catch (Exception e) {

        }

        //---------------------------------

        userFireBase = mAuth.getCurrentUser();

        //get user data

        try {
            if (userFireBase != null) {
                dataNameUser = userFireBase.getDisplayName();
                dataEmailUser = userFireBase.getEmail();
            }else {
                //
            }
        } catch (Exception e) {

        }
    }

    //Check Duplicate UserData To Firebase Realtime Database
    public boolean checkDuplicateLoginDataToFirebase(){

//        try {
//            databaseReference = FirebaseDatabase.getInstance().getReference("users");
//
//            String id = databaseReference.push().getKey();
//
//            User user = new User(dataNameUser, "", "", dataEmailUser, "");
//
//            databaseReference.child(id).setValue(user);
//
//            Toast.makeText(this, "Data User Added", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Toast.makeText(this, e+"", Toast.LENGTH_SHORT).show();
//        }
        if (dataNameUser != null && dataEmailUser != null){

            DatabaseReference database = FirebaseDatabase.getInstance().getReference();

            database.child("users").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    UserData userData = dataSnapshot.getValue(UserData.class);

//                    if (userData.memail.equals(dataEmailUser)==true){
//                        checkUserDuplicate = true;
////                        Toast.makeText(getApplicationContext(), "Duplicate !!!! "+checkUserDuplicate+"", Toast.LENGTH_SHORT).show();
//                    }

                    //add to arraylist listData
                    listData.add(userData);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        return checkUserDuplicate;
    }

    public void updateUserdataToFirebase(){

//        Toast.makeText(getApplicationContext(), checkUserDuplicate+"", Toast.LENGTH_SHORT).show();

        if (dataNameUser != null && dataEmailUser != null) {

            if (checkUserDuplicate==true){
                //khong lam gi het
            }else {
                try {
                    if (dataIdUser != "" && dataIdUser != null) {

                        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(dataIdUser);

                        UserData user = new UserData(dataIdUser, dataNameUser, user_sdt, user_diachi, dataEmailUser, user_ghichu);

                        databaseReference.setValue(user);

                        Toast.makeText(getContext(), "Data User Updated", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), e + "", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public void addDataToForm(){

        if (dataNameUser != null && dataEmailUser != null) {
//            for (int i = 0; i < listData.size(); i++){
//                if (dataEmailUser.equals(listData.get(i).memail)){
//                    et_hoten_thongtin.setText(listData.get(i).mHoTen.toString());
//                    et_email_thongtin.setText(listData.get(i).memail.toString());
//                }
//            }

            sharedPreferences = this.getActivity().getSharedPreferences(USER_DATABASE, Context.MODE_PRIVATE);

            emailToCheck = sharedPreferences.getString("memail", "");
            dataIdUser = sharedPreferences.getString("mID", "");

            if (emailToCheck.equals(dataEmailUser)) {

                et_hoten_thongtin.setText(sharedPreferences.getString("mHoTen", ""));
                et_sdt_thongtin.setText(sharedPreferences.getString("msdt", ""));
                et_diachi_thongtin.setText(sharedPreferences.getString("mdiachi", ""));
                et_email_thongtin.setText(sharedPreferences.getString("memail", ""));
                et_ghichu_thongtin.setText(sharedPreferences.getString("mghichu", ""));
            }else {
                et_hoten_thongtin.setText(dataNameUser);
                et_email_thongtin.setText(dataEmailUser);
            }

        }
    }
}


