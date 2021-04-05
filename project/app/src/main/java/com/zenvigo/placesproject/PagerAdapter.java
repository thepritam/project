package com.project.placesproject;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Chirag on 30-Jul-17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {

            case 0:
                UpcomingOrders upcomingOrders = new UpcomingOrders();
                return upcomingOrders;
            case 1:
                CompletedOrders completedOrders = new CompletedOrders();
                return  completedOrders;
            case 2:
                CancelledOrders cancelledOrders = new CancelledOrders();
                return  cancelledOrders;
            case 3:
                TodoList todoList = new TodoList();
                return todoList;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}