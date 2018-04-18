package com.example.peter.project1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.peter.project1.Adapter.AdapterSlideShow;
import com.example.peter.project1.Adapter.adapter_rc_horizontalview;
import com.example.peter.project1.Model.SanPham;
import com.example.peter.project1.Model.User;
import com.example.peter.project1.Model.UserData;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;


public class TrangChuActivity extends AppCompatActivity {
    ViewPager viewPager_slideshow;
    ImageButton imgb;
    DrawerLayout dw;
    NavigationView navigationView;
    TextView tv_xemtatca_mon_chinh,tv_xemtatca_mon_vat,tv_xemtatca_thuc_uong;
    RecyclerView rc_MonChinh,rc_Mon_Vat,rc_Thuc_uong;
    CircleIndicator circleIndicator;
    ArrayList<SanPham> arrayListSanPhamSlideShow;
    ArrayList<SanPham> arrayListMonChinh;
    ArrayList<SanPham> arrayListMonAnVat;
    ArrayList<SanPham> arrayListThucUong;
    ArrayList<SanPham> arrayListComVanPhong;
    ArrayList<SanPham> arrayListCafe;
    ArrayList<SanPham> arrayListTraSua;

    //Thanh Phan Login Google Ducnguyen
    public FirebaseUser user;
    public GoogleApiClient mGoogleApiClient;
    FirebaseAuth mAuth;

    ImageView imageViewAvatarGoogle;
    TextView textViewNameGoogle;
    //Ket Thuc Thanh Phan Login Google

    //User Data Post To Server DucNguyen

    String dataNameUser, dataEmailUser;
    DatabaseReference databaseReference;
    boolean checkUserDuplicate = false;
    ArrayList<UserData> listData = new ArrayList<UserData>();

