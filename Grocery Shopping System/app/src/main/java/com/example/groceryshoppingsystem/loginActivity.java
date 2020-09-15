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
            if (fauth.getCurrentUser().getEmail().equals("admin@gmail.com")) {
                startActivity(new Intent(loginActivity.this, AdminActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(loginActivity.this, MainActivity.class));
                finish();
            }

        }

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Email = mEmail.getText().toString().trim();
                final String Password = mPassword.getText().toString();
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

                // Authenticate
                fauth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (Email.equals("admin@gmail.com") && Password.equals("password")) {
                                Toast.makeText(loginActivity.this, "Welcome My Creator", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(loginActivity.this, AdminActivity.class));
                                finish();
                            } else {
                                Toast.makeText(loginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(loginActivity.this, MainActivity.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(loginActivity.this, "Wrong User name Or Password", Toast.LENGTH_SHORT).show();
                            mprogresspar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mforgerpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here we will send a verification message
                startActivity(new Intent(loginActivity.this , ForgetPassword.class));
            }
        });
    }
}