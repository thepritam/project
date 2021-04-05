package com.project.placesproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class yourorderactivity extends AppCompatActivity {


    ImageButton tutors,grocery;
    ImageView bgapp, clover;
    LinearLayout textsplash, texthome, menus;
    Button logout;
    Animation frombottom;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourorderactivity);
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

        int mydpi=440;
        double  translate=(double) densityDpi/mydpi;
        double yoffet=translate*1700;
        int off=(int)yoffet;
        off=-1*off;
        bgapp.animate().translationY(off).setDuration(800).setStartDelay(300);

//        if(height>=4&&height<=4.5){
//            bgapp.animate().translationY(-1200).setDuration(800).setStartDelay(300);}
//        else if(height>4.5&&height<=4.8){
//            bgapp.animate().translationY(-1700).setDuration(800).setStartDelay(300);}
//        else {
//            bgapp.animate().translationY(-2000).setDuration(800).setStartDelay(300);}

        clover.animate().alpha(0).setDuration(800).setStartDelay(600);
        textsplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(300);

        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);

        id =getIntent().getStringExtra("id");
         tutors=findViewById(R.id.buttontutororders);
         grocery=findViewById(R.id.buttongroceryorders);

        tutors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TutorOrders.class);
                startActivity(intent);
            }
        });


        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GroceryOrders.class);
                startActivity(intent);
            }
        });

    }
}
