package com.project.placesproject.tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.placesproject.R;
import com.project.placesproject.SaveDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmTutorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
   TextView textViewsubject,textViewstream,textViewboard,textViewclass,textView1,textView2,textView3,textView4;
   EditText editText;
   Button btn;
   String result,stream,class1,board,sp1,id,category;
   servicetutor mYourService= new servicetutor();
    HashMap<String, Object> userMap = new HashMap<>();
    HashMap<String, String> usersubject = new HashMap<>();
    HashMap<String, String> userfees = new HashMap<>();
    String [] user={"Monthly","Daily","Weekly"};
    String uid;
    Intent mServiceIntent;
    Spinner spinner ;
    EditText editfees;
    String fees;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase fb=FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_tutor);
        textView4=findViewById(R.id.textViewshowboard);
        textView3=findViewById(R.id.textView8);
        textViewsubject=findViewById(R.id.confirmdetails);
        textViewboard=findViewById(R.id.username);
        textViewclass=findViewById(R.id.useremail);
        textViewstream=findViewById(R.id.userphone);
        spinner= findViewById(R.id.spinner3);
        btn=findViewById(R.id.buttontutorpay);
        editfees=findViewById(R.id.fees);

        ArrayList<String> subjectslist=getIntent().getStringArrayListExtra("listdetails");
        result="";
        for(String s:subjectslist)
        {
            result+=s+", ";
            usersubject.put(s,"Yes");
        }

        category = getIntent().getStringExtra("Category");
        userMap.put("Subjects",usersubject);
        userMap.put("Category",category);
        textViewsubject.setText(result);
         stream=getIntent().getStringExtra("stream");
         board=getIntent().getStringExtra("board");

         class1=getIntent().getStringExtra("class");
         textViewclass.setText(class1);
         if(stream.length()>0){
             textView4.setVisibility(View.VISIBLE);
             textViewstream.setVisibility(View.VISIBLE);
             textViewstream.setText(stream);
             userMap.put("stream",stream);
         }
         else {
             userMap.put("stream","");
         }

         userMap.put("class",class1);
        if(board.length()>0){
            textView3.setVisibility(View.VISIBLE);
            textViewboard.setVisibility(View.VISIBLE);
            textViewboard.setText(board);
            userMap.put("board",board);
        }
        else{
            userMap.put("board","");
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fees=editfees.getText().toString();
                if(fees!=null){
                    userfees.put("Price",fees);}
                else {
                    userfees.put("Price","");
                }


                ProgressDialog progressDialog = new ProgressDialog(ConfirmTutorActivity.this);
                progressDialog.setTitle("Please wait...");
                progressDialog.setMessage("Processing your request");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                uploaddescription(progressDialog);



            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, user);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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
                           Toast.makeText(ConfirmTutorActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                       }
                   }
               });
      // progressDialog.show();

   }
   private void uploaddata(final ProgressDialog progressDialog) {
        Map<String,Object> data = new HashMap<>();
        data.put("Assigned", 0);
        data.put("AssignedId", "");
        data.put("Completed",0);
        data.put("Category","Tutor");
        data.put("CId", SaveDetails.id);
        data.put("OrderDescriptionID",uid);
        data.put("Latitude",Double.parseDouble(getIntent().getStringExtra("lat")));
        data.put("Longitude",Double.parseDouble(getIntent().getStringExtra("longi")));
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
                        mServiceIntent = new Intent(ConfirmTutorActivity.this,servicetutor.class);
                        mServiceIntent.putExtra("orderid",documentReference.getId());
                       // mServiceIntent.putExtra("id",getIntent().getStringExtra("ggkk"));

                        if (!isMyServiceRunning(servicetutor.class)) {

                            Log.d("show","service is starting");
                            startService(mServiceIntent);
                        }

                        putinuser(documentReference.getId(),getIntent().getStringExtra("Categoryservice"),progressDialog);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("upload", "Error adding document", e);
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Selected Class: "+user[position] ,Toast.LENGTH_SHORT).show();
        sp1= String.valueOf(spinner.getSelectedItem());
        userfees.put("Type",sp1);
        userMap.put("Preferred Price",userfees);
        //Toast.makeText(this, sp1, Toast.LENGTH_SHORT).show();
        Log.d("show", "onItemSelected: "+sp1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    private void putinuser(final String orderid, final String category, final ProgressDialog progressDialog) {

        HashMap<String, Object> userMap = new HashMap<>();

        userMap.put("OrderId",orderid);
        userMap.put("timestamp", ServerValue.TIMESTAMP);
        userMap.put("Category","Education");
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

                            Toast.makeText(ConfirmTutorActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if(mServiceIntent!=null)
        stopService(mServiceIntent);
        super.onDestroy();
    }
}
