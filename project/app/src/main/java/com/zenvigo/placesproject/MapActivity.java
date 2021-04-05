package com.project.placesproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import com.project.placesproject.driver.*;
import com.project.placesproject.tutor.*;
import com.project.placesproject.grocery.*;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    Vector lat=new Vector();
    Vector longe= new Vector ();
    String categoryservice;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;

    private MaterialSearchBar materialSearchBar;
    private View mapView;
    private Button btnFind,btnfind1;
    private RippleBackground rippleBg;
    Geocoder geocoder;
    List<Address> addresses;
    ArrayList<String> shopname,shopadress,shopid;
    String useradress;

    private final float DEFAULT_ZOOM = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        shopname=new ArrayList<>();
        shopadress=new ArrayList<>();
        shopid=new ArrayList<>();
        categoryservice=getIntent().getStringExtra("CategoryService");
        Log.d("show", "onCreate: "+categoryservice);
        FirebaseDatabase.getInstance().getReference().child("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("show", "onDataChange: ");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // User user = snapshot.getValue(User.class);
                            Map<String, Object> user = (Map<String, Object>) snapshot.getValue();
                            Log.d("show", "Value is: " + user);

                            // assert user != null;
                            Object object = user.get("Category");
                            if (object.getClass().equals(String.class)){
                                String category= user.get("Category").toString();
                                if (category.equals("Mechanic")&&categoryservice.equals("instantmechanic")) {
                                   // Long l1=new Long((String) user.get("Latitude"));
                                    //Object o=l1;
                                    double latitude = (double)user.get("Latitude");
                                    Log.d("show", "onDataChange: "+latitude);

                                    lat.add(latitude);
                                   // Long l2=new Long((String) user.get("Longitude"));
                                    //Object o1=l2;
                                    double longitude = (double)user.get("Longitude");
                                    Log.d("show", "onDataChange: "+longitude);
                                    longe.add(longitude);

                                }
                               else if(category.equals("General Stores")&&categoryservice.equals("General Stores")){
                                    double latitude = (double)user.get("Latitude");
                                    Log.d("show", "onDataChange: "+latitude);

                                    lat.add(latitude);
                                    // Long l2=new Long((String) user.get("Longitude"));
                                    //Object o1=l2;
                                    double longitude = (double)user.get("Longitude");
                                    Log.d("show", "onDataChange: "+longitude);
                                    longe.add(longitude);
                                    shopid.add(snapshot.getKey());
                                    Log.d("show", "onDataChange: "+snapshot.getKey());
                                    shopname.add(user.get("ShopName").toString());
                                    shopadress.add(user.get("Address").toString());

                                }
                               else if (category.equals("butcher")&&categoryservice.equals("bucher")){
                                    double latitude = (double)user.get("Latitude");
                                    Log.d("show", "onDataChange: "+latitude);

                                    lat.add(latitude);
                                    // Long l2=new Long((String) user.get("Longitude"));
                                    //Object o1=l2;
                                    double longitude = (double)user.get("Longitude");
                                    Log.d("show", "onDataChange: "+longitude);
                                    shopid.add(snapshot.getKey());

                                    longe.add(longitude);
                                    shopname.add(user.get("ShopName").toString());
                                    shopadress.add(user.get("Address").toString());

                                }
                                else if (category.equals("Stationery")&&categoryservice.equals("station")){
                                    double latitude = (double)user.get("Latitude");
                                    Log.d("show", "onDataChange: "+latitude);

                                    lat.add(latitude);
                                    shopid.add(snapshot.getKey());

                                    // Long l2=new Long((String) user.get("Longitude"));
                                    //Object o1=l2;
                                    double longitude = (double)user.get("Longitude");
                                    Log.d("show", "onDataChange: "+longitude);

                                    longe.add(longitude);
                                    shopname.add(user.get("ShopName").toString());
                                    shopadress.add(user.get("Address").toString());

                                }
                                else if (category.equals("grocery")&&categoryservice.equals("grocery")){
                                    double latitude = (double)user.get("Latitude");
                                    Log.d("show", "onDataChange: "+latitude);

                                    lat.add(latitude);
                                    shopid.add(snapshot.getKey());

                                    // Long l2=new Long((String) user.get("Longitude"));
                                    //Object o1=l2;
                                    double longitude = (double)user.get("Longitude");
                                    Log.d("show", "onDataChange: "+longitude);

                                    longe.add(longitude);
                                    shopname.add(user.get("ShopName").toString());
                                    shopadress.add(user.get("Address").toString());

                                }


                            }
                            else {
                                Map<String,Object> cat =(Map<String, Object>)user.get("Category");
                                String ctype= cat.get("Type").toString();
                                if (ctype.equals("Teacher")){
                                    //TO DO
                                }

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        materialSearchBar = findViewById(R.id.searchBar);
        btnFind = (Button) findViewById(R.id.btn_find);
        btnfind1= (Button) findViewById(R.id.btn_find1);
        rippleBg = findViewById(R.id.ripple_bg);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);
        Places.initialize(MapActivity.this, getString(R.string.google_maps_api));
        placesClient = Places.createClient(this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {

            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(), true, null, true);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                    //opening or closing a navigation drawer
                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    materialSearchBar.disableSearch();
                }
            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if (task.isSuccessful()) {
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            Log.d("autocomplete",""+predictionsResponse);
                            if (predictionsResponse != null) {
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionsList = new ArrayList<>();
                                for (int i = 0; i < predictionList.size(); i++) {
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionsList.add(prediction.getFullText(null).toString());
                                }
                                materialSearchBar.updateLastSuggestions(suggestionsList);
                                if (!materialSearchBar.isSuggestionsVisible()) {
                                    materialSearchBar.showSuggestionsList();
                                }
                            }
                        } else {
                            Log.i("mytag", "prediction fetching task unsuccessful");
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                if (position >= predictionList.size()) {
                    return;
                }
                AutocompletePrediction selectedPrediction = predictionList.get(position);
                String suggestion = materialSearchBar.getLastSuggestions().get(position).toString();
                materialSearchBar.setText(suggestion);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialSearchBar.clearSuggestions();
                    }
                }, 1000);
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                final String placeId = selectedPrediction.getPlaceId();
                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);

                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        Place place = fetchPlaceResponse.getPlace();
                        Log.i("mytag", "Place found: " + place.getName());
                        LatLng latLngOfPlace = place.getLatLng();
                        if (latLngOfPlace != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, DEFAULT_ZOOM));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            apiException.printStackTrace();
                            int statusCode = apiException.getStatusCode();
                            Log.i("mytag", "place not found: " + e.getMessage());
                            Log.i("mytag", "status code: " + statusCode);
                        }
                    }
                });
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final  LatLng currentMarkerLocation = mMap.getCameraPosition().target;
                geocoder = new Geocoder(MapActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(currentMarkerLocation.latitude, currentMarkerLocation.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }


                String id=getIntent().getStringExtra("uid").trim();
                FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Latitude").setValue(currentMarkerLocation.latitude);
                FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Longitude").setValue(currentMarkerLocation.longitude);
                rippleBg.startRippleAnimation();
                if(categoryservice.equals("tutor")){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("show", "run: ");
                        rippleBg.stopRippleAnimation();
                        showlocation(currentMarkerLocation);
                        btnfind1.setVisibility(View.VISIBLE);
                        btnfind1.setEnabled(true);
                    }
                }, 2000);

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    String CategoryService=getIntent().getStringExtra("CategoryService");
                    Log.d("show", "onClick: "+ CategoryService);
                    useradress=address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ";
                Log.d("show", "onClick: ");
                Log.d("data", "onClick: "+ address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ");
                    Toast.makeText(getApplicationContext(),useradress,Toast.LENGTH_LONG).show();
               // btnfind1.setEnabled(true);
               // btnFind.setVisibility(View.INVISIBLE);
                }
                else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("show", "run: ");
                            rippleBg.stopRippleAnimation();
                            showlocation(currentMarkerLocation);
                            btnfind1.setVisibility(View.VISIBLE);
                            btnfind1.setEnabled(true);
                        }
                    }, 2000);
                    Log.d("show", "onClick: ");
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    String CategoryService=getIntent().getStringExtra("CategoryService");
                    Log.d("show", "onClick: "+ CategoryService);
                    useradress=address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ";
                    Log.d("data", "onClick: "+ address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ");
                    Toast.makeText(getApplicationContext(),useradress,Toast.LENGTH_LONG).show();
                    // btnfind1.setEnabled(true);
                   // btnFind.setVisibility(View.INVISIBLE);
                }



            }
        });
        btnfind1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("show","entered here");
                final  LatLng currentMarkerLocation = mMap.getCameraPosition().target;
                geocoder = new Geocoder(MapActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(currentMarkerLocation.latitude, currentMarkerLocation.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    SaveDetails.Latitude = Double.toString(currentMarkerLocation.latitude);
                    SaveDetails.Longitude = Double.toString(currentMarkerLocation.longitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                String CategoryService=getIntent().getStringExtra("CategoryService");
                Log.d("show", "onClick: "+ CategoryService);
                useradress=address;
                Log.d("data", "onClick: "+ address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ");
                if(CategoryService.equals("instantmechanic")){
                Intent intent = new Intent(MapActivity.this,DescriptionActivity.class);
                Log.d("data", "onClick: "+ address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ");

                intent.putExtra("latitude",Double.toString(currentMarkerLocation.latitude));
                intent.putExtra("longitude",Double.toString(currentMarkerLocation.longitude));
                intent.putExtra("uid",getIntent().getStringExtra("uid"));
                startActivity(intent);
                //finish();
                }
                else if(CategoryService.equals("examinevehicle")){
                    Intent intent = new Intent(MapActivity.this,DescriptionActivity.class);
                    Log.d("data", "onClick: "+ address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ");

                    intent.putExtra("latitude",Double.toString(currentMarkerLocation.latitude));
                    intent.putExtra("longitude",Double.toString(currentMarkerLocation.longitude));
                    intent.putExtra("uid",getIntent().getStringExtra("uid"));
                    startActivity(intent);
                   // finish();
                }
                else if(CategoryService.equals("modifyvehicle")){
                    Intent intent = new Intent(MapActivity.this,DescriptionActivity.class);
                    Log.d("data", "onClick: "+ address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ");

                    intent.putExtra("latitude",Double.toString(currentMarkerLocation.latitude));
                    intent.putExtra("longitude",Double.toString(currentMarkerLocation.longitude));
                    intent.putExtra("uid",getIntent().getStringExtra("uid"));
                    startActivity(intent);
                //    finish();
                }
                else if(CategoryService.equals("servicevehicle")){
                    Intent intent = new Intent(MapActivity.this,MechanicpackageActivity.class);
                    Log.d("data", "onClick: "+ address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ");

                    intent.putExtra("latitude",Double.toString(currentMarkerLocation.latitude));
                    intent.putExtra("longitude",Double.toString(currentMarkerLocation.longitude));
                    intent.putExtra("uid",getIntent().getStringExtra("uid"));
                    startActivity(intent);
                    finish();}
                else if(CategoryService.equals("driver")){
                    Intent intent = new Intent(MapActivity.this,PackagedriverActivity.class);
                    Log.d("data", "onClick: "+ address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ");

                    intent.putExtra("latitude",Double.toString(currentMarkerLocation.latitude));
                    intent.putExtra("longitude",Double.toString(currentMarkerLocation.longitude));
                    intent.putExtra("uid",getIntent().getStringExtra("uid"));
                    startActivity(intent);
                    finish();}
                else if(CategoryService.equals("tutor")){
                    Intent intent = new Intent(MapActivity.this,tutorall.class);
                    Log.d("data", "onClick: "+ address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ");

                    intent.putExtra("latitude",Double.toString(currentMarkerLocation.latitude));
                    intent.putExtra("longitude",Double.toString(currentMarkerLocation.longitude));
                    intent.putExtra("uid",getIntent().getStringExtra("uid"));
                    intent.putExtra("useradress",useradress);

                    startActivity(intent);
                //    finish();
                }
                else if(CategoryService.equals("grocery")){
                    Intent intent = new Intent(MapActivity.this,grocerydisplay.class);
                    Log.d("data", "onClick: "+ address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ");

                    intent.putExtra("latitude",Double.toString(currentMarkerLocation.latitude));
                    intent.putExtra("longitude",Double.toString(currentMarkerLocation.longitude));
                    intent.putExtra("uid",getIntent().getStringExtra("uid"));
                    intent.putExtra("Category",CategoryService);
                    intent.putStringArrayListExtra("shopid",shopid);
                    intent.putStringArrayListExtra("shopname",shopname);
                    intent.putStringArrayListExtra("shopadress",shopadress);
                    intent.putExtra("useradress",useradress);
                    startActivity(intent);
                  //  finish();
                      }
                else if(CategoryService.equals("bucher")){
                    Intent intent = new Intent(MapActivity.this,grocerydisplay.class);
                    Log.d("data", "onClick: "+ address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ");

                    intent.putExtra("latitude",Double.toString(currentMarkerLocation.latitude));
                    intent.putExtra("longitude",Double.toString(currentMarkerLocation.longitude));
                    intent.putExtra("uid",getIntent().getStringExtra("uid"));
                    intent.putExtra("Category",CategoryService);

                    intent.putStringArrayListExtra("shopid",shopid);
                    intent.putStringArrayListExtra("shopname",shopname);
                    intent.putStringArrayListExtra("shopadress",shopadress);
                    intent.putExtra("useradress",useradress);

                    startActivity(intent);
                //    finish();
                }


                else if(CategoryService.equals("General Stores")){
                    Intent intent = new Intent(MapActivity.this,grocerydisplay.class);
                    Log.d("data", "onClick: "+ address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ");

                    intent.putExtra("latitude",Double.toString(currentMarkerLocation.latitude));
                    intent.putExtra("longitude",Double.toString(currentMarkerLocation.longitude));
                    intent.putExtra("Category",CategoryService);

                    intent.putExtra("uid",getIntent().getStringExtra("uid"));
                    intent.putStringArrayListExtra("shopname",shopname);
                    intent.putStringArrayListExtra("shopid",shopid);
                    intent.putStringArrayListExtra("shopadress",shopadress);
                    intent.putExtra("useradress",useradress);

                    startActivity(intent);
                //    finish();
                }
                else if(CategoryService.equals("station")){
                    Intent intent = new Intent(MapActivity.this,grocerydisplay.class);
                    Log.d("data", "onClick: "+ address+" "+city+" "+ state + " "+ country+ " "+postalCode+" "+knownName+" ");

                    intent.putExtra("latitude",Double.toString(currentMarkerLocation.latitude));
                    intent.putExtra("longitude",Double.toString(currentMarkerLocation.longitude));
                    intent.putExtra("Category",CategoryService);

                    intent.putExtra("uid",getIntent().getStringExtra("uid"));
                    intent.putStringArrayListExtra("shopname",shopname);
                    intent.putStringArrayListExtra("shopid",shopid);
                    intent.putStringArrayListExtra("shopadress",shopadress);
                    intent.putExtra("useradress",useradress);

                    startActivity(intent);
                //    finish();
                }
            }
        });
    }
    public void showlocation (LatLng location){
        Log.d("show", "showlocation: ");
        if(categoryservice.equals("instantmechanic")){
        for (int i=0;i<lat.size();i++){
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng((double)lat.get(i),(double)longe.get(i)))
                    .anchor(0.5f, 0.5f)
                    .title("Title"+i)
                    .snippet("Snippet1").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)));}

        }
        else if(categoryservice.equals("grocery")){
            for (int i=0;i<lat.size();i++){
                Marker m = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng((double)lat.get(i),(double)longe.get(i)))
                        .anchor(0.5f, 0.5f)
                        .title(shopname.get(i))
                        .snippet("Snippet1").draggable(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.icongrocery)));}

        }

    }
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("show", "onMapReady: ");
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }

        //check if gps is enabled or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(MapActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(MapActivity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (materialSearchBar.isSuggestionsVisible())
                    materialSearchBar.clearSuggestions();
                if (materialSearchBar.isSearchEnabled())
                    materialSearchBar.disableSearch();
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(MapActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
