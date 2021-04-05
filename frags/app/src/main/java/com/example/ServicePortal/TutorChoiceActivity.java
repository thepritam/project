package com.example.ServicePortal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;

public class TutorChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_choice);

       final Button education, music, musicinstru, musicsing, sports, sportstt, sportbadminton, sportlawnt, sportfitness;

        education = findViewById(R.id.education);
        music = findViewById(R.id.music);
        musicinstru = findViewById(R.id.musicinstru);
        musicsing = findViewById(R.id.musicsing);
        sports = findViewById(R.id.sports);
        sportstt = findViewById(R.id.sportstt);
        sportbadminton = findViewById(R.id.sportbadminton);
        sportlawnt = findViewById(R.id.sportlawnt);
        sportfitness = findViewById(R.id.sportfitness);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("Name");
        final String currentUid = intent.getStringExtra("id");
        final String category = intent.getStringExtra("Category");

        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorChoiceActivity.this, TutorDetailsActivity.class);
                intent.putExtra("Name",name);
                intent.putExtra("id",currentUid);
                intent.putExtra("Category",category);
                startActivity(intent);
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicinstru.setVisibility(View.VISIBLE);
                musicsing.setVisibility(View.VISIBLE);
            }
        });

        musicinstru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(currentUid).child("Category").child("Type").setValue("Instrumental");
                Intent intent = new Intent(TutorChoiceActivity.this, TutorExperienceActivity.class);
                intent.putExtra("Name",name);
                intent.putExtra("id",currentUid);
                intent.putExtra("Category",category);
                intent.putExtra("Instrument","Yes");
                startActivity(intent);
            }
        });

        musicsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(currentUid).child("Category").child("Type").setValue("Singing");
                Intent intent = new Intent(TutorChoiceActivity.this, TutorExperienceActivity.class);
                intent.putExtra("Name",name);
                intent.putExtra("id",currentUid);
                intent.putExtra("Category",category);
                intent.putExtra("Instrument","No");
                startActivity(intent);
            }
        });

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sportstt.setVisibility(View.VISIBLE);
                sportbadminton.setVisibility(View.VISIBLE);
                sportlawnt.setVisibility(View.VISIBLE);
                sportfitness.setVisibility(View.VISIBLE);
            }
        });

        sportstt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(currentUid).child("Category").child("Type").setValue("Table Tennis");
                Intent intent = new Intent(TutorChoiceActivity.this, TutorExperienceActivity.class);
                intent.putExtra("Name",name);
                intent.putExtra("id",currentUid);
                intent.putExtra("Category",category);
                intent.putExtra("Instrument","No");
                startActivity(intent);

            }
        });

        sportbadminton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(currentUid).child("Category").child("Type").setValue("Badmintion");
                Intent intent = new Intent(TutorChoiceActivity.this, TutorExperienceActivity.class);
                intent.putExtra("Name",name);
                intent.putExtra("id",currentUid);
                intent.putExtra("Category",category);
                intent.putExtra("Instrument","No");
                startActivity(intent);

            }
        });

        sportlawnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(currentUid).child("Category").child("Type").setValue("Lawn Tennis");
                Intent intent = new Intent(TutorChoiceActivity.this, TutorExperienceActivity.class);
                intent.putExtra("Name",name);
                intent.putExtra("id",currentUid);
                intent.putExtra("Category",category);
                intent.putExtra("Instrument","No");
                startActivity(intent);
            }
        });

        sportfitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(currentUid).child("Category").child("Type").setValue("Fitness");
                Intent intent = new Intent(TutorChoiceActivity.this, TutorExperienceActivity.class);
                intent.putExtra("Name",name);
                intent.putExtra("id",currentUid);
                intent.putExtra("Category",category);
                intent.putExtra("Instrument","No");
                startActivity(intent);
            }
        });

    }
}
