package com.project.placesproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TrackActivity extends AppCompatActivity {
    TextView name,nameshow,mail,mailshow,phone,phoneshow;
    RatingBar ratingBar;
    Geocoder geocoder;
    List<Address> addresses;
    TextView raitingtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        name=findViewById(R.id.name);
        nameshow=findViewById(R.id.nameshow);
        mail=findViewById(R.id.mail);
        mailshow=findViewById(R.id.mailshow);

        phone=findViewById(R.id.phone);
        phoneshow=findViewById(R.id.phoneshow);
        raitingtext=findViewById(R.id.textView13);
        name.setText("Name :");
        mail.setText("Mail :");
        phone.setText("Phone :");
        nameshow.setText(getIntent().getStringExtra("name"));
        mailshow.setText(getIntent().getStringExtra("mail"));
        phoneshow.setText(getIntent().getStringExtra("phone"));
        double latitude=Double.parseDouble(getIntent().getStringExtra("lat"));
        double longitude=Double.parseDouble(getIntent().getStringExtra("lon"));
        ratingBar=findViewById(R.id.ratingBar2);
        ratingBar.setNumStars(5);
        ratingBar.setRating((float)4.3);

        raitingtext.setText("4.3");






    }
}
