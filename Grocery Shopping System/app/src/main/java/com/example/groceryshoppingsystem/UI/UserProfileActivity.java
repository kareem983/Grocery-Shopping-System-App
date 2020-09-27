package com.example.groceryshoppingsystem.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.groceryshoppingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar;
    private TextView mPerson_name;
    private CircleImageView mPerson_image;
    //----------------------------
    private CircleImageView UserImage;
    private TextView UserName, UserEmail, UserPhone, UserFavorites, UserOrders;
    private ProgressBar progressBar;
    private final int GALARY_PICK=1;
    //----------------------------
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private String UserId;
    private StorageReference mStorageReference;
    //Custom Xml Views (cart Icon)
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle, CustomCartNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mAuth=FirebaseAuth.getInstance();
        CurrentUser =mAuth.getCurrentUser();
        UserId =CurrentUser.getUid();
        mStorageReference= FirebaseStorage.getInstance().getReference();

        //toolbar
        mToolBar = findViewById(R.id.UserProfileToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //define xml data
        UserImage =(CircleImageView)findViewById(R.id.UserImage);
        UserName = (TextView)findViewById(R.id.UserName);
        UserEmail =(TextView)findViewById(R.id.UserEmail);
        UserPhone =(TextView)findViewById(R.id.UserPhone);
        UserFavorites= (TextView)findViewById(R.id.UserFavorite);
        UserOrders= (TextView)findViewById(R.id.UserOrders);
        progressBar =(ProgressBar)findViewById(R.id.ProfileprogressBar);

        //get User Profile Data
        getUserProfileData();

        UserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImage();
            }
        });

        UserFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, favourites_activity.class));
            }
        });

        UserOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, OrderActivity.class));
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        //define Navigation Viewer and got its data
        DefineNavigation();

        //Refresh CartIcon
        showCartIcon();

        //to check if the total price is zero or not
        HandleTotalPriceToZeroIfNotExist();
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
            startActivity(new Intent(UserProfileActivity.this,MainActivity.class));
        }
        else if(id == R.id.favourites){
            startActivity(new Intent(UserProfileActivity.this, favourites_activity.class));
        }
        else if(id == R.id.Cart){
            startActivity(new Intent(UserProfileActivity.this, CartActivity.class));
        }
        else if(id == R.id.MyOrders){
            startActivity(new Intent(UserProfileActivity.this, OrderActivity.class));
        }
        else if(id==R.id.fruits){
            Intent intent =new Intent(UserProfileActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Fruits");
            startActivity(intent);
        }
        else if(id==R.id.vegetables){
            Intent intent =new Intent(UserProfileActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Vegetables");
            startActivity(intent);
        }
        else if(id==R.id.meats){
            Intent intent =new Intent(UserProfileActivity.this,CategoryActivity.class);
            intent.putExtra("Category Name","Meats");
            startActivity(intent);
        }
        else if(id==R.id.electronics){
            Intent intent =new Intent(UserProfileActivity.this,CategoryActivity.class);
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
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(UserProfileActivity.this);
        checkAlert.setMessage("Do you want to Logout?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(UserProfileActivity.this,loginActivity.class);
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

    private void DefineNavigation(){
        drawerLayout = (DrawerLayout) findViewById(R.id.UserProfileDrawer);
        navigationView = (NavigationView) findViewById(R.id.UserProfileNavigation);

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


    private void getUserProfileData(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("users").child(UserId);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String Name = snapshot.child("Name").getValue().toString();
                    String Image = snapshot.child("Image").getValue().toString();
                    String Phone = snapshot.child("Phone").getValue().toString();
                    UserName.setText(Name);
                    UserPhone.setText(Phone);
                    UserEmail.setText(CurrentUser.getEmail().toString());

                    if (Image.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(UserImage);
                    } else
                        Picasso.get().load(Image).placeholder(R.drawable.profile).into(UserImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }


    private void loadImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"), GALARY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //to crop image
        if (requestCode == GALARY_PICK && resultCode == RESULT_OK) {
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                progressBar.setVisibility(View.VISIBLE);
                UploadImageInStorageDataBase(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    private void UploadImageInStorageDataBase(Uri resultUri){
        final StorageReference FilePath =mStorageReference.child("users_image").child(UserId+"jpg");

        FilePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                FilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(UserId);
                        mUserDatabase.child("Image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Picasso.get().load(uri.toString()).placeholder(R.drawable.profile).into(UserImage);
                                progressBar.setVisibility(View.GONE);
                                getNavHeaderData();
                            }
                        });
                    }
                });
            }
        });

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

        PageTitle.setText("My Profile");
        setNumberOfItemsInCartIcon();

        CustomCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, CartActivity.class));
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