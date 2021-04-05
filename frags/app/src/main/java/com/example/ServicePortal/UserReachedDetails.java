package com.example.ServicePortal;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UserReachedDetails extends AppCompatActivity {

    public MediaPlayer player;
    private GridLayout mlayout;
    Context context;
    Button additem;
    DynamicViews dnv;
    private TableLayout tableLayout;
    int id = 0;
    public Runnable runnable;
    public Handler handler;
    String orderdetid;
    DocumentReference assignstate;

//    Context ctx;
//
//    public UserReachedDetails(Context ctx)
//    {
//        this.ctx = ctx;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reached_details);

        final TextView NameDet, PhoneDet, EmailDet, CategoryDet,DescDet;
        final Button gotoButton, showButton, playButton, stopButton, unsuccessBtn, completeBtn;
        final Button Start_Continue = findViewById(R.id.Start_Continue);
        final SeekBar seekBar;

        additem = (Button) findViewById(R.id.addbutton);

        handler = new Handler();

        NameDet = (TextView) findViewById(R.id.cname);
//        PhoneDet = (TextView) findViewById(R.id.phno);
//        EmailDet = (TextView) findViewById(R.id.email);
        DescDet = (TextView) findViewById(R.id.des);
        gotoButton = findViewById(R.id.gotobutton);
        showButton = findViewById(R.id.showBtn);
        playButton = findViewById(R.id.playBtn);
        stopButton = findViewById(R.id.stopBtn);
        seekBar = findViewById((R.id.seekbar));
        unsuccessBtn = findViewById(R.id.unsuccessBtn);
        completeBtn = findViewById(R.id.completeBtn);

        Intent intent = getIntent();
        String Cid = intent.getStringExtra("CId");
        final String desc = intent.getStringExtra("Description");
        final String Audiodesc = intent.getStringExtra("AudioDescription");
        final String Uid = intent.getStringExtra("Uid");
        final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        rootRef.collection("OrderDetails")
                .whereEqualTo("OrderId", Uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                orderdetid = document.getId();
                                Log.d("idorder",orderdetid);
                                assignstate = rootRef.collection("OrderDetails").document(orderdetid);

                                assignstate.update("inTime", FieldValue.serverTimestamp());
                                assignstate.update("hasReached", true);
                                Log.d("idorder","workblyat");
                            }
                        } else {

                        }

                    }
                });


        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        FirebaseDatabase.getInstance().getReference().child("Users").child(Cid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

                NameDet.setText(value.get("Name").toString());
//                PhoneDet.setText(user.Phone);
//                EmailDet.setText(user.Email);
                DescDet.setText(desc);



                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (player != null)
                        {
                            player.release();
                            player = null;


                            Log.d("this","happens");
                            stopButton.setVisibility(View.GONE);
                            playButton.setVisibility(View.VISIBLE);
                            seekBar.setVisibility(View.GONE);
//                                firebaseviewholder.seekBar.setProgress(0);
                        }

                        try {

                            player = new MediaPlayer();
                            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            player.setDataSource(Audiodesc);
                            player.prepare();
//            player.start();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer player) {
                                seekBar.setVisibility(View.VISIBLE);
                                seekBar.setMax(player.getDuration());
                                player.start();
                                changeSeekbar(seekBar);
                            }
                        });

//                            player.start();
//                            changeSeekbar();

                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                if(b){
                                    if(player != null)
                                        player.seekTo(i);

                                    else{
                                        stopButton.setVisibility(View.GONE);
                                        playButton.setVisibility(View.VISIBLE);
                                        seekBar.setVisibility(View.GONE);
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

                        playButton.setVisibility(View.GONE);
                        stopButton.setVisibility(View.VISIBLE);

                    }
                });
                stopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(player != null){
                            player.stop();
                            seekBar.setProgress(0);
                            player.release();
                            player = null;
                        }

                        stopButton.setVisibility(View.GONE);
                        playButton.setVisibility(View.VISIBLE);
                        seekBar.setVisibility(View.GONE);

                    }
                });

                unsuccessBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        assignstate.update("isUnSuccessful", true);

                    }
                });

                completeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        assignstate.update("outTime", FieldValue.serverTimestamp());

                    }
                });

//                gotoButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(AllWorkDetails.this, StartActivity.class);
//                        startActivity(intent);
//                    }
//                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        HashMap<String, String> jobdesc = new HashMap<>();


        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final EditText task=new EditText(getApplicationContext());
                task.setMaxWidth(100);
                task.setMinHeight(100);
                task.setText("Task");


                    tableLayout.addView(task);

                task.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if(s.length() != 0)
                            Log.d("edittext","abc"+task.getText().toString());
                    }
                });

                    String val = task.getText().toString();

//                    Toast.makeText(getApplicationContext(),""+val+"",Toast.LENGTH_LONG).show();
//                    Log.d("edittext","abc"+task.getText().toString());
//                id++;

            }
        });


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

//    public void onAddField(View v) {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View rowView = inflater.inflate(R.layout.field, null);
//
//
//        //  Log.d("text",""+ edittext_var.getText().toString());
//        // Add the new row before the add field button.
//        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
//    }
//
//
//    @SuppressLint("ResourceType")
//    public void onSelect(View v) { // on click event for a SELECT button
//
//        edittext_var = (EditText)((View) v.getParent()).findViewById(R.id.number_edit_text) ;
//        subjectslist.add(edittext_var.getText().toString());
//        selectbtn=(Button)((View) v.getParent()).findViewById(R.id.select_button);
//        selectbtn.setEnabled(false);
//        selectbtn.setBackgroundResource(R.drawable.done);
////            if(selectbtn!=null)
////                selectbtn1.setEnabled(false);
//        if(subjectslist.size()==0){
//            confirmbtn.setEnabled(false);
//            Toast.makeText(getApplicationContext(),"Please Enter the subject/s",Toast.LENGTH_SHORT).show();
//        }
//        else {
//            confirmbtn.setEnabled(true);
//        }
//
//        Log.d("text",edittext_var.getText().toString());
//    }
//
//
//    public void onDelete(View v) {
//        edittext_var = (EditText)((View) v.getParent()).findViewById(R.id.number_edit_text) ;
//        String toremove = edittext_var.getText().toString();
//        parentLinearLayout.removeView((View) v.getParent());
//
//        for(int i=0;i<subjectslist.size();i++)
//        {
//            if(toremove.equals(subjectslist.get(i))) {
//                subjectslist.remove(i);
//                break;
//            }
//        }
//        if(subjectslist.size()==0){
//            confirmbtn.setEnabled(false);
//            Toast.makeText(getApplicationContext(),"Please Enter the subject/s",Toast.LENGTH_SHORT).show();
//        }
//        else {
//            confirmbtn.setEnabled(true);
//        }
//
//    }
//
