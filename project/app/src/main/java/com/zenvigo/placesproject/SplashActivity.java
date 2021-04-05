package com.project.placesproject;

//import androidx.appcompat.app.ActionBar;
//
//
//import android.content.Intent;
//import android.view.WindowManager;
//
//import com.daimajia.androidanimations.library.Techniques;
//import com.viksaa.sssplash.lib.activity.AwesomeSplash;
//import com.viksaa.sssplash.lib.cnst.Flags;
//import com.viksaa.sssplash.lib.model.ConfigSplash;
//
//public class SplashActivity extends AwesomeSplash {
//
//  /*  @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//*/
//    //  TextView tx = (TextView)findViewById(R.id.textView3);
//
//    //Typeface custom_font = Typeface.createFromAsset(getAssets(), "C:\\Users\\ACER\\AndroidStudioProjects\\PlacesProject2\\app\\src\\main\\res\\font\\font1.ttf");
//
//    //tx.setTypeface(custom_font);
//       /* AssetManager am = context.getApplicationContext().getAssets();
//
//        typeface = Typeface.createFromAsset(am,
//                String.format(Locale.US, "fonts/%s", "abc.ttf"));
//
//        setTypeface(typeface);*/
//     /*   new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent= new Intent(SplashActivity.this,HomeActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        },500);
//}*/
//
//    @Override
//    public void initSplash(ConfigSplash configSplash) {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//     configSplash.setBackgroundColor(R.color.bg_color);
//     configSplash.setAnimCircularRevealDuration(1000);
//     configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
//        configSplash.setRevealFlagX(Flags.REVEAL_BOTTOM);
//        configSplash.setLogoSplash(R.drawable.splashlogo);
//        configSplash.setAnimLogoSplashDuration(1000);
//        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce);
//        configSplash.setTitleSplash("Where Servicemen meet Customers");
//        configSplash.setTitleTextColor(R.color.colorPrimary);
//        configSplash.setTitleTextSize(20f);
//        configSplash.setAnimTitleDuration(1000);
//        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
//    }
//
//    @Override
//    public void animationsFinished() {
//        Intent intent= new Intent(SplashActivity.this,HomeActivity.class);
//        startActivity(intent);
//        finish();
//    }
//}

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import static maes.tech.intentanim.CustomIntent.customType;

public class SplashActivity extends AppCompatActivity{

    private ImageView ivicon;
    private ImageView ivlogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivicon = (ImageView) findViewById(R.id.app_icon);
        ivlogo = (ImageView) findViewById(R.id.app_logo);

        final Animation splash_anim_alpha = AnimationUtils.loadAnimation(this, R.anim.splash_transition_alpha);
        final Animation splash_anim_fasteralpha = AnimationUtils.loadAnimation(this, R.anim.splash_transition_fasteralpha);
        final Animation splash_animmove = AnimationUtils.loadAnimation(this, R.anim.splash_hyperspacejump);

        ivicon.startAnimation(splash_anim_alpha);

        splash_anim_alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivicon.startAnimation(splash_animmove);

                splash_animmove.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        animation.setFillAfter(true);
                        animation.setFillEnabled(true);
                        ivlogo.setVisibility(View.VISIBLE);
                        ivlogo.startAnimation(splash_anim_fasteralpha);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        final Intent intent = new Intent(this, IntroActivity.class);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    customType(SplashActivity.this,"fadein-to-fadeout");
                    finish();

                }
            }
        };

        timer.start();

    }
}
