package com.project.placesproject.grocery;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.placesproject.R;

public class View_Holder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView shopname,description;
    ImageView shopimage;
    TextView address;


    View_Holder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardgrocery);
        shopname = (TextView) itemView.findViewById(R.id.tname);
        address = (TextView) itemView.findViewById(R.id.adress);
        description = (TextView)itemView.findViewById(R.id.description);
        shopimage = (ImageView)itemView.findViewById(R.id.shopimage);


    }
}
