package com.project.placesproject.grocery;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.snapshot.DoubleNode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.project.placesproject.R;
import com.project.placesproject.SaveDetails;
import com.project.placesproject.ServicemaActivity;
import com.project.placesproject.User;
import com.project.placesproject.VerifyActivity;
import com.project.placesproject.account_activity;
import com.project.placesproject.customercare;
import com.project.placesproject.yourorderactivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class ShowOrders extends AppCompatActivity implements PaymentResultListener {


    String ridersid,ridersname,ridersphone;
    double riderslatitude,riderslongitude;
    int ridersrating;
    String couponapplied="";
    double discountapplied=0;

    String getinvoiceid="";

    String quotid,orderid,shopname,riderorderid;
    ProgressDialog progressDialog;
    double pricetotal=0.00,finalprice=0.0;
    int i=10;
    String accesskm;
    TableLayout tableLayout,tableLayoutrider;
    double deliverycharge;
    EditText editTextcouponcode;
    Button btncouponapply;
    private View hiddenPanel;
    Button btn;
    LinearLayout layout;
    RatingBar ratingBar;
    TextView ridername,riderphone,ridersearchtextview;
    ProgressBar progressBar;
    RelativeLayout.LayoutParams layoutParams;
    private BottomNavigationView bottomNavigationView;


    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quotid = getIntent().getStringExtra("Quotation");
        orderid = getIntent().getStringExtra("OrderId");
        shopname = getIntent().getStringExtra("ShopName");
        riderorderid = getIntent().getStringExtra("RiderOrder");
        // shopaddress=getIntent().getStringExtra("shopadress");
        Log.d("show", "onCreate: "+orderid);





        if(quotid.isEmpty()==false)
        {
            displayquot(quotid);
            Log.d("recycle","yes");

        }

    }




    public void slideUpDown() {
        if (!isPanelShown()) {
            // Show the panel
            //  layoutParams.height=750;
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.slide_up);

            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
          //  btn.setVisibility(View.GONE);
            checkassignment(riderorderid);

        }
        else {
            Log.d("panel","entered else");
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.slide_down);

            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown() {
        return hiddenPanel.getVisibility() == View.VISIBLE;
    }

    public void displayquot(String qid)
    {
        setContentView(R.layout.activity_show_orders);


        layout=findViewById(R.id.buttongroerylayout);
        RelativeLayout linearLayout1=findViewById(R.id.main_screen);
        layoutParams= (RelativeLayout.LayoutParams) linearLayout1.getLayoutParams();
        hiddenPanel = findViewById(R.id.hidden_panel);
        progressBar=findViewById(R.id.loadingpanel);
        ratingBar=findViewById(R.id.ratingBarrider);
        ridersearchtextview=findViewById(R.id.ridersearchtextview);
        ridername=findViewById(R.id.ridername);
        riderphone=findViewById(R.id.riderphone);
        tableLayoutrider=findViewById(R.id.tableLayoutrider);

        progressDialog = new ProgressDialog(ShowOrders.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Loading your Order...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        FirebaseDatabase.getInstance().getReference().child("Quotations").child(qid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressDialog.dismiss();

                slideUpDown();

                TextView sname = (TextView)findViewById(R.id.groceryshopname);
                sname.setText(shopname);

//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                        final User user = dataSnapshot.getValue(User.class);

                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                Iterator hmIterator = value.entrySet().iterator();

                while (hmIterator.hasNext()) {
                    Map.Entry mapElement = (Map.Entry)hmIterator.next();
                    HashMap<String, Object> items = (HashMap<String, Object>) mapElement.getValue();

                    if(items.containsKey("QuotImageLink"))
                    {
                        progressDialog.dismiss();
                        //enter picture viewing code
                    }
                    else
                    {
                        tableLayout = (TableLayout) findViewById(R.id.displayitemslayout);
//                        Iterator itemiterator = items.entrySet().iterator();
//                        while(itemiterator.hasNext())
//                        {
//                            Map.Entry itemElement = (Map.Entry) hmIterator.next();
////                    int marks = ((int)mapElement.getValue() + 10);
//                            HashMap<String, Object> itemsdesc = (HashMap<String, Object>) itemElement.getValue();
                        TableRow tb = new TableRow(getApplicationContext());

                        TextView prod;
                        prod = new TextView(getApplicationContext());
                        prod.setMinWidth(100);
                        prod.setMinHeight(100);
                        prod.setTextSize(16);
                        prod.setGravity(Gravity.CENTER);
                        prod.setText("" + items.get("Product"));


                        TextView quantity;
                        quantity = new TextView(getApplicationContext());
                        quantity.setMinWidth(100);
                        quantity.setMinHeight(100);
                        quantity.setTextSize(16);
                        quantity.setGravity(Gravity.CENTER);
                        quantity.setText("" + items.get("Quantity"));


                        TextView price;
                        price = new TextView(getApplicationContext());
                        price.setMaxWidth(100);
                        price.setMinHeight(10);
                        price.setTextSize(16);
                        price.setGravity(Gravity.CENTER);
                        price.setText("" + items.get("Price"));
                       // pricetotal+=Double.parseDouble(items.get("Price").toString());




                        tb.addView(prod, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                        tb.addView(quantity, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                        tb.addView(price, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                        tableLayout.addView(tb);
                        Log.d("items", "" + items);
                    }



                }


                Log.d("show", "onDataChange: "+pricetotal);

                String invid = getIntent().getStringExtra("InvoiceId");
                Log.d("Invoice",invid);
                final DocumentReference docRef = db.collection("Invoice").document(invid);
                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("fire", "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Log.d("fire", "Current data: " + snapshot.getData());

                           // progressDialog.dismiss();

                          deliverycharge = Double.parseDouble(snapshot.getData().get("Delivery").toString());
                          pricetotal = Double.parseDouble(snapshot.getData().get("Shop_Price").toString());
                          double discount = Double.parseDouble(snapshot.getData().get("Discount").toString());
                          finalprice = pricetotal+deliverycharge-discount;

                            TableRow tb = new TableRow(getApplicationContext());

                            TextView deliverychargeView;
                            deliverychargeView= new TextView(getApplicationContext());
                            deliverychargeView.setMinWidth(100);
                            deliverychargeView.setMinHeight(100);
                            deliverychargeView.setTextSize(18);
                            deliverychargeView.setTextColor(getResources().getColor(R.color.colorPrimary));
                            deliverychargeView.setGravity(Gravity.CENTER);
                            deliverychargeView.setText("Delivery Charge"  );


                            TextView deliveryamount;
                            deliveryamount = new TextView(getApplicationContext());
                            deliveryamount.setId(i);
                            deliveryamount.setMinWidth(100);
                            deliveryamount.setMinHeight(100);
                            deliveryamount.setTextSize(16);
                            deliveryamount.setGravity(Gravity.CENTER);
                            deliveryamount.setText(Double.toString(deliverycharge));

                            TableRow tb1 = new TableRow(getApplicationContext());
                            TextView PriceView;
                            PriceView= new TextView(getApplicationContext());
                            PriceView.setMinWidth(100);
                            PriceView.setMinHeight(100);
                            PriceView.setTextSize(18);
                            PriceView.setGravity(Gravity.CENTER);
                            PriceView.setTextColor(getResources().getColor(R.color.colorPrimary));
                            PriceView.setText("Total Price"  );


                            TextView amount;
                            amount = new TextView(getApplicationContext());
                            amount.setId(i);
                            amount.setMinWidth(100);
                            amount.setMinHeight(100);
                            amount.setTextSize(16);
                            amount.setGravity(Gravity.CENTER);
                            amount.setText(Double.toString(pricetotal+deliverycharge));

                            tb1.addView(PriceView,new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                            tb1.addView(amount,new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                            tb.addView(deliverychargeView,new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                            tb.addView(deliveryamount,new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                            tableLayout.addView(tb);
                            tableLayout.addView(tb1);





                            TableRow tb2 = new TableRow(getApplicationContext());

                            TextView discountamountview;
                            discountamountview= new TextView(getApplicationContext());
                            discountamountview.setMinWidth(100);
                            discountamountview.setMinHeight(100);
                            discountamountview.setTextSize(18);
                            discountamountview.setGravity(Gravity.CENTER);
                            discountamountview.setText("Discount"  );
                            discountamountview.setTextColor(getResources().getColor(R.color.colorPrimary));


                            TextView discountamount;
                            discountamount = new TextView(getApplicationContext());
                            discountamount.setId(i);
                            discountamount.setMinWidth(100);
                            discountamount.setMinHeight(100);
                            discountamount.setTextSize(16);
                            discountamount.setText("-"+discount);
                            discountamount.setGravity(Gravity.CENTER);
                            TableRow tb3 = new TableRow(getApplicationContext());

                            TextView finalamountview;
                            finalamountview= new TextView(getApplicationContext());
                            finalamountview.setMinWidth(100);
                            finalamountview.setMinHeight(100);
                            finalamountview.setTextSize(18);
                            finalamountview.setText("Final Amount"  );
                            finalamountview.setTextColor(getResources().getColor(R.color.colorPrimary));


                            TextView finalamount;
                            finalamount = new TextView(getApplicationContext());
                            finalamount.setId(i);
                            finalamount.setMinWidth(100);
                            finalamount.setMinHeight(100);
                            finalamount.setTextSize(16);
                            finalamount.setGravity(Gravity.CENTER);
                            finalamount.setText(Double.toString(finalprice));
                            tb2.addView(discountamountview,new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                            tb2.addView(discountamount,new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                            tb3.addView(finalamountview,new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                            tb3.addView(finalamount,new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                            tableLayout.addView(tb2);
                            tableLayout.addView(tb3);







                        }
                    }
                });




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });




    }


    private void checkassignment(String riderorder) {

        final DocumentReference docRef = db.collection("Orders").document(riderorder);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("fire", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("fire", "Current data: " + snapshot.getData());
                    if(Long.parseLong(snapshot.getData().get("Assigned").toString())==1)
                    {
                        Log.d("fire","Entered: " + snapshot.getData());
//                        progressDialog.dismiss();
//                        Intent intent = new Intent(VerifyActivity.this,ServicemaActivity.class);
//                        intent.putExtra("orderid",id);
//                        intent.putExtra("Assignedid",(String)snapshot.getData().get("AssignedId"));
//                        startActivity(intent);
                        fetchrider(snapshot.getData().get("AssignedId").toString());

                    }
                } else {
                    Log.d("fire", "Current data: null");
                }
            }
        });




    }

    public void fetchrider(final String aid) {

        Log.d("show", "fetchrider: "+aid);
        FirebaseDatabase.getInstance().getReference().child("Users").child(aid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("show", "onDataChange: ");
                        //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String,Object> value = (HashMap<String,Object>) dataSnapshot.getValue();

                        progressBar.setVisibility(View.GONE);
                        ridersearchtextview.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                        tableLayoutrider.setVisibility(View.VISIBLE);
                        // assert user != null;
                        Log.d("show", "onDataChange: "+value.get("Latitude"));
                        Log.d("show", "onDataChange: "+value.get("Longitude"));
                        Log.d("show", "onDataChange: "+value.get("Name"));

                        ridername.setText(value.get("Name").toString());
                        Log.d("show", "onDataChange: "+value.get("Phone"));
                        Log.d("show", "onDataChange: "+value.get("Email"));
                        riderphone.setText(value.get("Phone").toString());
                        //mail=user.Email;
                        //phone= user.Phone;
                        double riderlat= Double.parseDouble(value.get("Latitude").toString());
                        double riderlon= Double.parseDouble(value.get("Longitude").toString());

                        ratingBar.setNumStars(5);
                        ratingBar.setRating((float)4.3);

                        String name = value.get("Name").toString();
                        String phone = value.get("Phone").toString();
                        int rating = 5;
                        String id = aid;


                        placeorder(id,name,phone,riderlat,riderlon,rating,orderid);


//                        mServiceIntent = new Intent(ServicemaActivity.this, mYourService.getClass());
//                        mServiceIntent.putExtra("id",getIntent().getStringExtra("orderid"));
//
//                        if (!isMyServiceRunning(mYourService.getClass())) {
//                            startService(mServiceIntent);
//                        }

                        //}

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        Toast.makeText(getApplicationContext(),"Congrats! Here is your Zenvman",Toast.LENGTH_LONG).show();
    }

    private void placeorder(final String id, final String name, final String phone, final double riderlat, final double riderlon,final int rating,final String shoporderid) {

        ridersname = name;
        ridersid = id;
        ridersphone = phone;
        riderslatitude = riderlat;
        riderslongitude = riderlon;
        ridersrating = rating;
        Button cod = (Button)findViewById(R.id.codgrocerybtn);
        Button online = (Button)findViewById(R.id.onlinegrocerybtn);
        final ProgressDialog dialog = new ProgressDialog(ShowOrders.this);
        dialog.setTitle("Placing Your Order...");
        dialog.setMessage("Just a couple of seconds...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                DocumentReference updatepaid = db.collection("Orders").document(shoporderid);
                // Set the "isCapital" field of the city 'DC'
                updatepaid.update("PaidFull", 1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                Log.d("updatepayment", "DocumentSnapshot successfully updated!");
                                progressDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), PlacedOrderDetails.class);
                                intent.putExtra("ridername", name);
                                intent.putExtra("riderphone", phone);
                                intent.putExtra("riderlatitude", Double.toString(riderlat));
                                intent.putExtra("riderlongitude", Double.toString(riderlon));
                                intent.putExtra("riderrating", Integer.toString(rating));
                                intent.putExtra("riderid", id);
                                intent.putExtra("orderid", shoporderid);
                                intent.putExtra("paymentcategory", "COD");

                                startActivity(intent);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.w("updatepayment", "Error updating document", e);
                            }
                        });


            }


        });






        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String userid = currentUser.getUid();



                final DocumentReference docRef = db.collection("Orders").document(shoporderid);
                docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {


                        if (e != null) {
                            Log.w("fire", "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Log.d("fire", "Current data: " + snapshot.getData());
                            getinvoiceid = snapshot.getData().get("InvoiceID").toString();

                            fetchcustomeremailandcontact(progressDialog);




                        } else {
                            Log.d("fire", "Current data: null");
                        }
                    }
                });









            }

        });

    }

    public void fetchcustomeremailandcontact(final ProgressDialog custdialog)
    {
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = currentuser.getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(userid.trim())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("show", "onDataChange: ");
                        custdialog.dismiss();
                        //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String,Object> value = (HashMap<String,Object>) dataSnapshot.getValue();




                        startPayment(value.get("Email").toString(),value.get("Phone").toString());


//                        mServiceIntent = new Intent(ServicemaActivity.this, mYourService.getClass());
//                        mServiceIntent.putExtra("id",getIntent().getStringExtra("orderid"));
//
//                        if (!isMyServiceRunning(mYourService.getClass())) {
//                            startService(mServiceIntent);
//                        }

                        //}

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }



    public void startPayment(String email,String contact) {
        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */

        final Activity activity = this;
        // Razorpay razorpay = new Razorpay(activity);


        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_Y1QEmKZqSu7ONS");





        //final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "project");
            options.put("description", "Please complete the payment...");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSX-HXB4YTEstkMafwkvBn0RWD80rQAxrrp4Qy_82LhrYKGaHZ7cg");
            options.put("currency", "INR");
            options.put("payment_capture", true);

            //  String payment = pricetotal;

            double total = pricetotal+deliverycharge-discountapplied;
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", contact);

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        ProgressDialog payDialog = new ProgressDialog(ShowOrders.this);
        payDialog.setTitle("Payment Successful...Please Hold On...");
        payDialog.setMessage("Loading the quote...");
        payDialog.setCancelable(false);
        payDialog.setCanceledOnTouchOutside(false);
        payDialog.show();
        Toast.makeText(getApplicationContext(),"Payment Successful"+s,Toast.LENGTH_LONG).show();

        updatepaymentstatus(payDialog);



    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),"Payment not Successful"+s,Toast.LENGTH_LONG).show();
    }

    public  void  updatepaymentstatus(final ProgressDialog payDialog)
    {


        DocumentReference updatepaid = db.collection("Orders").document(orderid);
        // Set the "isCapital" field of the city 'DC'
        updatepaid.update("PaidFull", 1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {



                        Log.d("updatepayment", "DocumentSnapshot successfully updated!");
                        updateinvoice(payDialog);



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.w("updatepayment", "Error updating document", e);
                    }
                });


    }

    public void updateinvoice(final ProgressDialog payDialog)
    {
        DocumentReference updateinvoicestatus = db.collection("Invoice").document(getinvoiceid);

        updateinvoicestatus.update("Status", "Completed","PaymentMode","Online")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        payDialog.dismiss();


                        Log.d("updatepayment", "DocumentSnapshot successfully updated!");

                        Intent intent = new Intent(getApplicationContext(), PlacedOrderDetails.class);
                        intent.putExtra("ridername", ridersname);
                        intent.putExtra("riderphone", ridersphone);
                        intent.putExtra("riderlatitude", Double.toString(riderslatitude));
                        intent.putExtra("riderlongitude", Double.toString(riderslongitude));
                        intent.putExtra("riderrating", Integer.toString(ridersrating));
                        intent.putExtra("riderid", ridersid);
                        intent.putExtra("orderid", orderid);
                        intent.putExtra("invoiceid",getinvoiceid);
                        intent.putExtra("paymentcategory", "Online");

                        finish();

                        startActivity(intent);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.w("updatepayment", "Error updating document", e);
                    }
                });


    }

}
