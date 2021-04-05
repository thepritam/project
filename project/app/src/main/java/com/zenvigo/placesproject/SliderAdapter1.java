package com.project.placesproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter1 extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter1(Context context)
    {
        this.context = context;
    }

    public int[] slide_images = {R.drawable.onboard_page_one_image,R.drawable.usericonfor ,R.drawable.customercareicon};

    public String[] slide_headings = {"project", "Enhance Your Life","Call US"};

    public String[] slide_desc = {"Welcome to project! You are just few steps away",
            "Where You get the best",
            "Dedicated to serve our customers..",
            };



    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_Image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_Heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideHeading.setTextColor(Color.parseColor("#ffffff"));
        slideDescription.setText(slide_desc[position]);
        slideDescription.setTextSize(16);
        slideDescription.setTextColor(Color.parseColor("#ffffff"));

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}