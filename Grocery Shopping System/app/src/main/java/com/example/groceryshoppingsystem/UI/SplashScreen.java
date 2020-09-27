package com.example.groceryshoppingsystem.UI;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.groceryshoppingsystem.R;

public class SplashScreen extends AppCompatActivity {
    private TextView txt, appText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else{
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

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