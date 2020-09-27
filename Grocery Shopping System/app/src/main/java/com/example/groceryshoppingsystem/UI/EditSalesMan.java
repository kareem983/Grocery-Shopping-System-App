package com.example.groceryshoppingsystem.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.groceryshoppingsystem.Model.SalesMan;
import com.example.groceryshoppingsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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

public class EditSalesMan extends AppCompatActivity {
    private TextInputEditText name , salary;
    private Button edit , choose;
    private ImageView img;
    private Uri imgUri;
    private StorageReference mStorageRef;
    private DatabaseReference mdataReference;
    private StorageTask mUploadTask;
    private String oldName , oldImagePath , oldQrPath;
    private byte[] oldImageBytes = null;
    private Toolbar mToolBar;
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sales_man);
        //tool bar
        mToolBar = (Toolbar)findViewById(R.id.EditSalesMen_ToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Edit SalesMan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mStorageRef = FirebaseStorage.getInstance().getReference("salesman");
        mdataReference = FirebaseDatabase.getInstance().getReference("salesman");

        name = findViewById(R.id.editTextSalesManNameEdit);
        salary = findViewById(R.id.editTextSalesManSalaryEdit);
        edit = findViewById(R.id.btnAddSalesManEdit);
        choose = findViewById(R.id.btnChooseSalesManImageEdit);
        img = findViewById(R.id.salesManImageEdit);

        Bundle b = getIntent().getExtras();

        oldName = b.getString("name");
        oldImagePath = b.getString("img");
        oldQrPath = b.getString("qrimg");
        name.setText(oldName);
        salary.setText(b.getString("salary"));
        Picasso.get().load(oldImagePath).fit().centerCrop().into(img);
        imgUri = Uri.parse(oldImagePath);

        StorageReference httpsRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldImagePath);
        final long ONE_MEGABYTE = 1024 * 1024 * 1024;
        httpsRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                oldImageBytes = bytes;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditSalesMan.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress())
                    Toast.makeText(EditSalesMan.this, "Upload Is In Progress", Toast.LENGTH_SHORT).show();
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditSalesMan.this).setTitle("Confirmation").setMessage("Are You Sure You Want To Save ?!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteImage();
                            deleteQr();
                            deleteData();
                            uploadData();
                            uploadQr();
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert);
                    dialog.show();
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

    @Override
    protected void onStart() {
        super.onStart();

        NotshowCartIcon();
    }

    public void uploadData()
    {
        if(name.getText().toString().isEmpty() || salary.getText().toString().isEmpty() || imgUri == null)
        {
            Toast.makeText(EditSalesMan.this, "Empty Cells", Toast.LENGTH_SHORT).show();
        }
        else
            uploadImage();
    }

    public void deleteData()
    {
        DatabaseReference reference = mdataReference.child(oldName);
        reference.removeValue();
    }

    public void deleteImage()
    {

        StorageReference a = mStorageRef.child(oldName + ".jpg");
        a.delete();

        StorageReference z = mStorageRef.child(oldName);
        z.delete();

    }

    public void deleteQr()
    {
        StorageReference b = mStorageRef.child(oldName + "qr");
        b.delete();
        StorageReference x = mStorageRef.child(oldName + "qr.jpg");
        x.delete();
    }

    public void uploadQr()
    {
        Bitmap bitmap = QrGenerate(name.getText().toString().trim());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        StorageReference fileReference = mStorageRef.child(name.getText().toString().trim() + "qr." + "jpg");
        mUploadTask = fileReference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();

                DatabaseReference z = FirebaseDatabase.getInstance().getReference("salesman")
                        .child(name.getText().toString().trim())
                        .child("qrimage");
                z.setValue(downloadUrl.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditSalesMan.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadImage()
    {
        if(imgUri.toString().equals(oldImagePath))
        {
            StorageReference fileReference = mStorageRef.child(name.getText().toString());
            mUploadTask = fileReference.putBytes(oldImageBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();

                    DatabaseReference a = mdataReference.child(name.getText().toString().trim());
                    DatabaseReference b = mdataReference.child(name.getText().toString().trim());
                    DatabaseReference c = mdataReference.child(name.getText().toString().trim());
                    a.child("img").setValue(downloadUrl.toString());
                    c.child("salary").setValue(salary.getText().toString().trim());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditSalesMan.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(imgUri != null)
        {
            StorageReference fileReference = mStorageRef.child(name.getText().toString() + "." + getFileExtension(imgUri));
            mUploadTask = fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                    SalesMan man = new SalesMan(salary.getText().toString().trim() ,
                            downloadUrl.toString());
                    DatabaseReference z = FirebaseDatabase.getInstance().getReference("salesman")
                            .child(name.getText().toString().trim());
                    z.setValue(man);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditSalesMan.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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