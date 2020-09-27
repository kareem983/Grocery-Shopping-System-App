package com.example.groceryshoppingsystem.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.groceryshoppingsystem.R;
/*import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
*/
public class ScanQRCodeActivity extends AppCompatActivity {
  /*  private SurfaceView cameraPreview;
    private TextView textView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private final int RequestCameraPermissionID = 1001;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private String UserId;
    private String OrderId;
    private Toolbar mToolBar;
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestCameraPermissionID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_q_r_code);
  /*      mAuth= FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        UserId = CurrentUser.getUid();

        //tool bar
        mToolBar = (Toolbar)findViewById(R.id.QRScanner_TooBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("QR Code Scanner");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NotshowCartIcon();

        OrderId = getIntent().getStringExtra("OrderId");

        cameraPreview = (SurfaceView) findViewById(R.id.surfaceView);
        textView = (TextView) findViewById(R.id.text);


        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480).build();


        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(ScanQRCodeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ScanQRCodeActivity.this,
                            new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                    return;
                }

                try {
                    cameraSource.start(cameraPreview.getHolder());
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {}
            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes =detections.getDetectedItems();

                SearchAboutSalesMan(qrcodes.valueAt(0).displayValue);

                if(qrcodes.size()!=0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator= (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                        }
                    });
                }
            }
        });
*/
    }

/*
    private void SearchAboutSalesMan(String name){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference x = root.child("salesman").child(name);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                   if(dataSnapshot.exists()){
                       FirebaseDatabase.getInstance().getReference().child("order").child(UserId).child(OrderId).child("IsChecked").setValue("true");
                       textView.setText("This Order Received Successfully");
                       OrderFregmant.fa.finish();
                       finish();
                   }
                   else {
                       textView.setText("This Delivery doesn't Exist in our System\nTry Again");
                   }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        x.addListenerForSingleValueEvent(valueEventListener);

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
*/
}