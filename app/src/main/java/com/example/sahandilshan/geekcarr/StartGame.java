package com.example.sahandilshan.geekcarr;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class StartGame extends AppCompatActivity {

    public static Users user;
    private TextView txtName;
    private Button btnStartGame,btnJoinGame,btnPractice, btnChangeCar,btnLogout;
    public static String address;

    public static boolean join;//to check whether user runs the application at least one time,


//    String address = null;
    BluetoothAdapter myBluetooth = null;
    private ProgressDialog progress;
    public static BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    public static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);
        Intent i=getIntent();
        user=(Users)i.getSerializableExtra("User");
        if(!join) {
            new ConnectBT().execute();

        }
        address=BluetoothDevices.address;
//        address=i.getStringExtra(EXTRA_ADDRESS);
//        Log.d("LLLLL1",address);
        btnStartGame=(Button)findViewById(R.id.btn_StartGame);
        btnJoinGame=(Button)findViewById(R.id.btn_joinGame);
        btnPractice=(Button)findViewById(R.id.btn_Practice);
        btnLogout=(Button)findViewById(R.id.btn_Logout);
        btnChangeCar =(Button)findViewById(R.id.btn_ChangeCar);
        txtName=(TextView)findViewById(R.id.txtName);

        String setText=user.getFirstName()+" "+user.getLastName()+"\nTotal Games :"+(user.getLoss()+user.getWins())
                +"\nWins : "+user.getWins()+"\nLoss :"+user.getLoss();
        txtName.setText(setText);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StartGame.this,SelectGame.class);
                i.putExtra("user",user);

                startActivity(i);
            }
        });

        btnJoinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StartGame.this,JoinGame.class);
                startActivity(i);
            }
        });

        btnChangeCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                Disconnect();
                Intent i =new Intent(StartGame.this,BluetoothDevices.class);
                i.putExtra("User",user);
                join=false;
                startActivity(i);
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect();
                Intent i =new Intent(StartGame.this,MainActivity.class);
                i.putExtra("User",user);
                join=false;
                startActivity(i);
            }
        });

        btnPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StartGame.this,TestCar.class);
                startActivity(i);
            }
        });

    }
    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(StartGame.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                toastMsg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                toastMsg("Connected.");
                isBtConnected = true;
                join=true;
            }
            progress.dismiss();
        }
    }

    private void toastMsg(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { toastMsg("Error");}
        }
        finish(); //return to the first layout

    }
}
