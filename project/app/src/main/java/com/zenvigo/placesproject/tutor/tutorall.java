package com.project.placesproject.tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.project.placesproject.HomeActivity;
import com.project.placesproject.ImageAdapter;
import com.project.placesproject.MainPage;
import com.project.placesproject.PermissionsActivity;
import com.project.placesproject.R;
import com.project.placesproject.SaveDetails;
import com.project.placesproject.SliderAdapter;
import com.project.placesproject.account_activity;
import com.project.placesproject.customercare;
import com.project.placesproject.yourorderactivity;

public class tutorall extends AppCompatActivity implements View.OnClickListener {

    Button instantmechanic,servicevehicle,modifyvehicle,examinevehicle,buttondriver,buttongrocery;

    private BottomNavigationView bottomNavigationView;


    Toolbar mtoolbar;
    SliderView sliderView;
    TextView textView;
    ImageButton buttonfitness,buttontt,buttonlt,buttontutor,buttonbadminton,buttonchess,buttonmusic,buttonsingteacher,buttonart;
    String id,name;
    ImageView bgapp, clover;
    LinearLayout textsplash, texthome, menus;
    Button logout;
    private TextView[] mDots;
    LinearLayout mDotLayout;
    Animation frombottom;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorall);

        //Toolbar toolbar =findViewById(R.id.toolbar);
        Toolbar toolbar =findViewById(R.id.toolbar);


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

        // Intent intent = new Intent(this,PackagedriverActivity.class);
        textView=findViewById(R.id.textviewname);
        name = getIntent().getStringExtra("Name");
        textView.setText(name);
        mDotLayout=findViewById(R.id.slider);
        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);

        logout=findViewById(R.id.logout1);
        bgapp = (ImageView) findViewById(R.id.bgapp);
        clover = (ImageView) findViewById(R.id.clover);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menus = (LinearLayout) findViewById(R.id.menus);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int densityDpi = (int)(metrics.density * 160f);



        Display display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        double width = (size. x)/(double)(densityDpi);
        double height = (size. y)/(double)(densityDpi);
        Log.d("Width", "" + width);
        Log.d("height", "" + height);

        double ytranslate = -1*312.5 * height;

//        if(height>=4&&height<=4.5){
//            bgapp.animate().translationY(-1200).setDuration(800).setStartDelay(300);}
//        else if(height>4.5&&height<=4.8){
//            bgapp.animate().translationY(-1700).setDuration(800).setStartDelay(300);}
//        else {
//            bgapp.animate().translationY(-2000).setDuration(800).setStartDelay(300);}



        int mydpi=440;
        double  translate=(double) densityDpi/mydpi;
        double yoffet=translate*1700;
        int off=(int)yoffet;
        off=-1*off;
        bgapp.animate().translationY(off).setDuration(800).setStartDelay(300);

        clover.animate().alpha(0).setDuration(800).setStartDelay(600);
        textsplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(300);

        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);
       buttonbadminton=findViewById(R.id.buttonbadminton);
       buttonchess=findViewById(R.id.buttonchess);
       buttonlt=findViewById(R.id.buttonlt);
       buttontt=findViewById(R.id.buttontt);
       buttontutor=findViewById(R.id.buttontutororg);
       buttonfitness=findViewById(R.id.buttonfitness);
       buttonart=findViewById(R.id.buttontutorart);
       buttonsingteacher=findViewById(R.id.buttonsinging);
       buttonmusic=findViewById(R.id.buttonmusic);
       buttonfitness.setOnClickListener(this);
        buttonchess.setOnClickListener(this);
        buttontutor.setOnClickListener(this);
        buttontt.setOnClickListener(this);
        buttonlt.setOnClickListener(this);
        buttonbadminton.setOnClickListener(this);
        buttonmusic.setOnClickListener(this);
        buttonart.setOnClickListener(this);
        buttonsingteacher.setOnClickListener(this);




        // toolbar.setTitle(name);





    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {    case R.id.buttontt:
            Intent intent = new Intent(getApplicationContext(),sportsactivity.class);
            intent.putExtra("id",id);
            intent.putExtra("Categoryservice","Tabletennis");
            intent.putExtra("latitude",getIntent().getStringExtra("latitude"));
            intent.putExtra("longitude",getIntent().getStringExtra("longitude"));
            startActivity(intent);
            break;
            case R.id.buttonlt:
                Intent intent1 = new Intent(getApplicationContext(), sportsactivity.class);
                intent1.putExtra("id",id);
                intent1.putExtra("Categoryservice","Lawntennis");
                intent1.putExtra("latitude",getIntent().getStringExtra("latitude"));
                intent1.putExtra("longitude",getIntent().getStringExtra("longitude"));
                startActivity(intent1);
                break;
            case R.id.buttonbadminton:
                Intent intent2 = new Intent(getApplicationContext(), sportsactivity.class);
                intent2.putExtra("id",id);
                intent2.putExtra("Categoryservice","Badminton");
                intent2.putExtra("latitude",getIntent().getStringExtra("latitude"));
                intent2.putExtra("longitude",getIntent().getStringExtra("longitude"));
                startActivity(intent2);
                break;
            case R.id.buttonchess:
                Intent intent3 = new Intent(getApplicationContext(), sportsactivity.class);
                intent3.putExtra("id",id);
                intent3.putExtra("Categoryservice","Chess");
                intent3.putExtra("latitude",getIntent().getStringExtra("latitude"));
                intent3.putExtra("longitude",getIntent().getStringExtra("longitude"));
                startActivity(intent3);
                break;

            case R.id.buttontutororg:Intent intent5 = new Intent(getApplicationContext(),tutorActivity.class);
                intent5.putExtra("id",id);
                intent5.putExtra("Categoryservice","Tutor");
                intent5.putExtra("latitude",getIntent().getStringExtra("latitude"));
                intent5.putExtra("longitude",getIntent().getStringExtra("longitude"));
                startActivity(intent5);
                break;
            case R.id.buttonfitness:Intent intent6 = new Intent(getApplicationContext(),sportsactivity.class);
                intent6.putExtra("id",id);
                intent6.putExtra("Categoryservice","Fitnesstrainer");
                intent6.putExtra("latitude",getIntent().getStringExtra("latitude"));
                intent6.putExtra("longitude",getIntent().getStringExtra("longitude"));
                startActivity(intent6);
                break;
            case R.id.buttontutorart:
                Intent intent7 = new Intent(getApplicationContext(), sportsactivity.class);
                intent7.putExtra("id",id);
                intent7.putExtra("Categoryservice","Art");
                intent7.putExtra("latitude",getIntent().getStringExtra("latitude"));
                intent7.putExtra("longitude",getIntent().getStringExtra("longitude"));
                startActivity(intent7);
                break;

            case R.id.buttonsinging:
                Intent intent8 = new Intent(getApplicationContext(),sportsactivity.class);
                intent8.putExtra("id",id);
                intent8.putExtra("Categoryservice","voicetutor");
                intent8.putExtra("latitude",getIntent().getStringExtra("latitude"));
                intent8.putExtra("longitude",getIntent().getStringExtra("longitude"));
                startActivity(intent8);
                break;
            case R.id.buttonmusic:
                Intent intent9 = new Intent(getApplicationContext(),sportsactivity.class);
                intent9.putExtra("id",id);
                intent9.putExtra("Categoryservice","Instrumentation");
                intent9.putExtra("latitude",getIntent().getStringExtra("latitude"));
                intent9.putExtra("longitude",getIntent().getStringExtra("longitude"));
                startActivity(intent9);
                break;

        }

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
