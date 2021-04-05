package com.project.placesproject.tutor;



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

public class TeacherUpcomingOrdersViewHolder extends RecyclerView.ViewHolder {

    public TextView Category,OrderId,OrderedOn;
    public CardView Card;
    public LinearLayout layout_to_hide;
    public Button detailsbtn;


    public TeacherUpcomingOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        layout_to_hide = itemView.findViewById(R.id.layout_to_hide);
        Card = itemView.findViewById((R.id.card));
        Category = itemView.findViewById(R.id.category);
        OrderId = itemView.findViewById(R.id.orderid);
        OrderedOn = itemView.findViewById(R.id.orderdate);

        detailsbtn = itemView.findViewById(R.id.detailsbtn);


    }
}
