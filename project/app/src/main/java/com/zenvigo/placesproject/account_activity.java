package com.project.placesproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class account_activity extends AppCompatActivity {
      Button logout;
      ImageButton photoupload;
      ImageView imageViewAvatar;
    String id=SaveDetails.id;
    private Uri filePath ;
    private final int PICK_IMAGE_REQUEST = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_activity);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        logout=findViewById(R.id.logout2);

        final TextView name,mail,dob,phone,sex,address;
        sex=findViewById(R.id.usergender);
        name=findViewById(R.id.username);
        mail=findViewById(R.id.useremail);
        phone=findViewById(R.id.userphone);
        dob=findViewById(R.id.userdob);
        imageViewAvatar=findViewById(R.id.profile_image);
        photoupload=findViewById(R.id.photoupload);
        Log.d("account", "onCreate: "+id);
        address=findViewById(R.id.userAdress);

        final ProgressDialog progressDialog= new ProgressDialog(account_activity.this);
        progressDialog.setTitle("Loading Your Account");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Document").child("Avatar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Log.d("account",""+dataSnapshot);
                    String value =  dataSnapshot.getValue().toString();
                    Log.d("account",""+value);





                    FirebaseStorage storage;
                    StorageReference storageReference;

                    storage = FirebaseStorage.getInstance();
                    storageReference = storage.getReference();


                    StorageReference httpsReference = storage.getReferenceFromUrl(value);
                    progressDialog.dismiss();

                    httpsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            try {
                                Log.d("show", "onSuccess: " + uri);

                                URL url = new URL(uri.toString());
                                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                                imageViewAvatar.setImageBitmap(bmp);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });


        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                Log.d("account", "onDataChange: "+user);
                name.setText(user.get("Name").toString());
                dob.setText(user.get("DateBirth").toString());
                phone.setText(user.get("Phone").toString());
                mail.setText(user.get("Email").toString());
                sex.setText(user.get("Gender").toString());
                address.setText(user.get("Address").toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent =new Intent(account_activity.this,HomeActivity.class);
                finish();
                startActivity(intent);
            }
        });
        photoupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectImage();

            }
        });
}
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);


                    imageViewAvatar.setImageBitmap(bitmap);
                    uploadImage();



            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {
        if (filePath != null) {

            final ProgressDialog progressDialogavatar
                    = new ProgressDialog(this);
            progressDialogavatar.setTitle("Uploading Avatar...");
            progressDialogavatar.show();
            FirebaseStorage storage;
            StorageReference storageReference;

            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();


            final StorageReference ref = storageReference.child("images/").child(id+"-Avatar");

            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("here", "onSuccess: " + uri);

                            FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Document").child("Avatar").setValue(uri.toString());

                            progressDialogavatar.dismiss();
                            Toast.makeText(getApplicationContext(),"Image Uploaded!!",Toast.LENGTH_LONG).show();


                        }
                    });


                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialogavatar.dismiss();
                            Toast.makeText(getApplicationContext(),"Failed!",Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialogavatar.setMessage("Uploaded "+ (int)progress + "%");
                                }
                            });


        }
        else {
            Toast.makeText(getApplicationContext(),"PLEASE UPLOAD IMAGES",Toast.LENGTH_LONG).show();
        }
    }

}