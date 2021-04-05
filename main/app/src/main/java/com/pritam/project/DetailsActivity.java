package com.pritam.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private String phone;
    private String name;
    private String email;
    private String category = "Customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Please wait while we create your account");

        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        final EditText nameEt = findViewById(R.id.edit_text_name_sign_up);
        final EditText emailEt = findViewById(R.id.edit_text_email_sign_up);
        final Button signUpBtn = findViewById(R.id.btn_sign_up);
        final Spinner spinner = findViewById(R.id.spinner_sign_up);

        phone = getIntent().getStringExtra("Phone");

//        if(phoneIntent != null) {
//            phone = phoneIntent;
//        } else {
//            phoneEt.setVisibility(View.VISIBLE);
//        }

        final String[] arr = new String[]{
                "Customer",
                "Mechanic",
                "Driver",
                "Modifier",
                "Fuel Delivery",
                "Spare Parts Seller"
        };

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arr);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = "Customer";
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                name = nameEt.getText().toString();
                email = emailEt.getText().toString();

                if (nameEt.getText().length() == 0) {
                    nameEt.setError("Please enter Name");
                } else if (emailEt.getText().length() == 0) {
                    emailEt.setError("Please enter Email"); //todo check for valid email with regex
                }
//                else if(phoneEt.getText().length() == 0 || phone == null) {
//                    phoneEt.setError("Please enter Phone");
//                }
                else {
                    Toast.makeText(getApplicationContext(),"+91" + phone,Toast.LENGTH_LONG).show();
                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("Name", name);
                    userMap.put("Email", email);
                    if (category.equals("Customer"))
                        userMap.put("IsAvailable", "1");
                    else
                        userMap.put("IsAvailable", "0");
                    userMap.put("Phone", phone);
                    userMap.put("Category", category);

                    final String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(currentUid).setValue(userMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),"Account created successfully",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(DetailsActivity.this, HallFinder.class);
                                        intent.putExtra("Name",name);
                                        intent.putExtra("id",currentUid);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(DetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    progressDialog.show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(DetailsActivity.this, HomeActivity.class);
        finish();
        startActivity(intent);
        Toast.makeText(this, "Registration not completed...", Toast.LENGTH_SHORT).show();
    }
}
