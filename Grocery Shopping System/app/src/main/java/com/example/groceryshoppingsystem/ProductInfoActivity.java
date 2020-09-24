package com.example.groceryshoppingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProductInfoActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView mPerson_name;
    private CircleImageView mPerson_image;
    private String ProductName, ProductPrice, ProductImage, ProductNExpiryDate, ProductIsFavorite, IsOffered;
    //xml views
    private ImageView PImage, PIsFav;
    private TextView PName, PCategory, PAmount, PPrice, OldPrice,OfferRate, PExpiryDate;
    private RelativeLayout AddToCartContainer,DeleteFromCartContainer,CheckCartContainer;
    private LinearLayout OfferContainer;
    private Button Back,Confirm;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        UserId = CurrentUser.getUid();

        //toolbar
        mToolbar =(Toolbar)findViewById(R.id.ProductToolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Product Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //have sending data
        ProductName= getIntent().getStringExtra("Product Name");
        ProductPrice= getIntent().getStringExtra("Product Price");
        ProductImage= getIntent().getStringExtra("Product Image");
        ProductNExpiryDate= getIntent().getStringExtra("Product ExpiryDate");
        ProductIsFavorite= getIntent().getStringExtra("Product IsFavorite");
        IsOffered = getIntent().getStringExtra("Is Offered");

        // define xml data
        DefineXmlViews();

        setProductData();
        onClicking();

    }


    private void DefineXmlViews(){
        PImage = (ImageView)findViewById(R.id.ProductImage);
        PIsFav = (ImageView)findViewById(R.id.ProductFav);
        PName = (TextView)findViewById(R.id.ProductName);
        PCategory = (TextView)findViewById(R.id.ProductCategory);
        PAmount = (TextView)findViewById(R.id.ProductAvailableAmount);
        PPrice = (TextView)findViewById(R.id.CurrentProductPrice);
        OldPrice = (TextView)findViewById(R.id.OldProductPrice);
        OfferRate = (TextView)findViewById(R.id.OfferRate);
        OfferContainer = (LinearLayout)findViewById(R.id.OfferContainer);
        PExpiryDate = (TextView)findViewById(R.id.ProductExpiryDate);
        AddToCartContainer = (RelativeLayout)findViewById(R.id.AddToCart);
        DeleteFromCartContainer = (RelativeLayout)findViewById(R.id.DeleteFromCart);
        CheckCartContainer = (RelativeLayout)findViewById(R.id.CheckCartContainer);
        Back = (Button)findViewById(R.id.BackBtn);
        Confirm= (Button)findViewById(R.id.ConformBtn);

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference x = root.child("cart").child(UserId).child(ProductName);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    AddToCartContainer.setVisibility(View.GONE);
                    DeleteFromCartContainer.setVisibility(View.VISIBLE);
                }
                else{
                    AddToCartContainer.setVisibility(View.VISIBLE);
                    DeleteFromCartContainer.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        x.addListenerForSingleValueEvent(valueEventListener);

    }

    private void onClicking(){
        PIsFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ProductIsFavorite.equalsIgnoreCase("true")){
                    PIsFav.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);
                    ProductIsFavorite="false";
                    //here Delete favourites from database
                    DatabaseReference x= FirebaseDatabase.getInstance().getReference().child("favourites").child(UserId);
                    x.child(ProductName).removeValue();
                }
                else{
                    PIsFav.setImageResource(R.drawable.ic_baseline_favorite_24);
                    ProductIsFavorite="true";
                    //here save favourites in database
                    DatabaseReference x= FirebaseDatabase.getInstance().getReference().child("favourites").child(UserId).child(ProductName);
                    x.child("checked").setValue(true);
                    x.child("productimage").setValue(ProductImage);
                    x.child("productprice").setValue("EGP "+ProductPrice);
                    x.child("producttitle").setValue(ProductName);

                }
            }
        });

        AddToCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckCartContainer.setVisibility(View.VISIBLE);
            }
        });

        DeleteFromCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToCartContainer.setVisibility(View.VISIBLE);
                DeleteFromCartContainer.setVisibility(View.GONE);

                DatabaseReference x = FirebaseDatabase.getInstance().getReference().child("cart").child(UserId);
                x.child(ProductName).removeValue();

                Toast.makeText(ProductInfoActivity.this,"The Product Deleted Successfully from your Cart",Toast.LENGTH_SHORT).show();
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckCartContainer.setVisibility(View.GONE);
            }
        });

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckCartContainer.setVisibility(View.GONE);
                DeleteFromCartContainer.setVisibility(View.VISIBLE);
                AddToCartContainer.setVisibility(View.GONE);
                Toast.makeText(ProductInfoActivity.this,"The Product Added Successfully to your Cart",Toast.LENGTH_SHORT).show();
                //here Add the product to the cart
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("productImage",ProductImage);
                hashMap.put("productPrice",ProductPrice);
                hashMap.put("quantity","1");
                int PriceAfterOffer;
                if(IsOffered.equalsIgnoreCase("yes"))PriceAfterOffer = (int) ((Integer.valueOf(ProductPrice)) - (Integer.valueOf(ProductPrice)*0.3));
                else PriceAfterOffer =(int)(Integer.valueOf(ProductPrice));

                hashMap.put("productPrice",String.valueOf(PriceAfterOffer));

                DatabaseReference x = FirebaseDatabase.getInstance().getReference().child("cart").child(UserId);
                x.child(ProductName).setValue(hashMap);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DefineNavigation();
    }

    private void setProductData(){
        Picasso.get().load(ProductImage).into(PImage);
        PName.setText(ProductName);

        if(IsOffered.equalsIgnoreCase("yes")){
            int PriceAfterOffer = (int) ((Integer.valueOf(ProductPrice)) - (Integer.valueOf(ProductPrice)*0.3));
            PPrice.setText("Price: "+PriceAfterOffer+" EGP");
            OldPrice. setText(ProductPrice+" EGP");
            OfferRate.setText("- 30%");
            OldPrice.setPaintFlags(OldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            OfferContainer.setVisibility(View.GONE);
            PPrice.setText("Price: "+ProductPrice+" EGP");
        }


        if(ProductIsFavorite.equalsIgnoreCase("true"))PIsFav.setImageResource(R.drawable.ic_baseline_favorite_24);
        else PIsFav.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);

        if(ProductNExpiryDate.equalsIgnoreCase("null") )PExpiryDate.setVisibility(View.GONE);
        else {PExpiryDate.setVisibility(View.VISIBLE); PExpiryDate.setText("Expiry Date: "+ProductNExpiryDate);}


        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("product");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.child("Fruits").getChildren()){
                        if(dataSnapshot.getKey().equals(ProductName)){
                            PCategory.setText("Category: Fruits");
                            PAmount.setText("Available Amounts: "+dataSnapshot.child("quantity").getValue());
                            break;}
                    }
                    for(DataSnapshot dataSnapshot : snapshot.child("Electronics").getChildren()){
                        if(dataSnapshot.getKey().equals(ProductName)){
                            PCategory.setText("Category: Electronics");
                            PAmount.setText("Available Amounts: "+dataSnapshot.child("quantity").getValue());
                            break;}
                    }

                    for(DataSnapshot dataSnapshot : snapshot.child("Meats").getChildren()){
                        if(dataSnapshot.getKey().equals(ProductName)){
                            PCategory.setText("Category: Meats");
                            PAmount.setText("Available Amounts: "+dataSnapshot.child("quantity").getValue());
                            break;}
                    }

                    for(DataSnapshot dataSnapshot : snapshot.child("Vegetables").getChildren()){
                        if(dataSnapshot.getKey().equals(ProductName)){
                            PCategory.setText("Category: Vegetables");
                            PAmount.setText("Available Amounts: "+dataSnapshot.child("quantity").getValue());
                            break;}
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        m.addListenerForSingleValueEvent(valueEventListener);

    }


    private void DefineNavigation(){
        drawerLayout = (DrawerLayout) findViewById(R.id.ProductDrawer);
        navigationView = (NavigationView) findViewById(R.id.ProductNavigation);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
        if(id==R.id.menuCartID){
            startActivity(new Intent(ProductInfoActivity.this, CartActivity.class));
        }
        if(mToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        if(id==R.id.Home){
            startActivity(new Intent(ProductInfoActivity.this,MainActivity.class));
        }
        else if(id==R.id.Profile){
            startActivity(new Intent(ProductInfoActivity.this,UserProfileActivity.class));
        }
        else if(id == R.id.favourites){
            startActivity(new Intent(ProductInfoActivity.this, favourites_activity.class));
        }
        else if(id == R.id.Cart){
            startActivity(new Intent(ProductInfoActivity.this, CartActivity.class));
        }
        else if(id == R.id.MyOrders){
            startActivity(new Intent(ProductInfoActivity.this, OrderActivity.class));
        }
        else if(id==R.id.fruits){
            Intent intent =new Intent(ProductInfoActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Fruits");
            startActivity(intent);
        }
        else if(id==R.id.vegetables){
            Intent intent =new Intent(ProductInfoActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Vegetables");
            startActivity(intent);
        }
        else if(id==R.id.meats){
            Intent intent =new Intent(ProductInfoActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Meats");
            startActivity(intent);
        }
        else if(id==R.id.electronics){
            Intent intent =new Intent(ProductInfoActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Electronics");
            startActivity(intent);
        }
        else if(id==R.id.Logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ProductInfoActivity.this,loginActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}