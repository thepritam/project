package com.project.placesproject.tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.placesproject.R;
import com.project.placesproject.SaveDetails;
import com.project.placesproject.account_activity;
import com.project.placesproject.customercare;
import com.project.placesproject.grocery.ShowQuotation;
import com.project.placesproject.yourorderactivity;

import java.util.HashMap;
import java.util.Map;

public class sportsactivity extends AppCompatActivity {
  Spinner spinner;
  String categorytutor;
  Button height,weight;
  EditText getheight,getweight,editTextdescription,editTextfees;
  String heightvalue,weightvalue,choice,priceaff;
  LinearLayout layoutimage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase fb=FirebaseDatabase.getInstance();
    servicetutor mYourService= new servicetutor();
    HashMap<String, Object> userMap = new HashMap<>();
    HashMap<String, String> usersubject = new HashMap<>();
    HashMap<String, String> userfees = new HashMap<>();
    String uid,id;
    Intent mServiceIntent;
    Button button;
  TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportsactivity);
        spinner=findViewById(R.id.spinner1sport);
        height=findViewById(R.id.add_confirm_height);
        weight=findViewById(R.id.add_confirm_weight);
        getheight=findViewById(R.id.editextheight);
        getweight=findViewById(R.id.editextweight);
        layoutimage=findViewById(R.id.layoutimage);
        editTextdescription=findViewById(R.id.edittextdescripiton);
        editTextfees=findViewById(R.id.edittextfess);
        button=findViewById(R.id.buttonsubmit);
        textView=findViewById(R.id.txtVwsport);
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
        categorytutor=getIntent().getStringExtra("Categoryservice");
        if(categorytutor.equals("Chess")){
            textView.setText("Select Rating");
            layoutimage.setBackground(getResources().getDrawable(R.drawable.chess));
        }
        else if(categorytutor.equals("Tabletennis")){
            textView.setText("Select Level");
            layoutimage.setBackground(getResources().getDrawable(R.drawable.tt));
        }
        else if(categorytutor.equals("Lawntennis")){
            textView.setText("Select Level");
            layoutimage.setBackground(getResources().getDrawable(R.drawable.longt));
        }
        else if(categorytutor.equals("Badminton")){
            textView.setText("Select Level");
            layoutimage.setBackground(getResources().getDrawable(R.drawable.badminton));
        }
        else if(categorytutor.equals("Fitnesstrainer")){
            textView.setText("Select Level");
            layoutimage.setBackground(getResources().getDrawable(R.drawable.personaltrainer));
        }
        else if(categorytutor.equals("Art")){
            textView.setText("Select Level");
            layoutimage.setBackground(getResources().getDrawable(R.drawable.art));
        }
        else if(categorytutor.equals("voicetutor")){
            textView.setText("Select Level");
            layoutimage.setBackground(getResources().getDrawable(R.drawable.singteacher));
        }
        else if(categorytutor.equals("Instrumentation")){
            textView.setText("Select Instrument");
            layoutimage.setBackground(getResources().getDrawable(R.drawable.instru));
        }
        Log.d("category", "onCreate: "+getIntent().getStringExtra("Categoryservice"));

        final String [] users={"choose Level","Beginner","Intermediate","AdVanced"};
        final String[] list1=new String[]{ "Choose  Rating","Ratings 1000-1300",
                    "Ratings 1300-1600",
                    "Ratings 1600-1900",
                    "Ratings 2000-2200",
                    "Ratings 2200-2500",
                   };
        final String[] list3= new String[]{"Choose Instruments","Musical note",
               " Trombone",
                "Saxophone","Xylophone","Harmonica","Violin" ,
                "Piano" ,"Drums/ Drum set",
                "Guitar"};
         if(categorytutor.equals("Chess")){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list1){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);}
       else  if(categorytutor.equals("Instrumentation")){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list3){
                @Override
                public boolean isEnabled(int position){
                    if(position == 0)
                    {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if(position == 0){
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    }
                    else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);}
         else {
             ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users){
                 @Override
                 public boolean isEnabled(int position){
                     if(position == 0)
                     {
                         // Disable the first item from Spinner
                         // First item will be use for hint
                         return false;
                     }
                     else
                     {
                         return true;
                     }
                 }
                 @Override
                 public View getDropDownView(int position, View convertView,
                                             ViewGroup parent) {
                     View view = super.getDropDownView(position, convertView, parent);
                     TextView tv = (TextView) view;
                     if(position == 0){
                         // Set the hint text color gray
                         tv.setTextColor(Color.GRAY);
                     }
                     else {
                         tv.setTextColor(Color.BLACK);
                     }
                     return view;
                 }
             };
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
             spinner.setAdapter(adapter);

         }
         spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 Log.d("show", "onItemSelected: "+list1[position]);
               choice=String.valueOf(spinner.getSelectedItem());
                 if (choice.equals("choose Level")){
                     button.setEnabled(false);
                    // Snackbar.make(relativeLayout,"Please Select any choice in the level/rating",2);
                     Toast.makeText(getApplicationContext(),"Please Select any choice in the level/rating",Toast.LENGTH_SHORT).show();

                 }
