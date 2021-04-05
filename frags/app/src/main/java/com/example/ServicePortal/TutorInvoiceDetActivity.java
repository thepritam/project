package com.example.ServicePortal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.HashMap;

public class TutorInvoiceDetActivity extends AppCompatActivity {

    String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_invoice_det);

        final TextView credit, discount, status;

        credit = findViewById(R.id.credit);
        discount = findViewById(R.id.discount);
        status = findViewById(R.id.status);

        Intent intent = getIntent();
        final double lat = intent.getDoubleExtra("Latitude",0.0);
        final double longi = intent.getDoubleExtra("Longitude",0.0);
        String Cid = intent.getStringExtra("CId");
        final String Uid = intent.getStringExtra("Uid");
        currentUid = CheckAvailability.id;
        final String QuotationID = intent.getStringExtra("QuotationID");
        final String InvoiceID = intent.getStringExtra("InvoiceID");
        final String orderdescriptionId = intent.getStringExtra("OrderDescriptionID");

        final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        rootRef.collection("Invoice")
                .document(InvoiceID)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            credit.setText(documentSnapshot.getDouble("Credit").toString());
                            discount.setText(documentSnapshot.getDouble("Discount").toString());
                            status.setText(documentSnapshot.getString("Status").toString());
                        }
                    });

    }
}
