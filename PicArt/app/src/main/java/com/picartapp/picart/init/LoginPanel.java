package com.picartapp.picart.init;

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
import com.picartapp.picart.MainPanel;
import com.picartapp.picart.R;

public class LoginPanel extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button etLoginButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_panel);
        etEmail = (EditText) findViewById(R.id.login_id);
        etPassword = (EditText) findViewById(R.id.password_id);
        etLoginButton = (Button) findViewById(R.id.login_button);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(getApplicationContext(),MainPanel.class));


                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }



    public void register(View view) {
        startActivity(new Intent(this, RegisterPanel.class));
    }

    public void login(View view) {
        String email = this.etEmail.getText().toString().trim();
        String password = this.etPassword.getText().toString().trim();

        if(email.isEmpty()){
            this.etEmail.setError("E-mail is required");
            this.etEmail.requestFocus();
        }
        if(password.isEmpty()){
            this.etPassword.setError("Password is required");
            this.etPassword.requestFocus();
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginPanel.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(LoginPanel.this, "Authentication succesed.",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainPanel.class));
                        }
                    }
                });
    }


}
