package com.project.placesproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static maes.tech.intentanim.CustomIntent.customType;

public class HomeActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                            Map<String, Object> user = (Map<String, Object>) dataSnapshot.getValue();
                            Log.d("show", "Value is: " + user);
                           // User user = dataSnapshot.getValue(User.class);
                            String signinName = user.get("Name").toString();

                            progressDialog.dismiss();
                            //Toast.makeText(getApplicationContext(),signinName,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(HomeActivity.this, MainPage.class);
                            intent.putExtra("Name", signinName);
                            intent.putExtra("id",uid);
                            finish();
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            customType(HomeActivity.this,"bottom-to-up");


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
            Button okBtn = findViewById(R.id.go_btn);


            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (phone.getText().length() != 10) {
                        phone.setError("Please Enter 10 digit Phone");
                    } else {
                        // Toast.makeText(getApplicationContext(),phone.getText().toString(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        intent.putExtra("Phone", phone.getText().toString());
                        finish();
                        startActivity(intent);
                    }
                }
            });


        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

