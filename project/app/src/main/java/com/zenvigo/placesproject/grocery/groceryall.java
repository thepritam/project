package com.project.placesproject.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.placesproject.MainPage;
import com.project.placesproject.R;
import com.project.placesproject.SaveDetails;
import com.project.placesproject.account_activity;
import com.project.placesproject.customercare;
import com.project.placesproject.tutor.servicetutor;
import com.project.placesproject.yourorderactivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class groceryall extends FragmentActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static String currentPhotoPath,filename;
    static  String filePath=null;
    RadioButton checkBox1,checkBox2;
    File f;
    Button selectbtn;
    ProgressDialog progressDialog;
    servicetutor mYourService= new servicetutor();
    Intent mServiceIntent;
    private StorageReference storageReference,filepath;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    final private int REQUEST_PERMISSION_CODE=1000;
    ArrayList<Pair<String,String>> itemslist=new ArrayList<>();
     private Button buttoncon;
     Bitmap bitmap1;
    Uri imageUri                      = null;
    String uid,oid;
    static TextView imageDetails      = null;
    public  static ImageView showImg  = null;
    groceryall CameraActivity = null;
    Animation animZoomIn;
    static int imageid;
    private Animator currentAnimator;
    HashMap<String, Object> userMap = new HashMap<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase fb=FirebaseDatabase.getInstance();
    private int shortAnimationDuration;
//    FirebaseStorage sref = FirebaseStorage.getInstance();
//    StorageReference storageRef = sref.getReference();
//    StorageReference mountainsRef = storageRef.child("grocery.jpg");
//
//    // Create a reference to 'images/mountains.jpg'
//    StorageReference mountainImagesRef = storageRef.child("images/grocery.jpg");
   // sref = FirebaseStorage.getInstance().getReference();
    ImageButton btnStart;
    String shopid,shopname;
    LinearLayout parentLinearLayout,parentlinearoutcamera,buttonlayout,edittextgrocery;
    FrameLayout frameLayout;
    ImageButton photo;
    TextView textViewenlarge;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(groceryall.this, MainPage.class);
        intent.putExtra("Name",SaveDetails.name);
        intent.putExtra("id",SaveDetails.id);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceryall);
       textViewenlarge=findViewById(R.id.clicktoenlarge);
        shopid = getIntent().getStringExtra("shopid");
        shopname=getIntent().getStringExtra("shopname");
        checkBox1=findViewById(R.id.checkBox1);
        checkBox2=findViewById(R.id.checkBox2);
        btnStart =  findViewById(R.id.btnStartgrocery);
        storageReference = FirebaseStorage.getInstance().getReference();
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layoutgrocery);
        parentlinearoutcamera=findViewById(R.id.parent_linear_layoutcamera);
        edittextgrocery=findViewById(R.id.layout_to_hide);
        buttonlayout=findViewById(R.id.button_layout_grocery);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView=findViewById(R.id.navigation);
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
      // selectbtn=findViewById(R.id.select_button_grocery);
       frameLayout=findViewById(R.id.container);
        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("project");
        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Please Enter the Item in the Cart",Toast.LENGTH_SHORT).show();
                edittextgrocery.setVisibility(View.VISIBLE);
                buttonlayout.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.INVISIBLE);
            }
        });
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Please click a Photo of Your list to order",Toast.LENGTH_SHORT).show();
                edittextgrocery.setVisibility(View.INVISIBLE);
                buttonlayout.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
            }
        });
//       selectbtn.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v1) {
//               if (checkBox1.isChecked()&&!checkBox2.isChecked()){
//                   edittextgrocery.setVisibility(View.VISIBLE);
//                   buttonlayout.setVisibility(View.VISIBLE);
//                   frameLayout.setVisibility(View.INVISIBLE);
//
//               }
//
//               else if (checkBox2.isChecked()&&!checkBox1.isChecked())
//               {      edittextgrocery.setVisibility(View.INVISIBLE);
//                   buttonlayout.setVisibility(View.INVISIBLE);
//                   frameLayout.setVisibility(View.VISIBLE);
//               }
//               else if(checkBox1.isChecked()&&checkBox2.isChecked()){
//                 Toast.makeText(getApplicationContext(),"Please Select any one ",Toast.LENGTH_SHORT).show();
//               }
//               else
//               {    edittextgrocery.setVisibility(View.INVISIBLE);
//                  frameLayout.setVisibility(View.INVISIBLE);
//                   buttonlayout.setVisibility(View.INVISIBLE);
//                   Toast.makeText(getApplicationContext(),"select any one",Toast.LENGTH_SHORT).show();
//               }
//           }
//       });
// Create a reference to "mountains.jpg"
       
