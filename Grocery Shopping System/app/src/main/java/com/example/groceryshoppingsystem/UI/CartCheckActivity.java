package com.example.groceryshoppingsystem.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import de.hdodenhof.circleimageview.CircleImageView;

public class CartCheckActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    String ttlPrice , DelPrice , ttlPrice2 , saved;
    DatabaseReference root;
    String CurrentUser;
    TextView totalPrice , DelivaryPrice , totalPrice2 , savedamount ;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView mPerson_name;
    private CircleImageView mPerson_image;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUsr;
    private String UserId;
    //Custom Xml Views (cart Icon)
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle;
    private TextView CustomCartNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_check);
        mAuth = FirebaseAuth.getInstance();
        CurrentUsr = mAuth.getCurrentUser();
        UserId = CurrentUsr.getUid();

        mToolbar = (Toolbar)findViewById(R.id.cartCheckToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button Done = findViewById(R.id.Done);
        Button Cancel = findViewById(R.id.Cancel);
        totalPrice = findViewById(R.id.Ttl_ItemsPrice);
        DelivaryPrice = findViewById(R.id.DelivaryPrice);
        totalPrice2 = findViewById(R.id.TotalAmount);
        savedamount=findViewById(R.id.SavedAmount);


        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedDate();
                CartActivity.fa.finish();
                finish();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getCheckData();

    }


    private void  getCheckData(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth ;
        mAuth = FirebaseAuth.getInstance();
        String CurrentUser = mAuth.getCurrentUser().getUid();
        DatabaseReference m = root.child("cart").child(CurrentUser);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ttlPrice = dataSnapshot.child("totalPrice").getValue().toString();
                    Log.d("ttl" , ttlPrice);
                    totalPrice.setText(ttlPrice);
                    DelivaryPrice.setText("Free");
                    totalPrice2.setText(ttlPrice);
                    savedamount.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }
    private void savedDate()
    {
        root = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth ;
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        DatabaseReference x = root.child("cart").child(CurrentUser);
        x.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseDatabase t = FirebaseDatabase.getInstance();
                String key  = t.getReference("order").push().getKey();
                root.child("order").child(CurrentUser).child(key).child("orderproducts").setValue(snapshot.getValue());
                root.child("order").child(CurrentUser).child(key).child("totalPrice").setValue(snapshot.child("totalPrice").getValue());
                root.child("order").child(CurrentUser).child(key).child("orderproducts").child("totalPrice").removeValue();
                root.child("order").child(CurrentUser).child(key).child("Date").setValue(String.valueOf(new SimpleDateFormat("dd MMM yyyy hh:mm a").format(Calendar.getInstance().getTime())));
                root.child("order").child(CurrentUser).child(key).child("IsChecked").setValue("false");
                Toast.makeText( getApplicationContext() ,"Confermed Completed" , Toast.LENGTH_LONG).show();
                root.child("cart").child(CurrentUser).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        drawerLayout = (DrawerLayout) findViewById(R.id.cartCheckDrawer);
        navigationView = (NavigationView) findViewById(R.id.cartCheckNavigationViewer);

        //navigation header
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        mPerson_name = view.findViewById(R.id.persname);
        mPerson_image = view.findViewById(R.id.circimage);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getNavHeaderData();

        //Refresh CartIcon
        showCartIcon();

        //to check if the total price is zero or not
        HandleTotalPriceToZeroIfNotExist();

    }


    private void getNavHeaderData(){
        FirebaseAuth mAuth ;
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("users").child(CurrentUser);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.Home){
            startActivity(new Intent(CartCheckActivity.this,MainActivity.class));
        }
        else if(id==R.id.Profile){
            startActivity(new Intent(CartCheckActivity.this,UserProfileActivity.class));
        }
        else if(id == R.id.favourites){
            startActivity(new Intent(CartCheckActivity.this, favourites_activity.class));
        }
        else if(id == R.id.Cart){
            startActivity(new Intent(CartCheckActivity.this, CartActivity.class));
        }
        else if(id == R.id.MyOrders){
            startActivity(new Intent(CartCheckActivity.this, OrderActivity.class));
        }
        else if(id==R.id.fruits){
            Intent intent =new Intent(CartCheckActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Fruits");
            startActivity(intent);
        }
        else if(id==R.id.vegetables){
            Intent intent =new Intent(CartCheckActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Vegetables");
            startActivity(intent);
        }
        else if(id==R.id.meats){
            Intent intent =new Intent(CartCheckActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Meats");
            startActivity(intent);
        }
        else if(id==R.id.electronics){
            Intent intent =new Intent(CartCheckActivity.this,CategoryActivity.class);
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
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(CartCheckActivity.this);
        checkAlert.setMessage("Do you want to Logout?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(CartCheckActivity.this,loginActivity.class);
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

        PageTitle.setText("Check Order");
        setNumberOfItemsInCartIcon();

        CustomCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartCheckActivity.this, CartActivity.class));
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
