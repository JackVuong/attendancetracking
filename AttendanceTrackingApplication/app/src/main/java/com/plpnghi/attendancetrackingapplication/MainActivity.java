package com.plpnghi.attendancetrackingapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by plpnghi on 29/07/2016.
 */
public class MainActivity extends ActionBarActivity {
    String username;
    String password;
    public static final String USER_NAME = "USERNAME";
    static String user = "";

//    private Button scannerButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        scannerButton = (Button) findViewById(R.id.btnLogin);
//
//        scannerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(v.getContext(), Selection.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    private Button btnLogin;
    private EditText txtUser, txtPass;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //btnLogin = (Button)findViewById(R.id.btnLogin);
        //btnLogin.setOnClickListener(this);
        txtUser = (EditText)findViewById(R.id.txtUser);
        txtPass = (EditText)findViewById(R.id.txtPass);
        session = new Session(this);

        if(session.loggedin()){
            startActivity(new Intent(MainActivity.this, Navigation.class));
            finish();
        }
    }
    public void invokeLogin(View view){
        username = txtUser.getText().toString();
        password = txtPass.getText().toString();
        user = username;
        if(isNetworkAvailable()) {
            login(username, password);
        }
        else{
            Toast.makeText(getApplicationContext(), "Vui Lòng Kết Nối Mạng", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    /*private void login(){
        String user = txtUser.getText().toString();
        String pass = txtPass.getText().toString();
        if(user.equals("admin") && pass.equals("123456")) {
            session.setLoggedin(true);
            startActivity(new Intent(MainActivity.this, Selection.class));
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Invalid Username Or Password", Toast.LENGTH_SHORT).show();
        }
    }*/
    private void login(final String username, String password){

        class LoginAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(MainActivity.this, "Please wait", "Loading...");
            }
            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];
                String pass = params[1];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", uname));
                nameValuePairs.add(new BasicNameValuePair("password", pass));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://thefirststep.esy.es/login.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line+ " ");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                int so = Integer.parseInt(s.replaceAll("[\\D]", ""));
                loadingDialog.dismiss();
                if(so == 1){
                    session.setLoggedin(true);
                    startActivity(new Intent(MainActivity.this, Navigation.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Invalid Username Or Password" , Toast.LENGTH_LONG).show();
                }
            }
        }
        LoginAsync la = new LoginAsync();
        la.execute(username, password);
    }
}
