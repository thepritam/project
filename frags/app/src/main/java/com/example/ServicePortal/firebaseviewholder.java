package com.example.ServicePortal;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class firebaseviewholder extends RecyclerView.ViewHolder {

    public TextView Assigned,Completed,Category,AssignedId,CId,Description,Latitude,Longitude,Cname,tutortime;
    public CardView Card;
    public LinearLayout layout_to_hide;
    public Button assignButton;
    public Button showButton;
    public Button playButton;
    public Button stopButton;
    public Button cancelButton;
    public SeekBar seekBar;

    public firebaseviewholder(@NonNull View itemView) {
        super(itemView);

        layout_to_hide = itemView.findViewById(R.id.layout_to_hide);
        Card = itemView.findViewById((R.id.card));
        Assigned = itemView.findViewById(R.id.ass);
        Completed = itemView.findViewById(R.id.com);
        Category = itemView.findViewById(R.id.cat);
        AssignedId = itemView.findViewById(R.id.assign);
        CId = itemView.findViewById(R.id.cid);
        Cname = itemView.findViewById(R.id.cname);
        Description = itemView.findViewById(R.id.des);
        Latitude = itemView.findViewById(R.id.lat);
        Longitude = itemView.findViewById(R.id.longi);
        assignButton = itemView.findViewById(R.id.assignBtn);
        showButton = itemView.findViewById(R.id.showBtn);
        playButton = itemView.findViewById(R.id.playBtn);
        stopButton = itemView.findViewById(R.id.stopBtn);
        seekBar = itemView.findViewById(R.id.seekbar);
        cancelButton = itemView.findViewById(R.id.cancelBtn);
        tutortime = itemView.findViewById(R.id.tutortime);

    }
}
