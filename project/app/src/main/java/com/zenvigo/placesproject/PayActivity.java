package com.project.placesproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class PayActivity extends AppCompatActivity {
   Button button;
    BroadcastReceiver receiver;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        button=findViewById(R.id.buttonpaypal);
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
         textView = findViewById(R.id.textViewcount);
        final String oid = getIntent().getStringExtra("oid");
        final String assigned=getIntent().getStringExtra("asid");
        final String name=getIntent().getStringExtra("name");
        final String phone=getIntent().getStringExtra("phone");
        final String mail=getIntent().getStringExtra("mail");
        final String lat=getIntent().getStringExtra("lat");
        final String lon=getIntent().getStringExtra("lon");

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra("count");
                textView.setText(s);
                int cnt = Integer.parseInt(s);
                if(cnt>10) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("Orders").document(oid)
                            .update(
                                    "isCancelable", 1
                                    //"favorites.color", "Red"
                            )
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //    progressDialog.dismiss();
                                    Log.d("show", "Cancelled " + oid + " successfully!");

                                    Intent intent1 = new Intent(PayActivity.this, MainPage.class);
                                    intent1.putExtra("id", currentUser.getUid());
                                    finish();
                                    startActivity(intent1);
                                   /* Intent intent = new Intent(RESULT);
                                    intent.putExtra("count",Integer.toString(11));
                                    intent.putExtra("expired","yes");
                                    broadcaster.sendBroadcast(intent);*/


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("show", "Error updating document", e);
                                }
                            });
                }






                    // do something here.

            }
        };


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.background);
                progressDialog = new ProgressDialog(PayActivity.this);
                progressDialog.setTitle("Please Wait...");
                progressDialog.setMessage("We are looking for your zenvman...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                db.collection("Orders").document(oid)
                        .update(
                                "PaidCommission", 1
                                //"favorites.color", "Red"
                        )
                          .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Log.d("show", "DocumentSnapshot successfully updated!");
                Intent intent1 = new Intent(getApplicationContext(),TrackActivity.class);
                Log.d("show", "onClick: happening");
                intent1.putExtra("assigned",assigned);
                intent1.putExtra("name",name);
                intent1.putExtra("phone",phone);
                intent1.putExtra("mail",mail);
                intent1.putExtra("lat",lat);
                intent1.putExtra("lon",lon);
                startActivity(intent1);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("show", "Error updating document", e);
            }
        });


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();



    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(ServiceNoDelay.RESULT)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }
}
