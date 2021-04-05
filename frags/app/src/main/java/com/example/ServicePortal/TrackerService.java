package com.example.ServicePortal;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import android.util.Log;

public class TrackerService extends Service {


    Timer timer;
    TimerTask task;

    private static final String TAG = TrackerService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public void onCreate() {
        super.onCreate();
        buildNotification();

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {

                FirebaseDatabase.getInstance().getReference().child("Users").child(CheckAvailability.id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                        final User user = dataSnapshot.getValue(User.class);

                        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();


                        if(value.get("IsAvailable").equals("1"))
                        {
//                            Intent serviceIntent = new Intent(getApplicationContext(),TrackerService.class);
//                            stopService(serviceIntent);
//                            Toast.makeText(getApplicationContext(),"AvailStop",Toast.LENGTH_LONG).show();
                            stopSelf();
                            Log.d("availability","availstoptracker");
                        }
                        else
                        {
                            Log.d("availability","updatedforrequest");
                            requestLocationUpdates();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

            }
        };
        timer.schedule(task, 0, (5000));



//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//
//                final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//                Query query = rootRef.collection("Orders");
//
//            }
//
//        };
//        timer.schedule(task,0,(1000));

    }


    @Override
    public void onDestroy() {
        stopSelf();
    }

    private void buildNotification() {
        String stop = "stop";
//        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Your location is accessed")
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.ic_tracker_foreground);
//        startForeground(1, builder.build());
    }

//    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.d(TAG, "received stop broadcast");
//            // Stop the service when the notification is tapped
//            unregisterReceiver(stopReceiver);
//            stopSelf();
//        }
//    };

    private void loginToFirebase() {
        // Authenticate with Firebase, and request location updates
//        String email = getString(R.string.firebase_email);
//        String password = getString(R.string.firebase_password);
//        FirebaseAuth.getInstance().verifyPhoneNumber(
//                email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
//            @Override
//            public void onComplete(Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    Log.d(TAG, "firebase auth success");
//                    requestLocationUpdates();
//                } else {
//                    Log.d(TAG, "firebase auth failed");
//                }
//            }
//        });

        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
//        LocationRequest request = new LocationRequest();
//        request.setInterval(10000);
//        request.setFastestInterval(5000);

//                LocationRequest request = new LocationRequest();
//
//                request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                final FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
//                int permission = ContextCompat.checkSelfPermission(getApplicationContext(),
//                        Manifest.permission.ACCESS_FINE_LOCATION);
//                if (permission == PackageManager.PERMISSION_GRANTED) {
//                    // Request location updates and when an update is
//                    // received, store the location in Firebase
//                    client.requestLocationUpdates(request, new LocationCallback() {
//                        @Override
//                        public void onLocationResult(LocationResult locationResult) {
//                            Location location = locationResult.getLastLocation();
//                            if (location != null) {
//                                Log.d("availability", "LocationUpdated");
//                                stopSelf();
//
//                                FirebaseDatabase.getInstance().getReference().child("Users").child(CheckAvailability.id).child("Latitude").setValue(location.getLatitude());
//                                FirebaseDatabase.getInstance().getReference().child("Users").child(CheckAvailability.id).child("Longitude").setValue(location.getLongitude());
//                            }
//                        }
//                    }, null);
//                }

                FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getApplicationContext());

                int permission = ContextCompat.checkSelfPermission(getApplicationContext(),
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
                                double longitude = location.getLongitude();
                                double latitude = location.getLatitude();
                                Log.d("mypos", "" + longitude + "");
                                Log.d("mypos", "" + latitude + "");
                                FirebaseDatabase.getInstance().getReference().child("Users").child(CheckAvailability.id).child("Latitude").setValue(latitude);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(CheckAvailability.id).child("Longitude").setValue(longitude);
                            }
                        }
                    }, null);
                }

            }

    }
