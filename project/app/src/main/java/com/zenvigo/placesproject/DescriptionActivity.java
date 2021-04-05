package com.project.placesproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DescriptionActivity extends AppCompatActivity {
    private EditText editText;
    private Button button ;
    private Button floatingActionButton ;
    private  MediaRecorder recorder;
    private String fileName=null;
    private TextView textView ;
    private String data;
    private ProgressDialog progressDialog;
    private StorageReference storageReference,filepath;
    private long time;
    private Uri audiouri;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final private int REQUEST_PERMISSION_CODE=1000;
     Chronometer chronometer1;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
       // BottomNavigationItemView bottomNavigationItemView= (BottomNavigationItemView) findViewById(R.id.navigation);
        editText = findViewById(R.id.edittext);
        button = findViewById(R.id.buttoneditext);
        floatingActionButton = findViewById(R.id.button3);
        textView = findViewById(R.id.textView2);
         chronometer1 = findViewById(R.id.chronometer);
         progressDialog = new ProgressDialog(this);
         storageReference = FirebaseStorage.getInstance().getReference();
        if(!checkPermissionFromDevice()){
            requestPermission();
        }
       //  if(data!=null) { button.setEnabled(true);}
               button.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {


                       uploadaudio();

                      // uploaddata();
                   }
               });
            floatingActionButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(checkPermissionFromDevice()){
                        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();

                        fileName+="/recorded_voice.3gp";
                   if(event.getAction() == MotionEvent.ACTION_DOWN){
                        startRecording();
                        chronometer1.setBase(SystemClock.elapsedRealtime());
                        chronometer1.start();
                        textView.setText("started Recording!");
                    }else if(event.getAction() == MotionEvent.ACTION_UP){
                        stopRecording();
                        chronometer1.stop();

                        time= SystemClock.elapsedRealtime()-chronometer1.getBase();
                        time=time/1000;
                        //
                        if(time<10){
                            textView.setText("RECORD AGAIN");
                            textView.setTextColor(Color.RED);
                            Toast.makeText(DescriptionActivity.this, "RECORDING MUST BE OF 10 SECONDs", Toast.LENGTH_SHORT).show();
                            chronometer1.setBase(SystemClock.elapsedRealtime());


                        }
                        else if(time>=10&&time<=180) {
                            button.setEnabled(true);
                            textView.setText("FINISHED Recording!");
                            textView.setTextColor(Color.GREEN);
                        }
                        else {
                            button.setEnabled(false);
                            textView.setText("RECORDING MUST BE LESS THAN 3 MINUTES");
                            textView.setTextColor(Color.RED);
                        }


                    }}
                    else {
                        requestPermission();
                    }

                    return false;
                }
            });






        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data = editText.getText().toString();
                //  if(data!=null) { button.setEnabled(true);}
                String[] value = editText.getText().toString().split("\\s+");
                if (data == null)
                    button.setEnabled(false);
                else {
                    if (!data.isEmpty()) {
                        button.setEnabled(true);
                    }
                    if (value.length > 150) {
                        Toast.makeText(DescriptionActivity.this, "type less than 150 words ", Toast.LENGTH_SHORT).show();
                        button.setEnabled(false);

                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                data =editText.getText().toString();
                   if(data==null)
                       button.setEnabled(false);
            }
        });
    }

    private void uploaddata() {

        String Latitude=getIntent().getStringExtra("latitude").trim();
        String Longitude=getIntent().getStringExtra("longitude").trim();
       final double lat=Double.parseDouble(Latitude);
        final double lon=Double.parseDouble(Longitude);
        Map<String,Object> data = new HashMap<>();
        data.put("Assigned", 0);
        data.put("AssignedId", "");
        data.put("Completed",0);
        data.put("Category","Mechanic");
        data.put("CId","1AcMdMChUzRnPdokrGAIUEcfUSc2");
        data.put("Description",editText.getText().toString());
        data.put("AudioDescription",audiouri.toString());
        data.put("Latitude",lat);
        data.put("Longitude",lon);
        data.put("PaidCommission",0);
        data.put("PaidFull",0);

        db.collection("Orders")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("upload", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("upload", "Error adding document", e);
                    }
                });


    }

    private void uploadaudio() {
       // final Intent intent = new Intent(DescriptionActivity.this,VerifyActivity.class);
        final String Latitude=getIntent().getStringExtra("latitude").trim();
        final String Longitude=getIntent().getStringExtra("longitude").trim();
        final String id=getIntent().getStringExtra("uid");
        if(time>10 ) {


                progressDialog.setMessage("uploading..");
                progressDialog.show();
                filepath = storageReference.child("audio").child(id + "new_audio.3gp");
                Uri uri = Uri.fromFile(new File(fileName));
                Log.d("show", "uploadaudio: " + uri);
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();

                        Toast.makeText(DescriptionActivity.this, "Uploading Finished", Toast.LENGTH_SHORT).show();
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("show", "onSuccess: " + uri);
                                audiouri = uri;
                                Intent intent = new Intent(DescriptionActivity.this, VerifyActivity.class);
                          /*  if(data!=null&& audiouri==null){
                                // Intent intent = new Intent(DescriptionActivity.this,VerifyActivity.class);
                                intent.putExtra("data",editText.getText().toString());
                                intent.putExtra("uid",id);
                                intent.putExtra("latitude",Latitude);
                                intent.putExtra("longitude",Longitude);

                                startActivity(intent);
                            }*/
                                //  else {
                                intent.putExtra("link", audiouri.toString());
                                intent.putExtra("data", editText.getText().toString());
                                intent.putExtra("uid", id);
                                intent.putExtra("latitude", Latitude);
                                intent.putExtra("longitude", Longitude);
                                // }
                                startActivity(intent);

                                //  uploaddata();

                            }
                        });
                    }
                });

        }
        else {
           // Log.d("error","here");

            if(data!=null && !data.isEmpty()&& audiouri==null){
                Log.d("error",""+data.isEmpty()+"");
                 Intent intent = new Intent(DescriptionActivity.this,VerifyActivity.class);
                intent.putExtra("data",editText.getText().toString());
                intent.putExtra("uid",id);
                intent.putExtra("latitude",Latitude);
                intent.putExtra("longitude",Longitude);

                startActivity(intent);
            }
            else
                Toast.makeText(getApplicationContext(),"Please enter either description or audio",Toast.LENGTH_LONG).show();

        }
       // startActivity(intent);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_PERMISSION_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean checkPermissionFromDevice() {
        int write_External_Storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return ((write_External_Storage == PackageManager.PERMISSION_GRANTED)&& (record_audio_result==PackageManager.PERMISSION_GRANTED));
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        Toast.makeText(this,"started Recording ",Toast.LENGTH_SHORT).show();
        try {
            recorder.prepare();
            Log.d("sound", "startRecording: ");
        } catch (IOException e) {
            Log.d("sound", "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        try{
        recorder.stop();
        recorder.release();
        recorder = null;
         Toast.makeText(this,"finish Recording ",Toast.LENGTH_SHORT).show();}
        catch (RuntimeException stopException){

        }
    }
}
