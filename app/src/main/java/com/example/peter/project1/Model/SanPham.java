package com.example.peter.project1.Model;

import java.io.Serializable;

/**
 * Created by daovip on 3/27/2018.
 */

public class SanPham implements Serializable{
    private String tenSanPha;
    private int dongia;
    private String hinh;
    private int soluong;
    private int MaSP;
    private int MaDm;
    private String Gioithieu;
    private String loai;

    public void setMaDm(int maDm) {
        MaDm = maDm;
    }

    public void setGioithieu(String gioithieu) {
        Gioithieu = gioithieu;
    }

    public int getMaDm() {

        return MaDm;
    }

    public String getGioithieu() {
        return Gioithieu;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public SanPham(String tenSanPha, int dongia, String hinh, int soluong, int maSP, int maDm, String gioithieu) {
        this.tenSanPha = tenSanPha;
        this.dongia = dongia;
        this.hinh = hinh;
        this.soluong = soluong;
        MaSP = maSP;
        MaDm = maDm;
        Gioithieu = gioithieu;
    }

    public SanPham(String tenSanPha, int dongia, String hinh, int soluong, int maSP, int maDm, String gioithieu, String loai) {
        this.tenSanPha = tenSanPha;
        this.dongia = dongia;
        this.hinh = hinh;
        this.soluong = soluong;
        MaSP = maSP;
        MaDm = maDm;
        Gioithieu = gioithieu;
        this.loai = loai;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setMaSP(int maSP) {
        MaSP = maSP;
    }

    public int getMaSP() {
        return MaSP;

    }

    public void setSoluong(int soluong) {

        this.soluong = soluong;
    }

    public SanPham(String tenSanPha, int dongia, String hinh, int soluong, int MaSP) {
        this.tenSanPha = tenSanPha;
        this.dongia = dongia;
        this.hinh = hinh;
        this.soluong = soluong;
        this.MaSP=MaSP;
    }

    public SanPham(String tenSanPha, int dongia, String hinh) {
        this.tenSanPha = tenSanPha;
        this.dongia = dongia;
        this.hinh = hinh;
    }

    public SanPham() {
    }

    public String getTenSanPha() {
        return tenSanPha;
    }

    public int getDongia() {
        return dongia;
    }

    public String getHinh() {
        return hinh;
    }

    public void setTenSanPha(String tenSanPha) {
        this.tenSanPha = tenSanPha;
    }

    public void setDongia(int dongia) {
        this.dongia = dongia;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }
}
