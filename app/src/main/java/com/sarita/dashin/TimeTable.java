package com.sarita.dashin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sarita.dashin.adapters.TimeTableRecyclerView;
import com.sarita.dashin.models.CategoryDataClass;
import com.sarita.dashin.models.TimeTableData;
import com.sarita.dashin.utils.Constants;

public class TimeTable extends AppCompatActivity {

    RecyclerView recyclerView;
    TimeTableRecyclerView timeTableRecyclerView;
    FloatingActionButton addDraft;
    public static String caller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_table_layout);
        caller="manage";
        timeTableRecyclerView =new TimeTableRecyclerView(setRecyclerView());
        recyclerView=findViewById(R.id.menu_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(timeTableRecyclerView);
        addDraft=findViewById(R.id.addDiscount3);
        addDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TimeTable.this, SelectItems.class));
                caller="new";
            }
        });
    }
    FirestoreRecyclerOptions<TimeTableData> setRecyclerView()
    {
        Query query = FirebaseFirestore.getInstance().collection("VENDOR-MENU/"+ Constants.mAuth.getCurrentUser().getPhoneNumber()+"/MENU-LIST");
        FirestoreRecyclerOptions<TimeTableData> options =  new FirestoreRecyclerOptions
                .Builder<TimeTableData>()
                .setQuery(query,TimeTableData.class)
                .build();
        return options;
    }

    @Override
    protected void onStop() {
        super.onStop();
        timeTableRecyclerView.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        timeTableRecyclerView.startListening();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_items, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit_menu:
                startActivity(new Intent(TimeTable.this,CategoriesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
