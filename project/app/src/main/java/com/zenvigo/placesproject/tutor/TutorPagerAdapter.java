package com.project.placesproject.tutor;

import com.project.placesproject.CancelledOrders;
import com.project.placesproject.CompletedOrders;
import com.project.placesproject.TodoList;
import com.project.placesproject.UpcomingOrders;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Chirag on 30-Jul-17.
 */

public class TutorPagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public TutorPagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {

            case 0:
                TeacherUpcomingOrders teacherUpcomingOrders = new TeacherUpcomingOrders();
                return teacherUpcomingOrders;
            case 1:
                TeacherCompletedOrders teacherCompletedOrders = new TeacherCompletedOrders();
                return  teacherCompletedOrders;
            case 2:
                TeacherCancelledOrders teacherCancelledOrders = new TeacherCancelledOrders();
                return  teacherCancelledOrders;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}