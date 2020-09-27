package com.example.groceryshoppingsystem.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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
    //Custom Xml Views (cart Icon)
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle,CustomCartNumber;

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


    private void DefineXmlViews() {
        PImage = (ImageView) findViewById(R.id.ProductImage);
        PIsFav = (ImageView) findViewById(R.id.ProductFav);
        PName = (TextView) findViewById(R.id.ProductName);
        PCategory = (TextView) findViewById(R.id.ProductCategory);
        PAmount = (TextView) findViewById(R.id.ProductAvailableAmount);
        PPrice = (TextView) findViewById(R.id.CurrentProductPrice);
        OldPrice = (TextView) findViewById(R.id.OldProductPrice);
        OfferRate = (TextView) findViewById(R.id.OfferRate);
        OfferContainer = (LinearLayout) findViewById(R.id.OfferContainer);
        PExpiryDate = (TextView) findViewById(R.id.ProductExpiryDate);
        AddToCartContainer = (RelativeLayout) findViewById(R.id.AddToCart);
        DeleteFromCartContainer = (RelativeLayout) findViewById(R.id.DeleteFromCart);
        CheckCartContainer = (RelativeLayout) findViewById(R.id.CheckCartContainer);
        Back = (Button) findViewById(R.id.BackBtn);
        Confirm = (Button) findViewById(R.id.ConformBtn);

        RefreshContainers();
    }

    private void RefreshContainers(){
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

                //Refresh CartIcon
                showCartIcon();

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

                //Refresh CartIcon
                showCartIcon();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        DefineNavigation();

        //Refresh CartIcon
        showCartIcon();

        RefreshContainers();

        //to check if the total price is zero or not
        HandleTotalPriceToZeroIfNotExist();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
            CheckLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void CheckLogout(){
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(ProductInfoActivity.this);
        checkAlert.setMessage("Do you want to Logout?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(ProductInfoActivity.this,loginActivity.class);
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

        PageTitle.setText("Product Info");
        setNumberOfItemsInCartIcon();

        CustomCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductInfoActivity.this, CartActivity.class));
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