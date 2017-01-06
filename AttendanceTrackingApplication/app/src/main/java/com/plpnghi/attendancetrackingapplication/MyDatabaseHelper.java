package com.plpnghi.attendancetrackingapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GiaLuan on 12/26/2016.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";


    // Phiên bản
    private static final int DATABASE_VERSION = 1;


    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "MH_Manager";


    // Tên bảng: MH.
    private static final String TABLE_MH = "MonHoc";

    private static final String COLUMN_MH_ID ="MaMH";
    private static final String COLUMN_Ten ="TenMH";
    private static final String COLUMN_Nhom = "Nhom";
    private static final String COLUMN_To = "To";
    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Script tạo bảng.
        String script = "create table MonHoc(MaMH text primary key,TenMH text,Nhom integer,To integer)";
//        String script = "CREATE TABLE " + TABLE_MH + "("
//                + COLUMN_MH_ID + " TEXT PRIMARY KEY," + COLUMN_Ten + " TEXT,"
//                + COLUMN_Nhom + " INTEGER," + COLUMN_To + " INTEGER" + ")";
         //Chạy lệnh tạo bảng.

        sqLiteDatabase.execSQL(script);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MH);


        // Và tạo lại.
        onCreate(sqLiteDatabase);
    }
    public void addMH(MonHoc mh) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MH_ID, mh.getMaMH());
        values.put(COLUMN_Ten, mh.getTenMH());
        values.put(COLUMN_Nhom, mh.getNhom());
        values.put(COLUMN_To, mh.getTo());


        // Thêm một dòng dữ liệu vào bảng.
        db.insert(TABLE_MH, null, values);


        // Đóng kết nối database.
        //db.close();
    }
    public void addALL(List<MonHoc> ListMH) {
        for (MonHoc t:ListMH) {
            this.addMH(t);
        }
    }
    public List<MonHoc> getAll() {

        List<MonHoc> ListMH = new ArrayList<MonHoc>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MH;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            MonHoc mh = new MonHoc(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
            ListMH.add(mh);
            cursor.moveToNext();
        }
        return ListMH;
    }
    public int getNotesCount() {

        String countQuery = "SELECT  * FROM " + TABLE_MH;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }
}
