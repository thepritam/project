package com.project.placesproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
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
import com.project.placesproject.grocery.PlacedOrderDetails;
import com.project.placesproject.grocery.ShowCancelledOrder;
import com.project.placesproject.grocery.ShowOrders;
import com.project.placesproject.grocery.ShowQuotation;
import com.project.placesproject.tutor.TutorDetails;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static java.text.DateFormat.getDateTimeInstance;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpcomingOrders.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpcomingOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CancelledOrders extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ArrayList<UpcomingOrderFirebase> arrayList;

    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<UpcomingOrderFirebase> options;
    private FirebaseRecyclerAdapter<UpcomingOrderFirebase, UpcomingOrdersViewHolder> firebaseRecyclerAdapter;
    //initialize these variable


    @Override
    public void onStart() {
        if (firebaseRecyclerAdapter != null)
            firebaseRecyclerAdapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        if (firebaseRecyclerAdapter != null)
            firebaseRecyclerAdapter.stopListening();
        super.onStop();
    }

    public CancelledOrders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingOrders.
     */
    // TODO: Rename and change types and number of parameters
    public static CancelledOrders newInstance(String param1, String param2) {
        CancelledOrders fragment = new CancelledOrders();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_cancelled_orders, container, false);


        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        arrayList = new ArrayList<UpcomingOrderFirebase>();

        final String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Orders").child("grocery").orderByChild("timestamp").getRef();
        databaseReference.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<UpcomingOrderFirebase>().setQuery(databaseReference, UpcomingOrderFirebase.class).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UpcomingOrderFirebase, UpcomingOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final UpcomingOrdersViewHolder holder, int position, @NonNull final UpcomingOrderFirebase model) {
                //FirebaseRecyclerView main task where it fetching data from model


                DatabaseReference mDatabaseReference = firebaseRecyclerAdapter.getRef(position);
                final String post_key = mDatabaseReference.getKey();

                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                if(model.getCancelled() == 1 && model.getRiderOrder().isEmpty()==false) {


                    ViewGroup.LayoutParams params = holder.Card.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;

                    holder.Card.setLayoutParams(params);




                    holder.Card.setVisibility(View.VISIBLE);
                    holder.ShopName.setText(model.getShopName());
                    holder.Price.setText(model.getPrice());

                    String date = getTimeDate(model.getTimestamp());
                    holder.OrderedOn.setText(date);

                    Log.d("OrderId", model.getOrderId());

                    holder.detailsbtn.setOnClickListener(new View.OnClickListener() {

                                                             @Override
                                                             public void onClick(View v) {


                                                                 final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                                                 progressDialog.setTitle("Please Wait...");
                                                                 progressDialog.setMessage("Please wait while we fetch the details...");
                                                                 progressDialog.setCancelable(false);
                                                                 progressDialog.setCanceledOnTouchOutside(false);


                                                                 final DocumentReference docRef = db.collection("Orders").document(model.getRiderOrder());
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
                                                                             if (Long.parseLong(snapshot.getData().get("Assigned").toString()) == 1) {
                                                                                 Log.d("fire", "Entered: " + snapshot.getData());

                                                                                 fetchrider(snapshot.getData().get("AssignedId").toString(), progressDialog);

                                                                             } else {
                                                                                 final double lat = Double.parseDouble(snapshot.getData().get("PickupLatitude").toString());
                                                                                 final double lon = Double.parseDouble(snapshot.getData().get("PickupLongitude").toString());


                                                                                 final DocumentReference docRef = db.collection("Orders").document(model.getOrderId());
                                                                                 docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                                                     @Override
                                                                                     public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                                                                         @Nullable FirebaseFirestoreException e) {

                                                                                         Log.e("firestore", "Entered here!");
                                                                                         if (e != null) {
                                                                                             Log.w("fire", "Listen failed.", e);
                                                                                             return;
                                                                                         }

                                                                                         if (snapshot != null && snapshot.exists()) {
                                                                                             Log.d("fire", "Current data: " + snapshot.getData());

                                                                                             final String quot = snapshot.getData().get("QuotationID").toString();
                                                                                             final String invoice = snapshot.getData().get("InvoiceID").toString();


                                                                                             //  holder.Card.setVisibility(View.VISIBLE);


                                                                                             progressDialog.dismiss();

                                                                                             Intent intent = new Intent(getActivity(), ShowCancelledOrder.class);
                                                                                             intent.putExtra("Latitude", Double.toString(lat));
                                                                                             intent.putExtra("Longitude", Double.toString(lon));
                                                                                             intent.putExtra("RiderOrder", model.getRiderOrder());
                                                                                             intent.putExtra("ShopName", model.getShopName());
                                                                                             intent.putExtra("Quotation", quot);
                                                                                             intent.putExtra("GroceryInUser", post_key);
                                                                                             intent.putExtra("OrderId", model.getOrderId());
                                                                                             intent.putExtra("InvoiceId", invoice);

                                                                                             startActivity(intent);


                                                                                         }


                                                                                         //Log.d("fire", "Current data: null");
                                                                                     }
                                                                                 });


                                                                             }
                                                                         }
                                                                     }
                                                                 });


                                                             }
                                                         }
                    );

                }
                else
                {

                    ViewGroup.LayoutParams params = holder.Card.getLayoutParams();
                    params.height = 0;
                    params.width = 0;

                    holder.Card.setLayoutParams(params);


                    holder.Card.setVisibility(View.GONE);

                }

            }

            @NonNull
            @Override
            public UpcomingOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                //we need to return a object of our modelviewholder and modelview needs 2 parameter value
                //(Layout Inflater ,inflate)
                //Layout Inflater is the View Which will be replace
                //inflate which will take place
                //viewgroup means the recyclerview rows which will be replace
                //attachroot false as we dont want to show root

                return new UpcomingOrdersViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.upcoming, viewGroup, false));
            }
        };


        recyclerView.setAdapter(firebaseRecyclerAdapter);

        return root;

    }

    public void fetchrider(final String aid, final ProgressDialog progressDialog) {

        Log.d("show", "fetchrider: " + aid);
        FirebaseDatabase.getInstance().getReference().child("Users").child(aid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("show", "onDataChange: ");
                        //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();


                        Log.d("show", "onDataChange: " + value.get("Latitude"));
                        Log.d("show", "onDataChange: " + value.get("Longitude"));
                        Log.d("show", "onDataChange: " + value.get("Name"));


                        Log.d("show", "onDataChange: " + value.get("Phone"));
                        Log.d("show", "onDataChange: " + value.get("Email"));

                        //mail=user.Email;
                        //phone= user.Phone;
                        double riderlat = Double.parseDouble(value.get("Latitude").toString());
                        double riderlon = Double.parseDouble(value.get("Longitude").toString());



                        String name = value.get("Name").toString();
                        String phone = value.get("Phone").toString();
                        int rating = 5;
                        String id = aid;


                        placeorder(id, name, phone, riderlat, riderlon, rating,progressDialog);


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


        Toast.makeText(getActivity(), "Congrats! Here is your Zenvman", Toast.LENGTH_LONG).show();
    }

    private void placeorder(final String id, final String name, final String phone, final double riderlat, final double riderlon, final int rating,ProgressDialog progressDialog) {


        progressDialog.dismiss();


        Intent intent = new Intent(getActivity(), PlacedOrderDetails.class);
        intent.putExtra("ridername", name);
        intent.putExtra("riderphone", phone);
        intent.putExtra("riderlatitude", Double.toString(riderlat));
        intent.putExtra("riderlongitude", Double.toString(riderlon));
        intent.putExtra("riderrating", Integer.toString(rating));
        intent.putExtra("riderid", id);
        intent.putExtra("paymentcategory", "COD");

        startActivity(intent);

    }

    public static String getTimeDate(long timestamp) {
        try {
            DateFormat dateFormat = getDateTimeInstance();
            Date netDate = (new Date(timestamp));
            return dateFormat.format(netDate);
        } catch (Exception e) {
            return "date";
        }
    }












    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
