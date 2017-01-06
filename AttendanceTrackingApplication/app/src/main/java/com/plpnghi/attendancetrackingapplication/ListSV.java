package com.plpnghi.attendancetrackingapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by GiaLuan on 11/10/2016.
 */
public class ListSV extends AppCompatActivity  {
    RequestQueue requestQueue;
    String insertUrl = "http://thefirststep.esy.es/insert.php";
    ArrayAdapter<String> adapter;
    ListView lvSV;
    private Button btnSync;
    private Button btnAdd;
    private Button btnSave;
    private EditText txtMSSV;
    Calendar c = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String strDate = sdf.format(c.getTime());
    //String[] arrData;
    ArrayList<DiemDanh> diemdanh = new ArrayList<DiemDanh>();
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() +  "/AttendanceTrackingApplication";
    File file = new File(path, "data.txt");
    static String MaMH = "";
    static String Nhom = "";
    static String To = "";
    static String NamHoc = "2016";
    static String HocKy = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listsv);
        //Toast.makeText(getApplicationContext(), MaMH.toString(), Toast.LENGTH_SHORT).show();
        createDiemDanh();
        btnSync = (Button) findViewById(R.id.btnSync);
        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListSV.this);
                    builder.setMessage("Bạn Có Muốn Đồng Bộ?")
                            .setCancelable(false)
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    diemdanh = new ArrayList<DiemDanh>();
                                    createDiemDanh();
                                    for (int i = 0; i < diemdanh.size(); i++) {
                                        insert(diemdanh.get(i));
                                    }
                                    Toast.makeText(getApplicationContext(), "Đồng Bộ Thành Công!!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Thông Báo!");
                    alert.show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Vui Lòng Kết Nối Mạng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = new File(path);
                if(!dir.exists()){
                    dir.mkdir();
                }
                if(!file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                FileOutputStream stream = null;
                try {
                    stream = new FileOutputStream(file,true);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    for(int i = 0; i < BarcodeScanner.arrData.size(); i++){
                        String line = BarcodeScanner.arrData.get(i).toString() + "\t" + MaMH + "\t" + Nhom
                                + "\t" + To + "\t" + NamHoc + "\t" + HocKy + "\t" + strDate + "\n";
                        stream.write(line.getBytes());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getApplicationContext(),"Đã Lưu !!", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(ListSV.this, Navigation.class));
            }
        });
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMSSV = (EditText)findViewById(R.id.txtMSSV);
                if(txtMSSV.getText().toString().trim().equals(""))
                    return;
                BarcodeScanner.arrData.add(txtMSSV.getText().toString().trim());
                lvSV = (ListView) findViewById(R.id.listSV);
                adapter = new ArrayAdapter<String>(ListSV.this,android.R.layout.simple_list_item_1,BarcodeScanner.arrData);
                lvSV.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        });
        lvSV = (ListView) findViewById(R.id.listSV);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,BarcodeScanner.arrData);
        lvSV.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        lvSV,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {

                                for (int position : reverseSortedPositions) {
                                    BarcodeScanner.arrData.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
        lvSV.setOnTouchListener(touchListener);

    }

    private void createDiemDanh(){
        for(int i = 0; i < BarcodeScanner.arrData.size(); i++){
            DiemDanh d = new DiemDanh(BarcodeScanner.arrData.get(i).toString(), MaMH, Nhom
                    , To, NamHoc, HocKy, strDate);
            diemdanh.add(d);
        }
    }

    private void insert(final DiemDanh dd){
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("maSV", dd.getMaSV());
                parameters.put("maMH", dd.getMaMH());
                parameters.put("nhomMH", dd.getNhomMH());
                parameters.put("toMH", dd.getToMH());
                parameters.put("namHoc", dd.getNamHoc());
                parameters.put("hocKy", dd.getHocKy());
                parameters.put("ngayGio", dd.getNgayGio());
                return parameters;
            }
        };
        requestQueue.add(request);
    }

    private boolean readFile(File f){
        if(f.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line;

                while ((line = br.readLine()) != null) {
                    line.trim();
                    String[] str = line.split("\t");
                    if (str.length == 7) {
                        DiemDanh d = new DiemDanh(str[0], str[1], str[2], str[3], str[4], str[5], str[6]);
                        diemdanh.add(d);
                    }
                }

                br.close();
                f.delete();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}