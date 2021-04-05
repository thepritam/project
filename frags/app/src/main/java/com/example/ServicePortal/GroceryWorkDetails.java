package com.example.ServicePortal;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class GroceryWorkDetails extends AppCompatActivity {

    TableLayout tableLayout;
    ArrayList<String> product = new ArrayList<String>();
    ArrayList<String> qty = new ArrayList<String>();
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_work_details);

        final TextView NameDet, PhoneDet, EmailDet, CategoryDet,DescDet;
        final Button gotoButton, showButton;
        final Button Start_Continue = findViewById(R.id.Start_Continue);

        NameDet = (TextView) findViewById(R.id.cname);
        PhoneDet = (TextView) findViewById(R.id.phno);
        EmailDet = (TextView) findViewById(R.id.email);
        DescDet = (TextView) findViewById(R.id.des);
//        price = (TextView) findViewById(R.id.price);
//        product = (TextView) findViewById(R.id.product);
        gotoButton = findViewById(R.id.gotobutton);

        Intent intent = getIntent();
        final double lat = intent.getDoubleExtra("Latitude",0.0);
        final double longi = intent.getDoubleExtra("Longitude",0.0);
        String Cid = intent.getStringExtra("CId");
        final String OrderId = intent.getStringExtra("OrderId");
        final String OrderDescId = intent.getStringExtra("OrderDescriptionID");
        final String desc = intent.getStringExtra("Description");

        Log.d("lol",""+Cid);


        FirebaseDatabase.getInstance().getReference().child("Users").child(Cid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                        final User user = dataSnapshot.getValue(User.class);

                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

                NameDet.setText(value.get("Name").toString());
                PhoneDet.setText(value.get("Phone").toString());
                EmailDet.setText(value.get("Email").toString());
                DescDet.setText(desc);
//
//                Log.d("orderid",""+OrderId);

                gotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(GroceryWorkDetails.this, ConfirmGroceryWorkDetails.class);
                        intent.putExtra("id", CheckAvailability.id);
                        intent.putExtra("valueofi", i);
                        intent.putExtra("OrderDescId",OrderDescId);
                        intent.putExtra("OrderId",OrderId);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        FirebaseDatabase.getInstance().getReference().child("OrderDescription").child(OrderDescId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                        final User user = dataSnapshot.getValue(User.class);



                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();


                if (value.containsValue(OrderId+"-ItemPic.jpg"))
                {
                    TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
                    tableLayout.setVisibility(View.GONE);

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReference();
                    StorageReference ref = storageReference.child("images/").child(value.get("ItemPic").toString());

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("value", ""+"enters");

                            String url = uri.toString();
                            downloadFiles( GroceryWorkDetails.this, "ShoppingList-"+OrderId, ".jpg", DIRECTORY_DOWNLOADS, url);

                            LinearLayout ll = (LinearLayout) findViewById(R.id.listlayout);
                            ll.setVisibility(View.VISIBLE);

                            gotoButton.setText("Upload Pricing");

                        }
                    });

                }
                else {

                    tableLayout = (TableLayout) findViewById(R.id.tableLayout);
                    model.clearmap();

                    Iterator hmIterator = value.entrySet().iterator();

                    // Iterate through the hashmap
                    // and add some bonus marks for every student
                    i = 0;

                    while (hmIterator.hasNext()) {
                        Map.Entry mapElement = (Map.Entry) hmIterator.next();
//                    int marks = ((int)mapElement.getValue() + 10);
                        HashMap<String, Object> items = (HashMap<String, Object>) mapElement.getValue();
                        TableRow tb = new TableRow(getApplicationContext());

                        TextView prod;
                        prod = new TextView(getApplicationContext());
                        prod.setMinWidth(100);
                        prod.setMinHeight(100);
                        prod.setTextSize(22);
                        prod.setText("" + items.get("Product"));
                        model.setprod("" + items.get("Product"));

                        TextView quantity;
                        quantity = new TextView(getApplicationContext());
                        quantity.setMinWidth(100);
                        quantity.setMinHeight(100);
                        quantity.setTextSize(22);
                        quantity.setText("" + items.get("Quantity"));
                        model.setqty("" + items.get("Quantity"));

                        EditText price;
                        price = new EditText(getApplicationContext());
                        price.setMaxWidth(100);
                        price.setMinHeight(10);
                        price.setTextSize(22);
                        price.setInputType(InputType.TYPE_CLASS_NUMBER);
                        price.setId(i);
                        price.addTextChangedListener(new DynamicViews(price));

                        tb.addView(prod, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                        tb.addView(quantity, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                        tb.addView(price, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                        tableLayout.addView(tb);
                        Log.d("items", "" + items);

                        product.add("" + items.get("Product"));
                        qty.add("" + items.get("Quantity"));

                        i++;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


    }

    public void downloadFiles(Context context, String fileName, String fileExtension, String destinationDirectory, String url)
    {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName+fileExtension);

        downloadManager.enqueue(request);
    }
}

