package com.sarita.dashin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sarita.dashin.adapters.CategoriesRecyclerView;
import com.sarita.dashin.adapters.ItemDataRecyclerView;
import com.sarita.dashin.models.ItemData;
import com.sarita.dashin.models.ModelMenuItem;
import com.sarita.dashin.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class Additems extends AppCompatActivity {

    public long ItemArraySize;
    FloatingActionButton addButton;
    String CAT_ID;
    ItemDataRecyclerView ItemRecyclerView;
    String context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additems);

        Intent intent = getIntent();
        context = intent.getStringExtra("Context");
        getSupportActionBar().setTitle(context);

        addButton=findViewById(R.id.add_button_in_menu_items);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addField(CAT_ID);
            }
        });



        Query query =  Constants.mFirestore.collection("VENDORS")
                .document(Constants.mAuth.getCurrentUser().getPhoneNumber())
                .collection("MENU")
                .whereEqualTo("Type", context);

        FirestoreRecyclerOptions<ModelMenuItem> options =  new FirestoreRecyclerOptions
                .Builder<ModelMenuItem>()
                .setQuery(query,ModelMenuItem.class)
                .build();

        ItemRecyclerView=new ItemDataRecyclerView(options);
        RecyclerView recyclerView= findViewById(R.id.items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ItemRecyclerView);


    }

    void addField(String CAT_ID)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(Additems.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(Additems.this).inflate(R.layout.add_menu_item_form, viewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        ImageView close=dialogView.findViewById(R.id.close);
        final TextView type=dialogView.findViewById(R.id.menuType);
        final EditText name=dialogView.findViewById(R.id.item_name);
        final EditText description=dialogView.findViewById(R.id.item_descirption);
        final EditText price =dialogView.findViewById(R.id.menu_item_price);
        final CheckBox veg =dialogView.findViewById(R.id.vegNonveg);
        ImageView submit=dialogView.findViewById(R.id.submitDiscount);
        type.setText(context);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().equals(""))
                {
                    Toast.makeText(Additems.this,"Enter Name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (price.getText().toString().equals(""))
                {
                    Toast.makeText(Additems.this,"Enter Description",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (description.getText().toString().equals(""))
                {
                    Toast.makeText(Additems.this,"Enter Price",Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String,Object> discount = new HashMap<>();
                discount.put("Type",context);
                discount.put("Name",name.getText().toString());
                discount.put("Description",description.getText().toString());
                discount.put("Price",Integer.parseInt(price.getText().toString()));
                discount.put("VEG",veg.isChecked());
                Constants.mFirestore.collection("VENDORS")
                        .document(Constants.mAuth.getCurrentUser().getPhoneNumber())
                        .collection("MENU").add(discount).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(Additems.this,"Item Added !",Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.dismiss();
                            }



                });

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
    @Override
    protected void onStart() {
        super.onStart();
        ItemRecyclerView.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        ItemRecyclerView.stopListening();
    }
}
