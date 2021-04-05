package com.example.ServicePortal.ui.slideshow;

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
import com.example.ServicePortal.ShowonMap;
import com.example.ServicePortal.datasetfire;
import com.example.ServicePortal.firebaseviewholder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SlideshowFragment extends Fragment {


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

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

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
                    if (datasetfire.getAssigned() == 1 && datasetfire.getCompleted() == 1 && datasetfire.getAssignedId().equals(CheckAvailability.id)) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(datasetfire.getCId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                            final User user = dataSnapshot.getValue(User.class);
//                            Log.d("Running",user.Name);

                                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                String name = value.get("Name").toString();

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

                        firebaseviewholder.Description.setText(datasetfire.getDescription());
                        firebaseviewholder.Latitude.setText(String.valueOf(datasetfire.getLatitude()));
                        firebaseviewholder.Longitude.setText(String.valueOf(datasetfire.getLongitude()));


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
//
//
//
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });


//                    firebaseviewholder.assignButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            DocumentReference assignstate = rootRef.collection("Orders").document(datasetfire.getUid());
//
//                            assignstate.update("Assigned",1)
//
//                                    .addOnSuccessListener(new OnSuccessListener< Void >() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Toast.makeText(getActivity(), "Assigned Successfully",
//                                                    Toast.LENGTH_LONG).show();
//                                        }
//                                    });
//
//
//                            //   Toast.makeText(getApplicationContext(), datasetfire.getUid(), Toast.LENGTH_LONG).show();
////                            Intent intent = new Intent(getActivity(), AllWorkDetails.class);
////                            intent.putExtra("Assigned", datasetfire.getAssigned());
////                            intent.putExtra("Completed", datasetfire.getCompleted());
////                            intent.putExtra("Category", datasetfire.getCategory());
////                            intent.putExtra("AssignedId", datasetfire.getAssignedId());
////                            intent.putExtra("CId", datasetfire.getCId());
////                            intent.putExtra("Description", datasetfire.getDescription());
////                            intent.putExtra("Latitude", datasetfire.getLatitude());
////                            intent.putExtra("Longitude", datasetfire.getLongitude());
////
////                            startActivity(intent);
//                        }
//                    });
                    } else {
//                    firebaseviewholder.Card.setVisibility(View.GONE);
                        firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
                    }
                }
                else if ( CheckAvailability.Category.equals("Grocery") || CheckAvailability.Category.equals("General Store"))
                {
                    if (datasetfire.getAssigned() == 1 && datasetfire.getCompleted() == 1 && CheckAvailability.Category.equals(datasetfire.getCategory()) && datasetfire.getIsCancelled() == 0 && CheckAvailability.id.equals(datasetfire.getAssignedId())) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(datasetfire.getCId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                            final User user = dataSnapshot.getValue(User.class);
//                            Log.d("Running",user.Name);

                                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                String name = value.get("Name").toString();

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

                        firebaseviewholder.Description.setText(datasetfire.getDescription());
                        firebaseviewholder.Latitude.setText(String.valueOf(datasetfire.getLatitude()));
                        firebaseviewholder.Longitude.setText(String.valueOf(datasetfire.getLongitude()));


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

                        firebaseviewholder.stopButton.setVisibility(View.GONE);
                        firebaseviewholder.playButton.setVisibility(View.GONE);
                        firebaseviewholder.seekBar.setVisibility(View.GONE);

                    } else {
//                    firebaseviewholder.Card.setVisibility(View.GONE);
                        firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
                    }
                }

                else if(CheckAvailability.Category.equals("Tutor")) {
                    if (datasetfire.getAssigned() == 1 && datasetfire.getCompleted() == 1 && CheckAvailability.Category.equals(datasetfire.getCategory()) && datasetfire.getIsCancelled() == 0) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(datasetfire.getCId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                            final User user = dataSnapshot.getValue(User.class);
//                            Log.d("Running",user.Name);

                                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                String name = value.get("Name").toString();

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

                        firebaseviewholder.Description.setText(datasetfire.getDescription());
                        firebaseviewholder.Latitude.setText(String.valueOf(datasetfire.getLatitude()));
                        firebaseviewholder.Longitude.setText(String.valueOf(datasetfire.getLongitude()));


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

                        firebaseviewholder.stopButton.setVisibility(View.GONE);
                        firebaseviewholder.playButton.setVisibility(View.GONE);
                        firebaseviewholder.seekBar.setVisibility(View.GONE);

                    } else {
//                    firebaseviewholder.Card.setVisibility(View.GONE);
                        firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
                    }
                }

                else if ( CheckAvailability.Category.equals("Rider"))
                {
                    if (datasetfire.getAssigned() == 1 && datasetfire.getCompleted() == 1 && CheckAvailability.Category.equals(datasetfire.getCategory()) && datasetfire.getIsCancelled() == 0) {

                        FirebaseDatabase.getInstance().getReference().child("Users").child(datasetfire.getCId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                            final User user = dataSnapshot.getValue(User.class);
//                            Log.d("Running",user.Name);

                                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                String name = value.get("Name").toString();

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

                        firebaseviewholder.Description.setText(datasetfire.getDescription());
                        firebaseviewholder.Latitude.setText(String.valueOf(datasetfire.getLatitude()));
                        firebaseviewholder.Longitude.setText(String.valueOf(datasetfire.getLongitude()));


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

                        firebaseviewholder.stopButton.setVisibility(View.GONE);
                        firebaseviewholder.playButton.setVisibility(View.GONE);
                        firebaseviewholder.seekBar.setVisibility(View.GONE);

                    } else {
//                    firebaseviewholder.Card.setVisibility(View.GONE);
                        firebaseviewholder.layout_to_hide.setVisibility(View.GONE);
                    }
                }


            }

            @NonNull
            @Override
            public firebaseviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new firebaseviewholder(LayoutInflater.from(getActivity()).inflate(R.layout.row_com_work, parent, false));
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