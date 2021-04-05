package com.project.placesproject.tutor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.load.model.Model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.project.placesproject.MainPage;
import com.project.placesproject.R;
import com.project.placesproject.SaveDetails;
import com.project.placesproject.account_activity;
import com.project.placesproject.customercare;
import com.project.placesproject.grocery.ShowQuotation;
import com.project.placesproject.yourorderactivity;

import java.util.ArrayList;
import java.util.HashMap;

import static androidx.recyclerview.widget.RecyclerView.*;

public class DisplayActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ArrayList<DataSetFirebase> arrayList;
    String quotid;
    int endprice=1500;

    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<DataSetFirebase> options;
    private FirebaseRecyclerAdapter<DataSetFirebase,DataViewHolder> firebaseRecyclerAdapter;
    //initialize these variable



    @Override
    protected void onStart() {
        if(firebaseRecyclerAdapter!=null)
            firebaseRecyclerAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(firebaseRecyclerAdapter!=null)
            firebaseRecyclerAdapter.stopListening();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final String orderid = getIntent().getStringExtra("orderid");
        quotid = getIntent().getStringExtra("quotid");

        Log.d("quotid",quotid);

        if(quotid.isEmpty()==false)
        {
            setContentView(R.layout.activity_display);


            firebasesearch(quotid);
           // displaycards(quotid);
            Log.d("recycle","yes");

        }
        else {
            setContentView(R.layout.no_teachers);
            BottomNavigationView bottomNavigationView;
            bottomNavigationView=findViewById(R.id.navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Intent intent;
                    switch(menuItem.getItemId())
                    {
                        case R.id.recent:
                            intent = new Intent(getApplicationContext(), yourorderactivity.class);
                            startActivity(intent);
                            break;
                        case R.id.account:
                            intent = new Intent(getApplicationContext(), account_activity.class);
                            startActivity(intent);
                            break;

                        case R.id.review:
                            intent = new Intent(getApplicationContext(), customercare.class);
                            startActivity(intent);
                            break;

                    }
                    return false;
                }
            });
            Button button=findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(DisplayActivity.this, MainPage.class);
                    intent.putExtra("Name",SaveDetails.name);
                    intent.putExtra("id",SaveDetails.id);
                    finish();
                    startActivity(intent);

                }
            });

           final Intent intent = getIntent();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final DocumentReference docRef = db.collection("Orders").document(orderid);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("show", "Listen failed.", e);
                        return;
                    }


                    if (snapshot != null && snapshot.exists()) {
                        if (snapshot.getData().get("QuotationID").toString().isEmpty() == false) {
//                            sendNotification("Our professional tutors have proposed their offer.Please " +
//                                    "tap here to respond ",snapshot.getData().get("QuotationID").toString());
                            intent.putExtra("orderid", orderid);
                            intent.putExtra("quotid", snapshot.getData().get("QuotationID").toString());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            startActivity(intent);
                        }


                    } else {
                        Log.d("show", "Current data: null");
                    }
                }
            });
        }



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("Name is : ", SaveDetails.name);
        Log.d("Id is : ", SaveDetails.id);

        Intent intent =new Intent(this, MainPage.class);
        intent.putExtra("id",SaveDetails.id);
        intent.putExtra("Name",SaveDetails.name);
        finish();
        startActivity(intent);

    }
    public void displaycards(final String quotid)
    {





    }

    public void firebasesearch(String quotid)
    {



     Log.d("recycle","yes");
        Log.d("quotidrecycle",quotid);
        setContentView(R.layout.activity_display);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<DataSetFirebase>();



        int start=0;

        Query firebaseSearchQuery = FirebaseDatabase.getInstance().getReference().child("Quotations").child(quotid).orderByChild("Price");

        //set Options
        options =
                new FirebaseRecyclerOptions.Builder<DataSetFirebase>()
                        .setQuery(firebaseSearchQuery, DataSetFirebase.class)
                        .setLifecycleOwner(this)
                        .build();


       // databaseReference = FirebaseDatabase.getInstance().getReference().child("Quotations").child(quotid);
        //databaseReference.keepSynced(true);
        //options = new FirebaseRecyclerOptions.Builder<DataSetFirebase>().setQuery(databaseReference,DataSetFirebase.class).build();



    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataSetFirebase, DataViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DataViewHolder holder, int position, @NonNull final DataSetFirebase model) {
                //FirebaseRecyclerView main task where it fetching data from model
                FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
                        // String key = dataSnapshot.getKey();
                        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                        String name = value.get("Name").toString();
//                            final User user = dataSnapshot.getValue(User.class);
                        Log.d("Running", model.getUID());
                        Log.d("Running", "" + value);
//
                        holder.TeacherName.setText(name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
                holder.Card.setVisibility(View.VISIBLE);


                holder.Price.setText(Long.toString(model.getPrice()));
                holder.Payment.setText(model.getMode());

                holder.detailsbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getApplicationContext(),TutorDetails.class);
                        intent.putExtra("price",Long.toString(model.getPrice()));
                        intent.putExtra("mode",model.getMode());
                        intent.putExtra("uid",model.getUID());
                        intent.putExtra("link",model.getUri());
                        startActivity(intent);
                    }
                });



            }

            @NonNull
            @Override
            public DataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                //we need to return a object of our modelviewholder and modelview needs 2 parameter value
                //(Layout Inflater ,inflate)
                //Layout Inflater is the View Which will be replace
                //inflate which will take place
                //viewgroup means the recyclerview rows which will be replace
                //attachroot false as we dont want to show root

                return new DataViewHolder(LayoutInflater.from(DisplayActivity.this).inflate(R.layout.row,viewGroup,false));
            }
        };



        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }





    //load data into recycler view onStart





}