// While the file names are the same, the references point to different files
       // mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        //mountainsRef.getPath().equals(mountainImagesRef.getPath());

        //this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        //Button photoButton = (Button) this.findViewById(R.id.button1);
        CameraActivity = this;


       // imageDetails = (TextView) findViewById(R.id.imageDetails);
        buttoncon=findViewById(R.id.button_grocery_confirm);
        showImg = (ImageView) findViewById(R.id.showImg);

        photo = (ImageButton) findViewById(R.id.photoupload);

//        if(!checkPermissionFromDevice()){
//            requestPermission();
//        }
//        else {
//            requestPermission();
//        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            photo.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        photo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /*************************** Camera Intent Start ************************/

                // Define the file-name to save photo taken by Camera activity

                String fileName = "Camera_Example.jpg";

                // Create parameters for Intent with filename

                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.TITLE, fileName);

                values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");

                Log.d("called","Photo upload");

                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);



                Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );

                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                startActivityForResult( intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);




            }

        });

    }



    public File savebitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        // String temp = null;
        File file = new File(extStorageDirectory, "temp.png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "temp.png");

        }

        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
      final String filePath1 = file.getPath();
        Log.d("show","savebitmap"+filePath1);
        Log.d("show", "savebitmap: "+filePath);
        Log.d("show", "savebitmap: "+filename);

        buttoncon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.background);
                progressDialog = new ProgressDialog(groceryall.this);
                progressDialog.setTitle("Please Wait...");
                progressDialog.setMessage("Taking Your Order...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                uploadimage(bitmap1,filePath1);
            }
        });
