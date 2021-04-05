package com.example.ServicePortal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AllWorkDetails extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_all_work_details);

            final TextView NameDet, PhoneDet, EmailDet, CategoryDet,DescDet;
            final Button gotoButton, showButton;
            final Button Start_Continue = findViewById(R.id.Start_Continue);

            NameDet = (TextView) findViewById(R.id.cname);
            PhoneDet = (TextView) findViewById(R.id.phno);
            EmailDet = (TextView) findViewById(R.id.email);
            DescDet = (TextView) findViewById(R.id.des);
            gotoButton = findViewById(R.id.gotobutton);
            showButton = findViewById(R.id.showBtn);

            Intent intent = getIntent();
            final double lat = intent.getDoubleExtra("Latitude",0.0);
            final double longi = intent.getDoubleExtra("Longitude",0.0);
            String Cid = intent.getStringExtra("CId");
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
                        DescDet.setText(desc);

                        gotoButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CheckAvailability.availcheck = "0";
                                Intent intent = new Intent(AllWorkDetails.this, MainActivity.class);
                                intent.putExtra("availcheck", "0");
                                intent.putExtra("id", CheckAvailability.id);
                                intent.putExtra("Category", CheckAvailability.Category);
                                intent.putExtra("lat", CheckAvailability.lat);
                                intent.putExtra("longi", CheckAvailability.longi);
                                startActivity(intent);
                            }
                        });

                        showButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AllWorkDetails.this, ShowonMap.class);
                                intent.putExtra("Latitude", lat);
                                intent.putExtra("Longitude", longi);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

            }
        }

