package com.example.sahandilshan.geekcarr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LaunchGame extends AppCompatActivity {



//    private Button btnAns1;

    private List<Questions> qArray=new ArrayList<>();
    private int indexNo;
    private int answer;// answer which given by the user....

    private Button btnAns1,btnAns2,btnAns3,btnAns4,btnNext;
    private TextView txtQuestion,txtAns1,txtAns2,txtAns3,txtAns4;

    private int correctAns;
    private String childId;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    final DatabaseReference refToGames=myRef.child("Games");

//    private boolean firstUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_game);

        Intent i=getIntent();
        childId=i.getStringExtra("childId");
        Log.d("QQQQ",String.valueOf(SelectGame.firstUser));


        btnAns1=(Button)findViewById(R.id.btn1);
        btnAns2=(Button)findViewById(R.id.btn2);
        btnAns3=(Button)findViewById(R.id.btn3);
        btnAns4=(Button)findViewById(R.id.btn4);
        btnNext=(Button)findViewById(R.id.btn_Submit);

        txtQuestion=(TextView)findViewById(R.id.txtQueston);
        txtAns1=(TextView)findViewById(R.id.txtA1);
        txtAns2=(TextView)findViewById(R.id.txtA2);
        txtAns3=(TextView)findViewById(R.id.txtA3);
        txtAns4=(TextView)findViewById(R.id.txtA4);

        List<Questions>arr=new ArrayList<>();

        if(SelectGame.firstUser==true)
        {
            arr=SelectGame.questions;
        }
        else
        {
            arr=JoinGame.questions;
        }

//        new ConnectBT().execute();


//        qArray.add(new Questions("1+1","2","3","4","5",1));
//        qArray.add(new Questions("1+2","2","3","4","5",2));
//        qArray.add(new Questions("1+3","2","3","4","5",3));
//        qArray.add(new Questions("1+4","2","3","4","5",4));
//        qArray.add(new Questions("1+5","6","3","4","5",1));
try {
    for (int j = 0; j < SelectGame.qNumber; j++) {
        qArray.add(arr.get(j));
    }
}
catch (Exception e)
{

}

        changeQuestions();//load the question


        btnAns1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer=1;
                btnAns1.setBackgroundColor(Color.rgb(8,224,112));
                btnAns2.setBackgroundResource(android.R.drawable.btn_default);
                btnAns3.setBackgroundResource(android.R.drawable.btn_default);
                btnAns4.setBackgroundResource(android.R.drawable.btn_default);

            }
        });
        btnAns2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer=2;
                btnAns2.setBackgroundColor(Color.rgb(8,224,112));
                btnAns1.setBackgroundResource(android.R.drawable.btn_default);
                btnAns3.setBackgroundResource(android.R.drawable.btn_default);
                btnAns4.setBackgroundResource(android.R.drawable.btn_default);
            }
        });
        btnAns3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer=3;
                btnAns3.setBackgroundColor(Color.rgb(8,224,112));
                btnAns2.setBackgroundResource(android.R.drawable.btn_default);
                btnAns1.setBackgroundResource(android.R.drawable.btn_default);
                btnAns4.setBackgroundResource(android.R.drawable.btn_default);
            }
        });
        btnAns4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer=4;
                btnAns4.setBackgroundColor(Color.rgb(8,224,112));
                btnAns2.setBackgroundResource(android.R.drawable.btn_default);
                btnAns3.setBackgroundResource(android.R.drawable.btn_default);
                btnAns1.setBackgroundResource(android.R.drawable.btn_default);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
                changeQuestions();
                btnAns1.setBackgroundResource(android.R.drawable.btn_default);
                btnAns2.setBackgroundResource(android.R.drawable.btn_default);
                btnAns3.setBackgroundResource(android.R.drawable.btn_default);
                btnAns4.setBackgroundResource(android.R.drawable.btn_default);

//                checkAnswer();

            }
        });


    }
    private void changeQuestions()
    {
        if(indexNo<qArray.size()-1)
        {
            txtQuestion.setText(qArray.get(indexNo).getQuestions());
            txtAns1.setText(qArray.get(indexNo).getAns1());
            txtAns2.setText(qArray.get(indexNo).getAns2());
            txtAns3.setText(qArray.get(indexNo).getAns3());
            txtAns4.setText(qArray.get(indexNo).getAns4());
            indexNo++;
        }
        else if(indexNo==qArray.size()-1)
        {
            txtQuestion.setText(qArray.get(indexNo).getQuestions());
            txtAns1.setText(qArray.get(indexNo).getAns1());
            txtAns2.setText(qArray.get(indexNo).getAns2());
            txtAns3.setText(qArray.get(indexNo).getAns3());
            txtAns4.setText(qArray.get(indexNo).getAns4());
            btnNext.setText("Submit");
            indexNo++;

        }
        else if(indexNo==qArray.size())
        {
            if(SelectGame.firstUser==true)
            {
                refToGames.child(childId).child("FUCorrectAnswers").setValue(String.valueOf(correctAns));
            }
            else
            {
                refToGames.child(childId).child("SUCorrectAnswers").setValue(String.valueOf(correctAns));
            }



            Intent i=new Intent(LaunchGame.this,Finish.class);

            i.putExtra("childId",childId);

            startActivity(i);
        }



    }
    @Override
    public void onBackPressed() {



    }
//
//    private void Disconnect()
//    {
//        if (btSocket!=null) //If the btSocket is busy
//        {
//            try
//            {
//                btSocket.close(); //close connection
//            }
//            catch (IOException e)
//            { msg("Error");}
//        }
//        finish(); //return to the first layout
//
//    }

    private void checkAnswer()
    {
        if (StartGame.btSocket!=null)
        {
            try
            {
                if(answer==qArray.get(indexNo-1).getAns())
                {
                    StartGame.btSocket.getOutputStream().write("1".toString().getBytes());
                    correctAns++;

                }
                else
                {
//                    StartGame.btSocket.getOutputStream().write("0".toString().getBytes());
                }
                String ans="User :"+answer+"Real :"+qArray.get(indexNo-1).getAns();
                msg(ans);
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }



    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }


//    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
//    {
//
//        private boolean ConnectSuccess = true; //if it's here, it's almost connected
//
//        @Override
//        protected void onPreExecute() {
//            progress = ProgressDialog.show(LaunchGame.this, "Connecting...", "Please wait!!!");  //show a progress dialog
//        }
//
//        @Override
//        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
//        {
//            try {
//                if (btSocket == null || !isBtConnected) {
//                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
//                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(StartGame.address);//connects to the device's address and checks if it's available
//                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(StartGame.myUUID);//create a RFCOMM (SPP) connection
//                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
//                    btSocket.connect();//start connection
//                }
//            } catch (IOException e) {
//                ConnectSuccess = false;//if the try failed, you can check the exception here
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
//        {
//            super.onPostExecute(result);
//
//            if (!ConnectSuccess) {
//                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
//                finish();
//            } else {
//                msg("Connected.");
//                isBtConnected = true;
//            }
//            progress.dismiss();
//        }
//    }
}
