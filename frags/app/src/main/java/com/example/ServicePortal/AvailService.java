package com.example.ServicePortal;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.app.NotificationCompat;

public class AvailService extends Service {

    Timer timer;
    TimerTask task;

    public AvailService() {
    }

    private static final int PERMISSIONS_REQUEST = 1;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void buildNotification() {
        String stop = "stop";
//        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("AvailCheckRunning")
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.ic_tracker_foreground);
//        startForeground(1, builder.build());
    }

    @Override
    public void onCreate() {
        super.onCreate();

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

                        if(value.get("IsAvailable").equals("0"))
                        {
                            Intent serviceIntent = new Intent(getApplicationContext(),TrackerService.class);
                            startService(serviceIntent);
                            Toast.makeText(getApplicationContext(),"AvailStart",Toast.LENGTH_LONG).show();
                            Log.d("availability","availstart");
                        }

                        if(value.get("IsAvailable").equals("1"))
                        {
                            Intent serviceIntent = new Intent(getApplicationContext(),TrackerService.class);
                            stopService(serviceIntent);
                            Toast.makeText(getApplicationContext(),"AvailStop",Toast.LENGTH_LONG).show();
                            Log.d("availability","availstop");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

            }
        };
        timer.schedule(task, 0, (1000));

//        buildNotification();
    }

    @Override
    public boolean stopService(Intent name) {
//        timer.cancel();
//        task.cancel();
        Log.d("availability","availstoppedhere");
        return super.stopService(name);
    }

}
