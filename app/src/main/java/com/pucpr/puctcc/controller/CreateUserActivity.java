package com.pucpr.puctcc.controller;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pucpr.puctcc.R;
import com.pucpr.puctcc.dao.AuthFirebase;
import com.pucpr.puctcc.model.User;

public class CreateUserActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private EditText name;
    private RadioButton rbDoctor;
    private RadioButton rbNurse;
    private RadioButton rbSecretary;
    private Button btnCreate;
    private Button btnCancel;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseReference;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        email = findViewById(R.id.edtRegisterEmail);
        name = findViewById(R.id.edtRegisterName);

        password = findViewById(R.id.edtRegisterPassword);
        confirmPassword = findViewById(R.id.edtRegisterConfirmPassword);

        rbDoctor = findViewById(R.id.rbRegisterDoctor);
        rbNurse = findViewById(R.id.rbRegisterNurse);
        rbSecretary = findViewById(R.id.rbRegisterSecretary);

        btnCreate = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                    user = new User();
                    user.setEmail(email.getText().toString());
                    user.setName(name.getText().toString());

                    if (rbDoctor.isChecked()) {
                        user.setTypeUser("doctor");
                    } else if (rbNurse.isChecked()) {
                        user.setTypeUser("nurser");
                    } else if (rbSecretary.isChecked()) {
                        user.setTypeUser("secretary");
                    }
                    createUser();
                }else {
                    Toast.makeText(CreateUserActivity.this, "As senhas não são correspondentes", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private  void createUser(){
        firebaseAuth = AuthFirebase.getAuthFirebase();
        firebaseAuth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(CreateUserActivity.this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
//                    insertUser(user);
//                    finish();
                }else{
                    String error  = "";
                    try{
                        throw  task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        error = "Digite uma senha mais forte contendo no minimo 8 caracteres e que contenha letras e números";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        error = "O email digitado é invalido";
                    }catch (FirebaseAuthUserCollisionException e){
                        error = "Esse usuário já existe";
                    }catch (Exception e){
                        error = "Servidor";
                        e.printStackTrace();
                    }

                    Toast.makeText(CreateUserActivity.this, "error: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean insertUser(User u){
        try {
            firebaseReference = AuthFirebase.getFirebase().child("user");

            firebaseReference.push().setValue(u);

            Toast.makeText(CreateUserActivity.this, "Usuario cadastrado com sucesso", Toast.LENGTH_SHORT).show();
            return  true;

        }catch (Exception e){
            Toast.makeText(CreateUserActivity.this, "error ao gravar usuario ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }

    }
}
