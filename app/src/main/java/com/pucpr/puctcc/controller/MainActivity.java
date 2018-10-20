package com.pucpr.puctcc.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pucpr.puctcc.R;
import com.pucpr.puctcc.dao.AuthFirebase;
import com.pucpr.puctcc.model.User;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText edtLoginEmail;
    private EditText edtLoginPassword;
    private Button btnLogin;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        if(userLogged()) {
            Intent intentAccount = new Intent(MainActivity.this, FirstActivity.class);
            openActivity(intentAccount);
        }else {
            btnLogin.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!edtLoginEmail.getText().toString().equals("") && !edtLoginPassword.getText().toString().equals("")) {
                                user = new User();
                                user.setEmail(edtLoginEmail.getText().toString());
                                user.setPassword(edtLoginPassword.getText().toString());
                                authLogin();
                            } else {
                                Toast.makeText(MainActivity.this, "Preencha os campos de e-mail e senha", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
        }

    }

    private void authLogin(){
        auth = AuthFirebase.getAuthFirebase();
        auth.signInWithEmailAndPassword(user.getEmail().toString(), user.getPassword().toString()).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            openScreen();
                            Toast.makeText(MainActivity.this, "Login deu boa.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Usuário ou senha inválida.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
    private void openScreen(){
        Intent intentopenScreen = new Intent(MainActivity.this, FirstActivity.class);
        startActivity(intentopenScreen);

    }
    public boolean userLogged(){
        FirebaseUser fuser = FirebaseAuth.getInstance().getInstance().getCurrentUser();
        return (fuser != null) ? true : false;
    }
    public void openActivity(Intent it){
        startActivity(it);
    }
}
