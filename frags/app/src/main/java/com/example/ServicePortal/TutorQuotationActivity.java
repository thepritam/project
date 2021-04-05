package com.example.ServicePortal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class TutorQuotationActivity extends AppCompatActivity {

    private String mode;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri[] filePath = new Uri[3];
    private String[] filePathDisp = new String[3];
    private final int PICK_VIDEO_REQUEST = 22;
    String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_quotation);

        final TextView NameDet, EmailDet, CategoryDet,DescDet, showAddress;
        final EditText editprice,edittime;
        final Button gotoButton, showButton, uploadvid, showdet;
        final Button Start_Continue = findViewById(R.id.Start_Continue);

        NameDet = (TextView) findViewById(R.id.cname);
        showAddress = (TextView) findViewById(R.id.address);
        EmailDet = (TextView) findViewById(R.id.email);
        editprice = (EditText) findViewById(R.id.editprice);
        edittime = (EditText) findViewById(R.id.edittime);
        final Spinner editmode = (Spinner) findViewById(R.id.editmode);
        gotoButton = findViewById(R.id.gotobutton);
        showButton = findViewById(R.id.showBtn);
        showdet = findViewById(R.id.vieworder);
        uploadvid = findViewById(R.id.uploadvid);

        Intent intent = getIntent();
        final double lat = intent.getDoubleExtra("Latitude",0.0);
        final double longi = intent.getDoubleExtra("Longitude",0.0);
        String Cid = intent.getStringExtra("CId");
        final String Uid = intent.getStringExtra("Uid");
        currentUid = CheckAvailability.id;
        final String QuotationID = intent.getStringExtra("QuotationID");
        final String orderdescriptionId = intent.getStringExtra("OrderDescriptionID");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        final String[] arrClass = new String[]{
                "I go to your Home",
                "Come to my home",
                "Batch"
        };

        final ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrClass);

        adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        editmode.setAdapter(adapterClass);

        editmode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                mode = adapterClass.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                TextView errorText = (TextView)editmode.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("Select Subject");

            }
        });

        uploadvid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();

            }
        });

        showdet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "";
                if(CheckAvailability.Category.equals("Tutor"))
                    category="Education";

                Intent intent = new Intent(TutorQuotationActivity.this, ViewTutorOrderActivity.class);
                        intent.putExtra("Category", category);
                        intent.putExtra("OrderDescriptionId", orderdescriptionId);
                        intent.putExtra("OrderId", Uid);
                        startActivity(intent);

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(Cid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                        final User user = dataSnapshot.getValue(User.class);

                final HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

                NameDet.setText(value.get("Name").toString());
                showAddress.setText(value.get("Address").toString());
                EmailDet.setText(value.get("Email").toString());

                gotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final HashMap<String, String> entryval = new HashMap<>();
                        entryval.put("Mode",mode.toString());
                        entryval.put("Price",editprice.getText().toString());
                        entryval.put("NoOfClass",edittime.getText().toString());
                        entryval.put("UID",CheckAvailability.id);

                        DocumentReference assignstate = FirebaseFirestore.getInstance().collection("Orders").document(Uid);

                        if(QuotationID.equals(""))
                        {
                            final String keyUp = FirebaseDatabase.getInstance().getReference().child("Quotations").push().getKey();

                            final String keyBelow = FirebaseDatabase.getInstance().getReference().child("Quotations").child(keyUp).push().getKey();


                            assignstate.update("QuotationID", keyUp);

                            final ProgressDialog progressDialogCert10
                                    = new ProgressDialog(TutorQuotationActivity.this);
                            progressDialogCert10.setTitle("Uploading Demo Video");
                            progressDialogCert10.show();

                            final StorageReference ref = storageReference.child("videos/").child(keyUp+" "+keyBelow+"-DemoVid");

                            ref.putFile(filePath[0]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("here", "onSuccess: " + uri);

                                            FirebaseDatabase.getInstance().getReference().child("Quotations").child(keyUp).child(keyBelow).setValue(entryval);
                                            FirebaseDatabase.getInstance().getReference().child("Quotations").child(keyUp).child(keyBelow).child("DemoVid").setValue(uri.toString());

                                            progressDialogCert10.dismiss();
                                            Toast.makeText(TutorQuotationActivity.this, "Video Uploaded!!", Toast.LENGTH_LONG).show();

                                        }
                                    });


                                }
                            })

                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialogCert10.dismiss();
