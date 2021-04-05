package com.project.placesproject.tutor;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.placesproject.R;


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

    public class servicetutor extends Service {


        String orderid;
        private static final String TAG = "show";

        @Override
        public IBinder onBind(Intent intent) {return null;}

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStartCommand(intent, flags, startId);
            Log.d("count", "Received start id " + startId + ": " + intent);
            orderid = intent.getStringExtra("orderid");

            Log.d("show", "onStartCommand: "+orderid);
            checkteachers(orderid);
            return START_STICKY;
        }

        @Override
        public void onCreate() {
            super.onCreate();

            Log.d("show", "onCreate: ");



        }



        public void checkteachers(final String oid)
        {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final DocumentReference docRef = db.collection("Orders").document(oid);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }



                    if (snapshot != null && snapshot.exists()) {
                        if(snapshot.getData().get("QuotationID").toString().isEmpty() == false)
//                            sendNotification("Our professional tutors have proposed their offer.Please " +
//                                    "tap here to respond ",snapshot.getData().get("QuotationID").toString());

                            checkinQuot(snapshot.getData().get("QuotationID").toString(),oid);

                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }





        public void checkinQuot(final String quotid,final String orderid)
        {

            FirebaseDatabase.getInstance().getReference().child("Quotations").child(quotid).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {


                    HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

                    Log.d("added:",""+dataSnapshot.getValue());

                    findname(quotid,value.get("UID").toString(),orderid);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }


        public void findname(final String quotid, String userid,final String orderid)

        {
            FirebaseDatabase.getInstance().getReference().child("Users").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                        final User user = dataSnapshot.getValue(User.class);

                    HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();


                    sendNotification(value.get("Name").toString(),quotid,orderid);
                    Log.d("Name:",""+value.get("Name"));


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }





        @Override
        public void onDestroy() {
            stopSelf();
        }

        private void sendNotification(String messageBody,String quotid,String orderid) {

            String message = messageBody+" has recently proposed his quotation"+"\n"+"for your teacher request" +
                    "\n"+"Tap here to view all the quotations";
            Intent intent = new Intent(this, DisplayActivity.class);
            intent.putExtra("orderid",orderid);
            intent.putExtra("quotid",quotid);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String channelId = getString(R.string.default_notification_channel_id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_stat_ic_notification)
                            .setContentTitle(getString(R.string.fcm_message))
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }

        @Override
        public void onTaskRemoved(Intent rootIntent) {
            super.onTaskRemoved(rootIntent);
        }
    }
