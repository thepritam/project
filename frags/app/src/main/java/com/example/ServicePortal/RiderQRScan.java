package com.example.ServicePortal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

public class RiderQRScan extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    static final Integer CAMERA = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_qrscan);

        Intent intent = getIntent();
        long Assigned = intent.getLongExtra("Assigned",0);
        long Completed = intent.getLongExtra("Completed",0);
        String Category = intent.getStringExtra("Category");
        String AssignedId = intent.getStringExtra("AssignedId");
        final String CId = intent.getStringExtra("CId");
        final String OrderId = intent.getStringExtra("Uid");
        String Description = intent.getStringExtra("Description");
        double Latitude = intent.getDoubleExtra("Latitude",0);
        double Longitude = intent.getDoubleExtra("Longitude",0);
        String AudioDescription = intent.getStringExtra("AudioDescription");
        boolean hasPicked = intent.getBooleanExtra("hasPicked", false);
        boolean hasDropped = intent.getBooleanExtra("hasDropped", false);
        String Uid = intent.getStringExtra("Uid");


        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        askForPermission(Manifest.permission.CAMERA, CAMERA);

        if(!hasPicked)
        {
            Toast.makeText(getApplicationContext(),"PICK UP SCAN",Toast.LENGTH_LONG).show();
        }
        else if(!hasDropped)
        {
            Toast.makeText(getApplicationContext(),"DROP OFF SCAN",Toast.LENGTH_LONG).show();
        }

//        intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra("Assigned", Assigned);
//        intent.putExtra("Completed", Completed);
//        intent.putExtra("Category", Category);
//        intent.putExtra("AssignedId", AssignedId);
//        intent.putExtra("CId", CId);
//        intent.putExtra("Description", Description);
//        intent.putExtra("Latitude", Latitude);
//        intent.putExtra("Longitude", Longitude);
//        intent.putExtra("AudioDescription", AudioDescription);
//        intent.putExtra("Uid", Uid);
//        startActivity(intent);
    }

    private void askForPermission(String permission, Integer requestCode) {

        if (ContextCompat.checkSelfPermission(RiderQRScan.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(RiderQRScan.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(RiderQRScan.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(RiderQRScan.this, new String[]{permission}, requestCode);
            }
        } else {

            // Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Camera
                case 1:
                    mScannerView.setResultHandler(this);
                    mScannerView.startCamera();
                    break;


            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        Toast.makeText(this, "Contents = " + result.getText() +
                ", Format = " + result.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        // * Wait 3 seconds to resume the preview.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(RiderQRScan.this);
            }
        }, 3000);

    }

}
