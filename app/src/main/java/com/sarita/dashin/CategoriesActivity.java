package com.sarita.dashin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sarita.dashin.adapters.CategoriesRecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sarita.dashin.models.CategoryDataClass;

import java.util.HashMap;
import java.util.Map;

public class CategoriesActivity extends AppCompatActivity {

    RecyclerView listViewOfCategories;
    CategoriesRecyclerView categoriesRecyclerView;
    static long ArraySize;
    FloatingActionButton addButton;

    CardView thali, rice, roti, sabji, sweets, beverages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Your menu");

        thali = findViewById(R.id.thaliLayout);
        rice = findViewById(R.id.ricePulavLayout);
        roti = findViewById(R.id.rotiChapatiLayout);
        sabji = findViewById(R.id.sabji_layout);
        sweets = findViewById(R.id.sweetsLayout);
        beverages = findViewById(R.id.beveragesLayout);

        thali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onButtonClicked("Thali"); }});

        rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onButtonClicked("Rice"); }});

        roti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onButtonClicked("Roti"); }});

        sabji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onButtonClicked("Sabji"); }});

        sweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onButtonClicked("Sweets"); }});

        beverages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onButtonClicked("Beverages"); }});


//        addButton=(FloatingActionButton)findViewById(R.id.add_button_in_categories);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addField("9998887776");
//            }
//        });
//        categoriesRecyclerView=new CategoriesRecyclerView(makeCategoriesRecyclerView());
//        RecyclerView recyclerView= findViewById(R.id.categories);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(categoriesRecyclerView);
        /*
        */
    }
//    void addField(String number)
//    {
//        Map<String,Object> m = new HashMap();
//        m.put("CAT_NAME","");
//        m.put("IS_THALI",false);
//        m.put("CAT_ID","CAT-"+(ArraySize+1));
//        FirebaseFirestore.getInstance().collection("VENDOR-MENU/9998887776/CAT").document("CAT-"+(ArraySize+1)).set(m);
//        FirebaseFirestore.getInstance().collection("VENDOR-MENU").document("9998887776").update("CAT-COUNT",++ArraySize);
//    }
//    FirestoreRecyclerOptions<CategoryDataClass> makeCategoriesRecyclerView()
//    {
//        Query query = FirebaseFirestore.getInstance().collection("VENDOR-MENU/9998887776/CAT");
//        FirebaseFirestore.getInstance().collection("VENDOR-MENU").document("9998887776").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                ArraySize=documentSnapshot.getLong("CAT-COUNT");
//            }
//        });
//        FirestoreRecyclerOptions<CategoryDataClass> options =  new FirestoreRecyclerOptions
//                .Builder<CategoryDataClass>()
//                .setQuery(query,CategoryDataClass.class)
//                .build();
//        return options;
//    }


    private void onButtonClicked(String str){

        Intent intent = new Intent(CategoriesActivity.this, Additems.class);
        intent.putExtra("Context", str);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //categoriesRecyclerView.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // categoriesRecyclerView.stopListening();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
