package com.example.ServicePortal;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class TutorDetailsActivity extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    HashMap<String, String> klass_Montessori =  new HashMap<>();
    HashMap<String, String> klass_1_5 =  new HashMap<>();
    HashMap<String, String> klass_6_8 =  new HashMap<>();
    HashMap<String, String> klass_9_10 =  new HashMap<>();
    HashMap<String, String> klass_11_12 =  new HashMap<>();
    HashMap<String, String> klass_Graduate =  new HashMap<>();
    HashMap<String, HashMap<String,String>> klass = new HashMap<>();
    String graddeg = "0";
    String board;

    LinearLayout linear_1_5,linear_6_8,linear_9_10,linear_11_12,linear_Graduate;
    TableLayout tableLayout_1_5,tableLayout_6_8,tableLayout_9_10,tableLayout_11_12,tableLayout_Graduate;
//    TableRow tableRow_1_5,tableRow_6_8,tableRow_9_10,tableRow_11_12,tableRow_Graduate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_details);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Please wait while we create your account");

        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        Intent intent = getIntent();
        final String currentUid = intent.getStringExtra("id");
        final String category = intent.getStringExtra("Category");
        final String name = intent.getStringExtra("Name");

        final CheckBox chk_Montessori,chk_1_5,chk_6_8,chk_9_10,chk_11_12,chk_Graduate;
        final Button btn_1_5,btn_6_8,btn_9_10,btn_11_12,btn_Graduate;

        chk_Montessori = (CheckBox)findViewById(R.id.checkbox_Montessori);
        chk_1_5 = (CheckBox)findViewById(R.id.checkbox_1_5);
        chk_6_8 = (CheckBox)findViewById(R.id.checkbox_6_8);
        chk_9_10 = (CheckBox)findViewById(R.id.checkbox_9_10);
        chk_11_12 = (CheckBox)findViewById(R.id.checkbox_11_12);
        chk_Graduate = (CheckBox)findViewById(R.id.checkbox_Graduate);

        btn_1_5 = (Button) findViewById(R.id.btn_1_5);
        btn_6_8 = (Button) findViewById(R.id.btn_6_8);
        btn_9_10 = (Button) findViewById(R.id.btn_9_10);
        btn_11_12 = (Button) findViewById(R.id.btn_11_12);
        btn_Graduate = (Button) findViewById(R.id.btn_Graduate);

