package com.example.ServicePortal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private TextView[] mDots;

    private SliderAdapter sliderAdapter;
    private Button nextBtn;
    private Button prevBtn;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        prevBtn = (Button) findViewById(R.id.prevBtn);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCurrentPage == mDots.length - 1)
                {
                    Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
                    startActivity(intent);
                }

                mSlideViewPager.setCurrentItem(mCurrentPage + 1);
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });
    }

    public void addDotsIndicator(int position){

        mDots = new TextView[5];
        mDotLayout.removeAllViews();

        for(int i = 0 ; i < mDots.length; i++)
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(Color.CYAN);

            mDotLayout.addView(mDots[i]);
        }

        if ( mDots.length > 0 ){
            mDots[position].setTextColor(Color.BLUE);
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);

            mCurrentPage = i;

            if(i == 0 )
            {
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(false);
                prevBtn.setVisibility(View.GONE);
                nextBtn.setText("NEXT");
            }
            else if(i == mDots.length - 1 )
            {
                prevBtn.setEnabled(true);
                nextBtn.setEnabled(true);
                prevBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("FINISH");

            }
            else
            {
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(true);
                nextBtn.setVisibility(View.VISIBLE);
                prevBtn.setVisibility(View.VISIBLE);

                nextBtn.setText("NEXT");
                prevBtn.setText("BACK");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
