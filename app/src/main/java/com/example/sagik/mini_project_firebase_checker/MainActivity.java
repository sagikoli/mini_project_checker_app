package com.example.sagik.mini_project_firebase_checker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Intent i;
     DatabaseReference id;
     DatabaseReference myRef;
    public static FirebaseDatabase database;
    public static boolean haschild=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Login(View view)
    {

        final TextView username=findViewById(R.id.username);
        final TextView pass=findViewById(R.id.passwd);
        final String unm=username.getText().toString();
        database= FirebaseDatabase.getInstance();
        myRef= database.getReference("checkers");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(unm))
                {
                    //id = myRef.child(username.getText().toString());
                    if(pass.getText().toString().equals( dataSnapshot.child(unm).child("password").getValue().toString()))
                    {
                        Toast.makeText(getApplicationContext(), "Login sucessesful", Toast.LENGTH_LONG).show();
                        i = new Intent(MainActivity.this, check.class);
                        i.putExtra("name",dataSnapshot.child(unm).child("name").getValue().toString());
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "incorrect password", Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"User not registered",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}