//        final LinearLayout linear_1_5,linear_6_8,linear_9_10,linear_11_12,linear_Graduate;

        linear_1_5 = (LinearLayout) findViewById(R.id.linear_1_5);
        linear_6_8 = (LinearLayout) findViewById(R.id.linear_6_8);
        linear_9_10 = (LinearLayout) findViewById(R.id.linear_9_10);
        linear_11_12 = (LinearLayout) findViewById(R.id.linear_11_12);
        linear_Graduate = (LinearLayout) findViewById(R.id.linear_Graduate);

        tableLayout_1_5 = (TableLayout) findViewById(R.id.tableLayout_1_5);
        tableLayout_6_8 = (TableLayout) findViewById(R.id.tableLayout_6_8);
        tableLayout_9_10 = (TableLayout) findViewById(R.id.tableLayout_9_10);
        tableLayout_11_12 = (TableLayout) findViewById(R.id.tableLayout_11_12);
        tableLayout_Graduate = (TableLayout) findViewById(R.id.tableLayout_Graduate);

        final Spinner spinner = findViewById(R.id.spinner_board);

        Button submitbutton = (Button) findViewById(R.id.submitbutton);

        final String[] arr = new String[]{
                "ICSE",
                "CBSE",
                "WBSE"
        };

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arr);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                board = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TextView errorText = (TextView)spinner.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("Select Board");
            }
        });

        chk_Montessori.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (chk_Montessori.isChecked()) {
                    klass_Montessori.put("All Subjects","Yes");
                }
                else{
                    if(klass_Montessori.containsKey("All Subjects")){
                        klass_Montessori.remove("All Subjects");
                    }
                }

            }
        });

        chk_1_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (chk_1_5.isChecked()) {
                    linear_1_5.setVisibility(View.VISIBLE);

                    btn_1_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onAddFieldlinear_1_5(view);
                        }
                    });
                }
                else{
                    linear_1_5.setVisibility(View.GONE);
                }
            }
        });

        chk_6_8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chk_6_8.isChecked()) {
                    linear_6_8.setVisibility(View.VISIBLE);

                    btn_6_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onAddFieldlinear_6_8(view);
                        }
                    });
                }
                else{
                    linear_6_8.setVisibility(View.GONE);
                }
            }
        });

        chk_9_10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chk_9_10.isChecked()) {
                    linear_9_10.setVisibility(View.VISIBLE);

                    btn_9_10.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onAddFieldlinear_9_10(view);
                        }
                    });
                }
                else{
                    linear_9_10.setVisibility(View.GONE);
                }
            }
        });

        chk_11_12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chk_11_12.isChecked()) {
                    linear_11_12.setVisibility(View.VISIBLE);

                    btn_11_12.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onAddFieldlinear_11_12(view);
                        }
                    });
                }
                else{
                    linear_11_12.setVisibility(View.GONE);
                }
            }
        });

        chk_Graduate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chk_Graduate.isChecked()) {
                    linear_Graduate.setVisibility(View.VISIBLE);

                    btn_Graduate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onAddFieldlinear_Graduate(view);
                        }
                    });
                    graddeg = "1";
                }
                else{
                    linear_Graduate.setVisibility(View.GONE);
                    graddeg = "0";
                }
            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressDialog
                        = new ProgressDialog(TutorDetailsActivity.this);
                progressDialog.setTitle("Uploading Preferences");
                progressDialog.show();

                if (chk_Montessori.isChecked()) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Category").child("Preferences").child("Class").child("Montessori").setValue(klass_Montessori);
                }
                if (chk_1_5.isChecked() && !klass_1_5.isEmpty()) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Category").child("Preferences").child("Class").child("1-5").setValue(klass_1_5);
                }
                if (chk_6_8.isChecked() && !klass_6_8.isEmpty()) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Category").child("Preferences").child("Class").child("6-8").setValue(klass_6_8);
                }
                if (chk_9_10.isChecked() && !klass_9_10.isEmpty()) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Category").child("Preferences").child("Class").child("9-10").setValue(klass_9_10);
                }
                if (chk_11_12.isChecked() && !klass_11_12.isEmpty()) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Category").child("Preferences").child("Class").child("11-12").setValue(klass_11_12);
                }
                if (chk_Graduate.isChecked() && !klass_Graduate.isEmpty()) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Category").child("Preferences").child("Class").child("Graduate").setValue(klass_Graduate);
                }

                FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Category").child("Preferences").child("Board").setValue(board);

