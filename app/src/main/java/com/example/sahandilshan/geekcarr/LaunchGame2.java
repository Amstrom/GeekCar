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

import java.util.ArrayList;
import java.util.List;

public class LaunchGame2 extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_game2);

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

        List<Questions> arr=new ArrayList<>();

        if(SelectGame.firstUser==true)
        {
            arr=SelectGame.questions;
        }
        else
        {
            arr=JoinGame.questions;
        }

        qArray=arr;

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
        Log.d("CCCCCCCC",String.valueOf(correctAns));
        if(correctAns<5)
        {
            txtQuestion.setText(qArray.get(indexNo).getQuestions());
            txtAns1.setText(qArray.get(indexNo).getAns1());
            txtAns2.setText(qArray.get(indexNo).getAns2());
            txtAns3.setText(qArray.get(indexNo).getAns3());
            txtAns4.setText(qArray.get(indexNo).getAns4());
            indexNo++;
        }
        else if(correctAns==SelectGame.qNumber)
        {
            txtQuestion.setText(qArray.get(indexNo).getQuestions());
            txtAns1.setText(qArray.get(indexNo).getAns1());
            txtAns2.setText(qArray.get(indexNo).getAns2());
            txtAns3.setText(qArray.get(indexNo).getAns3());
            txtAns4.setText(qArray.get(indexNo).getAns4());
//            btnNext.setText("Submit");
            if(SelectGame.firstUser==true)
            {
                refToGames.child(childId).child("FUCorrectAnswers").setValue(String.valueOf(correctAns));
            }
            else
            {
                refToGames.child(childId).child("SUCorrectAnswers").setValue(String.valueOf(correctAns));
            }



            Intent i=new Intent(LaunchGame2.this,Finish.class);

            i.putExtra("childId",childId);

            startActivity(i);
            indexNo++;
            correctAns++;
        }
        else if(correctAns==6)
        {

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

        try {


            if (answer == qArray.get(indexNo - 1).getAns()) {
                StartGame.btSocket.getOutputStream().write("1".toString().getBytes());
                correctAns++;
            }
            String ans = "User :" + answer + "Real :" + qArray.get(indexNo - 1).getAns();
            msg(ans);
        }
        catch (Exception ex)
        {
            msg("Error");
        }
    }






    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

}

