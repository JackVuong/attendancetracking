package com.plpnghi.attendancetrackingapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Event extends AppCompatActivity {

    private Button btnOK;
    private EditText txtName, txtPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        btnOK = (Button) findViewById(R.id.btnOK);
        txtName = (EditText)findViewById(R.id.txtEventName);
        txtPlace = (EditText)findViewById(R.id.txtPlace);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListSV.MaMH = txtName.getText().toString().trim();
                ListSV.Nhom = txtPlace.getText().toString().trim();
                ListSV.To = "0";
                Intent intent = new Intent(v.getContext(),BarcodeScanner.class);
                startActivity(intent);
            }
        });
    }
}
