package com.example.ServicePortal;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context)
    {
        this.context = context;
    }

    public int[] slide_images = {R.drawable.onboard_page_one_image,R.drawable.onboard_work,R.drawable.onboard_gotolocation,R.drawable.onboard_taskdone,R.drawable.onboard_getpaid};

    public String[] slide_headings = {"project", "Assign a Task","Go to Location","Finish the task","Get Paid!"};

    public String[] slide_desc = {"Welcome to project! The one stop shop for all the work you can find. Just follow these few easy steps!",
            "Find a task which suits your skills from the wide variety of available tasks. Tap ASSIGN to get the task!",
            "Follow the map to the location of your customer!",
            "Successfully complete the task as per the Customer's requests!",
            "After successfully completing the task, Get Paid! Easy as that!"};



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
        slideDescription.setText(slide_desc[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
