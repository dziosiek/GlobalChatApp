package com.picartapp.picart.init;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.picartapp.picart.R;
import com.picartapp.picart.init.LoginPanel;

public class RegisterPanel extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText etEmail, etPassword, etRepeatPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_panel);
        etEmail = findViewById(R.id.login_id);
        etPassword = findViewById(R.id.password_id);
        etRepeatPassword = findViewById(R.id.password_repeat_id);

        mAuth = FirebaseAuth.getInstance();
    }

    public boolean check(){
        String email = this.etEmail.getText().toString().trim();
        String password = this.etPassword.getText().toString().trim();
        String repeat_password = this.etRepeatPassword.getText().toString().trim();

        if(email.isEmpty()){
            this.etEmail.setError("E-mail is required");
            this.etEmail.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please enter a valid e-mail");
            etEmail.requestFocus();
            return false;
        }

        if(password.isEmpty()){
            this.etPassword.setError("Password is required");
            this.etPassword.requestFocus();
            return false;
        }
        if(password.length() < 6){
            this.etPassword.setError("Password is too short");
            this.etPassword.requestFocus();
            return false;
        }
        if(repeat_password.isEmpty()){
            this.etRepeatPassword.setError("Enter the password again");
            this.etRepeatPassword.requestFocus();
            return false;
        }
        if(!password.equals(repeat_password)){
            this.etRepeatPassword.setError("The passwords differ");
            this.etRepeatPassword.requestFocus();
            return false;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"User Register Succesful",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return true;

    }

    public void register(View view) {

        if(check())startActivity(new Intent(this, LoginPanel.class));
    }
}
