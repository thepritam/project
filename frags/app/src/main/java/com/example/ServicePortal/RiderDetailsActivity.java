package com.example.ServicePortal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RiderDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_details);

        final TextView NameDet, PhoneDet, EmailDet, CategoryDet,DescDet,PickupLatitude,PickupLongitude,DropLatitude,DropLongitude ;
        final Button gotoButton, showButton;
        final Button Start_Continue = findViewById(R.id.Start_Continue);

        NameDet = (TextView) findViewById(R.id.cname);
        PhoneDet = (TextView) findViewById(R.id.phno);
        EmailDet = (TextView) findViewById(R.id.email);
        DescDet = (TextView) findViewById(R.id.des);
        PickupLatitude = (TextView) findViewById(R.id.PickupLatitude);
        PickupLongitude = (TextView) findViewById(R.id.PickupLongitude);
        DropLatitude = (TextView) findViewById(R.id.DropLatitude);
        DropLongitude = (TextView) findViewById(R.id.DropLongitude);

        gotoButton = findViewById(R.id.gotobutton);
        showButton = findViewById(R.id.showBtn);

        Intent intent = getIntent();
        String Cid = intent.getStringExtra("CId");
        final String Uid = intent.getStringExtra("OrderId");
        final double picklat = intent.getDoubleExtra("PickupLatitude",0.0);
        final double picklongi = intent.getDoubleExtra("PickupLongitude",0.0);
        final double droplat = intent.getDoubleExtra("DropLatitude",0.0);
        final double droplongi = intent.getDoubleExtra("DropLongitude",0.0);
        final String desc = intent.getStringExtra("Description");


        FirebaseDatabase.getInstance().getReference().child("Users").child(Cid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                        final User user = dataSnapshot.getValue(User.class);

                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

                NameDet.setText(value.get("Name").toString());
                PhoneDet.setText(value.get("Phone").toString());
                EmailDet.setText(value.get("Email").toString());
                PickupLatitude.setText(""+picklat);
                PickupLongitude.setText(""+picklongi);
                DropLatitude.setText(""+droplat);
                DropLongitude.setText(""+droplongi);
                DescDet.setText(desc);

                gotoButton.setText("Confirm Order");

                gotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        HashMap<String, Object> orderDet = new HashMap<>();
                        orderDet.put("AssignedId", CheckAvailability.id);
                        orderDet.put("OrderId", Uid);
                        orderDet.put("hasPicked", false);
                        orderDet.put("PickUpTime", new Timestamp(new Date()));
                        orderDet.put("hasDropped", false);
                        orderDet.put("isCancelled", false);
                        orderDet.put("DropTime", new Timestamp(new Date()));

                        final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                        rootRef.collection("OrderDetails")
                                .add(orderDet)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("issuc", "success");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("issuc", "nosuccess");
                                    }
                                });

                        DocumentReference assignstate = rootRef.collection("Orders").document(Uid);
                        FirebaseDatabase.getInstance().getReference().child("Users").child(CheckAvailability.id).child("IsAvailable").setValue("0");
                        assignstate.update("Assigned", 1);
                        assignstate.update("AssignedId", CheckAvailability.id);

                        CheckAvailability.availcheck = "0";
                        Intent intent = new Intent(RiderDetailsActivity.this, MainActivity.class);
                        intent.putExtra("availcheck", "0");
                        intent.putExtra("id", CheckAvailability.id);
                        intent.putExtra("Category", CheckAvailability.Category);
                        intent.putExtra("PickupLatitude", picklat);
                        intent.putExtra("PickupLongitude", picklongi);
                        intent.putExtra("DropLatitude", droplat);
                        intent.putExtra("DropLongitude", droplongi);
                        startActivity(intent);

                    }
                });

                showButton.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
}

