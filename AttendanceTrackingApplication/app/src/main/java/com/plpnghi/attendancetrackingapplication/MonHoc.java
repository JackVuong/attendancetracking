package com.plpnghi.attendancetrackingapplication;

/**
 * Created by GiaLuan on 12/26/2016.
 */
public class MonHoc {
    private String MaMH;
    private String TenMH;
    private int nhom;
    private int to;
    private String TKB;
    private String NamHoc;
    private String HocKy;
    private boolean CT;
    public MonHoc()
    {}
    public MonHoc(String ma,String ten,int nhom,int to)
    {
        this.MaMH = ma;
        this.TenMH = ten;
        this.nhom = nhom;
        this.to = to;
    }

    public int getNhom() {
        return nhom;
    }

    public void setNhom(int nhom) {
        this.nhom = nhom;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
    public String getMaMH() {
        return this.MaMH;
    }

    public void setMaMH(String MaMH) {
        this.MaMH = MaMH;
    }

    public String getTenMH() {
        return this.TenMH;
    }

    public void setTenMH(String ten) {
        this.TenMH = ten;
    }
}
