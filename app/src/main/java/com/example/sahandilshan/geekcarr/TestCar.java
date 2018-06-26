package com.example.sahandilshan.geekcarr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class TestCar extends AppCompatActivity {

    private Button btnStart,btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_car);

        btnStart=(Button)findViewById(R.id.btn_Start);
        btnStop=(Button)findViewById(R.id.btn_Stop);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    StartGame.btSocket.getOutputStream().write("2".toString().getBytes());
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    StartGame.btSocket.getOutputStream().write("0".toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
