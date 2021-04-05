package com.project.placesproject.tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.iammert.library.readablebottombar.ReadableBottomBar;
import com.project.placesproject.R;
import com.project.placesproject.account_activity;
import com.project.placesproject.customercare;
import com.project.placesproject.yourorderactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class tutorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private LinearLayout parentLinearLayout,streamlayout;
    String[] users = {"please select Class","Nursery","1","2","3","4","5","6","7","8","9","10","11","12","Competitive Exams","Graduate","Master's" };
    Spinner spin,s2;
    String sp1,sp2;
    TableLayout tableLayout;
    EditText editText;
    Spinner spinnerdynamic ;
     String stream,text;
    String [] list;
    ArrayList<String> subjectslist;
    Button adbtn,confirmbtn,selectbtn,selectbtn1,getConfirmbtntext;
    private BottomNavigationView bottomNavigationView;
    Button addfield,buttonconfirmsub;
    EditText editTextsubject;

    private String Category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);
        subjectslist=new ArrayList<>();
        subjectslist.clear();
        Log.d("tutor","entered"+subjectslist);
        parentLinearLayout = (LinearLayout)findViewById(R.id.parent_linear_layout);
        streamlayout=findViewById(R.id.linear_layout);
         spin = (Spinner) findViewById(R.id.spinner1);
         spinnerdynamic = (Spinner)findViewById(R.id.spinnerdynamic);
         editText=findViewById(R.id.editextstream);
         s2=findViewById(R.id.spinner2);
         buttonconfirmsub=findViewById(R.id.add_confirm_subject);
         confirmbtn=findViewById(R.id.button_tutor_confirm);
        addfield=findViewById(R.id.add_field_button);
        // selectbtn1=findViewById(R.id.select_button1);

         getConfirmbtntext=findViewById(R.id.add_confirm);
        BottomNavigationView bottomNavigationView;
        bottomNavigationView=findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Intent intent;
                switch(menuItem.getItemId())
                {
                    case R.id.recent:
                        intent = new Intent(getApplicationContext(), yourorderactivity.class);
                        startActivity(intent);
                        break;
                    case R.id.account:
                        intent = new Intent(getApplicationContext(), account_activity.class);
                        startActivity(intent);
                        break;

                    case R.id.review:
                        intent = new Intent(getApplicationContext(), customercare.class);
                        startActivity(intent);
                        break;

                }
                return false;
            }
        });
         list = new String[]{"Please select class from above"};
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(tutorActivity.this,android.R.layout.simple_spinner_item, list);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.notifyDataSetChanged();
        spinnerdynamic.setAdapter(dataAdapter2);
        //spinnerdynamic.setSelection(0,false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        editTextsubject=findViewById(R.id.editextsubject);
        spin.setOnItemSelectedListener(this);
        bottomNavigationView=findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Intent intent;
                switch(menuItem.getItemId())
                {
                    case R.id.recent:
                        intent = new Intent(getApplicationContext(), yourorderactivity.class);
                        startActivity(intent);
                        break;
                    case R.id.account:
                        intent = new Intent(getApplicationContext(), account_activity.class);
                        startActivity(intent);
                        break;

                    case R.id.review:
                        intent = new Intent(getApplicationContext(), customercare.class);
                        startActivity(intent);
                        break;

                }
                return false;
            }
        });

        getConfirmbtntext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stream=editText.getText().toString();
                Log.d("show", "onClick: "+stream);
            }
        });
        buttonconfirmsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectslist.clear();
                subjectslist.add(editTextsubject.getText().toString());
                if(subjectslist.size()!=0){
                    confirmbtn.setEnabled(true);
                }
                else {
                    confirmbtn.setEnabled(false);
                }
            }
        });

            confirmbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(tutorActivity.this, ConfirmTutorActivity.class);
                    intent.putExtra("listdetails", subjectslist);
                    intent.putExtra("class", sp1);
                    intent.putExtra("lat", getIntent().getStringExtra("latitude"));
                    intent.putExtra("longi", getIntent().getStringExtra("longitude"));
                    intent.putExtra("id", getIntent().getStringExtra("uid"));
                    intent.putExtra("Category", Category);
                    if (sp2 != "Please mention the subject/s  below" && sp2 != "Please mention the subject/s and Stream below" && sp2 != "Please mention the subject/s and Exam name/s below seperated by commas(if multiple exams)")
                        intent.putExtra("board", sp2);
                    else
                        intent.putExtra("board", "");
                    if (stream != null) {
                        Log.d("show", "onClick: " + stream);
                        intent.putExtra("stream", stream);

                    } else {
                        intent.putExtra("stream", "");
                    }

                    startActivity(intent);

                }
            });

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp2= String.valueOf(s2.getSelectedItem());
                //Toast.makeText(get, sp1, Toast.LENGTH_SHORT).show();
                Log.d("show", "onItemSelected: "+sp2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//      adbtn.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View v) {
//              final EditText task=new EditText(getApplicationContext());
//              final EditText task1= new EditText(getApplicationContext());
//              task.setMaxWidth(60);
//              task.setMinHeight(100);
//             // task.setText("");
//              task1.setMaxWidth(40);
//              task1.setMinHeight(100);
//              //task1.setText("Task");
//
//
//              tableLayout.addView(task);
//              tableLayout.addView(task1);
//              task1.addTextChangedListener(new TextWatcher() {
//                  @Override
//                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                  }
//
//                  @Override
//                  public void onTextChanged(CharSequence s, int start, int before, int count) {
//                      if(s.length() != 0)
//                          Log.d("edittext","abc"+task.getText().toString());
//
//                  }
//
//                  @Override
//                  public void afterTextChanged(Editable s) {
//
//                  }
//              });
//
//              task.addTextChangedListener(new TextWatcher() {
//
//                  @Override
//                  public void afterTextChanged(Editable s) {}
//
//                  @Override
//                  public void beforeTextChanged(CharSequence s, int start,
//                                                int count, int after) {
//                  }
//
//                  @Override
//                  public void onTextChanged(CharSequence s, int start,
//                                            int before, int count) {
//                      if(s.length() != 0)
//                          Log.d("edittext","abc"+task.getText().toString());
//                  }
//              });
//          }
//      });

    }

    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        spinnerdynamic = (Spinner) (rowView.findViewById(R.id.spinnerdynamic));
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(tutorActivity.this,android.R.layout.simple_spinner_item, list);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.notifyDataSetChanged();
        spinnerdynamic.setAdapter(dataAdapter2);




        //  Log.d("text",""+ edittext_var.getText().toString());
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }


        @SuppressLint("ResourceType")
        public void onSelect(View v) { // on click event for a SELECT button

            spinnerdynamic = (Spinner) ((View) v.getParent()).findViewById(R.id.spinnerdynamic) ;
            String text = spinnerdynamic.getSelectedItem().toString();

          /*  spinnerdynamic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    text=spinnerdynamic.getSelectedItem().toString();
                    Toast.makeText(getApplicationContext(),"This is : "+text,Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
            subjectslist.add(text);
            selectbtn=(Button)((View) v.getParent()).findViewById(R.id.select_button);
            selectbtn.setEnabled(false);
            selectbtn.setBackgroundResource(R.drawable.done);
//            if(selectbtn!=null)
//                selectbtn1.setEnabled(false);
            if(subjectslist.size()==0){
                confirmbtn.setEnabled(false);
                Toast.makeText(getApplicationContext(),"Please Enter the subject/s",Toast.LENGTH_SHORT).show();
            }
            else {
                confirmbtn.setEnabled(true);
            }

           Log.d("text",text);
        }


    public void onDelete(View v) {
        spinnerdynamic = (Spinner) ((View) v.getParent()).findViewById(R.id.spinnerdynamic) ;
        String toremove = spinnerdynamic.getSelectedItem().toString();
        parentLinearLayout.removeView((View) v.getParent());

        for(int i=0;i<subjectslist.size();i++)
        {
            if(toremove.equals(subjectslist.get(i))) {
                subjectslist.remove(i);
                break;
            }
        }
        if(subjectslist.size()==0){
            confirmbtn.setEnabled(false);
            Toast.makeText(getApplicationContext(),"Please Enter the subject/s",Toast.LENGTH_SHORT).show();
        }
        else {
            confirmbtn.setEnabled(true);
        }

    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), "Selected Class: "+users[position] ,Toast.LENGTH_SHORT).show();
         sp1= String.valueOf(spin.getSelectedItem());
        //Toast.makeText(this, sp1, Toast.LENGTH_SHORT).show();
        Log.d("show", "onItemSelected: "+sp1);
        if(sp1.contentEquals("Nursery")||sp1.contentEquals("1")||sp1.contentEquals("2")||sp1.contentEquals("3")||sp1.contentEquals("4")||sp1.contentEquals("5")){
            list=new String[]{"please enter subject ","Mathematics",
                    "Science",
                    "English",
                    "Bengali",
                    "Hindi"

            };
            Category = "NonCompetitive";
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(tutorActivity.this,android.R.layout.simple_spinner_item, list){
                @Override
                public boolean isEnabled(int position){
                    if(position == 0)
                    {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if(position == 0){
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    }
                    else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            spinnerdynamic.setAdapter(dataAdapter2);

          //  spinnerdynamic.setSelection(0,false);
          //  streamlayout.setVisibility(View.INVISIBLE);
            //editText.setHint("Please mention the stream");

          //  streamlayout.setVisibility(View.INVISIBLE);
            //editText.setHint("Please mention the stream");
            Toast.makeText(getApplicationContext(),"Please mention the subject/s",Toast.LENGTH_SHORT).show();
            String [] list1={"Please Select Board","ICSE/ISC","CBSE","WBCHSE","WBBSE","OTHER BOARDs"};
           s2.setVisibility(View.VISIBLE);
            TextView textView=findViewById(R.id.txtV1);
            textView.setVisibility(View.VISIBLE);
           ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list1){
               @Override
               public boolean isEnabled(int position){
                   if(position == 0)
                   {
                       // Disable the first item from Spinner
                       // First item will be use for hint
                       return false;
                   }
                   else
                   {
                       return true;
                   }
               }
               @Override
               public View getDropDownView(int position, View convertView,
                                           ViewGroup parent) {
                   View view = super.getDropDownView(position, convertView, parent);
                   TextView tv = (TextView) view;
                   if(position == 0){
                       // Set the hint text color gray
                       tv.setTextColor(Color.GRAY);
                   }
                   else {
                       tv.setTextColor(Color.BLACK);
                   }
                   return view;
               }
           };
           dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
           s2.setAdapter(dataAdapter);
          // s2.setSelection(0,false);
            streamlayout.setVisibility(View.INVISIBLE);
            parentLinearLayout.setVisibility(View.VISIBLE);
            LinearLayout layout=findViewById(R.id.button_layout);
            layout.setVisibility(View.VISIBLE);
        }
        if(sp1.contentEquals("6")||sp1.contentEquals("7")||sp1.contentEquals("8")){
            list= new String[]{ "Please Select Subject", "Physics",
                    "Chemistry",
                    "Biology",
                    "Mathematics",
                    "History/Cvics",
                    "Geography",
                    "Computer Science",
                    "English",
                    "Bengali",
                    "Sanskrit",
                    "Hindi",
                    "Physical Science"
            };
            Category = "NonCompetitive";
            s2.setVisibility(View.VISIBLE);
            TextView textView=findViewById(R.id.txtV1);
            textView.setVisibility(View.VISIBLE);
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(tutorActivity.this,android.R.layout.simple_spinner_item, list){
                @Override
                public boolean isEnabled(int position){
                    if(position == 0)
                    {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if(position == 0){
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    }
                    else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            spinnerdynamic.setAdapter(dataAdapter2);
          //  spinnerdynamic.setSelection(0,false);
          //  streamlayout.setVisibility(View.INVISIBLE);
           // editText.setHint("Please mention the stream");
            String [] list1={"Please Select Board","ICSE/ISC","CBSE","WBCHSE","WBBSE","OTHER BOARDs"};

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list1){
                @Override
                public boolean isEnabled(int position){
                    if(position == 0)
                    {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if(position == 0){
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    }
                    else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            s2.setAdapter(dataAdapter);
           // s2.setSelection(0,false);
            streamlayout.setVisibility(View.INVISIBLE);
            parentLinearLayout.setVisibility(View.VISIBLE);
            LinearLayout layout=findViewById(R.id.button_layout);
            layout.setVisibility(View.VISIBLE);

           // Toast.makeText(getApplicationContext(),"Please mention the subject/s",Toast.LENGTH_SHORT).show();
        }
        if(sp1.contentEquals("9")||sp1.contentEquals("10")||sp1.contentEquals("11")||sp1.contentEquals("12")){
            list=new String[]{"Please Select Subject" ,"Physics",
                    "Chemistry",
                    "Biology",
                    "Mathematics",
                    "History/Cvics",
                    "Geography",
                    "Computer Science",
                    "English",
                    "Bengali",
                    "Sanskrit",
                    "Hindi",
                    "Physical Science"};
            Category = "NonCompetitive";
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(tutorActivity.this,android.R.layout.simple_spinner_item, list){
                @Override
                public boolean isEnabled(int position){
                    if(position == 0)
                    {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if(position == 0){
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    }
                    else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            spinnerdynamic.setAdapter(dataAdapter2);
           // spinnerdynamic.setSelection(0,false);
           // streamlayout.setVisibility(View.INVISIBLE);
            String [] list1={"Select Board","ICSE/ISC","CBSE","WBCHSE","WBBSE","OTHER BOARDs"};
            s2.setVisibility(View.VISIBLE);
            TextView textView=findViewById(R.id.txtV1);
            textView.setVisibility(View.VISIBLE);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list1){
                @Override
                public boolean isEnabled(int position){
                    if(position == 0)
                    {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if(position == 0){
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    }
                    else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            s2.setAdapter(dataAdapter);
            s2.setSelection(0,false);
            streamlayout.setVisibility(View.INVISIBLE);
            parentLinearLayout.setVisibility(View.VISIBLE);
            LinearLayout layout=findViewById(R.id.button_layout);
            layout.setVisibility(View.VISIBLE);
            // editText.setHint("Please mention the stream");
          //  Toast.makeText(getApplicationContext(),"Please mention the subject/s",Toast.LENGTH_SHORT).show();
        }

//        if(sp1.contentEquals("Chess")){
//            String[] list1=new String[]{ "Ratings 1000-1300",
//                    "Ratings 1300-1600",
//                    "Ratings 1600-1900",
//                    "Ratings 2000-2200",
//                    "Ratings 2200-2500",
//                   };
//            Category = "NonCompetitive";
//            TextView textView=findViewById(R.id.txtV1);
//            textView.setText("Select Ratings");
//            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(tutorActivity.this,android.R.layout.simple_spinner_item, list1);
//            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            dataAdapter2.notifyDataSetChanged();
//           s2.setAdapter(dataAdapter2);
//            s2.setSelection(0,false);
//            parentLinearLayout.setVisibility(View.GONE);
//            addfield.setVisibility(View.GONE);
//           // streamlayout.setVisibility(View.INVISIBLE);
//            //editText.setHint("Please mention the stream");
//            Toast.makeText(getApplicationContext(),"Please mention the subject/s",Toast.LENGTH_SHORT).show();
//        }
//        if(sp1.contentEquals("Nursery")){
//            String [] list={"Please mention the subject/s  below"};
//            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_spinner_item, list);
//            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            dataAdapter2.notifyDataSetChanged();
//            s2.setAdapter(dataAdapter2);
//            s2.setSelection(0,false);
//            streamlayout.setVisibility(View.INVISIBLE);
//            editText.setHint("Please mention the stream");
//            Toast.makeText(getApplicationContext(),"Please mention the subject/s",Toast.LENGTH_SHORT).show();
//        }
//        if(sp1.contentEquals("1")||sp1.contentEquals("2")||sp1.contentEquals("3")||sp1.contentEquals("4")||sp1.contentEquals("5")||sp1.contentEquals("6")||sp1.contentEquals("7")||sp1.contentEquals("8")||sp1.contentEquals("9")||sp1.contentEquals("10")||sp1.contentEquals("11")||sp1.contentEquals("12")) {
//           // List<String> list = new ArrayList<String>();
//            //list.add("Salary");//You should add items from db here (first spinner)
//           String [] list={"ICSE/ISC","CBSE","WBCHSE","WBBSE","OTHER BOARDs"};
//
//            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_spinner_item, list);
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            dataAdapter.notifyDataSetChanged();
//            s2.setAdapter(dataAdapter);
//           // s2.setSelection(0,false);
//            streamlayout.setVisibility(View.INVISIBLE);
//        }

       if(sp1.contentEquals("Graduate")||sp1.contentEquals("Master's")||sp1.contentEquals("Graduate")) {
            //List<String> list = new ArrayList<String>();
           //list.add("Conveyance");//you should add items from db here(2nd spinner)
           Category = "NonCompetitive";
            list=new String[]{"Please mention the subject/s and Stream below"};
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                  android.R.layout.simple_spinner_item, list);
          dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         s2.setVisibility(View.INVISIBLE);
           TextView textView=findViewById(R.id.txtV1);
           textView.setVisibility(View.INVISIBLE);
            dataAdapter2.notifyDataSetChanged();
            s2.setAdapter(dataAdapter2);
           streamlayout.setVisibility(View.VISIBLE);
           parentLinearLayout.setVisibility(View.GONE);
           LinearLayout layout=findViewById(R.id.button_layout);
           layout.setVisibility(View.GONE);
           s2.setSelection(0,false);
            editText.setHint("Please mention the stream");
            editText.setTextSize(12);

           Toast.makeText(getApplicationContext(),"Please mention the subject/s and Stream below",Toast.LENGTH_SHORT).show();
        }
       if(sp1.contentEquals("Competitive Exams")) {

           Category = "Competitive";
            //List<String> list = new ArrayList<String>();
           //list.add("Conveyance");//you should add items from db here(2nd spinner)
             list=new String[]{"Please mention the subject/s and Exam name/s below seperated by commas(if multiple exams)"};
             s2.setVisibility(View.INVISIBLE);
           TextView textView=findViewById(R.id.txtV1);
           textView.setVisibility(View.INVISIBLE);
           ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                   android.R.layout.simple_spinner_item, list);
           dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            s2.setAdapter(dataAdapter2);
           streamlayout.setVisibility(View.VISIBLE);
           parentLinearLayout.setVisibility(View.GONE);
           LinearLayout layout=findViewById(R.id.button_layout);
           layout.setVisibility(View.GONE);
        //   confirmbtn.setEnabled(true);
            s2.setSelection(0,false);
           editText.setHint("Please mention Exam name/s seperated by commas");
           Toast.makeText(getApplicationContext(),"Please mention the subject/s and Exam name/s below seperated by commas(if multiple exams)",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
        Toast.makeText(getApplicationContext(),"Select any single category",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        Log.d("tutor", "onResume: ");
        subjectslist.clear();
        confirmbtn.setEnabled(false);
        //Refresh your stuff here
    }
}
