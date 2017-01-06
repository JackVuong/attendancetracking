package com.plpnghi.attendancetrackingapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by plpnghi on 29/07/2016.
 */
public class Selection extends AppCompatActivity {
    private Button btnStart;
    private JSONArray dsmh;
    ArrayList<String> arrMH = new ArrayList<String>();
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() +  "/AttendanceTrackingApplication";
    File file = new File(path, "monhoc.txt");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        btnStart = (Button) findViewById(R.id.btnStart);
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
        if(!isNetworkAvailable())
        {
            readFile(file);
        }
        final Spinner spin=(Spinner) findViewById(R.id.spinnerMH);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, arrMH);
        adapter.setDropDownViewResource
                (R.layout.my_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = spin.getSelectedItem().toString().trim();
                String[] separated1 = selectedItem.split("-Ma");
                ListSV.MaMH = separated1[1];
                String[] separated2 = separated1[0].split("-To");
                ListSV.To = separated2[1];
                String[] separated3 = separated2[0].split("-Nhom");
                ListSV.Nhom = separated3[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), BarcodeScanner.class);
                startActivity(intent);
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!isNetworkAvailable())
                    return;
                new docJSon().execute("http://thefirststep.esy.es/monhoc.php");
            }
        });
    }
//    private void logout() {
//        session.setLoggedin(false);
//        finish();
//        startActivity(new Intent(Selection.this, MainActivity.class));
//    }
    private void readFile(File f){
        if(f.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line;

                while ((line = br.readLine()) != null) {
                    line.trim();
                    arrMH.add(line);
                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class docJSon extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {


            try {

                dsmh = new JSONArray(s);
                FileOutputStream stream = null;
                try {
                    stream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                for(int i = 0; i<dsmh.length();i++){
                    JSONObject mh = dsmh.getJSONObject(i);
                    String txtMH = mh.getString("tenMH")+"-Nhom"+mh.getString("nhomMH")+"-To"+mh.getString("toMH")+"-Ma"+mh.getString("maMH");
                    arrMH.add(txtMH);
                    stream.write((txtMH+"\n").getBytes());

                }
                final Spinner spin=(Spinner) findViewById(R.id.spinnerMH);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, arrMH);
                adapter.setDropDownViewResource
                        (R.layout.my_item);
                spin.setAdapter(adapter);
                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String selectedItem = spin.getSelectedItem().toString().trim();
                        String[] separated1 = selectedItem.split("-Ma");
                        ListSV.MaMH = separated1[1];
                        String[] separated2 = separated1[0].split("-To");
                        ListSV.To = separated2[1];
                        String[] separated3 = separated2[0].split("-Nhom");
                        ListSV.Nhom = separated3[1];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private class MyProcessEvent implements
                AdapterView.OnItemSelectedListener
        {
            //Khi có chọn lựa thì vào hàm này
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1,
                                       int arg2,
                                       long arg3) {
                //arg2 là phần tử được chọn trong data source
                //selection1.setText(arrMH.get(arg2));
            }
            //Nếu không chọn gì cả
            public void onNothingSelected(AdapterView<?> arg0) {
                //selection1.setText("");
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private static String docNoiDung_Tu_URL(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }

}
