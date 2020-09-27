package com.example.groceryshoppingsystem.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.groceryshoppingsystem.Adapters.CategoryProductInfoAdapter;
import com.example.groceryshoppingsystem.Model.CategoryProductInfo;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar;
    private TextView mPerson_name;
    private CircleImageView mPerson_image;
    //------------------------------------
    private String CategoryName;
    private RecyclerView recyclerView;
    private ArrayList<CategoryProductInfo> CategoryProducts;
    private CategoryProductInfoAdapter adapter;
    //------------------------------------
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private String UserId;
    private CategoryProductInfoAdapter.RecyclerViewClickListener listener;
    //Custom Xml Views (cart Icon)
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle, CustomCartNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        UserId = CurrentUser.getUid();

        CategoryName = getIntent().getStringExtra("Category Name");


        //on clicking any product (go to ProductInfo Activity to show it's info)
        onClickAnyProduct();

    }

    @Override
    protected void onStart() {
        super.onStart();

        setCategoryData();

        //define Navigation Viewer and got its data
        DefineNavigation();

        //Refresh CartIcon
        showCartIcon();

        //to check if the total price is zero or not
        HandleTotalPriceToZeroIfNotExist();

    }

    private void onClickAnyProduct(){
        listener = new CategoryProductInfoAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                CategoryProductInfo product = CategoryProducts.get(position);

                Intent intent = new Intent(CategoryActivity.this,ProductInfoActivity.class);
                intent.putExtra("Product Name",product.getProductName());
                intent.putExtra("Product Price",product.getProductPrice());
                intent.putExtra("Product Image",product.getProductImage());
                intent.putExtra("Product ExpiryDate",product.getProductExpiryDate());
                intent.putExtra("Product IsFavorite",String.valueOf(product.getIsFavorite()));
                intent.putExtra("Is Offered","no");

                startActivity(intent);
            }
        };
    }


    private void setCategoryData(){
        //toolbar
        mToolBar =(Toolbar)findViewById(R.id.CategoryTooBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView)findViewById(R.id.CategoryRecycler);
        CategoryProducts = new ArrayList<>();

        adapter = new CategoryProductInfoAdapter(CategoryActivity.this,CategoryProducts,listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));
        recyclerView.setAdapter(adapter);

        getProductsData();

        showCartIcon();

    }

    private void getProductsData(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("product").child(CategoryName);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        final String ProductName = dataSnapshot.getKey().toString();
                        final String ProductPrice = dataSnapshot.child("price").getValue().toString();
                        final String ProductImage = dataSnapshot.child("image").getValue().toString();
                        final String ProductExpiryDate = dataSnapshot.child("expired").getValue().toString();

                        //check favorites
                        DatabaseReference Root = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference x = Root.child("favourites").child(UserId).child(ProductName);
                        ValueEventListener vvalueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    CategoryProducts.add(new CategoryProductInfo(ProductImage,ProductName,ProductPrice,ProductExpiryDate,true));
                                }
                                else{
                                    CategoryProducts.add(new CategoryProductInfo(ProductImage,ProductName,ProductPrice,ProductExpiryDate,false));
                                }
                                adapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        };
                        x.addListenerForSingleValueEvent(vvalueEventListener);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }



    private void DefineNavigation(){
        drawerLayout = (DrawerLayout) findViewById(R.id.CategoryDrawer);
        navigationView = (NavigationView) findViewById(R.id.CategoryNavigation);

        //navigation header
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        mPerson_name = view.findViewById(R.id.persname);
        mPerson_image = view.findViewById(R.id.circimage);

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
                    String Name = snapshot.child("Name").getValue().toString();
                    String Image = snapshot.child("Image").getValue().toString();
                    mPerson_name.setText(Name);
                    if (Image.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(mPerson_image);
                    } else
                        Picasso.get().load(Image).placeholder(R.drawable.profile).into(mPerson_image);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        if(id==R.id.Home){
            startActivity(new Intent(CategoryActivity.this,MainActivity.class));
        }
        else if(id==R.id.Profile){
            startActivity(new Intent(CategoryActivity.this,UserProfileActivity.class));
        }
        else if(id == R.id.Cart){
            startActivity(new Intent(CategoryActivity.this, CartActivity.class));
        }
        else if(id == R.id.MyOrders){
            startActivity(new Intent(CategoryActivity.this, OrderActivity.class));
        }
        else if(id == R.id.favourites){
            startActivity(new Intent(CategoryActivity.this, favourites_activity.class));
        }
        else if(id==R.id.fruits){
            CategoryName = "Fruits";
            setCategoryData();
        }
        else if(id==R.id.vegetables){
            CategoryName = "Vegetables";
            setCategoryData();
        }
        else if(id==R.id.meats){
            CategoryName = "Meats";
            setCategoryData();
        }
        else if(id==R.id.electronics){
            CategoryName = "Electronics";
            setCategoryData();
        }
        else if(id==R.id.Logout){
            CheckLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void CheckLogout(){
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(CategoryActivity.this);
        checkAlert.setMessage("Do you want to Logout?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(CategoryActivity.this,loginActivity.class);
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

        PageTitle.setText(CategoryName);
        setNumberOfItemsInCartIcon();

        CustomCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoryActivity.this, CartActivity.class));
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