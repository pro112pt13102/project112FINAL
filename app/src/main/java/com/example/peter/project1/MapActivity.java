package com.example.peter.project1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener{

    GoogleMap map;
    LocationManager locationManager;
    String mprovider;
    Location location;
    ImageButton img_btn_back;
    TextView tv_chinhanh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        img_btn_back=findViewById(R.id.img_btn_back);
        tv_chinhanh=findViewById(R.id.textView_chinhanh);

        img_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.mymap);
//        mapFragment.getMapAsync(this);

        initmap();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//bang M
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                xulyGPS();
                //  Toast.makeText(this, "e", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                //  Toast.makeText(this, "d", Toast.LENGTH_SHORT).show();
            }
        } else {
            xulyGPS();
            //  Toast.makeText(this, "f", Toast.LENGTH_SHORT).show();
        }
    }

    public void initmap()
    {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mymap);
        mapFragment.getMapAsync(this);
    }

    public void xulyGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        mprovider = locationManager.getBestProvider(criteria, false);

        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 1500, 1, this);
            //  Toast.makeText(this, "b", Toast.LENGTH_SHORT).show();
            if (location != null) {
                onLocationChanged(location);
                // Toast.makeText(this, "c", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);


        Bundle a = getIntent().getExtras();                         //Nhận data được gửi tương ứng từ LienHeActivity
        Bundle b = getIntent().getExtras();

        double lat = a.getDouble("Lat");
        double lng = b.getDouble("Lng");
        String chinhanh = getIntent().getStringExtra("chinhanh");
        String dc = getIntent().getStringExtra("diachi");
        tv_chinhanh.setText(chinhanh);

        LatLng pos = new LatLng(lat, lng);      //Thiết lập tọa độ marker


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 18));         //Thiết lập tầm nhìn trên map


        map.addMarker(new MarkerOptions()           //Tạo marker
                .title(chinhanh)
                .snippet(dc)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker))
                .position(pos))
                .showInfoWindow();      //tự động hiện infowindow
    }

    @Override
    public void onLocationChanged(Location location) {
        double lati=location.getLatitude();
        double lngi=location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override               //Xin quyền truy cập GPS
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Toast.makeText(this, "xx", Toast.LENGTH_SHORT).show();
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Đã xin quyền", Toast.LENGTH_SHORT).show();
            initmap();              //Khởi tạo map
            xulyGPS();              //Lấy vị trí người dụng hiện tại
        }
    }
}
