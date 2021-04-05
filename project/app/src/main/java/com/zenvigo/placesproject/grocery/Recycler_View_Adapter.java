package com.project.placesproject.grocery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.project.placesproject.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Recycler_View_Adapter extends RecyclerView.Adapter<View_Holder> implements Filterable {

         List<Data> exampleListFull;
        List<Data> list = Collections.emptyList();
        Context context;

        public Recycler_View_Adapter(List<Data> list, Context context) {
            this.list = list;
            exampleListFull=new ArrayList<>(list);
            this.context = context;
        }

        @Override
        public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Inflate the layout, initialize the View Holder
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_grocery, parent, false);
            View_Holder holder = new View_Holder(v);
            return holder;

        }

        @Override
        public void onBindViewHolder(View_Holder holder, int position) {

            //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
            holder.shopname.setText(list.get(position).title);
            holder.address.setText(list.get(position).adress);
            holder.description.setText("Get  "+list.get(position).description);



            //animate(holder);

        }

        @Override
        public int getItemCount() {
            //returns the number of elements the RecyclerView will display
            return list.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        // Insert a new item to the RecyclerView on a predefined position
        public void insert(int position, Data data) {
            list.add(position, data);
            notifyItemInserted(position);
        }

        // Remove a RecyclerView item containing a specified Data object
        public void remove(Data data) {
            int position = list.indexOf(data);
            list.remove(position);
            notifyItemRemoved(position);
        }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Data> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Data item : exampleListFull) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}


