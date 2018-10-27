package com.pucpr.puctcc.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pucpr.puctcc.R;
import com.pucpr.puctcc.model.User;

public class FirstActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference refFirebase;
    private TextView typeUser;
    private User user;
    private String typeUserEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        typeUser = findViewById(R.id.txtTypeUser);
        auth = FirebaseAuth.getInstance();
        String email = auth.getCurrentUser().getEmail().toString();
        refFirebase = FirebaseDatabase.getInstance().getReference();

        refFirebase.child("users").orderByChild("email").equalTo(email.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ps : dataSnapshot.getChildren()){
                    typeUserEmail = ps.child("typeUser").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_secretary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();
        if(id == R.id.action_add_user){
            openCreateUser();
        }else if(id == R.id.action_logout){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private  void openCreateUser(){
        Intent intent = new Intent(FirstActivity.this, CreateUserActivity.class);
        startActivity(intent);
    }

    private  void logout(){
        auth.signOut();
        Intent intent = new Intent(FirstActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
