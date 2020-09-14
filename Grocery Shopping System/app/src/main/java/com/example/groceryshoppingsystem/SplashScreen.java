package com.example.groceryshoppingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SplashScreen extends AppCompatActivity {
    private TextView txt, appText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        appText = findViewById(R.id.appSplashName);
        txt = findViewById(R.id.welcome);

        YoYo.with(Techniques.FlipInX)
                .duration(3000)
                .repeat(0)
                .playOn(appText);
        YoYo.with(Techniques.ZoomIn)
                .duration(3000)
                .repeat(0)
                .playOn(txt);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, loginActivity.class));
                finish();
            }
        }, 3500);
    }
}