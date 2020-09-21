package com.example.groceryshoppingsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class AddSalesMan extends AppCompatActivity {

    private EditText name , salary;
    private Button add , choose;
    private ImageView img;
    private Uri imgUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDataBaseRef;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales_man);

        mStorageRef = FirebaseStorage.getInstance().getReference("salesman");
        mDataBaseRef = FirebaseDatabase.getInstance().getReference("salesman");

        name = findViewById(R.id.editTextSalesManName);
        salary = findViewById(R.id.editTextSalesManSalary);
        add = findViewById(R.id.btnAddSalesMan);
        choose = findViewById(R.id.btnChooseSalesManImage);
        img = findViewById(R.id.salesManImage);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUploadTask != null && mUploadTask.isInProgress())
                    Toast.makeText(AddSalesMan.this, "Upload Is In Progress", Toast.LENGTH_SHORT).show();
                else
                {
                    uploadData();
                    uploadQr();
                    Toast.makeText(AddSalesMan.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
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
        if(name.getText().toString().isEmpty() || salary.getText().toString().isEmpty() || imgUri == null)
        {
            Toast.makeText(AddSalesMan.this, "Empty Cells", Toast.LENGTH_SHORT).show();
        }
        else
            uploadImage();
    }

    public void uploadQr()
    {
        Bitmap bitmap = QrGenerate(name.getText().toString().trim());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        StorageReference fileReference = mStorageRef.child(name.getText().toString().trim() + "qr." + "jpg");
        fileReference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();
                DatabaseReference z = mDataBaseRef.child(name.getText().toString().trim())
                        .child("qrimage");
                z.setValue(downloadUrl.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddSalesMan.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadImage()
    {
        if(imgUri != null)
        {
            StorageReference fileReference = mStorageRef.child(name.getText().toString().trim() + "." + getFileExtension(imgUri));
            mUploadTask = fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();
                    DatabaseReference z = mDataBaseRef.child(name.getText().toString().trim())
                            .child("img");
                    z.setValue(downloadUrl.toString());
                    DatabaseReference x = mDataBaseRef.child(name.getText().toString().trim())
                            .child("salary");
                    x.setValue(salary.getText().toString().trim());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddSalesMan.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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

    public Bitmap QrGenerate(String x)
    {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        QRGEncoder encoder = new QRGEncoder(x , null , QRGContents.Type.TEXT , smallerDimension);
        return encoder.getBitmap();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RegisterActivity.GALARY_PICK && resultCode == Activity.RESULT_OK && data.getData() != null && data != null)
        {
            imgUri = data.getData();

            try {
                Picasso.get().load(imgUri).fit().centerCrop().into(img);
            } catch (Exception e) {
                Log.e(this.toString() , e.getMessage().toString());
            }

        }
    }
}