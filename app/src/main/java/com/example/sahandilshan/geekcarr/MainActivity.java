package com.example.sahandilshan.geekcarr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText txtUsername,txtPassword;
    private Button btnLogin,btnAddNew;
    private boolean added=false;
    public static List<Users> users=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin =(Button)findViewById(R.id.btn_Login);
        txtUsername=(EditText)findViewById(R.id.txtUsername);
        txtPassword=(EditText)findViewById(R.id.txtPassword);
        btnAddNew=(Button)findViewById(R.id.btn_AddNew);


//        myRef=new Firebase("https://fir-test1-e02dc.firebaseio.com/");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        final DatabaseReference db=myRef.child("Users");


        final ChildEventListener childEventListener= db.addChildEventListener(new ChildEventListener() {
            int z=1;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Map<String,String>m=dataSnapshot.getValue(Map.class);
//                GenericTypeIndicator<List<Users>> t = new GenericTypeIndicator<List<Users>>() {};
//                List<Users> m = dataSnapshot.getValue(t);

//                while (it.hasNext())
//                {
//                    Log.d("SSSS", "Value is: " + it.next().toString());
//                }
//                String key=db1.getKey();
                if(!added) {// to call the change method
                    Map<String, Object> m = (Map<String, Object>) dataSnapshot.getValue();
                    String username=(String)m.get("Username");
                    char [] password=((String)m.get("Password")).toCharArray();
                    String firstName=(String)m.get("FirstName");
                    String lastName=(String) m.get("LastName");
                    long lWins=(Long)m.get("Wins");
                    long lLoss=(Long)m.get("Loss");
                    int wins=(int)lWins;
                    int loss=(int)lLoss;
                    String key=dataSnapshot.getKey();
                    Log.d("KKKKKKKKKKk",key);
                    Log.d("Values",m.toString());
                    Users x=new Users(username,password,firstName,lastName,wins,loss,key);
                    Log.d("SSSS" + z, "Username :" + m.get("Username") + "    Password :" + Arrays.toString(password));
                    Log.d("Name of child",dataSnapshot.getKey());
                    z++;
                    users.add(x);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> m = (Map<String, Object>) dataSnapshot.getValue();
                Log.d("SSSS"+z, "Username :"+m.get("Username")+"    Password :"+m.get("Password"));
                String username=(String)m.get("Username");
                char [] password=((String)m.get("Password")).toCharArray();
                String firstName=(String)m.get("FirstName");
                String lastName=(String) m.get("LastName");
                int wins=(Integer)m.get("Wins");
                int loss=(Integer)m.get("Loss");
                Log.d("Values",m.toString());
                Users x=new Users(username,password,firstName,lastName,wins,loss);
                for (int i=0;i<users.size();i++)
                {
                    if(users.get(i).getUsername().equals(username))
                    {
                        users.set(i,x);
                    }
                }
                z++;
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


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                /*added=true;
                String val=txtEdit.getText().toString();
                String pass=txtPassword.getText().toString();
                DatabaseReference db= myRef.child("Users").push();
                db.child("password").setValue(pass);
                db.child("Username").setValue(val);


                txtPassword.setText("");
                txtEdit.setText("");
                Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_LONG).show();*/
                boolean found = false;
                String user_username = txtUsername.getText().toString();
                char[] password = txtPassword.getText().toString().toCharArray();
                if(users.size()==0)
                {
                    toastMsg("Unable to connect ot the database,Please check your network connection");
                }
                else {
                    if (user_username.equals("")) {
                        toastMsg("Please enter the username");
                        txtUsername.requestFocus();
                    } else {
                        for (Users x : users) {
                            if (x.getUsername().equals(user_username) && Arrays.equals(x.getPassword(), password)) {
//                        Users us=x;
                                db.removeEventListener(childEventListener);
                                Intent intent = new Intent(MainActivity.this, BluetoothDevices.class);
                                intent.putExtra("User", x);
                                startActivity(intent);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            toastMsg("Invalid credentials");
                        }

                    }
                }
            }

        });

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                txtUsername.getText().clear();
//                txtPassword.getText().clear();
                db.removeEventListener(childEventListener);
                if(users.size()==0)
                {
                    toastMsg("Unable to connect ot the database,Please check your network connection");
                }
                else {
                    Intent i = new Intent(MainActivity.this, CreateUser.class);
                    startActivity(i);
                }
                }
            });

    }
    private void toastMsg(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

}
