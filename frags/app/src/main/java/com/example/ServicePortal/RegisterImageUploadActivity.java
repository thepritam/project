package com.example.ServicePortal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterImageUploadActivity extends AppCompatActivity {

    private Button btnSelectAvatar,btnSelectAadhar, btnUpload;
    private ImageView imageViewAvatar,imageViewAadhar;
    private Uri[] filePath = new Uri[2];
    private final int PICK_IMAGE_REQUEST = 22;
    String currentUid;
    int i;
    Intent intent;
    String name;
    String category;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_image_upload);

        Intent intent = getIntent();
        currentUid = intent.getStringExtra("id");
        final String name = intent.getStringExtra("Name");
        final String category = intent.getStringExtra("Category");

        btnSelectAvatar = findViewById(R.id.btnChooseavatar);
        btnSelectAadhar = findViewById(R.id.btnChooseAadhar);
        btnUpload = findViewById(R.id.btnUpload);
        imageViewAvatar = findViewById(R.id.imgViewAvatar);
        imageViewAadhar = findViewById(R.id.imgViewAadhar);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnSelectAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                i = 0;
                SelectImage();
            }
        });

        btnSelectAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                i =1;
                SelectImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                uploadImage();
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
            filePath[i] = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath[i]);

                if (i == 0) {
                    imageViewAvatar.setImageBitmap(bitmap);
                }
                else if(i == 1) {
                    imageViewAadhar.setImageBitmap(bitmap);
                }

            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage()
    {
        if (filePath[0] != null && filePath[1] != null) {

            final ProgressDialog progressDialogavatar
                    = new ProgressDialog(this);
            progressDialogavatar.setTitle("Uploading Avatar...");
            progressDialogavatar.show();

            StorageReference ref = storageReference.child("images/");

            ref.child(currentUid+"-Avatar").putFile(filePath[0]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Document").child("Avatar").setValue(currentUid+"-Avatar");

                                    progressDialogavatar.dismiss();
                                    Toast.makeText(RegisterImageUploadActivity.this,"Image Uploaded!!",Toast.LENGTH_LONG).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialogavatar.dismiss();
                            Toast.makeText(RegisterImageUploadActivity.this,"Failed!",Toast.LENGTH_LONG).show();
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

            final ProgressDialog progressDialogaadhar
                    = new ProgressDialog(this);
            progressDialogaadhar.setTitle("Uploading Aadhar...");
            progressDialogaadhar.show();

            ref.child(currentUid+"-Aadhar").putFile(filePath[1]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Document").child("Aadhar").setValue(currentUid+"-Aadhar");

                    progressDialogaadhar.dismiss();
                    Toast.makeText(RegisterImageUploadActivity.this,"Image Uploaded!!",Toast.LENGTH_LONG).show();

                    intent = new Intent(RegisterImageUploadActivity.this, ValidityCheck.class);
                    intent.putExtra("Name",name);
                    intent.putExtra("id",currentUid);
                    intent.putExtra("Category",category);
                    startActivity(intent);
                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialogaadhar.dismiss();
                            Toast.makeText(RegisterImageUploadActivity.this,"Failed!",Toast.LENGTH_LONG).show();
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
                                    progressDialogaadhar.setMessage("Uploaded "+ (int)progress + "%");
                                }
                            });
        }
        else {
            Toast.makeText(getApplicationContext(),"PLEASE UPLOAD IMAGES",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(RegisterImageUploadActivity.this, HomeActivity.class);
        finish();
        startActivity(intent);
        Toast.makeText(this, "Registration not completed...", Toast.LENGTH_SHORT).show();
    }
}
