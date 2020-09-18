package com.example.groceryshoppingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar;
    private TextView mperson_name;
    private CircleImageView image;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mAuth=FirebaseAuth.getInstance();
        CurrentUser =mAuth.getCurrentUser();
        UserId =CurrentUser.getUid();

        //toolbar
        mToolBar = findViewById(R.id.UserProfileToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("بقالة");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //define Navigation Viewer and got its data
        DefineNavigation();




    }






    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        if(id==R.id.Home){
            finish();
        }
        else if(id==R.id.Logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(UserProfileActivity.this,loginActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    private void DefineNavigation(){
        drawerLayout = (DrawerLayout) findViewById(R.id.UserProfileDrawer);
        navigationView = (NavigationView) findViewById(R.id.UserProfileNavigation);

        //navigation header
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        mperson_name = view.findViewById(R.id.persname);
        image = view.findViewById(R.id.circimage);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getNavHeaderData();
    }

    private void getNavHeaderData(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("users").child(UserId);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()){
                   String UserName = snapshot.child("Name").getValue().toString();
                   String USerImage = snapshot.child("Image").getValue().toString();
                   mperson_name.setText(UserName);
                   if (USerImage.equals("default")) {
                       Picasso.get().load(R.drawable.profile_icon).into(image);
                   } else
                       Picasso.get().load(USerImage).placeholder(R.drawable.profile_icon).into(image);
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }

}