//                            Toast.makeText(RegisterImageUploadActivity.this,"Failed!",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnProgressListener(
                                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                                @Override
                                                public void onProgress(
                                                        UploadTask.TaskSnapshot taskSnapshot) {
                                                    double progress
                                                            = (100.0
                                                            * taskSnapshot.getBytesTransferred()
                                                            / taskSnapshot.getTotalByteCount());
                                                    progressDialogCert10.setMessage("Uploaded " + (int) progress + "%");
                                                }
                                            });
                        }
                        else
                        {
                            final String key = FirebaseDatabase.getInstance().getReference().child("Quotations").child(QuotationID).push().getKey();

                            final ProgressDialog progressDialogCert10
                                    = new ProgressDialog(TutorQuotationActivity.this);
                            progressDialogCert10.setTitle("Uploading Class 10 Certificate");
                            progressDialogCert10.show();

                            final StorageReference ref = storageReference.child("videos/").child(QuotationID+" "+key+"-DemoVid");


                            ref.putFile(filePath[0]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("here", "onSuccess: " + uri);

                                            FirebaseDatabase.getInstance().getReference().child("Quotations").child(QuotationID).child(key).setValue(entryval);
                                            FirebaseDatabase.getInstance().getReference().child("Quotations").child(QuotationID).child(key).child("DemoVid").setValue(uri.toString());

                                            progressDialogCert10.dismiss();
                                            Toast.makeText(TutorQuotationActivity.this, "Video Uploaded!!", Toast.LENGTH_LONG).show();

                                        }
                                    });


                                }
                            })

                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialogCert10.dismiss();
//                            Toast.makeText(RegisterImageUploadActivity.this,"Failed!",Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnProgressListener(
                                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                                @Override
                                                public void onProgress(
                                                        UploadTask.TaskSnapshot taskSnapshot) {
                                                    double progress
                                                            = (100.0
                                                            * taskSnapshot.getBytesTransferred()
                                                            / taskSnapshot.getTotalByteCount());
                                                    progressDialogCert10.setMessage("Uploaded " + (int) progress + "%");
                                                }
                                            });
                        }

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String date = dateFormat.format(calendar.getTime()).toString();

                        HashMap<String, Object> quotDet = new HashMap<>();
                        quotDet.put(Uid, date);

                        FirebaseDatabase.getInstance().getReference().child("TutorDetails").child(CheckAvailability.id).setValue(quotDet);

//                        Intent intent = new Intent(TutorQuotationActivity.this, MainActivity.class);
//                        intent.putExtra("availcheck", "0");
//                        intent.putExtra("id", CheckAvailability.id);
//                        intent.putExtra("Category", CheckAvailability.Category);
//                        intent.putExtra("lat", CheckAvailability.lat);
//                        intent.putExtra("longi", CheckAvailability.longi);
//                        startActivity(intent);
                    }
                });

                showButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TutorQuotationActivity.this, ShowonMap.class);
                        intent.putExtra("Latitude", lat);
                        intent.putExtra("Longitude", longi);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Video from here..."),
                PICK_VIDEO_REQUEST);
    }

    private void uploadImage() {
        if (filePath[0] != null && filePath[1] != null && filePath[2] != null) {

            final ProgressDialog progressDialogCert10
                    = new ProgressDialog(this);
            progressDialogCert10.setTitle("Uploading Demo Video");
            progressDialogCert10.show();

            StorageReference ref = storageReference.child("videos/");

            ref.child(currentUid + "-Cert10").putFile(filePath[0]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Category").child("Certificates").child("Class10").child("Cert10").setValue(currentUid + "-Cert10");

                    progressDialogCert10.dismiss();
                    Toast.makeText(TutorQuotationActivity.this, "Image Uploaded!!", Toast.LENGTH_LONG).show();
                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialogCert10.dismiss();
//                            Toast.makeText(RegisterImageUploadActivity.this,"Failed!",Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialogCert10.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });

            final ProgressDialog progressDialogCert12
                    = new ProgressDialog(this);
            progressDialogCert12.setTitle("Uploading Class 12 Certificate");
            progressDialogCert12.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == PICK_VIDEO_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath[0] = data.getData();
        }
    }
}

