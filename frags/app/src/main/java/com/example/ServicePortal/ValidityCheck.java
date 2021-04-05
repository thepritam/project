package com.example.ServicePortal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class ValidityCheck extends AppCompatActivity {

    private User user;
    HashMap<String, Object> dummy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validity_check);

        Intent intent = getIntent();
        final String currentUid = intent.getStringExtra("id");
        final String name = intent.getStringExtra("Name");
        final String category = intent.getStringExtra("Category");

        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

          final HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                //final User user = dataSnapshot.getValue(User.class);
                Log.d("Running", "" + value.get("isValid").toString());
                Log.d("Running","current uid : "+currentUid);


                if(Integer.parseInt(value.get("isValid").toString()) == 1)

               // final HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

//                Log.d("Running", "" + user.isValid);


                if(value.get("isValid").toString().equals("1") && value.get("CooldownCat").toString().equals("0"))

                {
                    Intent intent2 = new Intent(ValidityCheck.this, StartActivity.class);
                    intent2.putExtra("Name",name);
                    intent2.putExtra("id",currentUid);
                    intent2.putExtra("Category",category);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent2);
                }
                else if ( value.get("isValid").toString().equals("1") && value.get("CooldownCat").toString().equals("1") )
                {
                    TextView chtxt = (TextView) findViewById(R.id.textView10);
                    final TextView chtxt2 = (TextView) findViewById(R.id.textView9);

                    chtxt.setTextSize(20);
                    chtxt.setText("You have been issued a temporary ban because of your carelessness to uphold your duty. Please wait for-");

//                    Timestamp ts1 = Timestamp.now();
//
//                    Timestamp ts2 = new Timestamp(value.get("timeCooldown"));
//
//                    int diff = ts1.compareTo(ts2);
//
//                    SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//                    sfd.format(new Date(diff));
//
//                    chtxt2.setText(sfd.toString());

                    FirebaseDatabase.getInstance().getReference().child("Dummy").child("Time").setValue(ServerValue.TIMESTAMP);

                    FirebaseDatabase.getInstance().getReference().child("Dummy").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                            User user = dataSnapshot.getValue(User.class);

                            dummy = (HashMap<String, Object>) dataSnapshot.getValue();
                            Log.d("timehaloe",""+dummy);

                            dummy.get("Time");

                            // FIX THIS DUMMY VARIABLE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                            if((((long)value.get("timeCooldown") + 43200000 )-(long)dummy.get("Time")) > 0) {

                                long hours = ((((long) value.get("timeCooldown") + 43200000) - (long) dummy.get("Time")) / 1000) / 3600;
                                long minutes = (((((long) value.get("timeCooldown") + 43200000) - (long) dummy.get("Time")) / 1000) % 3600) / 60;
                                long seconds = ((((long) value.get("timeCooldown") + 43200000) - (long) dummy.get("Time")) / 1000) % 60;

                                String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

//                            chtxt2.setText(sfd.format(new Date(((long)value.get("timeCooldown") + 43200000 )-(long)dummy.get("Time"))));
                                chtxt2.setText(timeString);
                            }
                            else{

                                FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("CooldownCat").setValue("0");

                                Intent intent2 = new Intent(ValidityCheck.this, StartActivity.class);
                                intent2.putExtra("Name",name);
                                intent2.putExtra("id",currentUid);
                                intent2.putExtra("Category",category);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent2);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

//                    chtxt2.setText(sfd.format(new Date(Math.abs((((long)value.get("timeCooldown"))+43200000)-(new Timestamp(new Date()).compareTo((Timestamp)value.get("timeCooldown")))))));
                }
                else if ( value.get("isValid").toString().equals("1") && value.get("CooldownCat").toString().equals("2") )
                {
                    TextView chtxt = (TextView) findViewById(R.id.textView10);
                    final TextView chtxt2 = (TextView) findViewById(R.id.textView9);

                    chtxt.setTextSize(20);
                    chtxt.setText("You have been issued a temporary ban because of your incompetence to perform accepted work. Please wait for-");

//                    Timestamp ts1 = Timestamp.now();
//
//                    Timestamp ts2 = new Timestamp(value.get("timeCooldown"));
//
//                    int diff = ts1.compareTo(ts2);
//
//                    SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//                    sfd.format(new Date(diff));
//
//                    chtxt2.setText(sfd.toString());

                    FirebaseDatabase.getInstance().getReference().child("Dummy").child("Time").setValue(ServerValue.TIMESTAMP);

                    FirebaseDatabase.getInstance().getReference().child("Dummy").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getActivity(),"occur",Toast.LENGTH_LONG).show();
//                            User user = dataSnapshot.getValue(User.class);

                            dummy = (HashMap<String, Object>) dataSnapshot.getValue();
                            Log.d("timehaloe",""+dummy);

                            dummy.get("Time");

                            // FIX THIS DUMMY VARIABLE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                            if((((long)value.get("timeCooldown") + 43200000 )-(long)dummy.get("Time")) > 0) {

                                long hours = ((((long) value.get("timeCooldown") + 7200000) - (long) dummy.get("Time")) / 1000) / 3600;
                                long minutes = (((((long) value.get("timeCooldown") + 7200000) - (long) dummy.get("Time")) / 1000) % 3600) / 60;
                                long seconds = ((((long) value.get("timeCooldown") + 7200000) - (long) dummy.get("Time")) / 1000) % 60;

                                String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

//                            chtxt2.setText(sfd.format(new Date(((long)value.get("timeCooldown") + 43200000 )-(long)dummy.get("Time"))));
                                chtxt2.setText(timeString);
                            }
                            else{

                                FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("CooldownCat").setValue("0");

                                Intent intent2 = new Intent(ValidityCheck.this, StartActivity.class);
                                intent2.putExtra("Name",name);
                                intent2.putExtra("id",currentUid);
                                intent2.putExtra("Category",category);
                                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent2);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });



                    // ENTER COOLDOWN TIME DETAILS
                }
                else if ( value.get("isValid").toString().equals("0"))
                {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
