package com.sarita.dashin;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sarita.dashin.adapters.SelectCategoryAdapter;
import com.sarita.dashin.adapters.TimeTableRecyclerView;
import com.sarita.dashin.models.CategoryDataClass;
import com.sarita.dashin.models.ItemData;
import com.sarita.dashin.models.MenuTime;
import com.sarita.dashin.utils.Constants;

import org.joda.time.DateTime;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectItems extends AppCompatActivity {

    RecyclerView recyclerView;
    SelectCategoryAdapter adapter;
    TextView start,end;
    EditText name;
    FloatingActionButton save;
    Spinner timings;
    public static Map<String,ArrayList<String>> menu;
    FirebaseFirestore db;
    CollectionReference ref ;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_items);
        final HashMap<String,String> Starttime = new HashMap<>();
        final HashMap<String,String> Endtime = new HashMap<>();
        timings=findViewById(R.id.timings);
        db = FirebaseFirestore.getInstance();
        ref = db.collection("VENDORS/"+Constants.mAuth.getCurrentUser().getPhoneNumber()+"/MENU_TIME");

        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    List<String> timename = new ArrayList<>();
                    QuerySnapshot document = task.getResult();
                        MenuTime menuTime = document.getDocuments().get(0).toObject(MenuTime.class);
                        MenuTime menuTime1 = document.getDocuments().get(1).toObject(MenuTime.class);
                        if(menuTime.getBreakfast()!=null)
                        {
                            Starttime.put("Breakfast",menuTime.getBreakfast());
                            Endtime.put("Breakfast",menuTime1.getBreakfast());
                            timename.add("Breakfast");
                        }

                        if(menuTime.getLunch()!=null)
                        {
                            Starttime.put("Lunch",menuTime.getLunch());
                            Endtime.put("Lunch",menuTime1.getLunch());
                            timename.add("Lunch");
                        }

                        if(menuTime.getDinner()!=null)
                        {
                            Starttime.put("Dinner",menuTime.getDinner());
                            Endtime.put("Dinner",menuTime1.getDinner());
                            timename.add("Dinner");
                        }
                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SelectItems.this, android.R.layout.simple_spinner_item, timename);

                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to spinner
                    timings.setAdapter(dataAdapter);

                    if(TimeTable.caller.equals("manage"))
                    {
                        for (int i=0;i<timename.size();i++)
                        {
                            if(Starttime.get(timename.get(i)).equals(TimeTableRecyclerView.current.getSTART_TIME()))
                            {
                                timings.setSelection(i);
                            }
                        }
                    }
                } else {

                }
            }
        });

        menu=new HashMap<>();
        adapter= new SelectCategoryAdapter(makeCATRecyclerView(Constants.mAuth.getCurrentUser().getPhoneNumber() ));
        recyclerView= findViewById(R.id.cat_list);
//        start=findViewById(R.id.time_start);
//        end=findViewById(R.id.time_stop);
        name=findViewById(R.id.name_of_draft);
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(SelectItems.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        start.setText(selectedHour + ":" + selectedMinute);
//                    }
//                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Start Time");
//                mTimePicker.show();
//
//            }
//        });
//        end.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(SelectItems.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        end.setText(selectedHour + ":" + selectedMinute);
//                    }
//                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Stop time");
//                mTimePicker.show();
//
//            }
//        });
        if(TimeTable.caller.equals("manage"))
        {
            name.setText(TimeTableRecyclerView.current.getNAME());

            name.setEnabled(false);
        }
        save=findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String st,en,nm;
               // st=start.getText().toString();
                st=Starttime.get(timings.getSelectedItem());
                //en=end.getText().toString();
                en=Endtime.get(timings.getSelectedItem());
                nm=name.getText().toString();
                if(st.equals("")||st.equals(null)||en.equals("")||en.equals(null)||nm.equals("")||nm.equals(null))
                {
                    Toast.makeText(getApplicationContext(), "Please fill text/time details", Toast.LENGTH_LONG).show();
                }
                else {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(SelectItems.this);
                    builder.setMessage("This menu will appear from " + st + " to " + en).setTitle("Are you sure?")
                            .setCancelable(false)
                            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Map p = new HashMap();
                                    p.put("MENU", menu);
                                    p.put("START_TIME", st);
                                    p.put("END_TIME", en);
                                    p.put("NAME", name.getText().toString());
                                    p.put("LIVE", false);
                                    System.out.println(p);
                                    System.out.println(name.getText().toString());
                                    FirebaseFirestore.getInstance().collection("VENDOR-MENU/"+ Constants.mAuth.getCurrentUser().getPhoneNumber() +"/MENU-LIST").document(name.getText().toString()).set(p);
                                    finish();
                                }
                            })
                            .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    FirestoreRecyclerOptions<CategoryDataClass> makeCATRecyclerView(String number)
    {
        Query query = FirebaseFirestore.getInstance().collection("VENDOR-MENU/"+number+"/CAT");
        FirestoreRecyclerOptions<CategoryDataClass> options = new FirestoreRecyclerOptions
                .Builder<CategoryDataClass>()
                .setQuery(query,CategoryDataClass.class)
                .build();
        return options;
    }
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
