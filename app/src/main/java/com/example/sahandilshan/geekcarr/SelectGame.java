package com.example.sahandilshan.geekcarr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SelectGame extends AppCompatActivity {

    private Button btnStart,btnStartGame;
    private NestedScrollView ns;
    private RadioGroup rgSelectSubject,rgSelectMode;
    private TextView txtMyName,txtOppenentName,txtRoomId;
    private Users user;
    public static List<Questions> questions=new ArrayList<>();
    private static long childId;
    private boolean first;// to avoid continuously changing

    public static boolean firstUser,mode1; // to make sure which user is this
    public static final int qNumber=5;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    final DatabaseReference refToGames=myRef.child("Games");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        first=true;
//        firstUser=false;
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        rgSelectMode=(RadioGroup)findViewById(R.id.rbg_GameMode);
        rgSelectSubject=(RadioGroup)findViewById(R.id.rbg_Subject);
        btnStart=(Button)findViewById(R.id.btn_Start);
        btnStartGame=(Button)findViewById(R.id.btn_StartGame) ;
        txtMyName=(TextView)findViewById(R.id.txt_MyName);
        txtOppenentName=(TextView)findViewById(R.id.txt_opponentName);
        txtRoomId=(TextView)findViewById(R.id.txt_RoomId);
        Intent i=getIntent();
        user=(Users)i.getSerializableExtra("user");
        ns=(NestedScrollView)findViewById(R.id.nsv);




        valueEventListener=refToGames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Map<String,String>m=(Map<String, String>)dataSnapshot.getValue();
                long x;
                if(first==true) {
                    x = dataSnapshot.getChildrenCount();
                    childId = x + 1000;
//                    first = false;

                    //                        childId--;
                    Log.d("SSSSSS", String.valueOf(childId));
                    String use = "";
                    try {
                        use = dataSnapshot.child(String.valueOf(childId)).child("used").getValue().toString();
                    } catch (Exception e) {
                        used = true;
                        childId = 1001;
                    }
                    Log.d("UUUUUUUUUUUUUUU", use);
                    if (use.equals("no")) {
                        used = true;//if privious child isn't complete
                    }
                    Log.d("USED", use);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refToGames.removeEventListener(valueEventListener);
                RadioButton selectedSubject=(RadioButton)findViewById(rgSelectSubject.getCheckedRadioButtonId());
                RadioButton selectedMode=(RadioButton)findViewById(rgSelectMode.getCheckedRadioButtonId());
                String subject =selectedSubject.getText().toString();
                String mode=selectedMode.getText().toString();
                if (mode.equals(getString(R.string.mode1)))
                    mode1=true;
                else
                    mode1=false;
                Log.d("LLLLLL",mode);
                Log.d("TTTTTTTTT",String.valueOf(SelectGame.mode1));
                questions=new ArrayList<>();//otherwise previous questions will be not deleted

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

                Log.d("JJJJ","dasdas"+childId);
                Log.d("FFFF",String.valueOf(childId));
                if(!used) {
                    childId++;
                    used=true;
                }


                Log.d("WRITE_CHILD",String.valueOf(childId));
                DatabaseReference newRefToGame=refToGames.child(String.valueOf(childId));

                newRefToGame.child("FirstUserName").setValue(user.getFirstName()+" "+user.getLastName());
                newRefToGame.child("FUCorrectAnswers").setValue("---");
                newRefToGame.child("SecondUserName").setValue("---");
                newRefToGame.child("SUCorrectAnswers").setValue("---");
                newRefToGame.child("Winner").setValue("---");
                newRefToGame.child("used").setValue("no");
                newRefToGame.child("Subject").setValue(subject);
                newRefToGame.child("Mode").setValue(mode);

                txtMyName.setText(user.getFirstName()+" "+user.getLastName());
                txtOppenentName.setText("-------");
                txtRoomId.setText(String.valueOf(childId));


                newRefToGame.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String opponentName=dataSnapshot.child("SecondUserName").getValue(String.class);

                        if (opponentName.equals("---")) {
                            txtOppenentName.setText("--------------");
                        }
                        else
                        {
                            txtOppenentName.setText(opponentName);
                            found2nd = true;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                startGame();


            }
        });

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTheGame();
            }
        });

    }


    boolean used=false;

    private void startGame()
    {
        refToGames.child("Test").setValue("Test");
        Log.d("FFFF",String.valueOf(childId));
        if(!used) {
            childId++;
        }
//        if( childId ==1000 || childId ==1) {
//            childId=1001;
//        }


        Log.d("WRITE_CHILD",String.valueOf(childId));
        DatabaseReference newRefToGame=refToGames.child(String.valueOf(childId));
        newRefToGame.child("FirstUserName").setValue(user.getFirstName()+" "+user.getLastName());
        newRefToGame.child("FUCorrectAnswers").setValue("---");
        newRefToGame.child("SecondUserName").setValue("---");
        newRefToGame.child("SUCorrectAnswers").setValue("---");
        newRefToGame.child("Winner").setValue("---");
        newRefToGame.child("used").setValue("no");

        txtMyName.setText(user.getFirstName()+" "+user.getLastName());
        txtOppenentName.setText("-------");
        txtRoomId.setText(String.valueOf(childId));


        newRefToGame.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String opponentName=dataSnapshot.child("SecondUserName").getValue(String.class);
                txtOppenentName.setText(opponentName);
                found2nd=true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    boolean found2nd=false;

    private void startTheGame()
    {
        if(found2nd)
        {
            refToGames.child(String.valueOf(childId)).child("used").setValue("yes");
            if (mode1) {
                Intent i = new Intent(SelectGame.this, LaunchGame.class);
                i.putExtra("childId", String.valueOf(childId));
                shuffleArray(questions);
                startActivity(i);
            }
            else
            {
                Intent i = new Intent(SelectGame.this, LaunchGame2.class);
                i.putExtra("childId", String.valueOf(childId));
                shuffleArray(questions);

                startActivity(i);
            }
            refToGames.removeEventListener(valueEventListener);
            firstUser=true;

        }
        else
        {
            toastMsg("Please wait while the second user joins the game");
        }
    }

    private void toastMsg(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
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

    private ValueEventListener valueEventListener;
}
