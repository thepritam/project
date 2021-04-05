package com.project.placesproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ServicemaActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textViewname,textViewnameshow,textViewmail,textViewmailshow,raitingtext;
     RatingBar ratingBar;
     Button button;
    Intent mServiceIntent;
     private ServiceNoDelay mYourService;
     String name=null,mail=null,phone=null;
     double lat,lon;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ServicemaActivity.this,MainPage.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicema);
        mYourService = new ServiceNoDelay();
        Log.d("count", "onClick: ");

        textViewname=findViewById(R.id.name);
        textViewname.setText("NAME");
        textViewnameshow=findViewById(R.id.textnameshow);
        textViewmail=findViewById(R.id.mail);
        textViewmail.setText("MAIL");
        textViewmailshow=findViewById(R.id.textmailshow);
        raitingtext=findViewById(R.id.textView10);
         ratingBar=findViewById(R.id.ratingBar);
         button=findViewById(R.id.buttonpaid);

         button.setOnClickListener(new View.OnClickListener() {
             @RequiresApi(api = Build.VERSION_CODES.O)
             @Override
             public void onClick(View v) {
                 //mYourService = new ServiceNoDelay(getApplicationContext(),getIntent().getStringExtra("orderid"));




                 if(name!=null&&mail!=null){
                 Intent intent = new Intent(ServicemaActivity.this,PayActivity.class);
                 intent.putExtra("name",name);
                 intent.putExtra("phone",phone);
                 intent.putExtra("mail",mail);
                 intent.putExtra("lat",Double.toString(lat));
                 intent.putExtra("lon",Double.toString(lon));
                 intent.putExtra("asid",getIntent().getStringExtra("Assignedid"));
                 intent.putExtra("oid",getIntent().getStringExtra("orderid"));
                 finish();
                 startActivity(intent);}
             }
         });



       // textViewname.setText(getIntent().getStringExtra("orderid")+" "+getIntent().getStringExtra("Assignedid"));
        FirebaseDatabase.getInstance().getReference().child("Users").child(getIntent().getStringExtra("Assignedid"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("show", "onDataChange: ");
                       //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = dataSnapshot.getValue(User.class);

                            // assert user != null;
                            Log.d("show", "onDataChange: "+user.Category);
                        Log.d("show", "onDataChange: "+user.Name);
                        name=user.Name;
                        textViewnameshow.setText(user.Name);
                        Log.d("show", "onDataChange: "+user.Phone);
                        Log.d("show", "onDataChange: "+user.Email);
                        textViewmailshow.setText(user.Email);
                        mail=user.Email;
                        phone= user.Phone;
                        lat=user.Latitude;
                        lon=user.Longitude;
                        button.setEnabled(true);
                        ratingBar.setNumStars(5);
                        ratingBar.setRating((float)4.3);

                        raitingtext.setText("4.3");
                        mServiceIntent = new Intent(ServicemaActivity.this, mYourService.getClass());
                        mServiceIntent.putExtra("id",getIntent().getStringExtra("orderid"));

                        if (!isMyServiceRunning(mYourService.getClass())) {
                            startService(mServiceIntent);
                        }

                        //}

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        Toast.makeText(getApplicationContext(),"Congrats! Here is your Zenvman",Toast.LENGTH_LONG).show();
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }
    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        super.onDestroy();
    }
}
