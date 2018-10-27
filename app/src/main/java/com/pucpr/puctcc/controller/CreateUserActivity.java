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
    private User usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        email = findViewById(R.id.edtRegisterEmail);
        name = findViewById(R.id.edtRegisterName);

        email = findViewById(R.id.edtRegisterEmail);
        name = findViewById(R.id.edtRegisterName);

        password = findViewById(R.id.edtRegisterPassword);
        confirmPassword = findViewById(R.id.edtRegisterConfirmPassword);

        rbDoctor = findViewById(R.id.rbRegisterDoctor);
        rbNurse = findViewById(R.id.rbRegisterNurse);
        rbSecretary = findViewById(R.id.rbRegisterSecretary);

        btnCreate = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);

        rbDoctor = findViewById(R.id.rbRegisterDoctor);
        rbNurse = findViewById(R.id.rbRegisterNurse);
        rbSecretary = findViewById(R.id.rbRegisterSecretary);

        btnCreate = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                    usuario = new User();

                    usuario.setEmail(email.getText().toString());
                    usuario.setPassword(password.getText().toString());
                    usuario.setName(name.getText().toString());

                    if (rbDoctor.isChecked()) {
                        usuario.setTypeUser("Administrador");
                    } else if (rbNurse.isChecked()) {
                        usuario.setTypeUser("Atendente");
                    }
                    //chamada de método para cadastro de usuários
                    createUser();

                } else {
                    Toast.makeText(CreateUserActivity.this, "As senhas não se correspondem!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createUser() {

        firebaseAuth = AuthFirebase.getAuthFirebase();
        firebaseAuth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getPassword()
        ).addOnCompleteListener(CreateUserActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    insereUsuario(usuario);
                    finish();
                    //deslogar ao adicionar o usuário
                    firebaseAuth.signOut();
                    //para abrir a nossa tela principal após a re-autenticação
                    abreTelaPrincipal();
                } else {
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, contendo no mínimo 8 caracteres e que contenha letras e números!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é invalido, digite um novo e-mail";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está cadastro!";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }

                    Toast.makeText(CreateUserActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private boolean insereUsuario(User usuario) {

        try {

            firebaseReference = AuthFirebase.getFirebase().child("users");
            firebaseReference.push().setValue(usuario);
            Toast.makeText(CreateUserActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
            return true;

        } catch (Exception e) {
            Toast.makeText(CreateUserActivity.this, "Erro ao gravar o usuário!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }

    private void abreTelaPrincipal() {

    }
}
