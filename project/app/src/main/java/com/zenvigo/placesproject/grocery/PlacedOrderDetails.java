package com.project.placesproject.grocery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.placesproject.R;

import java.util.HashMap;

public class PlacedOrderDetails extends AppCompatActivity implements OnMapReadyCallback {

    TextView ridername,riderphone;
    RatingBar ratingbarrider;
    String name,riderid,orderid,invoiceid;
    long phone;
    int rating;
    private GoogleMap mMap;
    Button ordersummary,qrcode;
    double latitude,longitude;
    private HashMap<String, Marker> mMarkers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order_details);
        ridername = (TextView)findViewById(R.id.ridername);
        riderphone = (TextView)findViewById(R.id.riderphone);
        ratingbarrider = (RatingBar)findViewById(R.id.ratingBarrider);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(PlacedOrderDetails.this);


        name = getIntent().getStringExtra("ridername");
        orderid = getIntent().getStringExtra("orderid");
        riderid = getIntent().getStringExtra("riderid");
        phone = Long.parseLong(getIntent().getStringExtra("riderphone"));
        latitude = Double.parseDouble(getIntent().getStringExtra("riderlatitude"));
        longitude = Double.parseDouble(getIntent().getStringExtra("riderlongitude"));
        rating  = Integer.parseInt(getIntent().getStringExtra("riderrating"));
        invoiceid = getIntent().getStringExtra("invoiceid");

        ridername.setText(name);
        riderphone.setText(Double.toString(phone));
        ratingbarrider.setRating(rating);



        qrcode = (Button)findViewById(R.id.qrcodebtn);

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlacedOrderDetails.this,Qrcodescangrocery.class);
                intent.putExtra("invoiceid",invoiceid);
                startActivity(intent);
            }
        });






    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Authenticate with Firebase when the Google map is loaded
        mMap = googleMap;
        mMap.setMaxZoomPreference(12);
        subscribeToUpdates();
    }


    private void subscribeToUpdates() {
        String path = "Users/"+riderid;
      DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Child", "Child added"+dataSnapshot);
                if(dataSnapshot.getKey().toString().equals(riderid)) {

                    setMarker(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Child", "Child changed"+dataSnapshot);
                if(dataSnapshot.getKey().toString().equals(riderid)) {

                    setMarker(dataSnapshot);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("maptrack", "Failed to read value.", error.toException());
            }
        });
    }

    private void setMarker(DataSnapshot dataSnapshot) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once

        Log.d("datasnapshot",""+dataSnapshot);
        String key = dataSnapshot.getKey();

        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
        double lat = Double.parseDouble(value.get("Latitude").toString());
        double lng = Double.parseDouble(value.get("Longitude").toString());
        Log.d("locationSentLat", ""+lat+"");
        Log.d("locationSentLong", ""+lng+"");
        LatLng location = new LatLng(lat, lng);
        if (!mMarkers.containsKey(key)) {
            mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location)));
        } else {
            mMarkers.get(key).setPosition(location);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkers.values()) {
            builder.include(marker.getPosition());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
    }


}

