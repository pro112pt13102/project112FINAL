package com.example.peter.project1.Model;

import java.util.ArrayList;

/**
 * Created by daovip on 4/21/2018.
 */

public class DonHang {
    ArrayList<SanPham> arrayGiohang;
    User userinfo;
    String ghichu;

    public DonHang(ArrayList<SanPham> arrayGiohang, User userinfo, String ghichu) {
        this.arrayGiohang = arrayGiohang;
        this.userinfo = userinfo;
        this.ghichu = ghichu;
    }
}
