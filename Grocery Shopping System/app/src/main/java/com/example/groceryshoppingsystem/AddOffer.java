package com.example.groceryshoppingsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;

public class AddOffer extends AppCompatActivity {

    private EditText name , description;
    private Button add , choose;
    private ImageView img;
    private Uri imgUri;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        name = findViewById(R.id.editTextOfferName);
        description = findViewById(R.id.editTextOfferDescription);
        add = findViewById(R.id.btnAddOffer);
        choose = findViewById(R.id.btnChooseOfferImg);
        img = findViewById(R.id.offerImage);

        mStorageRef = FirebaseStorage.getInstance().getReference("offers");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

    }

    public void uploadData()
    {
        if(name.getText().toString().isEmpty() || description.getText().toString().isEmpty() || imgUri == null)
        {
            Toast.makeText(AddOffer.this, "Empty Cells", Toast.LENGTH_SHORT).show();
        }
        else
        {
            uploadImage();
        }
    }

    public void uploadImage()
    {
        if(imgUri != null)
        {
            StorageReference fileReference = mStorageRef.child(name.getText().toString() + "." + getFileExtension(imgUri));
            fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();
                    Offer offer = new Offer(description.getText().toString().trim() ,
                            downloadUrl.toString());
                    DatabaseReference z = FirebaseDatabase.getInstance().getReference("offers");
                    z.child(name.getText().toString().trim()).setValue(offer);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    public void openImage()
    {
        Intent i =  new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i , RegisterActivity.GALARY_PICK);
    }

    public String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RegisterActivity.GALARY_PICK && resultCode == Activity.RESULT_OK && data != null)
        {
            imgUri = data.getData();
            try {
                Picasso.get().load(imgUri).into(img);
            } catch (Exception e) {
                Log.e(this.toString() , e.getMessage().toString());
            }
        }
    }

}