package com.project.placesproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerifyActivity extends AppCompatActivity {
    private TextView textView ;
    private ProgressDialog progressDialog;
    private StorageReference storageReference,filepath;
    private Uri audiouri;
    public MediaPlayer player;
    public ImageButton playButton,stopButton;
    public SeekBar seekBar;
    public Runnable runnable;
    public Handler handler;
    private Button button;
    private RelativeLayout relativeLayout,relativeLayout1;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String link,data,datastring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        textView=findViewById(R.id.textView3);
        playButton=findViewById(R.id.buttonstart);
        stopButton=findViewById(R.id.buttonstop);
        seekBar=findViewById(R.id.seekBar);
        relativeLayout=findViewById(R.id.sound);
        relativeLayout1=findViewById(R.id.textshow);
        button=findViewById(R.id.buttonpaid);
        handler = new Handler();
         link=getIntent().getStringExtra("link");
         data=getIntent().getStringExtra("data");
         datastring=getIntent().getStringExtra("data");
        Log.d("show", "onCreate:"+getIntent().getStringExtra("link"));
        textView.setText(data);
        if(data==null){
            relativeLayout1.setVisibility(View.INVISIBLE);
        }
        if(link==null){
            relativeLayout.setVisibility(View.INVISIBLE);
        }
        if (link!=null) {
            try {

                player = new MediaPlayer();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(link);
                player.prepare();
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        seekBar.setMax(mp.getDuration());

                    }
                });

//            player.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    player.start();
                    changeSeekbar(seekBar);

                    playButton.setVisibility(View.GONE);
                    stopButton.setVisibility(View.VISIBLE);

                }
            });
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        if (player != null)
                            player.seekTo(progress);

                        else {
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
              /*  handler.removeCallbacks(runnable);
                int totalDuration = player.getDuration();
                //int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                player.seekTo(totalDuration);*/

                }
            });
            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    player.stop();
                    // changeSeekbar(seekBar);
                    //handler.removeCallbacks(runnable);

                    try {

                        player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(link);
                        player.prepare();
//            player.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stopButton.setVisibility(View.GONE);
                    playButton.setVisibility(View.VISIBLE);

                }
            });
          if (this.isFinishing()) { //basically BACK was pressed from this activity
              Log.d("stopping","here");
                player.stop();
                Toast.makeText(VerifyActivity.this, "YOU PRESSED BACK FROM YOUR 'HOME/MAIN' ACTIVITY", Toast.LENGTH_SHORT).show();
            }
            Context context = getApplicationContext();
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            if (!taskInfo.isEmpty()) {
                ComponentName topActivity = taskInfo.get(0).topActivity;
                if (!topActivity.getPackageName().equals(context.getPackageName())) {
                    player.stop();
                    Toast.makeText(VerifyActivity.this, "YOU LEFT YOUR APP", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerifyActivity.this, "YOU SWITCHED ACTIVITIES WITHIN YOUR APP", Toast.LENGTH_SHORT).show();
                }
            }
            super.onPause();
        }
        String Latitude=getIntent().getStringExtra("latitude").trim();
        String Longitude=getIntent().getStringExtra("longitude").trim();
        String id=getIntent().getStringExtra("uid");
        Log.d("show", "onCreate: "+Latitude);
        Log.d("show", "onCreate: "+Longitude);
        Log.d("show", "onCreate: "+id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               uploaddata();
            }
        });
    }
    @Override
    public void onPause() {
        if (isApplicationSentToBackground(this)){

            Log.d("isPaused","true");
            if(player!=null){
            player.stop();}
            // Do what you want to do on detecting Home Key being Pressed
        }
        super.onPause();
    }
    public boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(player!=null){
        player.stop();}
        Toast.makeText(VerifyActivity.this, "YOU PRESSED BACK FROM YOUR 'HOME/MAIN' ACTIVITY", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }


    public void changeSeekbar(final SeekBar skb)
    {
        Log.d("show", "changeSeekbar: ");
        if(player != null) {
            Log.d("show", "changeSeekbar: plays");
            skb.setProgress(player.getCurrentPosition());
            Log.d("show", "changeSeekbar: tocheck");
            if (player.isPlaying()) {
                Log.d("show", "changeSeekbar:bal ");
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.d("show", "run: ");
                        changeSeekbar(skb);
                    }
                };

                handler.postDelayed(runnable, 1000);
            }
        }
    }


  /*  private void changeseekbar() {
        try {
            if (player.isPlaying()){
            seekBar.setProgress(player.getCurrentPosition());
            runnable = new Runnable() {
                @Override
                public void run() {
                    long totalDuration = player.getDuration();
                    long currentDuration = player.getCurrentPosition();
                    int progress = (int)(currentDuration/totalDuration)*100;
                    //Log.d("Progress", ""+progress);
                    seekBar.setProgress(progress);

                    // Running this thread after 100 milliseconds
                    handler.postDelayed(runnable, 100);
                    //changeseekbar();
                }
            };
           // handler.postDelayed(runnable,1000);
        }
        } catch (NullPointerException ne){

        }
    }*/
    public void updateProgressBar() {
        handler.postDelayed(runnable, 100);
    }



    private void uploaddata() {
        String Latitude=getIntent().getStringExtra("latitude").trim();
        String Longitude=getIntent().getStringExtra("longitude").trim();
        String id=getIntent().getStringExtra("uid");
        final double lat=Double.parseDouble(Latitude);
        final double lon=Double.parseDouble(Longitude);
        Map<String,Object> data = new HashMap<>();
        data.put("Assigned", 0);
        data.put("AssignedId", "");
        data.put("Completed",0);
        data.put("Category","Mechanic");
        data.put("CId",id);
        if(datastring!=null){
        data.put("Description",datastring);}
        else {
            data.put("Description","");
        }
        if(link!=null){
        data.put("AudioDescription",link);}
        else {
            data.put("AudioDescription","");
        }
        data.put("Latitude",lat);
        data.put("Longitude",lon);
        data.put("PaidCommission",0);
        data.put("PaidFull",0);
        data.put("isCancelled",0);

        db.collection("Orders")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("upload", "DocumentSnapshot written with ID: " + documentReference.getId());
                        searchserviceman(documentReference.getId());
                        //Intent intent = new Intent(VerifyActivity.this,ServicemaActivity.class);
                        //startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("upload", "Error adding document", e);
                    }
                });


    }

    private void searchserviceman(final String id) {

        setContentView(R.layout.background);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("We are looking for your zenvman...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        final DocumentReference docRef = db.collection("Orders").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("fire", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("fire", "Current data: " + snapshot.getData());
                    if((Long)(snapshot.getData().get("Assigned"))==1)
                    {
                        progressDialog.dismiss();
                        Intent intent = new Intent(VerifyActivity.this,ServicemaActivity.class);
                        intent.putExtra("orderid",id);
                        intent.putExtra("Assignedid",(String)snapshot.getData().get("AssignedId"));
                        startActivity(intent);

                    }
                } else {
                    Log.d("fire", "Current data: null");
                }
            }
        });


    }


}
