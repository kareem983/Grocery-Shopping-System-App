package com.example.groceryshoppingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mtoggle;
    private CircleImageView image;
    private TextView mperson_name;
    private FirebaseAuth mAuth;
    private String Uid, name, photo;
    private FirebaseUser CurrentUser;
    private NavigationView navigationView;
    private ViewPager pager;
    private My_Adapter adapter;
    private List<model> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        Uid = CurrentUser.getUid();

        navigationView = findViewById(R.id.navegation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        mperson_name = view.findViewById(R.id.persname);
        image = view.findViewById(R.id.circimage);

        //slider
        models = new ArrayList<>();
        models.add(new model(R.drawable.ic_baseline_favorite_24, "a7a", "eh elkalam asdasd asd ad asdadwq dqweqw qw wqe qwe wqe qwe wq asd sa dsad sa zxzcxzc"));
        models.add(new model(R.drawable.ic_baseline_my_orders_24, "a7a", "eh elkalam"));
        models.add(new model(R.drawable.profile_icon, "a7a", "eh elkalam"));
        models.add(new model(R.drawable.ic_baseline_exit_to_app_24, "a7a", "eh elkalam"));
        adapter = new My_Adapter(models, this);
        pager = findViewById(R.id.cardview);
        pager.setAdapter((PagerAdapter) adapter);
        pager.setPadding(130, 0, 130, 0);

        //toolbar
        mToolBar = findViewById(R.id.main_TooBar);
        drawerLayout = findViewById(R.id.drawer);
        setSupportActionBar(mToolBar);
        mtoggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setTitle("بقالة");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ///----------------------------
        //View view1 = LayoutInflater.inflate(R.layout.activity_main, this);


        Navigation_view_header_data();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, loginActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void Navigation_view_header_data() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("users").child(Uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name = dataSnapshot.child("Name").getValue().toString();
                    photo = dataSnapshot.child("Image").getValue().toString();
                    if (photo.equals("default")) {
                        Picasso.get().load(R.drawable.profile_icon).into(image);
                    } else
                        Picasso.get().load(photo).placeholder(R.drawable.profile_icon).into(image);
                    mperson_name.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }
}
/*
class fragmant_home extends Fragment {


    List<HorizontalProductModel> models;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView gridLayoutTitle = view.findViewById(R.id.grid_product_layout);
        Button gridBtn = view.findViewById(R.id.grid_button_layout_viewall_button);
        GridView gridView = view.findViewById(R.id.product_layout_gridview);
        models = new ArrayList<>();
        models.add(new HorizontalProductModel(R.drawable.ic_baseline_delete_24, "a7a", "ae el kalam", "sfveksbmsv"));
        models.add(new HorizontalProductModel(R.drawable.ic_baseline_delete_24, "a7a", "ae el kalam", "sfveksbmsv"));
        models.add(new HorizontalProductModel(R.drawable.ic_baseline_delete_24, "a7a", "ae el kalam", "sfveksbmsv"));
        models.add(new HorizontalProductModel(R.drawable.ic_baseline_delete_24, "a7a", "ae el kalam", "sfveksbmsv"));


        gridView.setAdapter(new GridproductAdapter(models));

        return view;
    }
}*/

