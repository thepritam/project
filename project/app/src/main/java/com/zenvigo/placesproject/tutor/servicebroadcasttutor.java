package com.project.placesproject.tutor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.project.placesproject.ServiceNoDelay;

public class servicebroadcasttutor extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("count", "Service tried to stop");
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, servicetutor.class));
        } else {
            context.startService(new Intent(context, servicetutor.class));
        }
    }

}
