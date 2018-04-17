package com.example.sagik.mini_project_firebase_checker;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class info extends AppCompatActivity {
    DatabaseReference user;
    DatabaseReference map;
    FirebaseDatabase database;
    DataSnapshot userdata;
    RadioGroup grp;
    int i=0;
    String route_name;
    HashMap<Integer,String> textviewmap=new HashMap<>();
    ArrayList<Integer> idArrayList=new ArrayList<>();
    ArrayList<Integer> textidArrayList=new ArrayList<>();
    String user_id;
    String id="";
    ImageView img;
    private StorageReference mStorageRef;
    int key_5[]={0,2,4,6,8,1,3,5,7,9};
    int key_2[]={0,5,1,6,2,7,3,8,4,9};
    int key_1[]={0,1,2,3,4,5,6,7,8,9};
    int key_4[]={0,7,5,3,1,8,6,4,2,9};
    TextView name,age,address,gender;
    LinearLayout route_col,avail_pas_col,select_col;
    HashMap<Integer,int[]> key_map=new HashMap<Integer,int[]>();
    String decode(String code,int key)
    {
        String temp="";
        char digits[]=code.toCharArray();
        int val[]=key_map.get(key);
        for(int i=0;i<digits.length;i++)
        {
            temp+=val[Character.digit(digits[i],10)];
        }
        return temp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        database=FirebaseDatabase.getInstance();
        key_map.put(4,key_4);
        key_map.put(2,key_2);
        key_map.put(5,key_5);
        key_map.put(1,key_1);
        name=findViewById(R.id.name);
        gender=findViewById(R.id.gender);
        age=findViewById(R.id.age);
        address=findViewById(R.id.address);
        route_col=findViewById(R.id.route_column);
        avail_pas_col=findViewById(R.id.avail_pass_column);
        select_col=findViewById(R.id.select_column);
        Bundle code=getIntent().getExtras();
       // final TextView scanned_code=(TextView)findViewById(R.id.scanned_code);
        //TextView time=findViewById(R.id.time_of_scan);
        //TextView decrypted_id=findViewById(R.id.decrypted_id);
        if(code==null)
            Toast.makeText(this,"no info",Toast.LENGTH_LONG).show();
        String data=code.getString("code");
        if(data.length()!=27)
        {
            Toast.makeText(this,"wrong code",Toast.LENGTH_LONG).show();
        }
        //scanned_code.setText(data);
        char data_arr[]=data.toCharArray();
        String date="";
        String encoded_id="";

        int key=0;
        int counter=1;
        for (int i=0;i<data_arr.length;i++)
        {
            if(i==0)
            {
                key=Character.digit(data_arr[i],10);
            }
            else if(i%2!=0)
            {
                date+=data_arr[i];
            }
            else if(counter<6)
            {
                encoded_id+=data_arr[i];
                counter++;
            }
        }
        id=decode(encoded_id,key);
        StringBuffer temp=new StringBuffer(id);
        id=temp.reverse().toString();
//        time.setText(new Date(Long.parseLong(date)).toString());
  //      decrypted_id.setText(id);

       // map=database.getReference("mapping").child(id);
        user=database.getReference("users").child(id);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //user_id=dataSnapshot.getValue().toString();
              //  Log.i("data_base", user_id);
                setdata(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    public void setdata(DataSnapshot dataSnapshot)
    {
        name.setText(dataSnapshot.child("name").getValue().toString());
        address.setText(dataSnapshot.child("address").getValue().toString());
        gender.setText(dataSnapshot.child("gender").getValue().toString());
        age.setText(dataSnapshot.child("date of birth").getValue().toString());
        grp=new RadioGroup(getBaseContext());
        select_col.addView(grp);
        HashMap<Object,Object> routes= (HashMap<Object, Object>) dataSnapshot.child("routes").getValue();

        for (Map.Entry<Object,Object> entry : routes.entrySet())
        {
            TextView temp=new TextView(getBaseContext());
            temp.setText(entry.getKey().toString());
            temp.setTextColor(Color.WHITE);
            temp.setGravity(Gravity.CENTER);
            temp.setPaddingRelative(4,10,1,10);
            route_col.addView(temp);

            TextView temp2=new TextView(getBaseContext());
            temp2.setText(entry.getValue().toString());
            temp2.setTextColor(Color.WHITE);
            temp2.setGravity(Gravity.CENTER);
            temp2.setPaddingRelative(0,9,0,10);
            avail_pas_col.addView(temp2);

            textviewmap.put(i,entry.getKey().toString());

            RadioButton radioButton=new RadioButton(getBaseContext());
            radioButton.setId(i);
            radioButton.setBottom(10);
            idArrayList.add(radioButton.getId());
            //radioButton.setPaddingRelative(0,1,1,0);
            grp.addView(radioButton);
            i++;
        }
    }
    @Override
        protected void onPostCreate(@Nullable Bundle savedInstanceState) {
            img = findViewById(R.id.imageView);
        mStorageRef = FirebaseStorage.getInstance().getReference();
            mStorageRef.child("users/"+id+".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    InputStream is = null;
                    try {
                        URL u=new URL(task.getResult().toString());
                        is=u.openStream();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.i("data_base",task.getResult().getPath());
                    Log.i("data_base",task.getResult().toString());
                    img.setImageBitmap(BitmapFactory.decodeStream(is));
                    Toast.makeText(getApplicationContext(),"Image download complete",Toast.LENGTH_LONG);
                }
            });

            super.onPostCreate(savedInstanceState);

        }

    public void proceed(View view)
    {
        RadioButton radioButton;
      //  Log.i("zxc", Integer.toString(idArrayList.size())+"\t"+Integer.toString(textidArrayList.size()));
       // Log.i("zxc",textviewmap.get(1));
        for(int j=0;j<idArrayList.size();j++)
        {
         radioButton=(RadioButton) findViewById(idArrayList.get(j));
         //radioButton.setBackgroundColor(Color.WHITE);
           // Log.i("zxc", "checked:"+grp.getCheckedRadioButtonId());
         if(radioButton.isChecked())
         {
             Log.i("zxc", "checked:"+textviewmap.get(j));
             route_name=textviewmap.get(j);
             user.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                    int passes=  Integer.parseInt(dataSnapshot.child("routes").child(route_name).getValue().toString());
                     Log.i("zxc", "passes :\t"+"...!"+passes+"!...");

                     if(passes>0) {
                         user.child("routes").child(route_name).setValue(passes - 1);
                         Toast.makeText(getApplicationContext(), "TRANSACTION SUCCESSFUL....!!!!", Toast.LENGTH_LONG).show();
                         Intent intent = new Intent(getBaseContext(), check.class);
                         finish();
                         startActivity(intent);
                     }
                     else
                     {
                         Toast.makeText(getApplicationContext(), "NO AVAILABLE PASSES FOR SELECTED ROUTE.!", Toast.LENGTH_LONG).show();
                     }

                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });

             break;
         }
        }
    }


}
