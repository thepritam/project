package com.project.placesproject.grocery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import com.project.placesproject.tutor.DataSetFirebase;
import com.project.placesproject.tutor.DataViewHolder;
import com.project.placesproject.tutor.DisplayActivity;
import com.project.placesproject.tutor.TutorDetails;
import com.project.placesproject.yourorderactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class grocerydisplay extends AppCompatActivity  {
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;
    List<Data> data;
    Recycler_View_Adapter adapter;
    SearchView searchView;
    MenuItem searchItem;
    LinearLayout linearLayout;
    TextView textViewadress,textViewlocation;
    EditText editTextSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocerydisplay);
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
         data = fill_with_data();
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        shimmerFrameLayout=findViewById(R.id.shimmer_view_container);
        textViewadress=findViewById(R.id.addressshow);
        textViewadress.setText(getIntent().getStringExtra("useradress"));
        textViewadress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Please Select Your Location",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

       linearLayout=findViewById(R.id.bottomnavshimmer);
       initCountDownTimer(2000);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = new Recycler_View_Adapter(data, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.addOnItemTouchListener(new CustomRVItemTouchListener(this, recyclerView, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
              Intent intent = new Intent(getApplicationContext(),groceryall.class);

              intent.putExtra("shopid",getIntent().getStringArrayListExtra("shopid").get(position));
                Log.d("show", "onClick: "+getIntent().getStringArrayListExtra("shopid").get(position));
                intent.putExtra("shopname",getIntent().getStringArrayListExtra("shopname").get(position));
                intent.putExtra("shopadress",getIntent().getStringArrayListExtra("shopadress"));
                intent.putExtra("latitude",getIntent().getStringExtra("latitude"));
                intent.putExtra("longitude",getIntent().getStringExtra("longitude"));
                Log.d("show", "onClick: +user adress"+ getIntent().getStringExtra("latitude")+ " "+getIntent().getStringExtra("longitude") );
                intent.putExtra("uid",getIntent().getStringExtra("uid"));
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
               // filter(editable.toString());
                adapter.getFilter().filter(editable.toString());
            }
        });

    }
    public List<Data> fill_with_data() {

        List<Data> data = new ArrayList<>();
        ArrayList<String> shopnames= getIntent().getStringArrayListExtra("shopname");
        ArrayList<String> shopadress=getIntent().getStringArrayListExtra("shopadress");
        for (int i=0;i<shopnames.size();i++){
            if(getIntent().getStringExtra("Category").equals("bucher"))
            data.add(new Data(shopnames.get(i),shopadress.get(i),"Daily Home Products "));
            else if(getIntent().getStringExtra("Category").equals("General Stores"))
            {
                data.add(new Data(shopnames.get(i),shopadress.get(i),"Fresh Chicken, Mutton"));


            }

            else if(getIntent().getStringExtra("Category").equals("grocery"))
            {
                data.add(new Data(shopnames.get(i),shopadress.get(i),"Fruits, Vegetables"));


            }
            else if(getIntent().getStringExtra("Category").equals("station"))
            {
                data.add(new Data(shopnames.get(i),shopadress.get(i),"Stationary Items"));


            }
        }



        return data;
    }
    public interface RecyclerViewItemClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }
    public void initCountDownTimer(int time) {
        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.d("show", "onTick: seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Log.d("show", "onFinish: done!");
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                editTextSearch.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);

              //  searchView.setVisibility(View.VISIBLE);
                //searchItem.setVisible(true);
                editTextSearch.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        }.start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Enter Shop name");
        searchView.setVisibility(View.INVISIBLE);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

}
