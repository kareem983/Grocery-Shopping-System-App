package com.example.groceryshoppingsystem.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.groceryshoppingsystem.Adapters.MyAdapter_Recycler_View;
import com.example.groceryshoppingsystem.Model.favouritesClass;
import com.example.groceryshoppingsystem.R;
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

public class favourites_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar;
    private TextView mperson_name;
    private CircleImageView image;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private String UserId;
    private RecyclerView.Adapter my_adapter;
    //Custom Xml Views (cart Icon)
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle;
    private TextView CustomCartNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_activity);

        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        UserId = CurrentUser.getUid();

        mToolBar = findViewById(R.id.main_Toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //define Navigation Viewer and got its data
        DefineNavigation();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Refresh CartIcon
        showCartIcon();

        //to check if the total price is zero or not
        HandleTotalPriceToZeroIfNotExist();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.Home) {
            startActivity(new Intent(favourites_activity.this, MainActivity.class));
        }
        else if (id == R.id.Profile) {
            startActivity(new Intent(favourites_activity.this, UserProfileActivity.class));
        }
        else if(id == R.id.Cart){
            startActivity(new Intent(favourites_activity.this, CartActivity.class));
        }
        else if(id == R.id.MyOrders){
            startActivity(new Intent(favourites_activity.this, OrderActivity.class));
        }
        else if(id==R.id.fruits){
            Intent intent =new Intent(favourites_activity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Fruits");
            startActivity(intent);
        }
        else if(id==R.id.vegetables){
            Intent intent =new Intent(favourites_activity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Vegetables");
            startActivity(intent);
        }
        else if(id==R.id.meats){
            Intent intent =new Intent(favourites_activity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Meats");
            startActivity(intent);
        }
        else if(id==R.id.electronics){
            Intent intent =new Intent(favourites_activity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Electronics");
            startActivity(intent);
        }
        else if (id == R.id.Logout) {
            CheckLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void CheckLogout(){
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(favourites_activity.this);
        checkAlert.setMessage("Do you want to Logout?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(favourites_activity.this,loginActivity.class);
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

    private void DefineNavigation() {
        View mnavigationview;
        navigationView = findViewById(R.id.navegation_view2);
        drawerLayout = findViewById(R.id.drawer2);

        navigationView.setNavigationItemSelectedListener(this);
        mnavigationview = navigationView.getHeaderView(0);
        mperson_name = mnavigationview.findViewById(R.id.persname);
        image = mnavigationview.findViewById(R.id.circimage);


        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getNavHeaderData();
    }

    public void Retrieve_fav() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.recyclerViewlayout);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.favourite_recycler_view, mylayout, false);
        final RecyclerView rc = mylayout.findViewById(R.id.recyclerView);
        // rc.setHasFixedSize(true);
        // rc.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager mGridLayoutManager;
        mGridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rc.setLayoutManager(mGridLayoutManager);
        final List<favouritesClass> favourite_list = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("favourites")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    favouritesClass fav = new favouritesClass();
                    fav = ds.getValue(favouritesClass.class);
                    favourite_list.add(fav);
                }
                my_adapter = new MyAdapter_Recycler_View(favourite_list);
                rc.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    private void getNavHeaderData() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("users").child(UserId);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("Name").getValue().toString();
                    String photo = dataSnapshot.child("Image").getValue().toString();
                    if (photo.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(image);
                    } else
                        Picasso.get().load(photo).placeholder(R.drawable.profile).into(image);
                    mperson_name.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
        Retrieve_fav();
    }


    private void showCartIcon(){
        //toolbar & cartIcon
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.main2_toolbar,null);
        actionBar.setCustomView(view);

        //************custom action items xml**********************
        CustomCartContainer = (RelativeLayout)findViewById(R.id.CustomCartIconContainer);
        PageTitle =(TextView)findViewById(R.id.PageTitle);
        CustomCartNumber = (TextView)findViewById(R.id.CustomCartNumber);

        PageTitle.setText("Favourites");
        setNumberOfItemsInCartIcon();

        CustomCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(favourites_activity.this, CartActivity.class));
            }
        });

    }


    private void setNumberOfItemsInCartIcon(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("cart").child(UserId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if(dataSnapshot.getChildrenCount()==1){
                        CustomCartNumber.setVisibility(View.GONE);
                    }
                    else {
                        CustomCartNumber.setVisibility(View.VISIBLE);
                        CustomCartNumber.setText(String.valueOf(dataSnapshot.getChildrenCount()-1));
                    }
                }
                else{
                    CustomCartNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        m.addListenerForSingleValueEvent(eventListener);
    }

    private void HandleTotalPriceToZeroIfNotExist(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("cart").child(UserId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    FirebaseDatabase.getInstance().getReference().child("cart").child(UserId).child("totalPrice").setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        m.addListenerForSingleValueEvent(eventListener);

    }

}