//                 else {
//                     button.setEnabled(true);
//                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });
         if (categorytutor.equals("Fitnesstrainer")){
             LinearLayout layout=findViewById(R.id.linear_layout);

             layout.setVisibility(View.VISIBLE);
         }
         else {
             LinearLayout layout=findViewById(R.id.linear_layout);
             layout.setVisibility(View.GONE);
         }
        final RelativeLayout relativeLayout=findViewById(R.id.snackbar);
        height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heightvalue=getheight.getText().toString();
               // Toast.makeText(getApplicationContext(),"Done!",Toast.LENGTH_SHORT).show();
                Snackbar.make(relativeLayout,"Done",2);
                Log.d("show", "onClick: "+heightvalue);
                if( getheight.getText().toString().length() == 0 ){
                    getheight.setError( "Please Enter Height!" );
                    button.setEnabled(false);
                }
                else {
                    button.setEnabled(true);
                }
            }
        });
        weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(relativeLayout,"Done",2);
                weightvalue=getweight.getText().toString();
                if( getweight.getText().toString().length() == 0 ){
                    getweight.setError( "Please Enter Height!" );
                    button.setEnabled(false);
                }
                else {
                    button.setEnabled(true);
                }
            }
        });
        final Spinner spinner2=findViewById(R.id.spinner1sportfees);
        String [] list2={"Select..","Monthly","Weekly","Daily"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priceaff=String.valueOf(spinner2.getSelectedItem());
                if (priceaff.equals("Select..")){
                    button.setEnabled(false);
                    Toast.makeText(getApplicationContext(),"Please Select Any One From DropDown",Toast.LENGTH_SHORT).show();

                }
                else {
                    button.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( priceaff.length()==0 ||choice.length()==0){
                    Toast.makeText(getApplicationContext(),"Please Select from dropdown",Toast.LENGTH_SHORT).show();;

                    if(editTextfees.getText().toString().length() == 0)
                        editTextfees.setError( "Please Enter Your Affordable Price!" );
                    if(priceaff.length()==0)
                    Toast.makeText(getApplicationContext(),"Please Select Any One From DropDown",Toast.LENGTH_SHORT).show();
                    if( getweight.getText().toString().length() == 0 )
                        getweight.setError( "Please Enter Weight!" );
                    if( getheight.getText().toString().length() == 0 )
                        getheight.setError( "Please Enter Height!" );
                    if (choice.length()==0){

                    }

                button.setEnabled(false);
            }
                else {

                    userfees.put("Type",priceaff);
                    userfees.put("Price",editTextfees.getText().toString());
                    userMap.put("Level",choice);
                    userMap.put("Description",editTextdescription.getText().toString());
                    userMap.put("Preferred Price",userfees);
                    if (categorytutor.equals("Fitnesstrainer")){

                        userMap.put("Weight",weightvalue);
                        userMap.put("Height",heightvalue);
                    }
                button.setEnabled(true);

                    ProgressDialog progressDialog = new ProgressDialog(sportsactivity.this);
                    progressDialog.setTitle("Please wait...");
                    progressDialog.setMessage("Processing your request");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    uploaddescription(progressDialog);
            }
                Log.d("show", "onClick: "+userMap);

            }
        });


    }
    private void uploaddata(final ProgressDialog progressDialog) {
        Map<String,Object> data = new HashMap<>();
        data.put("Assigned", 0);
        data.put("AssignedId", "");
        data.put("Completed",0);
        data.put("Category",getIntent().getStringExtra("Categoryservice"));
        data.put("CId", SaveDetails.id);
        data.put("OrderDescriptionID",uid);
        data.put("Latitude",Double.parseDouble(getIntent().getStringExtra("latitude")));
        data.put("Longitude",Double.parseDouble(getIntent().getStringExtra("longitude")));
        data.put("PaidCommission",0);
        data.put("PaidFull",0);
        data.put("isCancelled",0);
        data.put("QuotationID","");
        data.put("InvoiceID","");

        db.collection("Orders")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("upload", "DocumentSnapshot written with ID: " + documentReference.getId());
                        //Intent intent = new Intent(VerifyActivity.this,ServicemaActivity.class);
                        //startActivity(intent);
                        mServiceIntent = new Intent(sportsactivity.this,servicetutor.class);
                        mServiceIntent.putExtra("orderid",documentReference.getId());
                        // mServiceIntent.putExtra("id",getIntent().getStringExtra("ggkk"));

                        if (!isMyServiceRunning(servicetutor.class)) {

                            Log.d("show","service is starting");
                            startService(mServiceIntent);
                        }

                        putinuser(documentReference.getId(),getIntent().getStringExtra("Categoryservice"),progressDialog);

                        Intent intent=new Intent(getApplicationContext(),DisplayActivity.class);
                        intent.putExtra("orderid",documentReference.getId());
                        intent.putExtra("quotid","");
                        startActivity(intent);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("upload", "Error adding document", e);
                    }
                });
    }

    private void putinuser(final String orderid, final String category, final ProgressDialog progressDialog) {

        HashMap<String, Object> userMap = new HashMap<>();

        userMap.put("OrderId",orderid);
        userMap.put("timestamp", ServerValue.TIMESTAMP);
        userMap.put("Category",category);
        userMap.put("Completed",0);
        userMap.put("Cancelled",0);
        final String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final String tutor_user = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUid) .child("Orders").child("Tutor").push().getKey();

        DatabaseReference newRef =  FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUid) .child("Orders").child("Tutor").child(tutor_user);

        newRef.setValue(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            progressDialog.dismiss();

                            Intent intent=new Intent(getApplicationContext(),DisplayActivity.class);
                            intent.putExtra("orderid",orderid);
                            intent.putExtra("quotid","");
                            startActivity(intent);
                        } else {

                            Toast.makeText(sportsactivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private   void uploaddescription(final ProgressDialog progressDialog)
    {
        //uid = UUID.randomUUID().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        uid = database.getReference("OrderDescription").push().getKey();
        uid = uid.substring(1);

        FirebaseDatabase.getInstance().getReference()
                .child("OrderDescription").child(uid).setValue(userMap)
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
                            id=fb.getReference().child("OrderDescription").push().getKey();
                            Log.d("show", id);
                            uploaddata(progressDialog);

                        } else {
                            Toast.makeText(sportsactivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        // progressDialog.show();

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