//                klass.put("Montessori",klass_Montessori);
//                klass.put("1-5",klass_1_5);
//                klass.put("6-8",klass_6_8);
//                klass.put("9-10",klass_9_10);
//                klass.put("11-12",klass_11_12);
//                klass.put("Graduate",klass_Graduate);
//                CheckAvailability.klass = klass;

                FirebaseDatabase.getInstance().getReference().child("Users").child("8IIob3cXZnV3za4As5t05BPTmRg1").child("Category").child("Type").setValue("Tutor").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(TutorDetailsActivity.this, TutorImageUploadActivity.class);
                        intent.putExtra("Name",name);
                        intent.putExtra("id",currentUid);
                        intent.putExtra("Category",category);
                        intent.putExtra("Gradreq",graddeg);
                        startActivity(intent);
                    }
                });
            }
        });

    }

        public void onAddFieldlinear_1_5(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final TableRow rowView_1_5 = (TableRow)inflater.inflate(R.layout.field, null);

            final Spinner rowViewSpinner_1_5 = rowView_1_5.findViewById(R.id.spinnerStream);
            final Button delete_button = rowView_1_5.findViewById(R.id.delete_button);
            final Button select_button = rowView_1_5.findViewById(R.id.select_button);

            final String[] arrClass = new String[]{
                    "Select Subject",
                    "All Subjects",
                    "Mathematics",
                    "Science",
                    "English"
            };

        final ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrClass);

            adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            rowViewSpinner_1_5.setAdapter(adapterClass);


            rowViewSpinner_1_5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                    select_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String subj = adapterClass.getItem(position);

                            if(subj .equals("Select Subject"))
                            {
                                TextView errorText = (TextView)rowViewSpinner_1_5.getSelectedView();
                                errorText.setError("");
                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                errorText.setText("Select Subject");
                            }
                            else if(klass_1_5.containsKey(subj)){
                                TextView errorText = (TextView)rowViewSpinner_1_5.getSelectedView();
                                errorText.setError("");
                                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                                errorText.setText("Already Selected");
                            }
                            else {
                                rowViewSpinner_1_5.setEnabled(false);
                                klass_1_5.put(subj, "Yes");
                                Log.d("klassvalue",""+klass_1_5);
                            }
                        }
                    });

                    delete_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String subj = adapterClass.getItem(position);
                            tableLayout_1_5.removeView(rowView_1_5);
                            if(klass_1_5.containsKey(subj))
                            {
                                klass_1_5.remove(subj);
                            }
                            Log.d("klassvalue",""+klass_1_5);
                        }
                    });

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    TextView errorText = (TextView)rowViewSpinner_1_5.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select Subject");

                }
            });

            tableLayout_1_5.addView(rowView_1_5);

        }

    public void onAddFieldlinear_6_8(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final TableRow rowView_6_8 = (TableRow)inflater.inflate(R.layout.field, null);

        final Spinner rowViewSpinner_6_8 = rowView_6_8.findViewById(R.id.spinnerStream);
        final Button delete_button = rowView_6_8.findViewById(R.id.delete_button);
        final Button select_button = rowView_6_8.findViewById(R.id.select_button);

        final String[] arrClass = new String[]{
                "Select Subject",
                            "All Subjects",
                            "Physics",
                            "Chemistry",
                            "Biology",
                            "Mathematics",
                            "History",
                            "Geography",
                            "Computer Science",
                            "English"
        };

        final ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrClass);

        adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        rowViewSpinner_6_8.setAdapter(adapterClass);

        rowViewSpinner_6_8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                select_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subj = adapterClass.getItem(position);

                        if(subj .equals("Select Subject"))
                        {
                            TextView errorText = (TextView)rowViewSpinner_6_8.getSelectedView();
                            errorText.setError("");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText("Select Subject");
                        }
                        else if(klass_6_8.containsKey(subj)){
                            TextView errorText = (TextView)rowViewSpinner_6_8.getSelectedView();
                            errorText.setError("");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText("Already Selected");
                        }
                        else {
                            rowViewSpinner_6_8.setEnabled(false);
                            klass_6_8.put(subj, "Yes");
                        }
                    }
                });

                delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subj = adapterClass.getItem(position);
                        tableLayout_6_8.removeView(rowView_6_8);
                        if(klass_6_8.containsKey(subj))
                        {
                            klass_6_8.remove(subj);
                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                TextView errorText = (TextView)rowViewSpinner_6_8.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("Please select a Stream");

            }
        });

        tableLayout_6_8.addView(rowView_6_8);
    }

    public void onAddFieldlinear_9_10(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final TableRow rowView_9_10 = (TableRow)inflater.inflate(R.layout.field, null);

        final Spinner rowViewSpinner_9_10 = rowView_9_10.findViewById(R.id.spinnerStream);
        final Button delete_button = rowView_9_10.findViewById(R.id.delete_button);
        final Button select_button = rowView_9_10.findViewById(R.id.select_button);

        final String[] arrClass = new String[]{
                "Select Subject",
                            "All Subjects",
                            "Physics",
                            "Chemistry",
                            "Biology",
                            "Mathematics",
                            "History",
                            "Geography",
                            "Computer Science",
                            "English"
        };

        final ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrClass);

        adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        rowViewSpinner_9_10.setAdapter(adapterClass);

        rowViewSpinner_9_10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                select_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subj = adapterClass.getItem(position);

                        if(subj .equals("Select Subject"))
                        {
                            TextView errorText = (TextView)rowViewSpinner_9_10.getSelectedView();
                            errorText.setError("");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText("Select Subject");
                        }
                        else if(klass_9_10.containsKey(subj)){
                            TextView errorText = (TextView)rowViewSpinner_9_10.getSelectedView();
                            errorText.setError("");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText("Already Selected");
                        }
                        else {
                            rowViewSpinner_9_10.setEnabled(false);
                            klass_9_10.put(subj, "Yes");
                        }
                    }
                });

                delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subj = adapterClass.getItem(position);
                        tableLayout_9_10.removeView(rowView_9_10);
                        if(klass_9_10.containsKey(subj))
                        {
                            klass_9_10.remove(subj);
                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                TextView errorText = (TextView)rowViewSpinner_9_10.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("Please select a Stream");

            }
        });

        tableLayout_9_10.addView(rowView_9_10);
    }

    public void onAddFieldlinear_11_12(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final TableRow rowView_11_12 = (TableRow)inflater.inflate(R.layout.field, null);

        final Spinner rowViewSpinner_11_12 = rowView_11_12.findViewById(R.id.spinnerStream);
        final Button delete_button = rowView_11_12.findViewById(R.id.delete_button);
        final Button select_button = rowView_11_12.findViewById(R.id.select_button);

        final String[] arrClass = new String[]{
                "Select Subject",
                            "All Subjects",
                            "Physics",
                            "Chemistry",
                            "Biology",
                            "Mathematics",
                            "History",
                            "Geography",
                            "Commerce",
                            "Computer Science",
                            "English"
        };

        final ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrClass);

        adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        rowViewSpinner_11_12.setAdapter(adapterClass);

        rowViewSpinner_11_12.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                select_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subj = adapterClass.getItem(position);

                        if(subj .equals("Select Subject"))
                        {
                            TextView errorText = (TextView)rowViewSpinner_11_12.getSelectedView();
                            errorText.setError("");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText("Select Subject");
                        }
                        else if(klass_11_12.containsKey(subj)){
                            TextView errorText = (TextView)rowViewSpinner_11_12.getSelectedView();
                            errorText.setError("");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText("Already Selected");
                        }
                        else {
                            rowViewSpinner_11_12.setEnabled(false);
                            klass_11_12.put(subj, "Yes");
                        }
                    }
                });

                delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subj = adapterClass.getItem(position);
                        tableLayout_11_12.removeView(rowView_11_12);
                        if(klass_11_12.containsKey(subj))
                        {
                            klass_11_12.remove(subj);
                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                TextView errorText = (TextView)rowViewSpinner_11_12.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("Please select a Stream");

            }
        });

        tableLayout_11_12.addView(rowView_11_12);
    }

    public void onAddFieldlinear_Graduate(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final TableRow rowView_Graduate = (TableRow)inflater.inflate(R.layout.field, null);

        final Spinner rowViewSpinner_Graduate = rowView_Graduate.findViewById(R.id.spinnerStream);
        final Button delete_button = rowView_Graduate.findViewById(R.id.delete_button);
        final Button select_button = rowView_Graduate.findViewById(R.id.select_button);

        final String[] arrClass = new String[]{
                "Select Subject",
                            "All Subjects",
                            "Physics",
                            "Chemistry",
                            "Biology",
                            "Mathematics",
                            "History",
                            "Geography",
                            "Commerce",
                            "Economics",
                            "Pharmacy",
                            "Computer Science",
                            "English"
        };

        final ArrayAdapter<String> adapterClass = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrClass);

        adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        rowViewSpinner_Graduate.setAdapter(adapterClass);

        rowViewSpinner_Graduate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                select_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subj = adapterClass.getItem(position);

                        if(subj .equals("Select Subject"))
                        {
                            TextView errorText = (TextView)rowViewSpinner_Graduate.getSelectedView();
                            errorText.setError("");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText("Select Subject");
                        }
                        else if(klass_1_5.containsKey(subj)){
                            TextView errorText = (TextView)rowViewSpinner_Graduate.getSelectedView();
                            errorText.setError("");
                            errorText.setTextColor(Color.RED);//just to highlight that this is an error
                            errorText.setText("Already Selected");
                        }
                        else {
                            rowViewSpinner_Graduate.setEnabled(false);
                            klass_Graduate.put(subj, "Yes");
                        }
                    }
                });

                delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String subj = adapterClass.getItem(position);
                        tableLayout_Graduate.removeView(rowView_Graduate);
                        if(klass_Graduate.containsKey(subj))
                        {
                            klass_Graduate.remove(subj);
                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                TextView errorText = (TextView)rowViewSpinner_Graduate.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("Please select a Stream");

            }
        });

        tableLayout_Graduate.addView(rowView_Graduate);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        finish();
        startActivity(intent);
        Toast.makeText(this, "Registration not completed...", Toast.LENGTH_SHORT).show();
    }
}
