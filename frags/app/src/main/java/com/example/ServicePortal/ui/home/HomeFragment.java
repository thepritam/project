package com.example.ServicePortal.ui.home;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ServicePortal.AllWorkDetails;
import com.example.ServicePortal.CheckAvailability;
import com.example.ServicePortal.GroceryWorkDetails;
import com.example.ServicePortal.R;
import com.example.ServicePortal.RiderDetailsActivity;
import com.example.ServicePortal.ShowonMap;
import com.example.ServicePortal.TutorQuotationActivity;
import com.example.ServicePortal.User;
import com.example.ServicePortal.datasetfire;
import com.example.ServicePortal.firebaseviewholder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {


    private RecyclerView reCyclerView;
    private ArrayList<datasetfire> arrayList;
    private FirestoreRecyclerOptions<datasetfire> options;
    private FirestoreRecyclerAdapter<datasetfire, firebaseviewholder> adapter;
    public String Cname;
    public String Cid;
    public boolean willshow;
    public View root;
    public User user;
    firebaseviewholder fvh;
    public MediaPlayer player;
    public Runnable runnable;
    public Handler handler;


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



    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        final View root = inflater.inflate(R.layout.fragment_home, container, false);



        TextView chkconfirm = root.findViewById(R.id.chkconfirm);

        if(CheckAvailability.availcheck.equals("0"))
        {
            chkconfirm.setVisibility(View.VISIBLE);
        }

//        player = MediaPlayer.create(getActivity(), R.raw.song);


        reCyclerView = root.findViewById(R.id.recycler);
        reCyclerView.setHasFixedSize(true);
        reCyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        arrayList = new ArrayList<datasetfire>();
        // FirebaseFirestore db;
        final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        rootRef.setFirestoreSettings(settings);
        Query query = rootRef.collection("Orders");
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        //databaseReference.keepSynced(true);


        // options = new FirestoreRecyclerOptions.Builder< datasetfire >().setQuery(query,datasetfire.class).build();
        options = new FirestoreRecyclerOptions.Builder<datasetfire>().setQuery(query, new SnapshotParser<datasetfire>() {
            @NonNull
            @Override
            public datasetfire parseSnapshot(@NonNull DocumentSnapshot snapshot) {
//                    Log.d("Running", "" + snapshot);
                final datasetfire dsfire = snapshot.toObject(datasetfire.class);
                    // so i wanted to add the key of the node as a field in the node object.
                    dsfire.setUid(snapshot.getId());
                    Cid = dsfire.getCId();
                    return dsfire;
            }
        }).build();

        handler = new Handler();

        //options = new FirebaseRecyclerOptions.Builder< datasetfire >().setQuery(databaseReference,datasetfire.class).build();
        adapter = new FirestoreRecyclerAdapter<datasetfire, firebaseviewholder>(options) {

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            protected void onBindViewHolder(@NonNull final firebaseviewholder firebaseviewholder, int i, @NonNull final datasetfire datasetfire) {

             //   firebaseviewholder.Card.setVisibility(View.INVISIBLE);


//                Toast.makeText(getActivity(),CheckAvailability.availcheck,Toast.LENGTH_LONG).show();
                if(CheckAvailability.Category.equals("Mechanic") || CheckAvailability.Category.equals("Driver")) {
                    if (datasetfire.getAssigned() == 0 && datasetfire.getCompleted() == 0 && CheckAvailability.availcheck.equals("1") && CheckAvailability.Category.equals(datasetfire.getCategory()) && datasetfire.getIsCancelled() == 0) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(datasetfire.getCId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
                                // String key = dataSnapshot.getKey();
                                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                String name = value.get("Name").toString();
//                            final User user = dataSnapshot.getValue(User.class);
//                                Log.d("Running", datasetfire.getCId());
//                                Log.d("Running", "" + value);
//
                                firebaseviewholder.Cname.setText(name);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                        firebaseviewholder.Card.setVisibility(View.VISIBLE);
                        firebaseviewholder.Assigned.setText(datasetfire.getAssigned().toString());
                        firebaseviewholder.Completed.setText(datasetfire.getCompleted().toString());
                        firebaseviewholder.Category.setText(datasetfire.getCategory());
                        firebaseviewholder.AssignedId.setText(datasetfire.getAssignedId());
                        firebaseviewholder.CId.setText(datasetfire.getCId());
                        firebaseviewholder.Description.setText(datasetfire.getDescription());
                        firebaseviewholder.Latitude.setText(String.valueOf(datasetfire.getLatitude()));
                        firebaseviewholder.Longitude.setText(String.valueOf(datasetfire.getLongitude()));


                        if ((CheckAvailability.lat + CheckAvailability.longi) - (datasetfire.getLatitude() + datasetfire.getLongitude()) > 10) {
                            Toast.makeText(getActivity(), "occur", Toast.LENGTH_LONG).show();
                            firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
//                        Log.d("Running", "Mapcheck Invalid " + Cid);
                        }




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

                        firebaseviewholder.assignButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                HashMap<String, Object> orderDet = new HashMap<>();
                                orderDet.put("AssignedId", datasetfire.getAssignedId());
                                orderDet.put("OrderId", datasetfire.getUid());
                                orderDet.put("hasReached", false);
                                orderDet.put("inTime", new Timestamp(new Date()));
                                orderDet.put("isCancelled", false);
                                orderDet.put("isUnSuccessful", false);
                                orderDet.put("outTime", new Timestamp(new Date()));

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

                                DocumentReference assignstate = rootRef.collection("Orders").document(datasetfire.getUid());
                                CheckAvailability.availcheck = "0";
                                FirebaseDatabase.getInstance().getReference().child("Users").child(CheckAvailability.id).child("IsAvailable").setValue("0");
                                FirebaseDatabase.getInstance().getReference().child("Users").child(CheckAvailability.id).child("IsAvailable").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        CheckAvailability.availcheck = (String) snapshot.getValue();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                                assignstate.update("Assigned", 1);
                                assignstate.update("AssignedId", CheckAvailability.id)

                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(), "Assigned Successfully. Check Confirmation page",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });


                                Toast.makeText(getActivity(), datasetfire.getUid(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), AllWorkDetails.class);
                                intent.putExtra("Assigned", datasetfire.getAssigned());
                                intent.putExtra("Completed", datasetfire.getCompleted());
                                intent.putExtra("Category", datasetfire.getCategory());
                                intent.putExtra("AssignedId", datasetfire.getAssignedId());
                                intent.putExtra("CId", datasetfire.getCId());
                                intent.putExtra("Description", datasetfire.getDescription());
                                intent.putExtra("Latitude", datasetfire.getLatitude());
                                intent.putExtra("Longitude", datasetfire.getLongitude());

                                startActivity(intent);
                            }
                        });


                    } else {
//                        firebaseviewholder.Card.setVisibility(View.GONE);
                        firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
                        //CHECK CONFIRM PAGE
                    }
                }

                else if ( CheckAvailability.Category.equals("Grocery") || CheckAvailability.Category.equals("General Store"))
                {
                    if (datasetfire.getAssigned() == 0 && datasetfire.getCompleted() == 0 && CheckAvailability.Category.equals(datasetfire.getCategory()) && datasetfire.getIsCancelled() == 0 && CheckAvailability.id.equals(datasetfire.getAssignedId())) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(datasetfire.getCId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
                                // String key = dataSnapshot.getKey();
                                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                String name = value.get("Name").toString();
//                            final User user = dataSnapshot.getValue(User.class);
//                                Log.d("Running", datasetfire.getCId());
//                                Log.d("Running", "" + value);
//
                                firebaseviewholder.Cname.setText(name);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                        firebaseviewholder.Card.setVisibility(View.VISIBLE);
                        firebaseviewholder.Assigned.setText(datasetfire.getAssigned().toString());
                        firebaseviewholder.Completed.setText(datasetfire.getCompleted().toString());
                        firebaseviewholder.Category.setText(datasetfire.getCategory());
                        firebaseviewholder.AssignedId.setText(datasetfire.getAssignedId());
                        firebaseviewholder.CId.setText(datasetfire.getCId());
                        firebaseviewholder.Description.setText(datasetfire.getDescription());
                        firebaseviewholder.Latitude.setText(String.valueOf(datasetfire.getLatitude()));
                        firebaseviewholder.Longitude.setText(String.valueOf(datasetfire.getLongitude()));


                        if ((CheckAvailability.lat + CheckAvailability.longi) - (datasetfire.getLatitude() + datasetfire.getLongitude()) > 10) {
                            Toast.makeText(getActivity(), "occur", Toast.LENGTH_LONG).show();
                            firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
//                        Log.d("Running", "Mapcheck Invalid " + Cid);
                        }


                        firebaseviewholder.playButton.setVisibility(View.GONE);
                        firebaseviewholder.showButton.setVisibility(View.GONE);
                        firebaseviewholder.assignButton.setText("Details");

                        firebaseviewholder.assignButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(getActivity(), datasetfire.getUid(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), GroceryWorkDetails.class);
                                intent.putExtra("Assigned", datasetfire.getAssigned());
                                intent.putExtra("OrderId", datasetfire.getUid());
                                intent.putExtra("Completed", datasetfire.getCompleted());
                                intent.putExtra("Category", datasetfire.getCategory());
                                intent.putExtra("AssignedId", datasetfire.getAssignedId());
                                intent.putExtra("CId", datasetfire.getCId());
                                intent.putExtra("OrderDescriptionID", datasetfire.getOrderDescriptionID());
                                intent.putExtra("Description", datasetfire.getDescription());
                                intent.putExtra("Latitude", datasetfire.getLatitude());
                                intent.putExtra("Longitude", datasetfire.getLongitude());

                                startActivity(intent);
                            }
                        });


                    } else {
                       firebaseviewholder.Card.setVisibility(View.GONE);
                  //      firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
                        //CHECK CONFIRM PAGE
                    }
                }

                else if(CheckAvailability.Category.equals("Tutor")) {

                    Log.d("Running","Entered here!");

                    if (datasetfire.getAssigned() == 0 && datasetfire.getCompleted() == 0 && CheckAvailability.Category.equals(datasetfire.getCategory()) && datasetfire.getIsCancelled() == 0) {
                        Log.d("Running","Entered inside if!");

                        FirebaseDatabase.getInstance().getReference().child("TutorDetails").child(CheckAvailability.id).orderByKey().equalTo(datasetfire.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(!dataSnapshot.exists())

                                {
                                    FirebaseDatabase.getInstance().getReference().child("OrderDescription").child(datasetfire.getOrderDescriptionID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                            String klass = value.get("class").toString();
                                            HashMap<String, String> subj = (HashMap<String, String>) value.get("Subjects");

                                            Log.d("Running", "" + klass);
                                            Log.d("Running", "" + subj);
                                            Log.d("Running", "" + CheckAvailability.klass);
                                            Log.d("Running", "" + CheckAvailability.klass.get(klass));
                                            final HashMap<String, String> fees = (HashMap<String, String>) value.get("Preferred Price");
                                            int flag=0;
                                            if(CheckAvailability.klass.get(klass) != null) {
                                                for (Map.Entry<String, String> entry : subj.entrySet()) {

                                                    if (CheckAvailability.klass.containsKey(klass) && CheckAvailability.klass.get(klass).containsKey(entry.getKey())) {
                                                        flag = 1;
                                                        break;
                                                    }
                                                }
                                            }

                                            Log.d("Running","flag:"+flag);
                                            if(flag==1){

                                                ViewGroup.LayoutParams params = firebaseviewholder.Card.getLayoutParams();
                                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                                params.width = ViewGroup.LayoutParams.MATCH_PARENT;

                                                firebaseviewholder.Card.setLayoutParams(params);

                                                firebaseviewholder.Card.setVisibility(View.VISIBLE);
                                                FirebaseDatabase.getInstance().getReference().child("Users").child(datasetfire.getCId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
                                                        // String key = dataSnapshot.getKey();
                                                        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                                        String name = value.get("Name").toString();
//                            final User user = dataSnapshot.getValue(User.class);
                                                        Log.d("Running", datasetfire.getCId());
                                                        Log.d("Running", "" + value);
//
                                                        firebaseviewholder.tutortime.setVisibility(View.VISIBLE);

                                                        Log.d("Running","Card created true");
                                                        firebaseviewholder.Cname.setText(name);
                                                        firebaseviewholder.Assigned.setText(datasetfire.getAssigned().toString());
                                                        firebaseviewholder.Completed.setText(datasetfire.getCompleted().toString());
                                                        firebaseviewholder.Category.setText(datasetfire.getCategory());
                                                        firebaseviewholder.AssignedId.setText(datasetfire.getAssignedId());
                                                        firebaseviewholder.CId.setText(datasetfire.getCId());
                                                        firebaseviewholder.Description.setText("Preferred Fees:" + fees.get("Price"));
                                                        firebaseviewholder.tutortime.setText("Preferred Mode:" + fees.get("Type"));
                                                        firebaseviewholder.Latitude.setText(String.valueOf(datasetfire.getLatitude()));
                                                        firebaseviewholder.Longitude.setText(String.valueOf(datasetfire.getLongitude()));


                                                        if ((CheckAvailability.lat + CheckAvailability.longi) - (datasetfire.getLatitude() + datasetfire.getLongitude()) > 10) {
                                                            Toast.makeText(getActivity(), "occur", Toast.LENGTH_LONG).show();
                                                            firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
//                        Log.d("Running", "Mapcheck Invalid " + Cid);
                                                        }

                                                        firebaseviewholder.playButton.setVisibility(View.GONE);
                                                        firebaseviewholder.assignButton.setText("Give Quotation");

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


                                                        firebaseviewholder.assignButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {


//                                                    DocumentReference assignstate = rootRef.collection("Orders").document(datasetfire.getUid());
//                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(CheckAvailability.id).child("IsAvailable").setValue("0");
//                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(CheckAvailability.id).child("IsAvailable").addValueEventListener(new ValueEventListener() {
//                                                        @Override
//                                                        public void onDataChange(DataSnapshot snapshot) {
//                                                            CheckAvailability.availcheck = (String) snapshot.getValue();
//                                                        }
//
//                                                        @Override
//                                                        public void onCancelled(DatabaseError databaseError) {
//                                                        }
//                                                    });
//
//                                                    assignstate.update("Assigned", 1);
//                                                    assignstate.update("AssignedId", CheckAvailability.id)
//
//                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                @Override
//                                                                public void onSuccess(Void aVoid) {
//                                                                    Toast.makeText(getActivity(), "Assigned Successfully. Check Confirmation page",
//                                                                            Toast.LENGTH_LONG).show();
//                                                                }
//                                                            });


                                                                Toast.makeText(getActivity(), datasetfire.getUid(), Toast.LENGTH_LONG).show();
                                                                Intent intent = new Intent(getActivity(), TutorQuotationActivity.class);
                                                                intent.putExtra("Assigned", datasetfire.getAssigned());
                                                                intent.putExtra("Completed", datasetfire.getCompleted());
                                                                intent.putExtra("Category", datasetfire.getCategory());
                                                                intent.putExtra("AssignedId", datasetfire.getAssignedId());
                                                                intent.putExtra("CId", datasetfire.getCId());
                                                                intent.putExtra("Uid", datasetfire.getUid());
                                                                intent.putExtra("QuotationID", datasetfire.getQuotationID());
                                                                intent.putExtra("Description", datasetfire.getDescription());
                                                                intent.putExtra("OrderDescriptionID", datasetfire.getOrderDescriptionID());
                                                                intent.putExtra("Latitude", datasetfire.getLatitude());
                                                                intent.putExtra("Longitude", datasetfire.getLongitude());

                                                                startActivity(intent);
                                                            }
                                                        });

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }

                                                });
                                            }

//                                    else {
//                                       // firebaseviewholder.Card.setVisibility(View.GONE);
//                                        Log.d("Card destroyed in flag","true");
//
//                                                 firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
//                                        //CHECK CONFIRM PAGE
//                                    }




                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }

                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



                    }
                    else {

                        ViewGroup.LayoutParams params = firebaseviewholder.Card.getLayoutParams();
                        params.height = 0;
                        params.width = 0;

                        firebaseviewholder.Card.setLayoutParams(params);


                        firebaseviewholder.Card.setVisibility(View.GONE);
                        Log.d("Running","Entered inside else!");
                        Log.d("Card destroyed","true");

                     //  firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
                        //CHECK CONFIRM PAGE
                    }


                }

                else if ( CheckAvailability.Category.equals("Rider"))
                {

                    ViewGroup.LayoutParams params = firebaseviewholder.Card.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;

                    firebaseviewholder.Card.setLayoutParams(params);

                    firebaseviewholder.Card.setVisibility(View.VISIBLE);

                    if (datasetfire.getAssigned() == 0 && datasetfire.getCompleted() == 0 && CheckAvailability.availcheck.equals("1") && CheckAvailability.Category.equals(datasetfire.getCategory()) && datasetfire.getIsCancelled() == 0) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(datasetfire.getCId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
                                // String key = dataSnapshot.getKey();
                                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                String name = value.get("Name").toString();
//                            final User user = dataSnapshot.getValue(User.class);
//                                Log.d("Running", datasetfire.getCId());
//                                Log.d("Running", "" + value);
//
                                firebaseviewholder.Cname.setText(name);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                        firebaseviewholder.Card.setVisibility(View.VISIBLE);
                        firebaseviewholder.Assigned.setText(datasetfire.getAssigned().toString());
                        firebaseviewholder.Completed.setText(datasetfire.getCompleted().toString());
                        firebaseviewholder.Category.setText(datasetfire.getCategory());
                        firebaseviewholder.AssignedId.setText(datasetfire.getAssignedId());
                        firebaseviewholder.CId.setText(datasetfire.getCId());
                        firebaseviewholder.Description.setText(datasetfire.getDescription());
                        firebaseviewholder.Latitude.setText("PickupLat:"+String.valueOf(datasetfire.getPickupLatitude())+"\nDropOffLat:"+String.valueOf(datasetfire.getDropLatitude()));
                        firebaseviewholder.Longitude.setText("PickupLong:"+String.valueOf(datasetfire.getPickupLatitude())+"\nDropOffLong:"+String.valueOf(datasetfire.getDropLatitude()));


//                        if ((CheckAvailability.lat + CheckAvailability.longi) - (datasetfire.getLatitude() + datasetfire.getLongitude()) > 10) {
//                            Toast.makeText(getActivity(), "occur", Toast.LENGTH_LONG).show();
//                            firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
////                        Log.d("Running", "Mapcheck Invalid " + Cid);
//                        }


                        firebaseviewholder.playButton.setVisibility(View.GONE);
                        firebaseviewholder.showButton.setVisibility(View.GONE);
                        firebaseviewholder.assignButton.setText("Take Order");

                        firebaseviewholder.assignButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Toast.makeText(getActivity(), datasetfire.getUid(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), RiderDetailsActivity.class);
                                intent.putExtra("Assigned", datasetfire.getAssigned());
                                intent.putExtra("OrderId", datasetfire.getUid());
                                intent.putExtra("Completed", datasetfire.getCompleted());
                                intent.putExtra("Category", datasetfire.getCategory());
                                intent.putExtra("AssignedId", datasetfire.getAssignedId());
                                intent.putExtra("CId", datasetfire.getCId());
                                intent.putExtra("PickupLatitude", datasetfire.getPickupLatitude());
                                intent.putExtra("PickupLongitude", datasetfire.getPickupLongitude());
                                intent.putExtra("DropLatitude", datasetfire.getDropLatitude());
                                intent.putExtra("DropLongitude", datasetfire.getDropLongitude());
                                intent.putExtra("OrderDescriptionID", datasetfire.getOrderDescriptionID());
                                intent.putExtra("Description", datasetfire.getDescription());
                                startActivity(intent);
                            }
                        });


                    } else {
//                        firebaseviewholder.Card.setVisibility(View.GONE);
                              firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
                        //CHECK CONFIRM PAGE
                    }
                }

            }

            @NonNull
            @Override
            public firebaseviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new firebaseviewholder(LayoutInflater.from(getActivity()).inflate(R.layout.row_all_work, parent, false));
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






