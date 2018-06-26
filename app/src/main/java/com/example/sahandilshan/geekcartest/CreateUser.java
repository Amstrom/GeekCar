package com.example.sahandilshan.geekcartest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateUser extends AppCompatActivity {

    private EditText etxtFirstName,etxtLastName,etxtUsername,etxtPassword,etxtRePassword;
    private Button btnCreateUser,btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        final DatabaseReference db=myRef.child("Users").push();

        etxtFirstName=(EditText)findViewById(R.id.etxt_FirstName);
        etxtLastName = (EditText)findViewById(R.id.etxt_LastName);
        etxtUsername=(EditText)findViewById(R.id.etxt_Username);
        etxtPassword=(EditText)findViewById(R.id.etxt_Password);
        etxtRePassword=(EditText)findViewById(R.id.etxt_ConfirmPassword);

        btnCreateUser=(Button)findViewById(R.id.btn_CreateUser);
        btnClear=(Button)findViewById(R.id.btn_Clear);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etxtFirstName.setText("");
                etxtLastName.setText("");
                etxtUsername.setText("");
                etxtPassword.setText("");
                etxtRePassword.setText("");
                etxtFirstName.requestFocus();
            }
        });

        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName,lastName,username,password,rePassword;
                firstName=etxtFirstName.getText().toString();
                lastName=etxtLastName.getText().toString();
                username=etxtUsername.getText().toString();
                password=etxtPassword.getText().toString();
                rePassword=etxtRePassword.getText().toString();

                if(firstName.equals("") || lastName.equals("") || username.equals("") || password.equals("") || rePassword.equals(""))
                {
                    toastMsg("Please fill all fields");
                    etxtFirstName.requestFocus();
                }
                else
                {
                    if(!rePassword.equals(password))
                    {
                        toastMsg("Passwords are not matching, Please re-enter the password");
                        etxtPassword.requestFocus();
                    }
                    else
                    {
                        boolean found=false;
                        for (Users x:MainActivity.users)
                        {
                            if(x.getUsername().equals(username))
                            {
                                found=true;
                                break;
                            }
                        }
                        if(found)
                        {
                            toastMsg("Username is already exist, Please enter another username");
                        }
                        else
                        {
                            db.child("Username").setValue(username);
                            db.child("Password").setValue(password);
                            db.child("FirstName").setValue(firstName);
                            db.child("LastName").setValue(lastName);
                            db.child("Wins").setValue(0);
                            db.child("Loss").setValue(0);
                            toastMsg("Account was successfully Created");
//
                            etxtFirstName.setText("");
                            etxtLastName.setText("");
                            etxtUsername.setText("");
                            etxtPassword.setText("");
                            etxtRePassword.setText("");
                            etxtFirstName.requestFocus();
                            Log.d("NNNNNN",db.getKey());
                            String key=db.getKey();
                            MainActivity.users.add(new Users(username,password.toCharArray(),firstName,lastName,0,0,key));
                        }
                    }
                }
            }
        });
    }

    private void toastMsg(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
