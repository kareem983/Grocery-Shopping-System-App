package com.example.groceryshoppingsystem.UI;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.groceryshoppingsystem.Model.Product;
import com.example.groceryshoppingsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddProduct extends AppCompatActivity {
    private TextInputEditText name, quantity, price, expDate;
    private Button add, choose;
    private ImageView img;
    private Uri imgUri;
    private String category;
    private StorageReference mStorageRef;
    private Spinner spinner;
    private StorageTask mUploadTask;
    private TextInputLayout nameLayout, quantityLayout, priceLayout, expDateLayout;
    private Toolbar mToolBar;
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        //tool bar
        mToolBar = (Toolbar)findViewById(R.id.AddProduct_ToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Add Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (TextInputEditText) findViewById(R.id.editTextProductName);
        quantity =(TextInputEditText) findViewById(R.id.editTextProductNumber);
        add = (Button) findViewById(R.id.btnAdd);
        choose = (Button)findViewById(R.id.btnChooseImg);
        img = (ImageView) findViewById(R.id.imgProduct);
        price =(TextInputEditText) findViewById(R.id.editTextProductPrice);
        expDate =(TextInputEditText) findViewById(R.id.editTextProductExpire);
        spinner = (Spinner)findViewById(R.id.spinner);

        nameLayout = (TextInputLayout) findViewById(R.id.editTextProductNameLayout);
        quantityLayout = (TextInputLayout) findViewById(R.id.editTextProductNumberLayout);
        priceLayout = (TextInputLayout) findViewById(R.id.editTextProductPriceLayout);
        expDateLayout = (TextInputLayout) findViewById(R.id.editTextProductExpireLayout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.productstypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference("products");


        nameLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (name.getText().toString().trim().isEmpty()) {
                    nameLayout.setErrorEnabled(true);
                    nameLayout.setError("Please Enter Offer Name");
                } else {
                    nameLayout.setErrorEnabled(false);
                }
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (name.getText().toString().trim().isEmpty()) {
                    nameLayout.setErrorEnabled(true);
                    nameLayout.setError("Please Enter Offer Name");
                } else {
                    nameLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        quantityLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (quantity.getText().toString().trim().isEmpty()) {
                    quantityLayout.setErrorEnabled(true);
                    quantityLayout.setError("Please Enter Offer Name");
                } else {
                    quantityLayout.setErrorEnabled(false);
                }
            }
        });

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (quantity.getText().toString().trim().isEmpty()) {
                    quantityLayout.setErrorEnabled(true);
                    quantityLayout.setError("Please Enter Offer Name");
                } else {
                    quantityLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        priceLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (price.getText().toString().trim().isEmpty()) {
                    priceLayout.setErrorEnabled(true);
                    priceLayout.setError("Please Enter Offer Name");
                } else {
                    priceLayout.setErrorEnabled(false);
                }
            }
        });

        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (price.getText().toString().trim().isEmpty()) {
                    priceLayout.setErrorEnabled(true);
                    priceLayout.setError("Please Enter Offer Name");
                } else {
                    priceLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        expDateLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (expDate.getText().toString().trim().isEmpty()) {
                    expDateLayout.setErrorEnabled(true);
                    expDateLayout.setError("Please Enter Offer Name");
                } else {
                    expDateLayout.setErrorEnabled(false);
                }
            }
        });

        expDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (expDate.getText().toString().trim().isEmpty()) {
                    expDateLayout.setErrorEnabled(true);
                    expDateLayout.setError("Please Enter Offer Name");
                } else {
                    expDateLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress())
                    Toast.makeText(AddProduct.this, "Upload Is In Progress", Toast.LENGTH_SHORT).show();
                else if (name.getText().toString().isEmpty() || quantity.getText().toString().isEmpty() || price.getText().toString().isEmpty() || expDate.getText().toString().isEmpty() || imgUri == null){
                    Toast.makeText(AddProduct.this, "Empty Cells", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadData();
                    Toast.makeText(AddProduct.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        NotshowCartIcon();
    }

    public void uploadData() {
        if (name.getText().toString().isEmpty() || quantity.getText().toString().isEmpty() || price.getText().toString().isEmpty() || expDate.getText().toString().isEmpty() || imgUri == null) {
            Toast.makeText(AddProduct.this, "Empty Cells", Toast.LENGTH_SHORT).show();
        } else {
            uploadImage();
        }
    }

    public void uploadImage() {
        if (imgUri != null) {
            StorageReference fileReference = mStorageRef.child(name.getText().toString() + "." + getFileExtension(imgUri));
            mUploadTask = fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    Product product = new Product(quantity.getText().toString().trim(),
                            price.getText().toString().trim(),
                            expDate.getText().toString().trim(),
                            downloadUrl.toString());
                    DatabaseReference z = FirebaseDatabase.getInstance().getReference()
                            .child("product")
                            .child(category)
                            .child(name.getText().toString());
                    z.setValue(product);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProduct.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void openImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, RegisterActivity.GALARY_PICK);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RegisterActivity.GALARY_PICK && resultCode == Activity.RESULT_OK && data.getData() != null && data != null) {
            imgUri = data.getData();
            try {
                Picasso.get().load(imgUri).fit().centerCrop().into(img);
            } catch (Exception e) {
                Log.e(this.toString(), e.getMessage().toString());
            }

        }
    }

    private void NotshowCartIcon(){
        //toolbar & cartIcon
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.main2_toolbar,null);
        //actionBar.setCustomView(view);

        //************custom action items xml**********************
        CustomCartContainer = (RelativeLayout)findViewById(R.id.CustomCartIconContainer);
        PageTitle =(TextView)findViewById(R.id.PageTitle);
        PageTitle.setVisibility(View.GONE);
        CustomCartContainer.setVisibility(View.GONE);

    }


}