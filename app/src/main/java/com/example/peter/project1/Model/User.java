package com.example.peter.project1.Model;

/**
 * Created by daovip on 4/3/2018.
 */

public class User {
    private String HoTen;
    private String sdt;
    private String diachi;
    private String email;
    private String ghichu;

    public User(String hoTen, String sdt, String diachi, String email, String ghichu) {
        HoTen = hoTen;
        this.sdt = sdt;
        this.diachi = diachi;
        this.email = email;
        this.ghichu = ghichu;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getHoTen() {
        return HoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public String getDiachi() {
        return diachi;
    }

    public String getEmail() {
        return email;
    }

    public String getGhichu() {
        return ghichu;
    }
}