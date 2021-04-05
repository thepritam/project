package com.example.ServicePortal;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

public class DynamicViews implements TextWatcher {

    public View view;
    public DynamicViews(View view) {
        this.view = view;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
//        switch(view.getId()){
//            case R.id.name:
//                model.setName(text);
//                break;
//            case R.id.email:
//                model.setEmail(text);
//                break;
//            case R.id.phone:
//                model.setPhone(text);
//                break;

        if(text!=null && text.isEmpty()==false)
        model.setPair(view.getId(),Double.parseDouble(text));

    }

}

