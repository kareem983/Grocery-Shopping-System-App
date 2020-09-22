package com.example.groceryshoppingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
    private DatabaseReference m;
    private View mnavigationview;
    private static List<favouritesClass> favourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        Uid = CurrentUser.getUid();

        navigationView = findViewById(R.id.navegation_view);
        navigationView.setNavigationItemSelectedListener(this);
        mnavigationview = navigationView.getHeaderView(0);
        mperson_name = mnavigationview.findViewById(R.id.persname);
        image = mnavigationview.findViewById(R.id.circimage);
        drawerLayout = findViewById(R.id.drawer);


        mToolBar = findViewById(R.id.main_TooBar);
        setSupportActionBar(mToolBar);
        mtoggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setTitle("بقالة");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onStart() {
        super.onStart();

        //Retrieve Header View User Data
        Navigation_view_header_data();

        //Retrieve Favourites
        Retrieve_fav();

        // FirstView
        Retrieve_Electroncis();

        // SecondView
        Retrieve_Fruits();

        //Third View
        Retrieve_Meats();

        // Fourth View
        Retrieve_Vegatables();

        // OFFERS
        Retrieve_offers();
        ///----------------------------
        //View view1 = LayoutInflater.inflate(R.layout.activity_main, this);

    }

    public void Retrieve_Electroncis() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Electronics");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridproductAdapter my_adapter;
        my_adapter = new GridproductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Electronics");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user my_user = new user();
                    my_user = ds.getValue(user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(), "EGP " + my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Category Name","Electronics");
                startActivity(intent);
            }
        });

    }

    public void Retrieve_fav() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("favourites")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        favourites = new ArrayList<>();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    favouritesClass fav = new favouritesClass();
                    fav = ds.getValue(favouritesClass.class);
                    favourites.add(fav);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    public void Retrieve_Fruits() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView2);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Fruits");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridproductAdapter my_adapter;
        my_adapter = new GridproductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Fruits");
        ValueEventListener eventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user my_user = new user();
                    my_user = ds.getValue(user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(), "EGP " + my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Category Name","Fruits");
                startActivity(intent);
            }
        });
    }

    public void Retrieve_Meats() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView3);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Meats");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridproductAdapter my_adapter;
        my_adapter = new GridproductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Meats");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user my_user = new user();
                    my_user = ds.getValue(user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(), "EGP " + my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Category Name","Meats");
                startActivity(intent);
            }
        });

    }

    public void Retrieve_Vegatables() {
        LinearLayout mylayout = (LinearLayout) findViewById(R.id.my_cardView4);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, mylayout, false);
        TextView gridlayouttitle = mylayout.findViewById(R.id.grid_product_layout_textview);
        gridlayouttitle.setText("Vegetables");
        Button GridLayoutViewBtn = mylayout.findViewById(R.id.grid_button_layout_viewall_button);
        final GridView gv = mylayout.findViewById(R.id.product_layout_gridview);
        final List<HorizontalProductModel> lastmodels = new ArrayList<>();
        final GridproductAdapter my_adapter;
        my_adapter = new GridproductAdapter(lastmodels, favourites,MainActivity.this);
        m = FirebaseDatabase.getInstance().getReference().child("product").child("Vegetables");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user my_user = new user();
                    my_user = ds.getValue(user.class);
                    my_user.setCategory(ds.getKey().toString());
                    lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(), "EGP " + my_user.getPrice(), false,my_user.getExpired()));
                }
                gv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Category Name","Vegetables");
                startActivity(intent);
            }
        });
    }

    public void Retrieve_offers() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("offers");
        models = new ArrayList<>();
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Offers offer = new Offers();
                    offer = ds.getValue(Offers.class);
                    offer.setTitle(ds.getKey().toString());
                    models.add(new model(offer.getImg(), offer.getTitle(), offer.getDescribtion()));
                    adapter = new My_Adapter(models, MainActivity.this);
                    pager = findViewById(R.id.cardview);
                    pager.setAdapter((PagerAdapter) adapter);
                    pager.setPadding(130, 0, 130, 0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        if(id==R.id.menuCartID){
            Toast.makeText(MainActivity.this,"ddd",Toast.LENGTH_SHORT).show();
        }
        if (mtoggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Profile) {
            startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
        }
        else if(id == R.id.favourites){
            startActivity(new Intent(MainActivity.this, favourites_activity.class));
        }
        else if(id==R.id.fruits){
            Intent intent =new Intent(MainActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Fruits");
            startActivity(intent);
        }
        else if(id==R.id.vegetables){
            Intent intent =new Intent(MainActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Vegetables");
            startActivity(intent);
        }
        else if(id==R.id.meats){
            Intent intent =new Intent(MainActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Meats");
            startActivity(intent);
        }
        else if(id==R.id.electronics){
            Intent intent =new Intent(MainActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Electronics");
            startActivity(intent);
        }
        else if (id == R.id.Logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, loginActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
                        Picasso.get().load(R.drawable.profile).into(image);
                    } else
                        Picasso.get().load(photo).placeholder(R.drawable.profile).into(image);
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
