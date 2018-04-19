package com.example.peter.project1.Model;

public class UserData {
    public String mId;
    public String mHoTen;
    public String msdt;
    public String mdiachi;
    public String memail;
    public String mghichu;

    public UserData() {
    }

    public UserData(String mHoTen, String msdt, String mdiachi, String memail, String mghichu) {
        this.mHoTen = mHoTen;
        this.msdt = msdt;
        this.mdiachi = mdiachi;
        this.memail = memail;
        this.mghichu = mghichu;
    }

    public UserData(String mId, String mHoTen, String msdt, String mdiachi, String memail, String mghichu) {
        this.mId = mId;
        this.mHoTen = mHoTen;
        this.msdt = msdt;
        this.mdiachi = mdiachi;
        this.memail = memail;
        this.mghichu = mghichu;
    }
}
