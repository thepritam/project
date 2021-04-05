package com.project.placesproject;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firestore.v1.StructuredQuery;
import com.project.placesproject.R;

public class UpcomingOrdersViewHolder extends RecyclerView.ViewHolder {

    public TextView ShopName,Price,OrderedOn;
    public CardView Card;
    public LinearLayout layout_to_hide;
    public Button detailsbtn;


    public UpcomingOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        layout_to_hide = itemView.findViewById(R.id.layout_to_hide);
        Card = itemView.findViewById((R.id.card));
        ShopName = itemView.findViewById(R.id.sname);
        Price= itemView.findViewById(R.id.price);
        OrderedOn = itemView.findViewById(R.id.orderdate);

        detailsbtn = itemView.findViewById(R.id.detailsbtn);


    }
}
