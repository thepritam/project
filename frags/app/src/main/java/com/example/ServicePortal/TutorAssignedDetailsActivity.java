package com.example.ServicePortal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class TutorAssignedDetailsActivity extends AppCompatActivity {

    String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_assigned_details);

        final TextView NameDet, EmailDet, CategoryDet,DescDet, showAddress;
        final EditText editprice,edittime;
        final Button reachedLoc, orderDet, invoiceDet;
        final Button Start_Continue = findViewById(R.id.Start_Continue);

        NameDet = (TextView) findViewById(R.id.cname);
        showAddress = (TextView) findViewById(R.id.address);
        EmailDet = (TextView) findViewById(R.id.email);
        reachedLoc = findViewById(R.id.reachedLoc);
        orderDet = findViewById(R.id.orderDet);
        invoiceDet = findViewById(R.id.invoiceDet);

        Intent intent = getIntent();
        final double lat = intent.getDoubleExtra("Latitude",0.0);
        final double longi = intent.getDoubleExtra("Longitude",0.0);
        String Cid = intent.getStringExtra("CId");
        final String Uid = intent.getStringExtra("Uid");
        currentUid = CheckAvailability.id;
        final String QuotationID = intent.getStringExtra("QuotationID");
        final String InvoiceID = intent.getStringExtra("InvoiceID");
        final String orderdescriptionId = intent.getStringExtra("OrderDescriptionID");

        FirebaseDatabase.getInstance().getReference().child("Users").child(Cid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                        final User user = dataSnapshot.getValue(User.class);

                final HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

                NameDet.setText(value.get("Name").toString());
                showAddress.setText(value.get("Address").toString());
                EmailDet.setText(value.get("Email").toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        reachedLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TutorAssignedDetailsActivity.this, TutorReachedLocActivity.class);
                intent.putExtra("Latitude", lat);
                intent.putExtra("Longitude", longi);
                intent.putExtra("OrderId", Uid);
                startActivity(intent);

            }
        });

        orderDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category = "";
                if(CheckAvailability.Category.equals("Tutor"))
                    category="Education";

                Intent intent = new Intent(TutorAssignedDetailsActivity.this, ViewTutorOrderActivity.class);
                intent.putExtra("Category", category);
                intent.putExtra("OrderDescriptionId", orderdescriptionId);
                intent.putExtra("OrderId", Uid);
                startActivity(intent);

            }
        });

        invoiceDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TutorAssignedDetailsActivity.this, TutorInvoiceDetActivity.class);
                intent.putExtra("OrderDescriptionId", orderdescriptionId);
                intent.putExtra("OrderId", Uid);
                intent.putExtra("InvoiceID", InvoiceID);
                startActivity(intent);

            }
        });

    }
}
