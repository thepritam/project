package com.example.ServicePortal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Map;

import java.util.HashMap;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class OTPActivity extends AppCompatActivity  implements
        View.OnClickListener {

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ViewGroup mPhoneNumberViews;
    private ViewGroup mSignedInViews;

    private TextView mStatusText;
    private TextView mDetailText;
    private EditText verificationnumber;
    private ProgressDialog progressDialog;

    private EditText mPhoneNumberField;
    private EditText mVerificationField;
    private String phoneno;
    private ImageView mStartButton;
    private Button resendit;
    private Button mVerifyButton;
    // private Button mResendButton;
    //private Button mSignOutButton;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Please wait while we give you the code...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        phoneno = getIntent().getStringExtra("Phone");
        //Toast.makeText(getApplicationContext(),phoneno,Toast.LENGTH_LONG).show();

        progressDialog.show();
        verificationnumber = (EditText)findViewById(R.id.verification_number);
        mStartButton = (ImageView) findViewById(R.id.verify_btn);

        resendit =(Button)findViewById(R.id.resend_btn);
        // Assign click listeners
        mStartButton.setOnClickListener(this);
        resendit.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;



                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                    verificationnumber.setError("Invalid id entered.");

                } else if (e instanceof FirebaseTooManyRequestsException) {

                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }


            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                if(progressDialog.isShowing())
                    progressDialog.dismiss();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                Toast.makeText(getApplicationContext(),"Code Sent!",Toast.LENGTH_LONG).show();

            }
        };
        // [END phone_auth_callbacks]
        startPhoneNumberVerification(phoneno);
    }




    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
//        mStatusText.setVisibility(View.INVISIBLE);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            final String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Log.d("currentId",currentUid);

                         /*   FirebaseDatabase.getInstance().getReference().child("Users")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Log.d("show", "onDataChange: ");
                                            int flag=0;
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                // User user = snapshot.getValue(User.class);
                                                Map<String, Object> value = (Map<String, Object>) snapshot.getValue();
                                                Log.d("show", "Value is: " + value);
                                                String phone = value.get("Phone").toString();
                                                Log.d("phoneget", phoneno);
                                                Log.d("phoneget", phone);
                                                if (phone.equals(phoneno)) {
                                                    Log.d("inserted", "yes ... :" + snapshot.getKey());
                                                    Intent intent = new Intent(OTPActivity.this, ValidityCheck.class);
                                                    intent.putExtra("Name", value.get("Name").toString());
                                                    intent.putExtra("id", snapshot.getKey());
                                                    finish();
                                                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    flag = 1;
                                                    break;
                                                }
                                            }

                                            if(flag==0)
                                                {
                                                    Intent intent = new Intent(OTPActivity.this,DetailsActivity.class);
                                                    intent.putExtra("Phone",phoneno);
                                                    finish();
                                                    startActivity(intent);
                                                }


                                                // assert user != null;


                                            }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });*/


                          FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // Get user information
                                            if(dataSnapshot.exists()) {
//                                                User user = dataSnapshot.getValue(User.class);
                                                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                                String name = value.get("Name").toString();
                                                String signinName = name;
                                                //Toast.makeText(getApplicationContext(),signinName,Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(OTPActivity.this, ValidityCheck.class);
                                                intent.putExtra("Name", signinName);
                                                intent.putExtra("id",currentUid);
                                                finish();
                                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }

                                            else
                                            {
                                                Intent intent = new Intent(OTPActivity.this,DetailsActivity.class);
                                                intent.putExtra("Phone",phoneno);
                                                finish();
                                                startActivity(intent);
                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });





                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                verificationnumber.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            //updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.verify_btn:
                String code = verificationnumber.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.resend_btn:
                resendVerificationCode(phoneno, mResendToken);
                break;

        }
    }





}
