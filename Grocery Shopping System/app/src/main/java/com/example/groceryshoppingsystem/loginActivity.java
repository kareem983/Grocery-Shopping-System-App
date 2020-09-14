package com.example.groceryshoppingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mlogin;
    TextView mCreateBtn, mforgerpassword;
    FirebaseAuth fauth;
    ProgressBar mprogresspar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.EmailLogin);
        mPassword = (EditText) findViewById(R.id.PasswordLogin);
        fauth = FirebaseAuth.getInstance();
        mlogin = (Button) findViewById(R.id.Login);
        mCreateBtn = (TextView) findViewById(R.id.SignUPtext);
        mprogresspar = (ProgressBar) findViewById(R.id.progressBar1);
        mforgerpassword = (TextView) findViewById(R.id.ForgetPassword);
        // Checking if the user is logging in or log out ! ;
        if (fauth.getCurrentUser() != null) {
            startActivity(new Intent(loginActivity.this, MainActivity.class));
            finish();
        }

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = mEmail.getText().toString().trim();
                String Password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(Email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    mPassword.setError("Password is required");
                    return;
                }
                if (Password.length() < 6) {
                    mPassword.setError("Password must be bigger than or equal 6 characters");
                    return;
                }
                // progress in background and i make it here visible.
                mprogresspar.setVisibility(View.VISIBLE);
                if (Email.equals("admin") && Password.equals("password")) {
                    Toast.makeText(loginActivity.this, "Welcome My Creator", Toast.LENGTH_SHORT).show();
                    // here we will go to admin page
                    finish();
                }
                // Authenticate
                fauth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(loginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(loginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            mprogresspar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here we will go to Register Activity
            }
        });

        mforgerpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here we will send a verification message
            }
        });
    }
}