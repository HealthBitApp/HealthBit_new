package com.ariel.healthbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainProfile extends AppCompatActivity
{
   Toolbar toolbar;
   Button logout,bmi,menu,act,tips,myprof,store,weightTracker,setting;
   TextView hello;
   DatabaseReference ref;
   FirebaseAuth fb;
   FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);
        ref=FirebaseDatabase.getInstance().getReference("users").child(fb.getInstance().getUid());
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null)
        {
            Intent myIntent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(myIntent);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbarMainProfile);
        setSupportActionBar(toolbar);

        hello=(TextView)findViewById(R.id.mainprofile_hello);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                User post = dataSnapshot.getValue(User.class);
                if(post!=null)
                {
                    hello.setText("hello " + post.name);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        };
        ref.addValueEventListener(postListener);


        logout = (Button) findViewById(R.id.mainprofile_logout); //logout, and move to the login activity
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(getApplicationContext(), signin.class);
                startActivity(myIntent);
            }
        });

        myprof = (Button) findViewById(R.id.main_myprofile); //move to the profile activity
        myprof.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), myprofile.class);
                startActivity(myIntent);
            }
        });

        act = (Button) findViewById(R.id.main_dailyact); //move to the activity main
        act.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), daily_activity.class);
                startActivity(myIntent);
            }

        });

        menu = (Button) findViewById(R.id.main_dailyMenu);//move to the daily menu activity
        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), dailymenu.class);
                startActivity(myIntent);
            }

        });

        bmi = (Button) findViewById(R.id.newItem);//move to the BMI activity
        bmi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), bmi_activity.class);
                startActivity(myIntent);
            }

        });

        store = (Button) findViewById(R.id.main_store);//move to the store activity
        store.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), store.class);
                startActivity(myIntent);
            }

        });

        tips = (Button) findViewById(R.id.main_tips); //move to the tips activity
        tips.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), tips.class);
                startActivity(myIntent);
            }

        });

        weightTracker = (Button) findViewById(R.id.updateItem); //move to the tips activity
        weightTracker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), weighttracker.class);
                startActivity(myIntent);
            }

        });

        setting = (Button) findViewById(R.id.main_setting); //move to the tips activity
        setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), settingprofile.class);
                startActivity(myIntent);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)//toolbar definition
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}