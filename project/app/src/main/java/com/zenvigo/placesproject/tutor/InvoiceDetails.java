package com.project.placesproject.tutor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.project.placesproject.R;

public class InvoiceDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);

        String invoiceid = getIntent().getStringExtra("InvoiceId");

        final ProgressDialog progressDialog= new ProgressDialog(InvoiceDetails.this);

        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Loading your Order...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection("Invoice").document(invoiceid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                progressDialog.dismiss();
                if (e != null) {
                    Log.w("fire", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("fire", "Current data: " + snapshot.getData());

                    TextView originalfees = (TextView) findViewById(R.id.originalfees);
                    TextView discount = (TextView) findViewById(R.id.discount);
                    TextView feespaid = (TextView) findViewById(R.id.feespaid);



                    originalfees.setText(snapshot.getData().get("Fees").toString());
                    discount.setText("-"+snapshot.getData().get("Discount").toString() + "(Coupon Applied:"+snapshot.getData().get("Coupon_Code").toString()+")" );

                    feespaid.setText(snapshot.getData().get("Credit").toString());




                } else {
                    Log.d("fire", "Current data: null");
                }
            }
        });

    }
}
