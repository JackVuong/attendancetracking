package com.plpnghi.attendancetrackingapplication;

/**
 * Created by GiaLuan on 12/22/2016.
 */
public class DiemDanh {
    private String maSV;
    private String maMH;
    private String nhomMH;
    private String toMH;
    private String namHoc;
    private String hocKy;
    private String ngayGio;

    public DiemDanh(String maSV, String maMH, String nhomMH, String toMH, String namHoc, String hocKy, String ngayGio){
        this.maSV = maSV;
        this.maMH = maMH;
        this.nhomMH = nhomMH;
        this.toMH = toMH;
        this.namHoc = namHoc;
        this.hocKy = hocKy;
        this.ngayGio = ngayGio;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getNhomMH() {
        return nhomMH;
    }

    public void setNhomMH(String nhomMH) {
        this.nhomMH = nhomMH;
    }

    public String getToMH() {
        return toMH;
    }

    public void setToMH(String toMH) {
        this.toMH = toMH;
    }

    public String getNamHoc() {
        return namHoc;
    }

    public void setNamHoc(String namHoc) {
        this.namHoc = namHoc;
    }

    public String getHocKy() {
        return hocKy;
    }

    public void setHocKy(String hocKy) {
        this.hocKy = hocKy;
    }

    public String getNgayGio() {
        return ngayGio;
    }

    public void setNgayGio(String ngayGio) {
        this.ngayGio = ngayGio;
    }
}
