package com.project.placesproject.tutor;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.placesproject.R;

public class DataViewHolder extends RecyclerView.ViewHolder {

    public TextView TeacherName,Price,Payment;
    public CardView Card;
    public LinearLayout layout_to_hide;
    public Button detailsbtn;


    public DataViewHolder(@NonNull View itemView) {
        super(itemView);

        layout_to_hide = itemView.findViewById(R.id.layout_to_hide);
        Card = itemView.findViewById((R.id.card));
        TeacherName = itemView.findViewById(R.id.tname);
        Price= itemView.findViewById(R.id.price);
        Payment = itemView.findViewById(R.id.pmode);

        detailsbtn = itemView.findViewById(R.id.detailsbtn);


    }
}
