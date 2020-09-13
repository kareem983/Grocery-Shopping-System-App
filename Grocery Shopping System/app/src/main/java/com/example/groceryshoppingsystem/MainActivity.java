package com.example.groceryshoppingsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        mToolBar=(Toolbar) findViewById(R.id.main_TooBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("بقالة");



    }
}