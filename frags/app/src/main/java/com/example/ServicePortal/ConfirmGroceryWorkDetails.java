package com.example.ServicePortal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Table;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmGroceryWorkDetails extends AppCompatActivity {

    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_grocery_work_details);

        final TextView NameDet, PhoneDet, EmailDet, CategoryDet,DescDet;
        final Button confirmbutton, showButton;
        final Button Start_Continue = findViewById(R.id.Start_Continue);

        NameDet = (TextView) findViewById(R.id.cname);
        PhoneDet = (TextView) findViewById(R.id.phno);
        EmailDet = (TextView) findViewById(R.id.email);
        DescDet = (TextView) findViewById(R.id.des);
//        price = (TextView) findViewById(R.id.price);
//        product = (TextView) findViewById(R.id.product);
        confirmbutton = (Button)findViewById(R.id.confirmbutton);

        Intent intent = getIntent();
        final double lat = intent.getDoubleExtra("Latitude",0.0);
        final double longi = intent.getDoubleExtra("Longitude",0.0);
        String Cid = intent.getStringExtra("CId");
        final String OrderId = intent.getStringExtra("OrderId");
        final String OrderDescId = intent.getStringExtra("OrderDescId");
        final String desc = intent.getStringExtra("Description");
        final  int max = intent.getIntExtra("valueofi",0);



//        FirebaseDatabase.getInstance().getReference().child("Users").child(Cid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
////                        final User user = dataSnapshot.getValue(User.class);
//
//                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
//
//                NameDet.setText(value.get("Name").toString());
//                PhoneDet.setText(value.get("Phone").toString());
//                EmailDet.setText(value.get("Email").toString());
//                DescDet.setText(desc);
////
////                Log.d("orderid",""+OrderId);
//
////                gotoButton.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////
////                        CheckAvailability.availcheck = "0";
////                        Intent intent = new Intent(GroceryWorkDetails.this, MainActivity.class);
////                        intent.putExtra("availcheck", "0");
////                        intent.putExtra("id", CheckAvailability.id);
////                        intent.putExtra("Category", CheckAvailability.Category);
////                        intent.putExtra("lat", CheckAvailability.lat);
////                        intent.putExtra("longi", CheckAvailability.longi);
////                        startActivity(intent);
////                    }
////                });
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });

        confirmbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Iterator hmIterator = model.getMap().entrySet().iterator();
                        int i = 0;

                        while (hmIterator.hasNext()) {

                            if ( max == i )
                            {
                                break;
                            }

                            FirebaseDatabase.getInstance().getReference().child("OrderDescription").child(OrderDescId).child("Item"+(i+1)).child("Price").setValue(""+model.getMap().get(i));
                            i++;
                        }

                        final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                        DocumentReference assignstate = rootRef.collection("Orders").document(OrderId);
                        assignstate.update("Assigned", 1);

                        Intent intent = new Intent(ConfirmGroceryWorkDetails.this, MainActivity.class);
                        intent.putExtra("availcheck", "0");
                        intent.putExtra("id", CheckAvailability.id);
                        intent.putExtra("Category", CheckAvailability.Category);
                        intent.putExtra("lat", CheckAvailability.lat);
                        intent.putExtra("longi", CheckAvailability.longi);
                        startActivity(intent);
                    }
                });

        int i = 0;
        ArrayList<String> quant = model.getquant();
        ArrayList<String> product = model.getprod();

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

//        HashMap<Integer,Double> value = model.getMap();

        Iterator hmIterator = model.getMap().entrySet().iterator();

        while (hmIterator.hasNext()) {

            if ( max == i )
            {
                break;
            }

//            Map.Entry mapElement = (Map.Entry)hmIterator.next();
////                    int marks = ((int)mapElement.getValue() + 10);
//            HashMap<String, Object> items = (HashMap<String, Object>) mapElement.getValue();
            TableRow tb=new TableRow(getApplicationContext());

            TextView prod;
            prod = new TextView(getApplicationContext());
            prod.setMinWidth(100);
            prod.setMinHeight(100);
            prod.setTextSize(22);

            prod.setText(""+product.get(i));


            TextView quantity;
            quantity = new TextView(getApplicationContext());
            quantity.setMinWidth(100);
            quantity.setMinHeight(100);
            quantity.setTextSize(22);

            quantity.setText(""+quant.get(i));

            TextView price;
            price = new TextView(getApplicationContext());
            price.setMaxWidth(100);
            price.setMinHeight(10);
            price.setTextSize(22);
            price.setText(""+model.getMap().get(i));

            tb.addView(prod , new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
            tb.addView(quantity, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
            tb.addView(price, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
            tableLayout.addView(tb);
            Log.d("items",""+i);

            i++;
        }



//        intent = new Intent(getApplicationContext(), GroceryWorkDetails.class);
//
//
//        startActivity(intent);


    }
}

