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
    private Menu menu1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        typeUser = findViewById(R.id.txtTypeUser);
        auth = FirebaseAuth.getInstance();

        refFirebase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        menu.clear();

        this.menu1 = menu;

        String email = auth.getCurrentUser().getEmail().toString();


        refFirebase.child("users").orderByChild("email").equalTo(email.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot pSnapshot : dataSnapshot.getChildren()){
                    typeUserEmail = pSnapshot .child("typeUser").getValue().toString();
                    menu.clear();
                    if (menu1.equals("admin")){
                        getMenuInflater().inflate(R.menu.menu_doctor, menu1);

                        typeUser.setText(typeUserEmail.toString());
                    }else if (menu1.equals("secretary")){
                        getMenuInflater().inflate(R.menu.menu_secretary, menu1);
                    }
                    else if (menu1.equals("nurse")){
                        getMenuInflater().inflate(R.menu.menu_nurse, menu1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
