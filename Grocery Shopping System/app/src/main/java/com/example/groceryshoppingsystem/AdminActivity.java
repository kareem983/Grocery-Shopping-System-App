package com.example.groceryshoppingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private FloatingActionButton floatingActionButton;
    private BottomNavigationView bottomNavigationView;
    private TextView FragmentTitle;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mAuth=FirebaseAuth.getInstance();

        //tool bar
        mToolBar = (Toolbar)findViewById(R.id.Admin_ToolBar);
        setSupportActionBar(mToolBar);

        FragmentTitle =(TextView)findViewById(R.id.FragmentTitle);
        floatingActionButton= (FloatingActionButton)findViewById(R.id.floatingBtnId);
        bottomNavigationView= (BottomNavigationView)findViewById(R.id.Bottom_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(naveListener);

        //default fragment is product (awl ma y sign in go to products fragment)
        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout,new ProductsFragment()).commit();
        FragmentTitle.setText("All Products");


        //on clicking to adding button
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here add button
            }
        });

    }


    private BottomNavigationView.OnNavigationItemSelectedListener naveListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment SelectedFragment =null;
                    int id = item.getItemId();
                    if(id==R.id.ProductID){
                        SelectedFragment = new ProductsFragment();
                        FragmentTitle.setText("All Products");
                    }
                    else if(id==R.id.OffersID){
                        SelectedFragment = new OffersFragment();
                        FragmentTitle.setText("All Offers");
                    }
                    else if(id==R.id.SalesMenID){
                        SelectedFragment = new SalesMenFragment();
                        FragmentTitle.setText("All SalesMen");
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout,SelectedFragment).commit();
                    return true;
                }
            };




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
        if(id==R.id.adminLogoutId){
            CheckLogout();
        }
        return super.onOptionsItemSelected(item);
    }


    private void CheckLogout(){
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(AdminActivity.this);
        checkAlert.setMessage("Do you want to Logout?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent intent=new Intent(AdminActivity.this,loginActivity.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = checkAlert.create();
        alert.setTitle("LogOut");
        alert.show();

    }
}