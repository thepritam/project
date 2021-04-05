package com.example.ServicePortal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class TutorImageUploadActivity extends AppCompatActivity {

    private Button btnSelect10,btnSelect12, btnSelectGrad, btnUpload;
    TextView cert10,cert12,certGrad;
    EditText exp;
    private Uri[] filePath = new Uri[3];
    private String[] filePathDisp = new String[3];
    private final int PICK_IMAGE_REQUEST = 22;
    String currentUid;
    int i;
    Intent intent;
    String name;
    String category;
    String graddeg;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_image_upload);

        Intent intent = getIntent();
        currentUid = intent.getStringExtra("id");
        final String name = intent.getStringExtra("Name");
        final String category = intent.getStringExtra("Category");
        graddeg = intent.getStringExtra("Gradreq");

        btnSelect10 = findViewById(R.id.btnChoose10);
        btnSelect12 = findViewById(R.id.btnChoose12);
        btnSelectGrad = findViewById(R.id.btnChooseGrad);
        btnUpload = findViewById(R.id.btnUpload);
        cert10 = findViewById(R.id.cert10det);
        cert12 = findViewById(R.id.cert12det);
        certGrad = findViewById(R.id.certGraddet);
        exp = findViewById(R.id.experience);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnSelect10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                i = 0;
                SelectImage();
            }
        });

        btnSelect12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                i =1;
                SelectImage();
            }
        });

        if (graddeg.equals("1")) {

            btnSelectGrad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i = 2;
                    SelectImage();
                }
            });
        }
        else {
            btnSelectGrad.setVisibility(View.GONE);
            certGrad.setVisibility(View.GONE);
        }

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
            filePathDisp[i] = filePath[i].toString().substring(filePath[i].toString().lastIndexOf("/")+1);

                if (i == 0) {
                    cert10.setText(""+filePathDisp[i]);
                }
                else if(i == 1) {
                    cert12.setText(""+filePathDisp[i]);
                }
                else if(i == 2) {
                    certGrad.setText(""+filePathDisp[i]);
                }

        }
    }

    // UploadImage method
    private void uploadImage()
    {
        if (filePath[0] != null && filePath[1] != null && filePath[2] != null) {

            final ProgressDialog progressDialogCert10
                    = new ProgressDialog(this);
            progressDialogCert10.setTitle("Uploading Class 10 Certificate");
            progressDialogCert10.show();

            StorageReference ref = storageReference.child("images/");

            ref.child(currentUid+"-Cert10").putFile(filePath[0]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Category").child("Certificates").child("Class10").child("Cert10").setValue(currentUid+"-Cert10");

                    progressDialogCert10.dismiss();
                    Toast.makeText(TutorImageUploadActivity.this,"Image Uploaded!!",Toast.LENGTH_LONG).show();
                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialogCert10.dismiss();
//                            Toast.makeText(RegisterImageUploadActivity.this,"Failed!",Toast.LENGTH_LONG).show();
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
                                    progressDialogCert10.setMessage("Uploaded "+ (int)progress + "%");
                                }
                            });

            final ProgressDialog progressDialogCert12
                    = new ProgressDialog(this);
            progressDialogCert12.setTitle("Uploading Class 12 Certificate");
            progressDialogCert12.show();

            ref.child(currentUid+"-Cert12").putFile(filePath[1]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Category").child("Certificates").child("Class12").child("Cert12").setValue(currentUid+"-Cert12");
                    progressDialogCert12.dismiss();
                    Toast.makeText(TutorImageUploadActivity.this,"Image Uploaded!!",Toast.LENGTH_LONG).show();
                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialogCert12.dismiss();
//                            Toast.makeText(RegisterImageUploadActivity.this,"Failed!",Toast.LENGTH_LONG).show();
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
                                    progressDialogCert12.setMessage("Uploaded "+ (int)progress + "%");
                                }
                            });

            final ProgressDialog progressDialogCertGrad
                    = new ProgressDialog(this);
            progressDialogCertGrad.setTitle("Uploading Class Graduation Certificate");
            progressDialogCertGrad.show();

            ref.child(currentUid+"-CertGrad").putFile(filePath[2]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Category").child("Certificates").child("Grad").child("CertGrad").setValue(currentUid+"-CertGrad");
                    progressDialogCertGrad.dismiss();
                    Toast.makeText(TutorImageUploadActivity.this,"Image Uploaded!!",Toast.LENGTH_LONG).show();

                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Category").child("Experience").setValue(exp.getText().toString());


                    intent = new Intent(TutorImageUploadActivity.this, RegisterImageUploadActivity.class);
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
                            progressDialogCertGrad.dismiss();
                            Toast.makeText(TutorImageUploadActivity.this,"Failed!",Toast.LENGTH_LONG).show();
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
                                    progressDialogCertGrad.setMessage("Uploaded "+ (int)progress + "%");
                                }
                            });
        }
        else if( filePath[0] != null && filePath[1] != null && graddeg.equals("0")){
            final ProgressDialog progressDialogCert10
                    = new ProgressDialog(this);
            progressDialogCert10.setTitle("Uploading Class 10 Certificate");
            progressDialogCert10.show();

            StorageReference ref = storageReference.child("images/");

            ref.child(currentUid+"-Cert10").putFile(filePath[0]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Category").child("Certificates").child("Class10").child("Cert10").setValue(currentUid+"-Cert10");

                    progressDialogCert10.dismiss();
                    Toast.makeText(TutorImageUploadActivity.this,"Image Uploaded!!",Toast.LENGTH_LONG).show();
                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialogCert10.dismiss();
//                            Toast.makeText(RegisterImageUploadActivity.this,"Failed!",Toast.LENGTH_LONG).show();
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
                                    progressDialogCert10.setMessage("Uploaded "+ (int)progress + "%");
                                }
                            });

            final ProgressDialog progressDialogCert12
                    = new ProgressDialog(this);
            progressDialogCert12.setTitle("Uploading Class 12 Certificate");
            progressDialogCert12.show();

            ref.child(currentUid+"-Cert12").putFile(filePath[1]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Category").child("Certificates").child("Class12").child("Cert12").setValue(currentUid+"-Cert12");
                    progressDialogCert12.dismiss();
                    Toast.makeText(TutorImageUploadActivity.this,"Image Uploaded!!",Toast.LENGTH_LONG).show();

                    intent = new Intent(TutorImageUploadActivity.this, RegisterImageUploadActivity.class);
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
                            progressDialogCert12.dismiss();
//                            Toast.makeText(RegisterImageUploadActivity.this,"Failed!",Toast.LENGTH_LONG).show();
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
                                    progressDialogCert12.setMessage("Uploaded "+ (int)progress + "%");
                                }
                            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"PLEASE UPLOAD IMAGES",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(TutorImageUploadActivity.this, HomeActivity.class);
        finish();
        startActivity(intent);
        Toast.makeText(this, "Registration not completed...", Toast.LENGTH_SHORT).show();
    }
}
