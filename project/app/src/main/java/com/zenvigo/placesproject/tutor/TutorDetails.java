package com.project.placesproject.tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.placesproject.R;
import com.project.placesproject.customercare;
import com.project.placesproject.yourorderactivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TutorDetails extends AppCompatActivity {


    String uid,mode;
    Long price;
    Button button;
    String phone;
    HashMap<String,Object> experience,category;

    String VIDEO_SAMPLE ;
    private VideoView mVideoView;
    private TextView nametext,experiencetext,tutorprice;
    // Current playback position (in milliseconds).
    private int mCurrentPosition = 0;
    private ProgressDialog progressDialog;
    private  MediaController controller;
    private BottomNavigationView bottomNavigationView;

    // Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_details);
        progressDialog = new ProgressDialog(this);
        bottomNavigationView=findViewById(R.id.navigation);
        VIDEO_SAMPLE = getIntent().getStringExtra("link");
        mVideoView = findViewById(R.id.videoview);
        button=findViewById(R.id.button_tutor_video);
        nametext=findViewById(R.id.tutorname);
        experiencetext=findViewById(R.id.tutorphone);
        tutorprice=findViewById(R.id.tutorprice);
        // progressBar = (ProgressBar) findViewById(R.id.progrss);
        //mBufferingTextView = findViewById(R.id.buffering_textview);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Intent intent;
                switch(menuItem.getItemId())
                {
                    case R.id.recent:
                        intent = new Intent(getApplicationContext(), yourorderactivity.class);
                        startActivity(intent);
                        break;
                    case R.id.account:
                        intent = new Intent(getApplicationContext(), account_activity.class);
                        startActivity(intent);
                        break;

                    case R.id.review:
                        intent = new Intent(getApplicationContext(), customercare.class);
                        startActivity(intent);
                        break;

                }
                return false;
            }
        });

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }
         controller = new MediaController(this);

        price =Long.parseLong(getIntent().getStringExtra("price"));
        uid = getIntent().getStringExtra("uid");
        mode = getIntent().getStringExtra("mode");




    }
    public void fetch(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
                // String key = dataSnapshot.getKey();
                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                String name = value.get("Name").toString();
                category=(HashMap<String, Object>) value.get("Category");
                experience=(HashMap<String, Object>) category.get("Experience");
                phone=value.get("Phone").toString();
                Log.d("show", "onDataChange: "+experience);
                Log.d("show", "onDataChange: "+phone);
                Log.d("show", "onDataChange: "+name);
                nametext.setText(name);
                Log.d("show", "onDataChange: ");

                String x="";int i=1;
                Iterator<Map.Entry<String, Object>> itr = experience.entrySet().iterator();
                while(itr.hasNext())
                {
                    Map.Entry<String, Object> entry = itr.next();
                    //Log.d(TAG, "onDataChange: "+x);
                    String p=String.valueOf(i);
                    i=i+1;
                    x=x+p+". "+entry.getValue()+" in "+ entry.getKey()+"\n";

                }
              /*  for(Object j:experience.values()){
                    //Log.d(TAG, "onDataChange: "+x);
                    String p=String.valueOf(i);
                    i=i+1;
                    x=x+p+". "+j.toString()+"\n";
                }*/
                experiencetext.setText(x);
                tutorprice.setText("Rs. "+price.toString());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("show", "onClick: ");
                        mVideoView.setVisibility(View.VISIBLE);
                        controller.setMediaPlayer(mVideoView);
                        mVideoView.setMediaController(controller);
                        initializePlayer();

                    }
                });

                // Set up the media controller widget and attach it to the video view.


//                            final User user = dataSnapshot.getValue(User.class);
                // Log.d("Running", model.getUID());
                //Log.d("Running", "" + value);
//

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        // Load the media each time onStart() is called.
        Log.d("show", "onStart: ");
        fetch();

    }

    @Override
    protected void onPause() {
        super.onPause();

        // In Android versions less than N (7.0, API 24), onPause() is the
        // end of the visual lifecycle of the app.  Pausing the video here
        // prevents the sound from continuing to play even after the app
        // disappears.
        //
        // This is not a problem for more recent versions of Android because
        // onStop() is now the end of the visual lifecycle, and that is where
        // most of the app teardown should take place.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
        releasePlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current playback position (in milliseconds) to the
        // instance state bundle.
        outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition());
    }

    private void initializePlayer() {
        // Show the "Buffering..." message while the video loads.
        // mBufferingTextView.setVisibility(VideoView.VISIBLE);
        // progressBar.setVisibility(View.VISIBLE);
        progressDialog.setMessage("loading..");
        progressDialog.show();
        // Buffer and decode the video sample.
        Uri videoUri = getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(videoUri);

        // Listener for onPrepared() event (runs after the media is prepared).
        mVideoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {

                        // Hide buffering message.
                        //  progressBar.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        // mBufferingTextView.setVisibility(VideoView.INVISIBLE);

                        // Restore saved position, if available.
                        if (mCurrentPosition > 0) {
                            mVideoView.seekTo(mCurrentPosition);
                        } else {
                            // Skipping to 1 shows the first frame of the video.
                            mVideoView.seekTo(1);
                        }

                        // Start playing!
                        mVideoView.start();
                    }
                });

        // Listener for onCompletion() event (runs after media has finished
        // playing).
        mVideoView.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Toast.makeText(TutorDetails.this,
                                "completed",
                                Toast.LENGTH_SHORT).show();

                        // Return the video position to the start.
                        mVideoView.seekTo(0);
                    }
                });
    }


    // Release all media-related resources. In a more complicated app this
    // might involve unregistering listeners or releasing audio focus.
    private void releasePlayer() {
        mVideoView.stopPlayback();
    }

    // Get a Uri for the media sample regardless of whether that sample is
    // embedded in the app resources or available on the internet.
    private Uri getMedia(String mediaName) {
        if (URLUtil.isValidUrl(mediaName)) {
            // Media name is an external URL.
            return Uri.parse(mediaName);
        } else {

            // you can also put a video file in raw package and get file from there as shown below

            return Uri.parse("android.resource://" + getPackageName() +
                    "/raw/" + mediaName);


        }
    }

    private class account_activity {
    }
}