    //End..

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trangchu);
        AnhXa();
       //get DAta from loading Screen
        getData();
        setUpAdapter();
        imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dw.openDrawer(GravityCompat.START);
            }
        });
        // Show all Món chính
        tv_xemtatca_mon_chinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TrangChuActivity.this,SanPhamActivity.class);
                i.putExtra("LOAI","Món chính");
                i.putExtra("DsMonChinh",arrayListMonChinh);
                startActivity(i);
            }
        });
        // Show all Món vặt
        tv_xemtatca_mon_vat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TrangChuActivity.this,SanPhamActivity.class);
                i.putExtra("LOAI","Món Ăn Vặt");
                i.putExtra("DsMonVat",arrayListMonAnVat);
                startActivity(i);
            }
        });
        // Show all Thức uống
        tv_xemtatca_thuc_uong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TrangChuActivity.this,SanPhamActivity.class);
                i.putExtra("LOAI","Thức uống");
                i.putExtra("DsThucUong",arrayListThucUong);
                startActivity(i);
            }
        });
        //Onclick Menu navigation
        onClicMenu();

        //LoginGoogle
        initGoogle();

        //Send User Data Login To Firebase
        startSendData();
    }
    public void AnhXa(){
        navigationView=findViewById(R.id.nav_view);
        arrayListThucUong=new ArrayList<>();
        arrayListMonAnVat=new ArrayList<>();
        arrayListMonChinh =new ArrayList<>();
        arrayListSanPhamSlideShow=new ArrayList<>();
        arrayListCafe=new ArrayList<>();
        arrayListComVanPhong=new ArrayList<>();
        arrayListTraSua =new ArrayList<>();
        rc_MonChinh=findViewById(R.id.rc_mon_chinh);
        rc_Mon_Vat=findViewById(R.id.rc_mon_van_vat);
        rc_Thuc_uong=findViewById(R.id.rc_thuc_uong);
        imgb = findViewById(R.id.img_btn_menu);
        dw = findViewById(R.id.drawer_layout);
        tv_xemtatca_mon_chinh = findViewById(R.id.tv_xemtatca_mon_chinh);
        tv_xemtatca_mon_vat = findViewById(R.id.tv_xemtatca_mon_vat);
        tv_xemtatca_thuc_uong = findViewById(R.id.tv_xemtatca_thuc_uong);
        viewPager_slideshow=findViewById(R.id.vp_slideshow);
        circleIndicator=findViewById(R.id.indicator_default);
    }
    public void getData(){
        arrayListMonChinh= (ArrayList<SanPham>) getIntent().getSerializableExtra("MonChinh");
        arrayListMonAnVat= (ArrayList<SanPham>) getIntent().getSerializableExtra("MonVat");
        arrayListThucUong= (ArrayList<SanPham>) getIntent().getSerializableExtra("ThucUong");
        arrayListSanPhamSlideShow= (ArrayList<SanPham>) getIntent().getSerializableExtra("SlideShow");
        arrayListComVanPhong= (ArrayList<SanPham>) getIntent().getSerializableExtra("ComVanPhong");
        arrayListCafe= (ArrayList<SanPham>) getIntent().getSerializableExtra("Cafe");
        arrayListTraSua= (ArrayList<SanPham>) getIntent().getSerializableExtra("TraSua");
    }
    public void setUpCircleIndicator(){
            FragmentPagerAdapter adapter= new AdapterSlideShow(getSupportFragmentManager(),arrayListSanPhamSlideShow);
            viewPager_slideshow.setAdapter(adapter);
            circleIndicator.setViewPager(viewPager_slideshow);

    }
    public void onClicMenu(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.monchinh:
                        Intent i1 = new Intent(TrangChuActivity.this,SanPhamActivity.class);
                        i1.putExtra("LOAI","Món chính");
                        i1.putExtra("DsMonChinh",arrayListMonChinh);
                        startActivity(i1);
                        break;
                    case R.id.monanvat:
                        Intent i2 = new Intent(TrangChuActivity.this,SanPhamActivity.class);
                        i2.putExtra("LOAI","Món Ăn Vặt");
                        i2.putExtra("DsMonVat",arrayListMonAnVat);
                        startActivity(i2);
                        break;
                    case R.id.trasua:
                        Intent i3 = new Intent(TrangChuActivity.this,SanPhamActivity.class);
                        i3.putExtra("LOAI","Trà Sữa");
                        i3.putExtra("TraSua",arrayListTraSua);
                        startActivity(i3);
                        break;
                    case R.id.cafe:
                        Intent i4 = new Intent(TrangChuActivity.this,SanPhamActivity.class);
                        i4.putExtra("LOAI","Cafe");
                        i4.putExtra("Cafe",arrayListCafe);
                        startActivity(i4);
                        break;
                    case R.id.khac:
                        Intent i5 = new Intent(TrangChuActivity.this,SanPhamActivity.class);
                        i5.putExtra("LOAI","Các Loại Khác");
                        i5.putExtra("ThucUong",arrayListThucUong);
                        startActivity(i5);
                        break;
                    case R.id.comvanphong:
                        Intent i6 = new Intent(TrangChuActivity.this,SanPhamActivity.class);
                        i6.putExtra("LOAI","Cơm văn phòng");
                        i6.putExtra("ComVanPhong",arrayListComVanPhong);
                        startActivity(i6);
                        break;
                    case R.id.thoat:
                        System.exit(0);
                        break;
                    case R.id.lienhe:
                        Intent i7 = new Intent(TrangChuActivity.this,LienHeActivity.class);
                        startActivity(i7);
                        break;
                }
                return false;
            }
        });
    }
    public void setUpRcMonChinh(){
        adapter_rc_horizontalview adapterMonChinh = new adapter_rc_horizontalview(arrayListMonChinh,TrangChuActivity.this);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rc_MonChinh.setLayoutManager(layoutManager);
        rc_MonChinh.setAdapter(adapterMonChinh);
    }
    public void setUpRcMonVat(){
        adapter_rc_horizontalview adapterMonChinh = new adapter_rc_horizontalview(arrayListMonAnVat,TrangChuActivity.this);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rc_Mon_Vat.setLayoutManager(layoutManager);
        rc_Mon_Vat.setAdapter(adapterMonChinh);
    }
    public void setUpRcThucUong(){
        adapter_rc_horizontalview adapterMonChinh = new adapter_rc_horizontalview(arrayListThucUong,TrangChuActivity.this);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rc_Thuc_uong.setLayoutManager(layoutManager);
        rc_Thuc_uong.setAdapter(adapterMonChinh);
    }
    public void setUpAdapter(){
        setUpRcMonChinh();
        setUpRcMonVat();
        setUpRcThucUong();
        setUpCircleIndicator();
    }

    private void initGoogle() {
        mAuth = FirebaseAuth.getInstance();

        //Get imageview and textview header
        View view = navigationView.getHeaderView(0);
        imageViewAvatarGoogle = (ImageView) view.findViewById(R.id.imgV_hinhuser);
        textViewNameGoogle = (TextView) view.findViewById(R.id.tv_tenuser);

        //------------GSO----------------

        try {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mGoogleApiClient.connect();
        } catch (Exception e) {
            Toast.makeText(this, e+"", Toast.LENGTH_SHORT).show();
        }

        //---------------------------------

        user = mAuth.getCurrentUser();

        //get user data

        try {
            if (user != null) {
                dataNameUser = user.getDisplayName();
                dataEmailUser = user.getEmail();

                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(imageViewAvatarGoogle);

                textViewNameGoogle.setText(user.getDisplayName());
            }else {
                imageViewAvatarGoogle.setImageResource(R.drawable.logo);
                textViewNameGoogle.setText("Hello");
            }
        } catch (Exception e) {
            Toast.makeText(this, e+"", Toast.LENGTH_SHORT).show();
        }

        //Su kien Logout
        imageViewAvatarGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewAvatarGoogle.setImageResource(R.drawable.logo);
                textViewNameGoogle.setText("Hello");

                //Sign-out
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // ...
                                Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                        });

//                FirebaseAuth.getInstance().signOut();

            }
        });
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

                    if (userData.memail.equals(dataEmailUser)==true){
                        checkUserDuplicate = true;
//                        Toast.makeText(getApplicationContext(), "Duplicate !!!! "+checkUserDuplicate+"", Toast.LENGTH_SHORT).show();
                    }

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

    //Send UserData To Firebase Realtime Database
    public void saveUserdataToFirebase(){

//        Toast.makeText(getApplicationContext(), checkUserDuplicate+"", Toast.LENGTH_SHORT).show();
        if (dataNameUser != null && dataEmailUser != null) {

            if (checkUserDuplicate==true){
                //khong lam gi het
            }else {
                try {
                    databaseReference = FirebaseDatabase.getInstance().getReference("users");

                    String id = databaseReference.push().getKey();

                    UserData user = new UserData(dataNameUser, "", "", dataEmailUser, "");

                    databaseReference.child(id).setValue(user);

                    Toast.makeText(getApplicationContext(), "Data User Added", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e + "", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public void startSendData(){

        checkDuplicateLoginDataToFirebase();

        final CountDownTimer countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long l) {
                Toast.makeText(TrangChuActivity.this, "Please Wait ...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                saveUserdataToFirebase();
            }
        };
        countDownTimer.start();

    }
}
