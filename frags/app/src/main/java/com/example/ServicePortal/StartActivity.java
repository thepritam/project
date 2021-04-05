package com.example.ServicePortal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class StartActivity extends AppCompatActivity {

    private LocationManager locationManager;
    public double longitude;
    public double latitude;
    FirebaseStorage storage;
    StorageReference storageReference;
    HashMap<String, Object> catvalue;
    HashMap<String, Object> value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        final TextView NameStart, PhoneStart, EmailStart, CategoryStart;
        final Button Start_Continue = findViewById(R.id.Start_Continue);
        ImageView avatar;

        NameStart = (TextView) findViewById(R.id.NameStart);
        PhoneStart = (TextView) findViewById(R.id.PhoneStart);
        EmailStart = (TextView) findViewById(R.id.EmailStart);
        CategoryStart = (TextView) findViewById(R.id.CategoryStart);
        avatar = (ImageView) findViewById(R.id.avatar_img);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    Activity#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for Activity#requestPermissions for more details.
//            return;
//        }
//        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
//
//        onLocationChanged(location);
//
//        if (locationManager != null) {
//            location = locationManager
//                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (location != null) {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//            }

//
//        Log.d("mypos", "" + longitude + "");
//        Log.d("mypos", "" + latitude + "");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        StorageReference ref = storageReference.child("images/");

//        ref.child(currentUid+"-Avatar").putFile(filePath[0]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
//            {
//                FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Document").child("Avatar").setValue(currentUid+"-Avatar");
//
//            }
//        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
                value = (HashMap<String, Object>) dataSnapshot.getValue();

                NameStart.setText(value.get("Name").toString());
                PhoneStart.setText(value.get("Phone").toString());
                EmailStart.setText(value.get("Email").toString());
//                CategoryStart.setText(value.get("Category").toString());

                dataSnapshot.getRef().child("Latitude").setValue(latitude);
                dataSnapshot.getRef().child("Longitude").setValue(longitude);

//                Start_Continue.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        finish();
//                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
//                        intent.putExtra("availcheck", value.get("IsAvailable").toString());
//                        intent.putExtra("id", id);
//                        intent.putExtra("Category", catvalue.get("Category").toString());
//                        intent.putExtra("lat", latitude);
//                        intent.putExtra("longi", longitude);
//                        startActivity(intent);
//                    }
//                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
                catvalue = (HashMap<String, Object>) dataSnapshot.getValue();

                CategoryStart.setText(catvalue.get("Type").toString());

                dataSnapshot.getRef().child("Latitude").setValue(latitude);
                dataSnapshot.getRef().child("Longitude").setValue(longitude);

                Start_Continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        intent.putExtra("availcheck", value.get("IsAvailable").toString());
                        intent.putExtra("id", id);
                        intent.putExtra("Category", catvalue.get("Type").toString());
                        intent.putExtra("lat", latitude);
                        intent.putExtra("longi", longitude);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

                    int permission = ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION);

                    LocationRequest request = new LocationRequest();

                    if (permission == PackageManager.PERMISSION_GRANTED) {
                        // Request location updates and when an update is
                        // received, store the location in Firebase
                        client.requestLocationUpdates(request, new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location = locationResult.getLastLocation();
                                if (location != null) {
                                    longitude = location.getLongitude();
                                    latitude = location.getLatitude();
                                    Log.d("mypos1", "" + longitude + "");
                                    Log.d("mypos1", "" + latitude + "");
                                }
                            }
                        }, null);
                    }

                    return;
                }
                // other 'case' lines to check for other
                // permissions this app might request
            }
        }
    }
}

