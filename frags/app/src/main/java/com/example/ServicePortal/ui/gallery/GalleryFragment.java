package com.example.ServicePortal.ui.gallery;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.ServicePortal.CheckAvailability;
import com.example.ServicePortal.R;
import com.example.ServicePortal.ReachedQRGen;
import com.example.ServicePortal.RiderQRGen;
import com.example.ServicePortal.RiderQRScan;
import com.example.ServicePortal.ShowonMap;
import com.example.ServicePortal.TutorAssignedDetailsActivity;
import com.example.ServicePortal.datasetfire;
import com.example.ServicePortal.firebaseviewholder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryFragment extends Fragment {


    /*public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

    private RecyclerView reCyclerView;
    private ArrayList<datasetfire> arrayList;
    private FirestoreRecyclerOptions<datasetfire> options;
    private FirestoreRecyclerAdapter<datasetfire, firebaseviewholder> adapter;
    public MediaPlayer player;
    public Runnable runnable;
    public Handler handler;
    public String orderdetid;
    private boolean hasDropped;
    private boolean hasPicked;

    private DatabaseReference databaseReference;

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        reCyclerView = root.findViewById(R.id.recycler);
        reCyclerView.setHasFixedSize(true);
        reCyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        arrayList = new ArrayList<datasetfire>();

        handler = new Handler();
        // FirebaseFirestore db;
        final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("Orders");
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        //databaseReference.keepSynced(true);

        // options = new FirestoreRecyclerOptions.Builder< datasetfire >().setQuery(query,datasetfire.class).build();
        options = new FirestoreRecyclerOptions.Builder<datasetfire>().setQuery(query, new SnapshotParser<datasetfire>() {
            @NonNull
            @Override
            public datasetfire parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                datasetfire dsfire = snapshot.toObject(datasetfire.class);
                // so i wanted to add the key of the node as a field in the node object.
                dsfire.setUid(snapshot.getId());
                return dsfire;
            }
        }).build();

        //options = new FirebaseRecyclerOptions.Builder< datasetfire >().setQuery(databaseReference,datasetfire.class).build();
        adapter = new FirestoreRecyclerAdapter<datasetfire, firebaseviewholder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final firebaseviewholder firebaseviewholder, int i, @NonNull final datasetfire datasetfire) {

                if(CheckAvailability.Category.equals("Mechanic") || CheckAvailability.Category.equals("Driver")) {
                    if (datasetfire.getAssigned() == 1 && datasetfire.getPaidCommission() == 1 && datasetfire.getAssignedId().equals(CheckAvailability.id) && datasetfire.getCompleted() == 0) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(datasetfire.getCId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                            User user = dataSnapshot.getValue(User.class);

                                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                Toast.makeText(getActivity(), value.get("Name").toString(), Toast.LENGTH_LONG).show();
                                firebaseviewholder.Cname.setText(value.get("Name").toString());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                        firebaseviewholder.Card.setVisibility(View.VISIBLE);
                        firebaseviewholder.Assigned.setText(datasetfire.getAssigned().toString());
                        firebaseviewholder.Completed.setText(datasetfire.getCompleted().toString());
                        //firebaseviewholder.Category.setText(datasetfire.getCategory());
                        firebaseviewholder.AssignedId.setText(datasetfire.getAssignedId());
                        firebaseviewholder.Description.setText(datasetfire.getDescription());
                        firebaseviewholder.Latitude.setText(String.valueOf(datasetfire.getLatitude()));
                        firebaseviewholder.Longitude.setText(String.valueOf(datasetfire.getLongitude()));


//                    HashMap<String, Object> orderDet = new HashMap<>();
//                    orderDet.put("AssignedId", datasetfire.getAssignedId());
//                    orderDet.put("OrderId", datasetfire.getUid());
//                    orderDet.put("hasReached", false);
//                    orderDet.put("inTime", new Timestamp(new Date()));
//                    orderDet.put("isCancelled", false);
//                    orderDet.put("isUnSuccessful", false);
//                    orderDet.put("outTime", new Timestamp(new Date()));
//
//                    rootRef.collection("OrderDetails")
//                            .add(orderDet)
//                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Log.d("issuc","success");
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.d("issuc","nosuccess");
//                                }
//                            });

//                    final String uid = datasetfire.getCId();
//                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
//                            .addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    // Get user information
//                                    User user = dataSnapshot.getValue(User.class);
//                                    String signinName = user.Name;
//                                    firebaseviewholder.CId.setText(signinName);
//
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });


                        firebaseviewholder.showButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(getActivity(), "de_map", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), ShowonMap.class);
                                intent.putExtra("Latitude", datasetfire.getLatitude());
                                intent.putExtra("Longitude", datasetfire.getLongitude());

                                startActivity(intent);
                            }
                        });

                        firebaseviewholder.playButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (player != null) {
                                    player.release();
                                    player = null;


                                    Log.d("this", "happens");
                                    firebaseviewholder.stopButton.setVisibility(View.GONE);
                                    firebaseviewholder.playButton.setVisibility(View.VISIBLE);
                                    firebaseviewholder.seekBar.setVisibility(View.GONE);
//                                firebaseviewholder.seekBar.setProgress(0);
                                }

                                try {

                                    player = new MediaPlayer();
                                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    player.setDataSource(datasetfire.getAudioDescription());
                                    player.prepare();
//            player.start();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer player) {
                                        firebaseviewholder.seekBar.setVisibility(View.VISIBLE);
                                        firebaseviewholder.seekBar.setMax(player.getDuration());
                                        player.start();
                                        changeSeekbar(firebaseviewholder.seekBar);
                                    }
                                });

//                            player.start();
//                            changeSeekbar();

                                firebaseviewholder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                        if (b) {
                                            if (player != null)
                                                player.seekTo(i);

                                            else {
                                                firebaseviewholder.stopButton.setVisibility(View.GONE);
                                                firebaseviewholder.playButton.setVisibility(View.VISIBLE);
                                                firebaseviewholder.seekBar.setVisibility(View.GONE);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {

                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {

                                    }
                                });

                                firebaseviewholder.playButton.setVisibility(View.GONE);
                                firebaseviewholder.stopButton.setVisibility(View.VISIBLE);

                            }
                        });
                        firebaseviewholder.stopButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (player != null) {
                                    player.stop();
                                    firebaseviewholder.seekBar.setProgress(0);
                                    player.release();
                                    player = null;
                                }

                                firebaseviewholder.stopButton.setVisibility(View.GONE);
                                firebaseviewholder.playButton.setVisibility(View.VISIBLE);
                                firebaseviewholder.seekBar.setVisibility(View.GONE);

                            }
                        });

                        firebaseviewholder.cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                rootRef.collection("OrderDetails")
                                        .whereEqualTo("OrderId", datasetfire.getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        orderdetid = document.getId();
                                                        Log.d("idorder", datasetfire.getUid());
                                                        DocumentReference assignstate = rootRef.collection("OrderDetails").document(orderdetid);

                                                        assignstate.update("isCancelled", true);

                                                        FirebaseDatabase.getInstance().getReference().child("Users").child(CheckAvailability.id).child("timeCooldown").setValue(ServerValue.TIMESTAMP);
                                                        FirebaseDatabase.getInstance().getReference().child("Users").child(CheckAvailability.id).child("CooldownCat").setValue("1");

                                                    }
                                                } else {
                                                    Log.d("idorder", "notwork");

                                                }

                                            }
                                        });


                            }
                        });

                        firebaseviewholder.assignButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

//                            FirebaseDatabase.getInstance().getReference().child("Users").child("JcneYFw5vNTRInabDZhC1EF5AXc2").child("IsAvailable").setValue("1");
//
//                            DocumentReference assignstate = rootRef.collection("Orders").document(datasetfire.getUid());
//
//                            assignstate.update("Completed", 1)
//
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Toast.makeText(getActivity(), "Completed Successfully!",
//                                                    Toast.LENGTH_LONG).show();


                                // ABOVE CODE IS USED TO SET AVAILABILITY TO 1 AND COMPLETED FIELD TO !. USE IT AFTER DOING THE OTP FOR REACHING AND STUFF


                                Intent intent = new Intent(getActivity(), ReachedQRGen.class);
                                intent.putExtra("Assigned", datasetfire.getAssigned());
                                intent.putExtra("Completed", datasetfire.getCompleted());
                                intent.putExtra("Category", datasetfire.getCategory());
                                intent.putExtra("AssignedId", datasetfire.getAssignedId());
                                intent.putExtra("CId", datasetfire.getCId());
                                intent.putExtra("Uid", datasetfire.getUid());
                                intent.putExtra("Description", datasetfire.getDescription());
                                intent.putExtra("Latitude", datasetfire.getLatitude());
                                intent.putExtra("Longitude", datasetfire.getLongitude());
                                intent.putExtra("AudioDescription", datasetfire.getAudioDescription());
                                startActivity(intent);
//                                        }
//                                    });


                                //   Toast.makeText(getApplicationContext(), datasetfire.getUid(), Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(getActivity(), CompletedWorksDetails.class);
//                            intent.putExtra("Assigned", datasetfire.getAssigned());
//                            intent.putExtra("Completed", datasetfire.getCompleted());
//                            //intent.putExtra("Category", datasetfire.getCategory());
//                            intent.putExtra("AssignedId", datasetfire.getAssignedId());
//                            intent.putExtra("CId", datasetfire.getCId());
//                            intent.putExtra("Description", datasetfire.getDescription());
//                            intent.putExtra("Latitude", datasetfire.getLatitude());
//                            intent.putExtra("Longitude", datasetfire.getLongitude());
//
//                            startActivity(intent);
                            }
                        });

                    } else {
//                    firebaseviewholder.Card.setVisibility(View.GONE);
                        firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
                    }
                }

                else if ( CheckAvailability.Category.equals("Grocery") || CheckAvailability.Category.equals("General Store"))
                {

                    if (datasetfire.getAssigned() == 1 && datasetfire.getPaidCommission() == 1 && datasetfire.getAssignedId().equals(CheckAvailability.id) && datasetfire.getCompleted() == 0) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(datasetfire.getCId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                            User user = dataSnapshot.getValue(User.class);

                                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                Toast.makeText(getActivity(), value.get("Name").toString(), Toast.LENGTH_LONG).show();
                                firebaseviewholder.Cname.setText(value.get("Name").toString());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                        firebaseviewholder.Card.setVisibility(View.VISIBLE);
                        firebaseviewholder.Assigned.setText(datasetfire.getAssigned().toString());
                        firebaseviewholder.Completed.setText(datasetfire.getCompleted().toString());
                        //firebaseviewholder.Category.setText(datasetfire.getCategory());
                        firebaseviewholder.AssignedId.setText(datasetfire.getAssignedId());
                        firebaseviewholder.Description.setText(datasetfire.getDescription());
                        firebaseviewholder.Latitude.setText(String.valueOf(datasetfire.getLatitude()));
                        firebaseviewholder.Longitude.setText(String.valueOf(datasetfire.getLongitude()));

                        firebaseviewholder.showButton.setVisibility(View.GONE);

                        firebaseviewholder.playButton.setVisibility(View.GONE);

                        firebaseviewholder.cancelButton.setVisibility(View.GONE);

                        firebaseviewholder.assignButton.setText("SCAN QR CODE");

                        firebaseviewholder.assignButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), RiderQRGen.class);
                                intent.putExtra("Assigned", datasetfire.getAssigned());
                                intent.putExtra("Completed", datasetfire.getCompleted());
                                intent.putExtra("Category", datasetfire.getCategory());
                                intent.putExtra("AssignedId", datasetfire.getAssignedId());
                                intent.putExtra("CId", datasetfire.getCId());
                                intent.putExtra("Uid", datasetfire.getUid());
                                intent.putExtra("Description", datasetfire.getDescription());
                                intent.putExtra("Latitude", datasetfire.getLatitude());
                                intent.putExtra("Longitude", datasetfire.getLongitude());
                                intent.putExtra("AudioDescription", datasetfire.getAudioDescription());
                                startActivity(intent);
                            }
                        });

//                        firebaseviewholder.assignButton.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
////                            FirebaseDatabase.getInstance().getReference().child("Users").child("JcneYFw5vNTRInabDZhC1EF5AXc2").child("IsAvailable").setValue("1");
////
////                            DocumentReference assignstate = rootRef.collection("Orders").document(datasetfire.getUid());
////
////                            assignstate.update("Completed", 1)
////
////                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
////                                        @Override
////                                        public void onSuccess(Void aVoid) {
////                                            Toast.makeText(getActivity(), "Completed Successfully!",
////                                                    Toast.LENGTH_LONG).show();
//
//
//                                // ABOVE CODE IS USED TO SET AVAILABILITY TO 1 AND COMPLETED FIELD TO !. USE IT AFTER DOING THE OTP FOR REACHING AND STUFF
//
//
//                                Intent intent = new Intent(getActivity(), ReachedQRGen.class);
//                                intent.putExtra("Assigned", datasetfire.getAssigned());
//                                intent.putExtra("Completed", datasetfire.getCompleted());
//                                intent.putExtra("Category", datasetfire.getCategory());
//                                intent.putExtra("AssignedId", datasetfire.getAssignedId());
//                                intent.putExtra("CId", datasetfire.getCId());
//                                intent.putExtra("Uid", datasetfire.getUid());
//                                intent.putExtra("Description", datasetfire.getDescription());
//                                intent.putExtra("Latitude", datasetfire.getLatitude());
//                                intent.putExtra("Longitude", datasetfire.getLongitude());
//                                intent.putExtra("AudioDescription", datasetfire.getAudioDescription());
//                                startActivity(intent);
////                                        }
////                                    });
//
//
//                                //   Toast.makeText(getApplicationContext(), datasetfire.getUid(), Toast.LENGTH_LONG).show();
////                            Intent intent = new Intent(getActivity(), CompletedWorksDetails.class);
////                            intent.putExtra("Assigned", datasetfire.getAssigned());
////                            intent.putExtra("Completed", datasetfire.getCompleted());
////                            //intent.putExtra("Category", datasetfire.getCategory());
////                            intent.putExtra("AssignedId", datasetfire.getAssignedId());
////                            intent.putExtra("CId", datasetfire.getCId());
////                            intent.putExtra("Description", datasetfire.getDescription());
////                            intent.putExtra("Latitude", datasetfire.getLatitude());
////                            intent.putExtra("Longitude", datasetfire.getLongitude());
////
////                            startActivity(intent);
//                            }
//                        });

                    } else {
//                    firebaseviewholder.Card.setVisibility(View.GONE);
                        firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
                    }

                }

                else if ( CheckAvailability.Category.equals("Tutor"))
                {

                    if (datasetfire.getAssigned() == 1 && datasetfire.getPaidCommission() == 1 && datasetfire.getAssignedId().equals(CheckAvailability.id) && datasetfire.getCategory().equals(CheckAvailability.Category)) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(datasetfire.getCId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                            User user = dataSnapshot.getValue(User.class);

                                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                Toast.makeText(getActivity(), value.get("Name").toString(), Toast.LENGTH_LONG).show();
                                firebaseviewholder.Cname.setText(value.get("Name").toString());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                        firebaseviewholder.Card.setVisibility(View.VISIBLE);
                        firebaseviewholder.Assigned.setText(datasetfire.getAssigned().toString());
                        firebaseviewholder.Completed.setText(datasetfire.getCompleted().toString());
                        //firebaseviewholder.Category.setText(datasetfire.getCategory());
                        firebaseviewholder.AssignedId.setText(datasetfire.getAssignedId());
                        firebaseviewholder.Description.setText(datasetfire.getDescription());

                        firebaseviewholder.showButton.setVisibility(View.GONE);

                        firebaseviewholder.playButton.setVisibility(View.GONE);

                        firebaseviewholder.cancelButton.setVisibility(View.GONE);

                        firebaseviewholder.assignButton.setText("View Details");

                        firebaseviewholder.assignButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), TutorAssignedDetailsActivity.class);
                                intent.putExtra("Assigned", datasetfire.getAssigned());
                                intent.putExtra("Completed", datasetfire.getCompleted());
                                intent.putExtra("Category", datasetfire.getCategory());
                                intent.putExtra("AssignedId", datasetfire.getAssignedId());
                                intent.putExtra("CId", datasetfire.getCId());
                                intent.putExtra("Uid", datasetfire.getUid());
                                intent.putExtra("InvoiceID", datasetfire.getInvoiceID());
                                intent.putExtra("Description", datasetfire.getDescription());
                                intent.putExtra("Latitude", datasetfire.getLatitude());
                                intent.putExtra("Longitude", datasetfire.getLongitude());
                                intent.putExtra("OrderDescriptionID", datasetfire.getOrderDescriptionID());
                                intent.putExtra("AudioDescription", datasetfire.getAudioDescription());
                                startActivity(intent);
                            }
                        });

                    } else {
//                    firebaseviewholder.Card.setVisibility(View.GONE);
                        firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
                    }

                }

                else if ( CheckAvailability.Category.equals("Rider"))
                {

                    if (datasetfire.getAssigned() == 1 && datasetfire.getPaidCommission() == 1 && datasetfire.getAssignedId().equals(CheckAvailability.id)) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(datasetfire.getCId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                            User user = dataSnapshot.getValue(User.class);

                                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                Toast.makeText(getActivity(), value.get("Name").toString(), Toast.LENGTH_LONG).show();
                                firebaseviewholder.Cname.setText(value.get("Name").toString());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                        firebaseviewholder.Card.setVisibility(View.VISIBLE);
                        firebaseviewholder.Assigned.setText(datasetfire.getAssigned().toString());
                        firebaseviewholder.Completed.setText(datasetfire.getCompleted().toString());
                        //firebaseviewholder.Category.setText(datasetfire.getCategory());
                        firebaseviewholder.AssignedId.setText(datasetfire.getAssignedId());
                        firebaseviewholder.Description.setText(datasetfire.getDescription());
                        firebaseviewholder.Latitude.setText("PickupLat:"+String.valueOf(datasetfire.getPickupLatitude())+"\nDropOffLat:"+String.valueOf(datasetfire.getDropLatitude()));
                        firebaseviewholder.Longitude.setText("PickupLong:"+String.valueOf(datasetfire.getPickupLatitude())+"\nDropOffLong:"+String.valueOf(datasetfire.getDropLatitude()));

                        firebaseviewholder.showButton.setVisibility(View.GONE);

                        firebaseviewholder.playButton.setVisibility(View.GONE);

                        firebaseviewholder.cancelButton.setVisibility(View.GONE);

                        rootRef.collection("OrderDetails")
                                .whereEqualTo("OrderId", datasetfire.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                orderdetid = document.getId();
                                                hasDropped = document.getBoolean("hasDropped");
                                                hasPicked = document.getBoolean("hasPicked");
                                                Log.d("idorder",orderdetid);

                                                if (!hasPicked)
                                                {
                                                    firebaseviewholder.assignButton.setText("SCAN QR CODE AT PICKUP");
                                                }
                                                else if(!hasDropped)
                                                {
                                                    firebaseviewholder.assignButton.setText("SCAN QR CODE AT CUSTOMER");
                                                }
                                            }
                                        } else {

                                        }

                                    }
                                });

                        firebaseviewholder.assignButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), RiderQRScan.class);
                                intent.putExtra("Assigned", datasetfire.getAssigned());
                                intent.putExtra("Completed", datasetfire.getCompleted());
                                intent.putExtra("Category", datasetfire.getCategory());
                                intent.putExtra("AssignedId", datasetfire.getAssignedId());
                                intent.putExtra("CId", datasetfire.getCId());
                                intent.putExtra("Uid", datasetfire.getUid());
                                intent.putExtra("Description", datasetfire.getDescription());
                                intent.putExtra("Latitude", datasetfire.getLatitude());
                                intent.putExtra("Longitude", datasetfire.getLongitude());
                                intent.putExtra("AudioDescription", datasetfire.getAudioDescription());
                                intent.putExtra("hasDropped", hasDropped);
                                intent.putExtra("hasPicked", hasPicked);
                                startActivity(intent);
                            }
                        });

                    } else {
//                    firebaseviewholder.Card.setVisibility(View.GONE);
                        firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
                    }

                }
            }

            @NonNull
            @Override
            public firebaseviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new firebaseviewholder(LayoutInflater.from(getActivity()).inflate(R.layout.row_assign_work, parent, false));
            }
        };

        reCyclerView.setAdapter(adapter);

        return root;
    }

    private void changeSeekbar(final SeekBar skb)
    {
        if(player != null) {
            skb.setProgress(player.getCurrentPosition());

            if (player.isPlaying()) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        changeSeekbar(skb);
                    }
                };

                handler.postDelayed(runnable, 1000);
            }
        }
    }
}