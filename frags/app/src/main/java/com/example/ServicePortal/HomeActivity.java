package com.example.ServicePortal;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private static final int PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "PLEASE ENABLE LOCATION SERVICES TO CONTINUE", Toast.LENGTH_LONG).show();
            finish();
        }

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
              ActivityCompat.requestPermissions(this,
              new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
              PERMISSIONS_REQUEST);
        }
        //FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            setContentView(R.layout.background);
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please Wait...");
            progressDialog.setMessage("Logging You In...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            final String uid = currentUser.getUid();



            FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user information
//                            User user = dataSnapshot.getValue(User.class);
                            final HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                            String signinName = value.get("Name").toString();
                            final long timestamp = Long.parseLong(value.get("StartBillingDate").toString());

                            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("StartBillingDate").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    long currenttimestamp = Long.parseLong(value.get("StartBillingDate").toString());

                                    if ( (currenttimestamp - timestamp) > 604800000 ){
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("StartBillingDate").setValue(timestamp + 604800000);
                                    }
                                }
                            });



                            //Toast.makeText(getApplicationContext(),signinName,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(HomeActivity.this, ValidityCheck.class);
                            intent.putExtra("Name", signinName);
                            intent.putExtra("id",uid);
                            finish();
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            //  Toast.makeText(getApplicationContext(), "Hii", Toast.LENGTH_LONG).show();
            // setContentView(R.layout.activity_home);


        } else {
            setContentView(R.layout.activity_home);


            final EditText phone = findViewById(R.id.edit_text_phone_pre_sign);
            ImageView okBtn = findViewById(R.id.go_btn);


            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (phone.getText().length() != 10) {
                        phone.setError("Please Enter 10 digit Phone");
                    } else {
                        // Toast.makeText(getApplicationContext(),phone.getText().toString(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(HomeActivity.this, OTPActivity.class);
                        intent.putExtra("Phone", phone.getText().toString());
                        finish();
                        startActivity(intent);
                    }
                }
            });


        }
    }
}
