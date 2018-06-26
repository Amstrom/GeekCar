package com.example.sahandilshan.geekcartest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Finish extends AppCompatActivity {

    private Button btnBackToMenu;
    private TextView txtResult;
    private String childId;
    private boolean onetime=true;

    private String winner="";

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    final DatabaseReference refToGames=myRef.child("Games");
    final DatabaseReference refToUsers=myRef.child("Users").child(StartGame.user.getKey());
    private ValueEventListener valueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        btnBackToMenu =(Button)findViewById(R.id.btn_MainMenu);
        txtResult=(TextView)findViewById(R.id.txt_result);
        Intent i=new Intent(getIntent());
        childId=i.getStringExtra("childId");


        valueEventListener=refToGames.child(childId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                    if (SelectGame.mode1) {
                        int fuAns, suAns;
                        Log.d("WEEEE", dataSnapshot.child("FUCorrectAnswers").getValue(String.class));
                        try {
                            fuAns = Integer.valueOf(dataSnapshot.child("FUCorrectAnswers").getValue(String.class));
                            suAns = Integer.valueOf(dataSnapshot.child("SUCorrectAnswers").getValue(String.class));
//                            myRef.removeEventListener(valueEventListener);
                            if (fuAns - suAns > 0) {

                                if (SelectGame.firstUser) {
                                    txtResult.setText("You Won...");
                                    StartGame.user.setWins(StartGame.user.getWins() + 1);
                                    refToUsers.child("Wins").setValue(StartGame.user.getWins());
//                                    myRef.removeEventListener(valueEventListener);
//                                    refToGames.child(childId).child("Winner").setValue("First");
                                    winner="First";

                                } else {
                                    txtResult.setText("You Lose...");
                                    StartGame.user.setLoss(StartGame.user.getLoss() + 1);
                                    refToUsers.child("Loss").setValue(StartGame.user.getLoss());
                                }


                            } else if (fuAns == suAns) {


                                StartGame.user.setWins(StartGame.user.getWins() + 1);
                                refToUsers.child("Wins").setValue(StartGame.user.getWins());
                                txtResult.setText("Draw...");

//                                refToGames.removeEventListener(valueEventListener);
//                                refToGames.child(childId).child("Winner").setValue("Both");
                                winner="Both";

                            } else {


                                if (!SelectGame.firstUser) {
                                    txtResult.setText("You Won...");
                                    StartGame.user.setWins(StartGame.user.getWins() + 1);
                                    refToUsers.child("Wins").setValue(StartGame.user.getWins());

//                                    refToGames.removeEventListener(valueEventListener);
//                                    refToGames.child(childId).child("Winner").setValue("Second");
                                    winner="Second";


                                } else {
                                    txtResult.setText("You Lose...");
                                    StartGame.user.setLoss(StartGame.user.getLoss() + 1);
                                    refToUsers.child("Loss").setValue(StartGame.user.getLoss());
                                }

                            }
                            btnBackToMenu.setEnabled(true);
//                    refToGames.removeEventListener(valueEventListener);
                        } catch (Exception e) {
                            Log.d("MMMMM", e.getMessage());
                        }
                    } else {
                        //Mode 2 code..........
//                    boolean error=false;
                        int fuAns, suAns;
                        Log.d("eeeeEEE", dataSnapshot.child("FUCorrectAnswers").getValue(String.class));
                        try {
                            fuAns = Integer.valueOf(dataSnapshot.child("FUCorrectAnswers").getValue(String.class));
                            suAns = Integer.valueOf(dataSnapshot.child("SUCorrectAnswers").getValue(String.class));

                            txtResult.setText("You Lose");
                            StartGame.user.setLoss(StartGame.user.getLoss() + 1);
                            refToUsers.child("Loss").setValue(StartGame.user.getLoss());
                            if (SelectGame.firstUser) {
//                                refToUsers.child(childId).child("Winner").setValue("Second");
                                winner="Second";
                            } else {
//                                refToUsers.child(childId).child("Winner").setValue("First");
                                winner="First";
                            }
                        } catch (Exception ex) {
//                        error =true;
                            refToGames.removeEventListener(valueEventListener);
                            txtResult.setText("You Won");
                            StartGame.user.setWins(StartGame.user.getWins() + 1);
                            refToUsers.child("Wins").setValue(StartGame.user.getWins());
                            if (SelectGame.firstUser) {
//                                refToGames.child(childId).child("Winner").setValue("First");
                                winner="First";
                            } else {
//                                refToGames.child(childId).child("Winner").setValue("Second");
                                winner="Second";
                            }
                        }

                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Finish.this,StartGame.class);
                i.putExtra("User",StartGame.user);
                StartGame.join=true;
                refToGames.child(childId).removeEventListener(valueEventListener);
                refToGames.child(childId).child("Winner").setValue(winner);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {

//        Intent setIntent = new Intent(Finish.this,StartGame.class);
////        Disconnect();
//        startActivity(setIntent);

    }

}
