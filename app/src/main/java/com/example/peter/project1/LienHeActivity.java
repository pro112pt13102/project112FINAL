package com.example.peter.project1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class LienHeActivity extends AppCompatActivity {

    Button btn_map1, btn_map2, btn_map3, btn_map4, btn_map5, btn_call1, btn_call2, btn_call3, btn_call4, btn_call5;
    ImageButton img_btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lien_he);

        btn_map1=findViewById(R.id.button_map1);
        btn_map2=findViewById(R.id.button_map2);
        btn_map3=findViewById(R.id.button_map3);
        btn_map4=findViewById(R.id.button_map4);
        btn_map5=findViewById(R.id.button_map5);
        btn_call1=findViewById(R.id.button_call1);
        btn_call2=findViewById(R.id.button_call2);
        btn_call3=findViewById(R.id.button_call3);
        btn_call4=findViewById(R.id.button_call4);
        btn_call5=findViewById(R.id.button_call5);
        img_btn_back=findViewById(R.id.img_btn_back);

        img_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_map1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LienHeActivity.this, MapActivity.class);

                Bundle a=new Bundle();
                Bundle b=new Bundle();

                a.putDouble("Lat", 10.790856);
                b.putDouble("Lng", 106.682392);

                i.putExtras(a);
                i.putExtras(b);
                i.putExtra("chinhanh", "Chi nhánh 1");
                i.putExtra("diachi", "391A Nam Kỳ Khởi Nghĩa P.7 Q.3");

                startActivity(i);
            }
        });

        btn_map2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LienHeActivity.this, MapActivity.class);

                Bundle a=new Bundle();
                Bundle b=new Bundle();

                a.putDouble("Lat", 10.788227);
                b.putDouble("Lng", 106.685809);

                i.putExtras(a);
                i.putExtras(b);
                i.putExtra("chinhanh", "Chi nhánh 2");
                i.putExtra("diachi", "278 Nam Kỳ Khởi Nghĩa P.7 Q.3");

                startActivity(i);
            }
        });

        btn_map3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LienHeActivity.this, MapActivity.class);

                Bundle a=new Bundle();
                Bundle b=new Bundle();

                a.putDouble("Lat", 10.784460);
                b.putDouble("Lng", 106.677182);

                i.putExtras(a);
                i.putExtras(b);
                i.putExtra("chinhanh", "Chi nhánh 3");
                i.putExtra("diachi", "865/19/11 Hoàng Sa P.9 Q.3");

                startActivity(i);
            }
        });

        btn_map4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LienHeActivity.this, MapActivity.class);

                Bundle a=new Bundle();
                Bundle b=new Bundle();

                a.putDouble("Lat", 10.775833);
                b.putDouble("Lng", 106.684340);

                i.putExtras(a);
                i.putExtras(b);
                i.putExtra("chinhanh", "Chi nhánh 4");
                i.putExtra("diachi", "12b CMT8 P.Bến Thành Q.1");

                startActivity(i);
            }
        });

        btn_map5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LienHeActivity.this, MapActivity.class);

                Bundle a=new Bundle();
                Bundle b=new Bundle();

                a.putDouble("Lat", 10.716660);
                b.putDouble("Lng", 106.628114);

                i.putExtras(a);
                i.putExtras(b);
                i.putExtra("chinhanh", "Chi nhánh 5");
                i.putExtra("diachi", "318/28 Lưu Hữu Phước P.15 Q.8");

                startActivity(i);
            }
        });

        btn_call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:(+84)1204825860"));
                startActivity(i);
            }
        });

        btn_call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:(+84)1228088593"));
                startActivity(i);
            }
        });

        btn_call3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:(+84)903484902"));
                startActivity(i);
            }
        });

        btn_call4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:(+84)2838291292"));
                startActivity(i);
            }
        });

        btn_call5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:(+84)1205558789"));
                startActivity(i);
            }
        });

    }
}
