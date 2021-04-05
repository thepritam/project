package com.project.placesproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.razorpay.PaymentResultListener;
import com.project.placesproject.tutor.InvoiceDetails;

import org.w3c.dom.Text;

public class PayAmount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_amount);

        String invoiceid = getIntent().getStringExtra("invoiceid");

        Log.d("invoiceidpay",invoiceid);

        final ProgressDialog progressDialog= new ProgressDialog(PayAmount.this);

        progressDialog.setTitle("Please Wait...");

        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection("Invoice").document(invoiceid.trim());
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

                    double amount = Double.parseDouble(snapshot.getData().get("Credit").toString());

                    TextView tobepaid = (TextView) findViewById(R.id.tobepaid);
                    tobepaid.setVisibility(View.VISIBLE);
                    tobepaid.setText(""+amount);





                } else {
                    Log.d("fire", "Current data: null");
                }
            }
        });
    }
}
