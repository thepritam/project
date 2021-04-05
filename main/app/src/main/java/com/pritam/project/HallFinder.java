package com.pritam.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class HallFinder extends AppCompatActivity {

    Toolbar mtoolbar;
    TextView showname,showid;
    String id,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_finder);

        id =getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("Name");

        showname = (TextView)findViewById(R.id.showname);
        showid = (TextView)findViewById(R.id.showid);

        showname.setText("Welcome "+name);
        showid.setText("Your Id is "+id);

        mtoolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent =new Intent(HallFinder.this,HomeActivity.class);
                finish();
                startActivity(intent);


                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}

