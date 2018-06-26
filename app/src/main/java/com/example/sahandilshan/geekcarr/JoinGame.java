package com.example.sahandilshan.geekcarr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class JoinGame extends AppCompatActivity {
    private TextView txtFirstUser,txtSecondUser;
    private EditText txtRoomId;
    private Button btnJoin;

    private boolean joins;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    final DatabaseReference refToGames=myRef.child("Games");

    public static List<Questions> questions=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        txtFirstUser=(TextView)findViewById(R.id.txt_FirstUser);
        txtSecondUser=(TextView)findViewById(R.id.txt_SecondUser);
        btnJoin=(Button)findViewById(R.id.btnJoinGame);
        txtRoomId=(EditText)findViewById(R.id.txt_RoomId);


        joins=false;

        btnJoin.setOnClickListener(new View.OnClickListener() {

            ValueEventListener valueEventListener;
            @Override
            public void onClick(View v) {
                final String roomId;
                roomId=txtRoomId.getText().toString();
                SelectGame.firstUser=false;
                final String myName=StartGame.user.getFirstName()+" "+StartGame.user.getLastName();


                if(roomId.equals(""))
                {
                    toastMsg("Please enter the Room id");
                    txtRoomId.requestFocus();
                }
                else
                {
                    if(joins==false) {
                        final DatabaseReference newReference = refToGames.child(roomId);
                        valueEventListener =newReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                boolean found = true;
                                String secondUser = "";
                                String used="";
                                String checkUsed="";
                                String mode="";
                                Log.d("AAAA", dataSnapshot.getKey());

                                try {
//                                secondUser = m.get("FirstUserName");
                                    secondUser = dataSnapshot.child("FirstUserName").getValue().toString();
                                    used=dataSnapshot.child("used").getValue().toString();
                                    String subject=dataSnapshot.child("Subject").getValue().toString();
                                    checkUsed=dataSnapshot.child("FUCorrectAnswers").getValue().toString();
                                    mode=dataSnapshot.child("Mode").getValue().toString();
                                    if (mode.equals(getString(R.string.mode1)))
                                        SelectGame.mode1=true;
                                    else
                                    {
                                        SelectGame.mode1=false;
                                    }

                                    DatabaseReference refToQuestions=database.getReference("Questions").child(subject);
                                    refToQuestions.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            Map<String,String> ques= (Map<String,String>)dataSnapshot.getValue();

                                            String question=ques.get("question:");
                                            String ans1=ques.get("ans1:");
                                            String ans2=ques.get("ans2:");
                                            String ans3=ques.get("ans3:");
                                            String ans4=ques.get("ans4:");
//                        int cAns=Integer.getInteger(ques.get("cAns"));
                                            String cAns=ques.get("cAns:");
                                            int ans=Integer.parseInt(cAns);
                                            questions.add(new Questions(question,ans1,ans2,ans3,ans4,ans));
//                        Log.d("Question",question+" "+ans1+" "+ans2+" "+ans3+" "+ans4+" "+ans);
                                            Log.d("SIZE",String.valueOf(questions.size()));

                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                } catch (Exception e) {
                                    found = false;
                                    toastMsg("Invalid room id");
//                                e.printStackTrace();
                                }

                                if (found) {
                                    if (checkUsed.equals("---"))
                                    {
                                        newReference.child("SecondUserName").setValue(myName);
                                        txtFirstUser.setText("Your Name :"+myName);
                                        txtSecondUser.setText("Opponent Name :"+secondUser);
                                        if(used.equals("yes"))
                                        {
                                            joins=true;
                                            if (SelectGame.mode1) {
                                                Intent i = new Intent(JoinGame.this, LaunchGame.class);
                                                i.putExtra("childId", roomId);
                                                shuffleArray(questions);
                                                newReference.removeEventListener(valueEventListener);
//                                            refToGames.removeEventListener(valueEventListener);
                                                startActivity(i);
                                            }
                                            else
                                            {
                                                Intent i = new Intent(JoinGame.this, LaunchGame2.class);
                                                i.putExtra("childId", roomId);
                                                startActivity(i);
//                                                shuffleArray(questions);
//                                                newReference.removeEventListener(valueEventListener);
                                                shuffleArray(questions);
                                                newReference.removeEventListener(valueEventListener);
//
                                            }

                                        }
                                    }
                                    else
                                    {
                                        toastMsg("Room was already used");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                        Log.d("TAG", newReference.toString());
//                        newReference.addValueEventListener(valueEventListener);
                    }
                }
            }

            });


    }
    private void toastMsg(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private static void shuffleArray(List<Questions> a) {
        int n = a.size();
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(a, i, change);
        }
    }

    private static void swap(List<Questions> a, int i, int change) {
        Questions helper = a.get(i);
        a.set(i,a.get(change));
        a.set(change,helper);
    }
}
