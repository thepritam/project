package com.project.placesproject;

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
import com.project.placesproject.tutor.*;


import com.google.android.material.tabs.TabLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.project.placesproject.grocery.*;

import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.project.placesproject.driver.*;
public class MainPage extends AppCompatActivity implements View.OnClickListener {

    Button instantmechanic,servicevehicle,modifyvehicle,examinevehicle,buttondriver,buttongrocery;

    private BottomNavigationView bottomNavigationView;


    Toolbar mtoolbar;
    SliderView sliderView;
    TextView textView;
    ImageButton bouchery,grocery,veg,stationary,buttontutor;
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
        setContentView(R.layout.activity_mainpage);

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
        Log.d("show", "onCreate: "+densityDpi);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        double width = (size. x)/(double)(densityDpi);
        double height = (size. y)/(double)(densityDpi);
        Log.d("Width", "" + width);
        Log.d("height", "" + height);

        double ytranslate = -1*312.5 * height;
        int mydpi=440;
        double  translate=(double) densityDpi/mydpi;
        double yoffet=translate*1700;
        int off=(int)yoffet;
        off=-1*off;
        bgapp.animate().translationY(off).setDuration(800).setStartDelay(300);
//        if(height>=4&&height<=4.3){
//        bgapp.animate().translationY(-1200).setDuration(800).setStartDelay(300);}
//        else if(height>=4.3&&height<=4.8){
//            bgapp.animate().translationY(-1700).setDuration(800).setStartDelay(300);}
      clover.animate().alpha(0).setDuration(800).setStartDelay(600);
      textsplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(300);


        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);

        id =getIntent().getStringExtra("id");


        Toast.makeText(getApplicationContext(),name, Toast.LENGTH_SHORT).show();
       // toolbar.setTitle(name);

        SaveDetails.id = id;
        SaveDetails.name = name;
        grocery=findViewById(R.id.button_groceryitems);
        bouchery=findViewById(R.id.buttonbucher);
        veg=findViewById(R.id.buttonfruitandveg);
        stationary=findViewById(R.id.button_stationary);
        grocery.setOnClickListener( this);
        bouchery.setOnClickListener(this);
        stationary.setOnClickListener(this);
        veg.setOnClickListener(this);
        buttontutor=findViewById(R.id.buttontutor);
        buttontutor.setOnClickListener(this);
        sliderView = findViewById(R.id.imageSlider);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPage);
        ImageAdapter adapterView = new ImageAdapter(MainPage.this);
        mViewPager.setAdapter(adapterView);
        TabLayout tabLayout =  findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent =new Intent(MainPage.this,HomeActivity.class);
                finish();
                startActivity(intent);
            }
        });

        final SliderAdapter adapter = new SliderAdapter(getApplicationContext());
        adapter.setCount(9);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {    case R.id.button_groceryitems:
            Intent intent = new Intent(getApplicationContext(), PermissionsActivity.class);
            intent.putExtra("id",getIntent().getStringExtra("id"));
            intent.putExtra("Categoryservice","General Stores");
            intent.putExtra("latitude",getIntent().getStringExtra("latitude"));
            intent.putExtra("longitude",getIntent().getStringExtra("longitude"));
            startActivity(intent);
            break;
            case R.id.buttonbucher:
                Intent intent1 = new Intent(getApplicationContext(), PermissionsActivity.class);
                intent1.putExtra("id",getIntent().getStringExtra("id"));
                intent1.putExtra("Categoryservice","bucher");
                intent1.putExtra("latitude",getIntent().getStringExtra("latitude"));
                intent1.putExtra("longitude",getIntent().getStringExtra("longitude"));
                startActivity(intent1);
                break;
            case R.id.buttonfruitandveg:
                Intent intent2 = new Intent(getApplicationContext(), PermissionsActivity.class);
                intent2.putExtra("id",getIntent().getStringExtra("id"));
                intent2.putExtra("Categoryservice","grocery");
                intent2.putExtra("latitude",getIntent().getStringExtra("latitude"));
                intent2.putExtra("longitude",getIntent().getStringExtra("longitude"));
                startActivity(intent2);
                break;
            case R.id.button_stationary:
                Intent intent3 = new Intent(getApplicationContext(), PermissionsActivity.class);
                intent3.putExtra("id",getIntent().getStringExtra("id"));
                intent3.putExtra("Categoryservice","station");
                intent3.putExtra("latitude",getIntent().getStringExtra("latitude"));
                intent3.putExtra("longitude",getIntent().getStringExtra("longitude"));
                startActivity(intent3);
                break;

            case R.id.buttontutor:Intent intent5 = new Intent(getApplicationContext(),PermissionsActivity.class);
                intent5.putExtra("id",id);
                intent5.putExtra("Categoryservice","tutor");
                startActivity(intent5);
                break;

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options, menu);
        return true;
    }
    public void addDotsIndicator(int position){

        mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for(int i = 0 ; i < mDots.length; i++)
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(Color.BLACK);

            mDotLayout.addView(mDots[i]);
        }

        if ( mDots.length > 0 ){
            mDots[position].setTextColor(Color.WHITE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent =new Intent(MainPage.this,HomeActivity.class);
                finish();
                startActivity(intent);


                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();



                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);


        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
