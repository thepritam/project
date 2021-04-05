package com.example.ServicePortal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

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
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                }
            }
        };

        timer.start();

    }
}