//            button.setVisibility(View.VISIBLE);
        //          button.setEnabled(true);

        //   Bitmap bitmap1 = BitmapFactory.decodeFile(filePath);
        // uploadimage(bitmap1);
        return file;
    }
    private void zoomImageFromThumb(final View thumbView, Bitmap mybitmap) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        Matrix matrix = new Matrix();

        matrix.postRotate(90);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(mybitmap, 1200, 1200, true);

        mybitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView =  findViewById(
                R.id.showImg);
        Log.d("show", "zoomImageFromThumb: "+mybitmap);
        expandedImageView.setImageBitmap(mybitmap);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
     //   expandedImageView.setPivotX(1f);
       // expandedImageView.setPivotY(1f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }
    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field1, null);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);




        //  Log.d("text",""+ edittext_var.getText().toString());
        // Add the new row before the add field button.

    }
    public void onSelect(View v){
        EditText editText = (EditText) ((View) v.getParent()).findViewById(R.id.edittextgrocey) ;
        EditText editText1=((View) v.getParent()).findViewById(R.id.edittextgroceyno) ;
        //spinnerdynamic = (Spinner) ((View) v.getParent()).findViewById(R.id.spinnerdynamic) ;
        //String text = spinnerdynamic.getSelectedItem().toString();
        String text = editText.getText().toString()+" "+editText1.getText().toString();

          /*  spinnerdynamic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    text=spinnerdynamic.getSelectedItem().toString();
                    Toast.makeText(getApplicationContext(),"This is : "+text,Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
          Pair<String,String> temp= Pair.create(editText.getText().toString(),editText1.getText().toString());
        itemslist.add(temp);
        Button selectbtn=(Button)((View) v.getParent()).findViewById(R.id.select_button);
        selectbtn.setEnabled(false);
        selectbtn.setBackgroundResource(R.drawable.done);
//            if(selectbtn!=null)
//                selectbtn1.setEnabled(false);
        if(itemslist.size()==0){
            buttoncon.setEnabled(false);
            buttoncon.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Please Enter the subject/s",Toast.LENGTH_SHORT).show();
        }
        else {
            buttoncon.setEnabled(true);
            buttoncon.setVisibility(View.VISIBLE);
        }

        Log.d("text",text);

    }
    public void onDelete(View v) {
       EditText itmname = (EditText) ((View) v.getParent()).findViewById(R.id.edittextgrocey) ;
       EditText quantity=((View) v.getParent()).findViewById(R.id.edittextgroceyno) ;
       // String toremove = editText.getText().toString()+" "+editText1.getText().toString();
        parentLinearLayout.removeView((View) v.getParent());

        for(int i=0;i<itemslist.size();i++)
        {
            Pair<String,String> temp = itemslist.get(i);
            String  itemname = temp.first;
            String qty = temp.second;
            if(itmname.getText().toString().equals(itemname) && quantity.getText().toString().equals(qty)) {
                itemslist.remove(i);
                break;
            }
        }
        if(itemslist.size()==0){
            buttoncon.setEnabled(false);
            buttoncon.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Please Enter the subject/s",Toast.LENGTH_SHORT).show();
        }
        else {
            buttoncon.setEnabled(true);
            buttoncon.setVisibility(View.VISIBLE);
        }

    }
    public void uploadimage(Bitmap bitmap,String filePathcomp){
        Log.d("show", "uploadimage: ");
        Log.d("show", "uploadimage: "+filePathcomp);
        // filename=filePath.substring(filePath.lastIndexOf("/")+1);
      //  String fileName = Environment.getExternalStorageDirectory().getAbsolutePath();

//        fileName+="/newimage.jpg";

        filepath = storageReference.child("image").child(SaveDetails.id+"new_image.jpg");
        Uri uri = Uri.fromFile(new File(filePathcomp));
        Log.d("show", "uploadimage: "+filePathcomp);
        Log.d("show", "uploadimage: " + uri);
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                Toast.makeText(groceryall.this, "Uploading Finished", Toast.LENGTH_SHORT).show();
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("here", "onSuccess: " + uri);

                         uploadorderdescription(uri);

                    }
                });
            }
        });
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//       // final StorageReference ref = storageRef.child("images/mountains.jpg");
//        //uploadTask = ref.putFile(file);
//
//
//
//        UploadTask uploadTask = mountainsRef.putBytes(data);
//        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    Log.d("show", "then: "+"unsucess");
//                    throw task.getException();
//
//                }
//
//                // Continue with the task to get the download URL
//                return mountainImagesRef.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                    Log.d("show", "onComplete: "+downloadUri);
//                } else {
//                    // Handle failures
//                    // ...
//                }
//            }
//        });
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot snapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = snapshot.getMetadata().getDownloadUrl();
//            }
//        });

    }

    public void uploadorderdescription(Uri uri) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        uid = database.getReference("OrderDescription").push().getKey();
        uid = uid.substring(1);
        for(int i=0;i<itemslist.size();i++)
        {
            Pair<String,String> temp = itemslist.get(i);
            String  itemname = temp.first;
            String qty = temp.second;
            Log.d("show", "uploadorderdescription: "+itemname+" "+qty);
           userMap.put(itemname,qty);
        }
        if(uri!=null){
            userMap.put("imagelink",uri.toString());
            Log.d("show", "uploadorderdescription: "+uri);
        }
        else {
            userMap.put("imagelink","");
        }

        Log.d("usermap",""+userMap);
        Log.d("gen_id",uid);

      FirebaseDatabase.getInstance().getReference().child("OrderDescription").child(uid).setValue(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Description successfully",Toast.LENGTH_LONG).show();
//                           Intent intent = new Intent(DetailsActivity.this, MainPage.class);
//                           intent.putExtra("Name",name);
//                           intent.putExtra("id",currentUid);
//                           intent.putExtra("Category",category);
//                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                           startActivity(intent);
                         //   oid=fb.getReference().child("OrderDescription").push().getKey();
                            Log.d("show", uid);
                           uploaddata(uid);

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(groceryall.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public  void uploaddata(String oid) {



        db = FirebaseFirestore.getInstance();
        Map<String,Object> data = new HashMap<>();
        data.put("Assigned", 1);
        data.put("AssignedId", getIntent().getStringExtra("shopid"));
        data.put("Completed",0);
        data.put("Category","grocery");
        data.put("CId",getIntent().getStringExtra("uid"));
        data.put("OrderDescriptionID",oid);

        data.put("Latitude",Double.parseDouble(getIntent().getStringExtra("latitude")));
        data.put("Longitude",Double.parseDouble(getIntent().getStringExtra("longitude")));
        data.put("PaidCommission",0);
        data.put("PaidFull",0);
        data.put("isCancelled",0);
        data.put("InvoiceID","");
        data.put("QuotationID","");


        db.collection("Orders")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("upload", "DocumentSnapshot written with ID: " + documentReference.getId());
                        //Intent intent = new Intent(VerifyActivity.this,ServicemaActivity.class);
                        //startActivity(intent);

                        updateinuser(documentReference.getId());



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Sorry ! server issues",Toast.LENGTH_LONG).show();
                        Log.w("upload", "Error adding document", e);
                    }
                });
    }

    private void updateinuser(final String id) {


        HashMap<String, Object> userMap = new HashMap<>();

        userMap.put("OrderId",id);
        userMap.put("timestamp", ServerValue.TIMESTAMP);
        userMap.put("ShopName",shopname);
        userMap.put("RiderOrder","");
        userMap.put("Price","");
        userMap.put("Completed",0);
        userMap.put("Cancelled",0);
        final String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String groc_user = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUid) .child("Orders").child("grocery").push().getKey();

        DatabaseReference newRef =  FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(currentUid) .child("Orders").child("grocery").child(groc_user);

        newRef.setValue(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {

                            mServiceIntent = new Intent(groceryall.this, servicegrocery.class);
                            mServiceIntent.putExtra("orderid",id);
                            mServiceIntent.putExtra("shopname",shopname);
                            mServiceIntent.putExtra("Latitude",getIntent().getStringExtra("latitude"));
                            mServiceIntent.putExtra("Longitude",getIntent().getStringExtra("longitude"));
                            mServiceIntent.putExtra("groceryinuserid",groc_user);
                            // mServiceIntent.putExtra("id",getIntent().getStringExtra("ggkk"));

                            if (!isMyServiceRunning(servicegrocery.class)) {

                                Log.d("show","service is starting");
                                startService(mServiceIntent);
                            }


                            progressDialog.dismiss();
                            Intent intent=new Intent(getApplicationContext(), ShowQuotation.class);
                            intent.putExtra("orderid",id);
                            intent.putExtra("shopname",shopname);
                            intent.putExtra("Latitude",getIntent().getStringExtra("latitude"));
                            intent.putExtra("Longitude",getIntent().getStringExtra("longitude"));
                            intent.putExtra("quotid","");
                            intent.putExtra("shopid",getIntent().getStringExtra("shopid"));
                            intent.putExtra("Category",getIntent().getStringExtra("Categoryservice"));
                            intent.putExtra("uid",getIntent().getStringExtra("uid"));
                            intent.putExtra("shopadress",getIntent().getStringExtra("shopadress"));
                            intent.putExtra("groceryinuserid",groc_user);
                            startActivity(intent);


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(groceryall.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                Log.d("called","onActivity result");

                /*********** Load Captured Image And Data Start ****************/

                String imageId = convertImageUriToFile(imageUri, CameraActivity);
                Log.d("image id",""+imageId);

                //  Create and excecute AsyncTask to load capture image

              LoadImagesFromSDCard loadImagesFromSDCard= (LoadImagesFromSDCard) new LoadImagesFromSDCard(buttoncon);

              loadImagesFromSDCard.execute("" + imageId);
                             buttoncon.setVisibility(View.VISIBLE);
                             buttoncon.setEnabled(true);

                /*********** Load Captured Image And Data End ****************/


            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String convertImageUriToFile ( Uri imageUri, Activity activity )  {

        Cursor cursor = null;
        int imageID = 0;

        try {
            String [] proj={
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Thumbnails._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION
            };

            cursor = activity.managedQuery(

                    imageUri,         //  Get data for specific image URI
                    proj,             //  Which columns to return
                    null,             //  WHERE clause; which rows to return (all rows)
                    null,             //  WHERE clause selection arguments (none)
                    null              //  Order-by clause (ascending by name)

            );

            //  Get Query Data

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);



            int size = cursor.getCount();



            if (size == 0) {


               // imageDetails.setText("No Image");
            }
            else
            {

                int thumbID = 0;
                if (cursor.moveToFirst()) {
                    imageID     = cursor.getInt(columnIndex);

                    thumbID     = cursor.getInt(columnIndexThumb);

                    String Path = cursor.getString(file_ColumnIndex);

                    //String orientation =  cursor.getString(orientation_ColumnIndex);

                    String CapturedImageDetails = " CapturedImageDetails : \n\n"
                            +" ImageID :"+imageID+"\n"
                            +" ThumbID :"+thumbID+"\n"
                            +" Path :"+Path+"\n";
                    Log.d("show", "convertImageUriToFile: "+Path);
                    imageid=imageID;
                    filePath=Path;
                    filename=filePath.substring(filePath.lastIndexOf("/")+1);

                    // Show Captured Image detail on activity
                  //  imageDetails.setText( CapturedImageDetails );

                }
            }
        } finally {
          /*  if (cursor != null) {
                cursor.close();
            }*/
        }

        // Return Captured Image ImageID ( By this ImageID Image will load from sdcard )

        return ""+imageID;
    }


    public class LoadImagesFromSDCard  extends AsyncTask<String, Void, Void> {
        protected Button button;
        LoadImagesFromSDCard(Button button2){
            button=button2;
        }

        private ProgressDialog Dialog = new ProgressDialog(groceryall.this);

        Bitmap mBitmap;

        protected void onPreExecute() {
            Dialog.setMessage(" Loading image from Sdcard..");
            Dialog.show();
        }


        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            Bitmap bitmap = null;
            Bitmap newBitmap = null;
            Uri uri = null;


            try {



                uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + urls[0]);

                /**************  Decode an input stream into a bitmap. *********/
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

                if (bitmap != null) {

                    /********* Creates a new bitmap, scaled from an existing bitmap. ***********/

                    newBitmap = Bitmap.createScaledBitmap(bitmap, 1200, 1200, true);

                    bitmap.recycle();

                    if (newBitmap != null) {

                        mBitmap = newBitmap;

                       f= savebitmap(newBitmap);

                        // filePath = f.getPath();
                       //Log.d("show", "doInBackground: "+filePath);

                      // Bitmap bitmap1 = BitmapFactory.decodeFile(filePath);

                       // mBitmap=bitmap1;
                    }
                }
            } catch (IOException e) {

                cancel(true);
            }

            return null;
        }


        protected void onPostExecute(Void unused) {

            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if(mBitmap != null)
            {
                // Set Image to ImageView

                bitmap1=mBitmap;

               // showImg.setImageBitmap(mBitmap);
                btnStart.setVisibility(View.VISIBLE);
                textViewenlarge.setVisibility(View.VISIBLE);
                GradientDrawable shape =  new GradientDrawable();
                shape.setCornerRadius(50);
                RoundedBitmapDrawable RBD = RoundedBitmapDrawableFactory.create(getResources(),mBitmap);
                float ImageRadius = 40.0f;
                RBD.setCornerRadius(ImageRadius);

                RBD.setAntiAlias(true);
                btnStart.setImageBitmap(RBD.getBitmap());

                btnStart.setBackgroundDrawable(shape);
                btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        zoomImageFromThumb(btnStart, mBitmap);
                    }
                });

                // Retrieve and cache the system's default "short" animation time.
                shortAnimationDuration = getResources().getInteger(
                        android.R.integer.config_shortAnimTime);

                Log.d("show", "onPostExecute: upload");
            }

        }

    }



    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode)
//        {
//            case REQUEST_PERMISSION_CODE:
//                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case MY_CAMERA_PERMISSION_CODE:
//                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                photo.setEnabled(true);
            }
        }
    }

    private boolean checkPermissionFromDevice() {
        int write_External_Storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA);
       // int record_audio_result = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return ((write_External_Storage == PackageManager.PERMISSION_GRANTED) && (camera == PackageManager.PERMISSION_GRANTED));//&& (record_audio_result==PackageManager.PERMISSION_GRANTED))
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }
    @Override
    protected void onDestroy() {
        if(mServiceIntent!=null)
            stopService(mServiceIntent);
        super.onDestroy();
    }

}
