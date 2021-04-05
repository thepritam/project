package com.example.ServicePortal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private static final int PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

//        FetchValues fetchAvail = new FetchValues(this);
//
//        fetchAvail.execute();

//        Toast.makeText(getApplicationContext(), CheckAvailability.availcheck, Toast.LENGTH_LONG).show();

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_LONG).show();
            finish();
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startAvailService();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }

        Intent intent = getIntent();
        String availcheck = intent.getStringExtra("availcheck");
        String id = intent.getStringExtra("id");
        double lat = intent.getDoubleExtra("lat",0.0);
        double longi = intent.getDoubleExtra("longi",0.0);
        String Category = intent.getStringExtra("Category");
        CheckAvailability.availcheck = availcheck;
        CheckAvailability.id = id;
        CheckAvailability.lat = lat;
        CheckAvailability.longi = longi;
        CheckAvailability.Category = Category;

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Category").child("Preferences").child("Class").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String,String>> value = (HashMap<String, HashMap<String,String>>) dataSnapshot.getValue();

                CheckAvailability.klass = value;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    private void startAvailService() {
        Intent serviceIntent = new Intent(getApplicationContext(),AvailService.class);
        getApplicationContext().startService(serviceIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            startAvailService();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                finish();
                startActivity(intent);


                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Log.d("insupport","yes");
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

//    public String checkAvail() {
//        FirebaseDatabase.getInstance().getReference().child("Users").child("mESPhuNHspPBq7THqF9Zfigyhqt2").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                User user = dataSnapshot.getValue(User.class);
//                availcheck = user.IsAvailable;
////                Toast.makeText(getApplicationContext(),availcheck,Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });
//        return availcheck;
//    }
}

//     class FetchValues extends AsyncTask<Void,Void,Void>
//{
//
//    public String availcheck;
//    public MainActivity activity;
//
//    FetchValues(MainActivity activity)
//    {
//        this.activity = activity;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//
//    }
//    @Override
//    public Void doInBackground(Void... params) {
//        FirebaseDatabase.getInstance().getReference().child("Users").child("mESPhuNHspPBq7THqF9Zfigyhqt2").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                User user = dataSnapshot.getValue(User.class);
//                availcheck = user.IsAvailable;
//                Log.d("checkthis:",availcheck);
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
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void result) {
//
//            CheckAvailability.availcheck = availcheck;
//        Toast.makeText(activity,"OCCUR OF main",Toast.LENGTH_LONG).show();
//
//        }
//    }

