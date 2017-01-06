package com.plpnghi.attendancetrackingapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RequestQueue requestQueue;
    private Button btnLop;
    private Button btnSuKien;
    private Button btnDongBo;
    private Session session;
    private TextView txtUser;
    final ArrayList<DiemDanh> diemdanh = new ArrayList<DiemDanh>();
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() +  "/AttendanceTrackingApplication";
    File file = new File(path, "data.txt");
    String insertUrl = "http://thefirststep.esy.es/insert.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        //txtUser = (TextView)findViewById(R.id.txtTen);
        //txtUser.setText("Vuong Gia Luan");
        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }
        btnLop = (Button) findViewById(R.id.btnClass);
        btnLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),Selection.class);
                startActivity(intent);
            }
        });
        btnSuKien = (Button) findViewById(R.id.btnEvent);
        btnSuKien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),Event.class);
                startActivity(intent);
            }
        });
        btnDongBo = (Button) findViewById(R.id.btnDongBo);
        btnDongBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    if (readFile(file)) {
                        for (int i = 0; i < diemdanh.size(); i++) {
                            insert(diemdanh.get(i));
                        }
                        Toast.makeText(getApplicationContext(), "Đồng Bộ Thành Công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Đồng Bộ Không Thành Công", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Vui Lòng Kết Nối Mạng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    private void logout() {
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(Navigation.this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // do nothing
        } else if (id == R.id.nav_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Navigation.this, MainActivity.class));
            logout();
            drawer.closeDrawers();
            return true;
        } else if (id == R.id.nav_about_us) {

        } else if (id == R.id.nav_privacy_policy) {

        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
