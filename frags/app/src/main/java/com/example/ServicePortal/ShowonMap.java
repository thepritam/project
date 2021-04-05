package com.example.ServicePortal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.net.Uri;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowonMap extends AppCompatActivity implements OnMapReadyCallback{

    Location currentlocation ;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;
    public double lat;
    public double longi;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showon_map);

        Intent intent = getIntent();
        lat = intent.getDoubleExtra("Latitude",0.0);
        longi = intent.getDoubleExtra("Longitude",0.0);
        Log.d("de_map",""+lat+"");
        Log.d("de_map",""+longi+"");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        Button dirbutton = findViewById(R.id.dirbutton);

        dirbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("gotopos",""+CheckAvailability.lat+"");
                Log.d("gotopos",""+CheckAvailability.longi+"");
                String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+CheckAvailability.lat+","+CheckAvailability.longi+"&daddr="+lat+","+longi;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(Intent.createChooser(intent, "Select an application"));


            }
        });

//
//        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
//
//        fetchlastlocation();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng dest = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(dest).title("Your Destination"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 10));

    }

//    private void fetchlastlocation() {
//        Log.d("show", "fetchlastlocation: ");
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,new String[]
//                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE
//            );
//
//            return;
//        }
//        Task<Location> task = fusedLocationProviderClient.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if(location != null){
//                    currentlocation=location;
//                    Toast.makeText(getApplicationContext(),currentlocation.getLatitude()+ " " + currentlocation.getLongitude(),Toast.LENGTH_SHORT).show();
//                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
//
//                    assert supportMapFragment != null;
//                    supportMapFragment.getMapAsync(ShowonMap.this);
//                }
//            }
//        });
//    }

//    public double  getDistanceFromLatLonInKm(double lat1,double lon1,double lat2,double lon2) {
//        int  R = 6371; // Radius of the earth in km
//        double dLat = deg2rad(lat2-lat1);  // deg2rad below
//        double dLon = deg2rad(lon2-lon1);
//        double a =
//                Math.sin(dLat/2) * Math.sin(dLat/2) +
//                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
//                                Math.sin(dLon/2) * Math.sin(dLon/2)
//                ;
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//        double d = R * c; // Distance in km
//        return d;
//    }
//
//    double  deg2rad(double deg) {
//        return deg * (Math.PI/180);
//    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        LatLng latLng = new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Destination");
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,5));
//        googleMap.addMarker(markerOptions);
//        Log.d("show", "onMapReady: ");
//        /*lat1=(double)lat.get(0);long1=(double)longe.get(0);
//        lat2=(double)lat.get(1);long2=(double)longe.get(1);
//        lat3=(double)lat.get(2);long3=(double)longe.get(2);*/
//            Marker m = googleMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(lat,longi))
//                    .anchor(0.5f, 0.5f)
//                    .title("Title")
//                    .snippet("Snippet1"));
//
//      /*  Marker m1 = googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(lat1,long1))
//                .anchor(0.5f, 0.5f)
//                .title("Title1")
//                .snippet("Snippet1"));
//        Marker m2 = googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(lat2,long2))
//                .anchor(0.5f, 0.5f)
//                .title("Title2")
//                .snippet("Snippet2"));
//        Marker m3 = googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(lat3,long3))
//                .anchor(0.5f, 0.5f)
//                .title("Title3")
//                .snippet("Snippet3"));*/
//
//
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (REQUEST_CODE){
//            case REQUEST_CODE :
//                if (grantResults.length>0&&grantResults[0] ==PackageManager.PERMISSION_GRANTED){
//                    fetchlastlocation();
//                }
//                break;
//        }
//    }
}
