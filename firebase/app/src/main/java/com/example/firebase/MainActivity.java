package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.MissingFormatArgumentException;
import java.util.Vector;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    Location currentlocation ;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;
    Vector lat=new Vector();
    Vector longe= new Vector ();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        FirebaseDatabase.getInstance().getReference().child("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("show", "onDataChange: ");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);

                            assert user != null;
                            if(user.category.equals("Mechanic"))
                            {
                                double latitude = user.latitude;

                                  lat.add(latitude);
                                double longitude = user.longitude;
                                longe.add(longitude);

                            }
                        }
                        fetchlastlocation();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void fetchlastlocation() {
        Log.d("show", "fetchlastlocation: ");
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE
            );

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
               if(location != null){
                   currentlocation=location;
                   Toast.makeText(getApplicationContext(),currentlocation.getLatitude()+ " " + currentlocation.getLongitude(),Toast.LENGTH_SHORT).show();
                   SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

                   assert supportMapFragment != null;
                   supportMapFragment.getMapAsync(MainActivity.this);
               }
            }
        });
    }

    public double  getDistanceFromLatLonInKm(double lat1,double lon1,double lat2,double lon2) {
        int  R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }

    double  deg2rad(double deg) {
        return deg * (Math.PI/180);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng = new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am Here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,5));
        googleMap.addMarker(markerOptions);
        Log.d("show", "onMapReady: ");
        /*lat1=(double)lat.get(0);long1=(double)longe.get(0);
        lat2=(double)lat.get(1);long2=(double)longe.get(1);
        lat3=(double)lat.get(2);long3=(double)longe.get(2);*/
           for (int i=0;i<lat.size();i++){
               Marker m = googleMap.addMarker(new MarkerOptions()
                       .position(new LatLng((double)lat.get(i),(double)longe.get(i)))
                       .anchor(0.5f, 0.5f)
                       .title("Title"+i)
                       .snippet("Snippet1"));
           }
      /*  Marker m1 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat1,long1))
                .anchor(0.5f, 0.5f)
                .title("Title1")
                .snippet("Snippet1"));



        Marker m2 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat2,long2))
                .anchor(0.5f, 0.5f)
                .title("Title2")
                .snippet("Snippet2"));

        Marker m3 = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat3,long3))
                .anchor(0.5f, 0.5f)
                .title("Title3")
                .snippet("Snippet3"));*/


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (REQUEST_CODE){
            case REQUEST_CODE :
                if (grantResults.length>0&&grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    fetchlastlocation();
                }
                break;
        }
    }
}

//
//class Database
//{
//    User user;
//    String Cname;
//
//    public void mReadDataOnce(String Cid, final OnGetDataListener listener) {
////        listener.onStart();
//        FirebaseDatabase.getInstance().getReference().child("Users").child(Cid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                listener.onSuccess(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                listener.onFailed(databaseError);
//            }
//        });
//    }
//}
//
//interface
// OnGetDataListener {
////    public void onStart();
//    public void onSuccess(DataSnapshot data);
//    public void onFailed(DatabaseError databaseError);
//}

//    public void mReadDataOnce(String child, final OnGetDataListener listener) {
//
//        FirebaseDatabase.getInstance().getReference().child("Users").child(Cid).addListenerForSingleValueEvent(new ValueEventListener() {
//             @Override
//             public void onDataChange(DataSnapshot dataSnapshot) {
////                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                 user = dataSnapshot.getValue(User.class);
//                 Log.d("thisCNAME",user.Name);
//                 Cname = user.Name;
//
//                 listener.onSuccess(dataSnapshot);
//
//             }
//
//             @Override
//             public void onCancelled(DatabaseError databaseError) {
//                 listener.onFailed(databaseError);
//             }
//
//         });
//    }
//
//    private void mCheckInforInServer(final View V,String Cid, final datasetfire dsfire) {
//        fvh = new firebaseviewholder(V);
//
//        new Database().mReadDataOnce(Cid , new OnGetDataListener() {
//
//            @Override
//            public void onSuccess(DataSnapshot data) {
//                //DO SOME THING WHEN GET DATA SUCCESS HERE
//                user = data.getValue(User.class);
//                Cname = user.Name;
//                Log.d("Running","Read happened="+Cname);
//                Log.d("Running","Display happened");
//                dsfire.setCname(Cname);
//            }
//
//            @Override
//            public void onFailed(DatabaseError databaseError) {
//                //DO SOME THING WHEN GET DATA FAILED HERE
//                Log.d("onFailed","true");
//            }
//        });
//
//    }
//}

