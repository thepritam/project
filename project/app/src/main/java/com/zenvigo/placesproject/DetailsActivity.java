package com.project.placesproject;



import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.project.placesproject.tutor.tutorActivity;

import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private String phone;
    private String name;
    private String email;
    private String dob;
    private String address;
    private String category = "Customer";
    private String Gender;
    private Spinner spinner;
    private String  currentUid;
    DatePickerDialog dpd;
    Calendar c;

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
        final TextView dobEt = findViewById(R.id.edit_text_date);
        final EditText addressEt = findViewById(R.id.edit_text_address);
        final Button signUpBtn = findViewById(R.id.btn_sign_up);
        spinner=findViewById(R.id.spinnergender);
      //  final Spinner spinner = findViewById(R.id.spinner_sign_up);

        phone = getIntent().getStringExtra("Phone");

//        if(phoneIntent != null) {
//            phone = phoneIntent;
//        } else {
//            phoneEt.setVisibility(View.VISIBLE);
//        }

        final String[] arr = new String[]{
                "Grocery",
                "General Store",
                "Mechanic",
                "Driver",
                "Modifier",
                "Tutor",
                "Spare Parts Seller"
        };
         String [] list = new String[]{"Male","Female","Others"};
        final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(DetailsActivity.this,android.R.layout.simple_spinner_item, list);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.notifyDataSetChanged();
        spinner.setAdapter(dataAdapter2);
     /*   final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
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
        });*/
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                  Gender = dataAdapter2.getItem(position);
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> parent) {
                                                  Gender="Others";
                                              }
                                          });

        dobEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                c.set(Calendar.MONTH, Calendar.NOVEMBER);
                c.set(Calendar.DAY_OF_MONTH, 9);
                c.set(Calendar.YEAR, 2012);

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(DetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        dobEt.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                    }
                }, day,month,year);
                dpd.show();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                name = nameEt.getText().toString();
                email = emailEt.getText().toString();
                dob = dobEt.getText().toString();
                address = addressEt.getText().toString();

                if (nameEt.getText().length() == 0) {
                    nameEt.setError("Please enter Name");
                } else if (emailEt.getText().length() == 0) {
                    emailEt.setError("Please enter Email"); //todo check for valid email with regex
                } else if (dobEt.getText().length() == 0) {
                    emailEt.setError("Please enter Date of Birth"); //todo check for valid email with regex
                } else if (addressEt.getText().length() == 0) {
                    emailEt.setError("Please enter Address"); //todo check for valid email with regex
                }
//                else if(phoneEt.getText().length() == 0 || phone == null) {
//                    phoneEt.setError("Please enter Phone");
//                }
                else {
                    Toast.makeText(getApplicationContext(),"+91" + phone,Toast.LENGTH_LONG).show();
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("Name", name);
                    userMap.put("Email", email);
                    userMap.put("DateBirth", dob);
                    userMap.put("Address", address);
                    userMap.put("IsAvailable", "1");
                    userMap.put("Latitude", 0.0);
                    userMap.put("Longitude", 0.0);
                    userMap.put("isValid", 0);
                    userMap.put("CooldownCat", 0);
                    userMap.put("timeCooldown", 0);
                    userMap.put("Gender",Gender);
                    userMap.put("Phone", phone);
                    userMap.put("Category", "Customer");

                    currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(currentUid).setValue(userMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),"Account created successfully",Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(DetailsActivity.this, MainPage.class);
                                        intent.putExtra("Name",name);
                                        intent.putExtra("id",currentUid);
                                        intent.putExtra("Category",category);
                                        intent.putExtra("Gender",Gender);
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
    protected void onDestroy() {
        super.onDestroy();
//        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
//        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d("show", "onSuccess: deleted");
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Registration not completed...", Toast.LENGTH_SHORT).show();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        mAuth.signOut();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Intent intent = new Intent(DetailsActivity.this, HomeActivity.class);
                finish();
                startActivity(intent);
               //
                Log.d("show", "onSuccess: deleted");
            }
        });

    }
}

