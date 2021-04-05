package com.project.placesproject.tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.placesproject.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ShowTutorUpcoming extends AppCompatActivity {

    String quotid,orderid,shopname,riderorderid,invid,category,orderdescriptionId;
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
        category = getIntent().getStringExtra("Category");
        orderdescriptionId = getIntent().getStringExtra("OrderDescriptionId");

        // shopaddress=getIntent().getStringExtra("shopadress");
        Log.d("show", "onCreate: "+orderid);


        if(category.equals("Education")){
            displayeducation(orderdescriptionId);
        }

        else
        {

            displayother(orderdescriptionId);
        }







    }

    public void displayother(String descid)
    {
        setContentView((R.layout.activity_show_othertutorsupcoming));




        progressDialog = new ProgressDialog(ShowTutorUpcoming.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Loading your Order...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        TextView pending = (TextView) findViewById(R.id.pending);

        Button seeexpertquotes = (Button)findViewById(R.id.seeexpertquotes);
        if(quotid.isEmpty() == false)
        {
            pending.setVisibility(View.VISIBLE);
            seeexpertquotes.setEnabled(true);

        }
        else
        {
            seeexpertquotes.setEnabled(false);
        }

        seeexpertquotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DisplayActivity.class);
                intent.putExtra("orderid",orderid);
                intent.putExtra("quotid",quotid);
                startActivity(intent);
            }
        });



        FirebaseDatabase.getInstance().getReference().child("OrderDescription").child(descid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressDialog.dismiss();

                // slideUpDown();

                TextView tutororderid = (TextView)findViewById(R.id.tutororderid);
                tutororderid.setText(orderid);

                TextView studentlevel = (TextView)findViewById(R.id.studentlevel);
                TextView description = (TextView) findViewById(R.id.edittextdescripiton);





                TextView preferredprice =  (TextView)findViewById(R.id.preferredprice);
                TextView preferredmode = (TextView)findViewById(R.id.preferredmode);


//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                        final User user = dataSnapshot.getValue(User.class);

                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

                Log.d("tutors",""+value);

                HashMap<String,Object> pricethings = (HashMap<String,Object>)value.get("Preferred Price");


                preferredprice.setText(pricethings.get("Price").toString());
                preferredmode.setText(pricethings.get("Type").toString());

                studentlevel.setText(value.get("Level").toString());




               description.setText(value.get("Description").toString());




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



    }

    public void displayeducation(String descid)
    {

        setContentView(R.layout.activity_show_tutor_upcoming);

        progressDialog = new ProgressDialog(ShowTutorUpcoming.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Loading your Order...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        TextView pending = (TextView) findViewById(R.id.pending);

        Button seeexpertquotes = (Button)findViewById(R.id.seeexpertquotes);
        if(quotid.isEmpty() == false)
        {
            pending.setVisibility(View.VISIBLE);
            seeexpertquotes.setEnabled(true);

        }
        else
        {
            seeexpertquotes.setEnabled(false);
        }

        seeexpertquotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DisplayActivity.class);
                intent.putExtra("orderid",orderid);
                intent.putExtra("quotid",quotid);
                startActivity(intent);
            }
        });



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
                TextView preferredprice =  (TextView)findViewById(R.id.preferredprice);
                TextView preferredmode = (TextView)findViewById(R.id.preferredmode);


//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                        final User user = dataSnapshot.getValue(User.class);

                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

                HashMap<String,Object> pricethings = (HashMap<String,Object>)value.get("Preferred Price");


                preferredprice.setText(pricethings.get("Price").toString());
                preferredmode.setText(pricethings.get("Type").toString());


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

