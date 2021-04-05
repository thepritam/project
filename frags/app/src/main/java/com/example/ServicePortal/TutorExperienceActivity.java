package com.example.ServicePortal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class TutorExperienceActivity extends AppCompatActivity {

    String instru;
    EditText exptext,achievetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_experience);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("Name");
        final String currentUid = intent.getStringExtra("id");
        final String category = intent.getStringExtra("Category");
//        String Instrument = intent.getStringExtra("Instrument");
        Button buttonsubmit = findViewById(R.id.buttonsubmit);

        exptext = findViewById(R.id.exptext);
        achievetext = findViewById(R.id.achievetext);
        TextView instrutext = findViewById(R.id.instrutext);

        final Spinner spinner = findViewById(R.id.spinner_instru);

        final String Instrument = "Yes";

        if(Instrument.equals("Yes"))
        {
            instrutext.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
        }

        final String[] arr = new String[]{
                "Guitar",
                "Piano",
                "Keyboard",
                "Flute",
                "Drums",
                "Sitar",
                "Other"
        };

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arr);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                instru = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TextView errorText = (TextView)spinner.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("Select Instrument");
            }
        });

        buttonsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(currentUid).child("Category").child("Experience").setValue(exptext.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(currentUid).child("Category").child("Achievement").setValue(achievetext.getText().toString());

                if(Instrument.equals("Yes"))
                {
                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(currentUid).child("Category").child("Achievement").setValue(instru.toString());
                }

                Intent intent = new Intent(TutorExperienceActivity.this, RegisterImageUploadActivity.class);
                intent.putExtra("Name",name);
                intent.putExtra("id",currentUid);
                intent.putExtra("Category",category);
                startActivity(intent);
            }
        });


    }
}
