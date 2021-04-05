package com.example.ServicePortal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class ViewTutorOrderActivity extends AppCompatActivity {

    String quotid,orderid,shopname,riderorderid,invid,category,orderdescriptionId,teacherId;
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

        orderid = getIntent().getStringExtra("OrderId");
        category = getIntent().getStringExtra("Category");
        orderdescriptionId = getIntent().getStringExtra("OrderDescriptionId");

        // shopaddress=getIntent().getStringExtra("shopadress");
        Log.d("showthis", ""+orderdescriptionId);


        if(category.equals("Education")){
            displayeducation(orderdescriptionId);
        }

        else
        {
           // displayother(orderdescriptionId);
        }







    }

//    public void displayother(String descid)
//    {
//        setContentView(R.layout.activity_view_tutor_order);
//
//        progressDialog = new ProgressDialog(ViewTutorOrderActivity.this);
//        progressDialog.setTitle("Please Wait...");
//        progressDialog.setMessage("Loading the Order...");
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//
//        Button invoicebtn = (Button) findViewById(R.id.invoicebtn);
//        invoicebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),InvoiceDetails.class);
//                intent.putExtra("InvoiceId",invid);
//                startActivity(intent);
//            }
//        });
//
//
//        FirebaseDatabase.getInstance().getReference().child("Users").child(teacherId.trim()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                HashMap<String,Object> value = (HashMap<String,Object>)dataSnapshot.getValue();
//
//                Log.d("tutors",teacherId);
//                TextView teachername = (TextView)findViewById(R.id.assteachername);
//                teachername.setText(value.get("Name").toString());
//                TextView teachercontact = (TextView)findViewById(R.id.teachercontactnumber);
//                teachercontact.setText(value.get("Phone").toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//
//
//        FirebaseDatabase.getInstance().getReference().child("OrderDescription").child(descid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                progressDialog.dismiss();
//
//                // slideUpDown();
//
//                TextView tutororderid = (TextView)findViewById(R.id.tutororderid);
//                tutororderid.setText(orderid);
//
//                TextView studentlevel = (TextView)findViewById(R.id.studentlevel);
//                TextView description = (TextView) findViewById(R.id.edittextdescripiton);
//
//
//
//
//
//                TextView preferredprice =  (TextView)findViewById(R.id.preferredprice);
//                TextView preferredmode = (TextView)findViewById(R.id.preferredmode);
//
//
////                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
////                        final User user = dataSnapshot.getValue(User.class);
//
//                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
//
//                HashMap<String,Object> pricethings = (HashMap<String,Object>)value.get("Preferred Price");
//
////                     preferredprice.setText(pricethings.get("Price").toString());
////                preferredmode.setText(pricethings.get("Type").toString());
//                studentlevel.setText(value.get("Level").toString());
//
//
//
//
//                description.setText(value.get("Description").toString());
//
//
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });
//
//
//    }

    public void displayeducation(String descid)
    {

        setContentView(R.layout.activity_view_tutor_order);

        progressDialog = new ProgressDialog(ViewTutorOrderActivity.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Loading the Order...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();








        FirebaseDatabase.getInstance().getReference().child("OrderDescription").child(descid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressDialog.dismiss();

                // slideUpDown();

                TextView tutororderid = (TextView)findViewById(R.id.tutororderid);
                tutororderid.setText(orderid);

                TextView studentboard = (TextView)findViewById(R.id.studentboard);
                TextView studentclass = (TextView) findViewById(R.id.studentclass);
                TextView studentexam = (TextView)findViewById(R.id.studentexams);

                TableRow rowclass,rowboard,rowcompetitive;



                rowboard = (TableRow)findViewById(R.id.rowboard);
                rowclass = (TableRow) findViewById(R.id.rowclass);
                rowcompetitive = (TableRow) findViewById(R.id.rowcompetitive);



//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                        final User user = dataSnapshot.getValue(User.class);

                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();



                if(value.get("Category").toString().equals("NonCompetitive")){
                    rowboard.setVisibility(View.VISIBLE);
                    rowclass.setVisibility(View.VISIBLE);
                    studentboard.setText(value.get("board").toString());
                    studentclass.setText(value.get("class").toString());


                }
                else if(value.get("Category").toString().equals("Competitive")){

                    rowcompetitive.setVisibility(View.VISIBLE);

                    studentexam.setText(value.get("Exams").toString());
                }

                HashMap<String,Object> subjects = (HashMap<String,Object>)value.get("Subjects");
                Iterator hmIterator = subjects.entrySet().iterator();


                int i=1;
                while (hmIterator.hasNext()) {
                    Map.Entry mapElement = (Map.Entry)hmIterator.next();
                    tableLayout = (TableLayout) findViewById(R.id.displayitemslayout);
//                        Iterator itemiterator = items.entrySet().iterator();
//                        while(itemiterator.hasNext())
//                        {
//                            Map.Entry itemElement = (Map.Entry) hmIterator.next();
////                    int marks = ((int)mapElement.getValue() + 10);
//                            HashMap<String, Object> itemsdesc = (HashMap<String, Object>) itemElement.getValue();
                    TableRow tb = new TableRow(getApplicationContext());

                    TextView serialno;
                    serialno = new TextView(getApplicationContext());
                    serialno.setMinWidth(100);
                    serialno.setMinHeight(100);
                    serialno.setTextSize(16);
                    serialno.setGravity(Gravity.CENTER);
                    serialno.setText(i+")");


                    TextView subjectname;
                    subjectname = new TextView(getApplicationContext());
                    subjectname.setMinWidth(100);
                    subjectname.setMinHeight(100);
                    subjectname.setTextSize(16);
                    subjectname.setGravity(Gravity.CENTER);
                    subjectname.setText("" + mapElement.getKey());



                    i++;



                    tb.addView(serialno, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                    tb.addView(subjectname, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33f));
                    tableLayout.addView(tb);
                    Log.d("items", "" + mapElement);




                }







            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

}


