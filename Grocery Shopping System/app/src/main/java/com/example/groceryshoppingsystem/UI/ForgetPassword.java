package com.example.groceryshoppingsystem.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.groceryshoppingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private EditText emailTxt;
    private Button sendForgetBtn;
    private ProgressBar progressbar;
    private FirebaseAuth mfirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        emailTxt = findViewById(R.id.editTextEmailAddress);
        sendForgetBtn = findViewById(R.id.forgetBtn);
        progressbar = findViewById(R.id.progressBar);
        mfirebaseAuth = FirebaseAuth.getInstance();

        sendForgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!emailTxt.getText().toString().equals(""))
                {
                    progressbar.setVisibility(View.VISIBLE);
                    mfirebaseAuth.sendPasswordResetEmail(emailTxt.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressbar.setVisibility(View.GONE);
                            if(task.isSuccessful())
                                Toast.makeText(ForgetPassword.this, "Password has been sent to your email please check your inbox", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(ForgetPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    emailTxt.setError("Field Is Empty");
            }
        });
    }
}