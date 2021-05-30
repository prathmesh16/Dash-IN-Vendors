package com.sarita.dashin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sarita.dashin.adapters.DiscountAdapter;
import com.sarita.dashin.models.Discount;
import com.sarita.dashin.utils.Constants;


import java.util.HashMap;

public class MyDiscounts extends AppCompatActivity {
    private RecyclerView discountView;
    private FirebaseFirestore db;
    private CollectionReference discountRef;
    private DiscountAdapter discountAdapter;
    private FloatingActionButton addDiscount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_discounts);
        getSupportActionBar().setTitle("My Discounts");
        discountView=findViewById(R.id.discountsView);
        db= FirebaseFirestore.getInstance();

        discountRef=db.collection("VENDORS/"+ Constants.mAuth.getCurrentUser().getPhoneNumber() +"/Discounts");
        Query query = discountRef.orderBy("Code",Query.Direction.ASCENDING);;

        FirestoreRecyclerOptions<Discount> options=new FirestoreRecyclerOptions.Builder<Discount>()
                .setQuery(query,Discount.class)
                .build();
        discountAdapter = new DiscountAdapter(options,this);
        discountView.setLayoutManager(new LinearLayoutManager(this));
        discountView.setAdapter(discountAdapter);

        addDiscount=findViewById(R.id.addDiscount);
        addDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyDiscounts.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(MyDiscounts.this).inflate(R.layout.add_discount_form, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                ImageView close=dialogView.findViewById(R.id.close);
                final Spinner discountType=dialogView.findViewById(R.id.discountType);
                final EditText discountCode=dialogView.findViewById(R.id.discountCode);
                final EditText discountValue=dialogView.findViewById(R.id.discountValue);
                ImageView submit=dialogView.findViewById(R.id.submitDiscount);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (discountType.getSelectedItemPosition()==0)
                        {
                            Toast.makeText(MyDiscounts.this,"Select Discount Type",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (discountCode.getText().toString().equals(""))
                        {
                            Toast.makeText(MyDiscounts.this,"Enter Discount Code",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (discountValue.getText().toString().equals(""))
                        {
                            Toast.makeText(MyDiscounts.this,"Enter Discount Value",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Query query1=discountRef.whereEqualTo("Code", discountCode.getText().toString());
                        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    QuerySnapshot documentSnapshot = task.getResult();
                                    if(documentSnapshot != null && !documentSnapshot.isEmpty()) {
                                        Toast.makeText(MyDiscounts.this,"This Code is taken...Try with different Code!",Toast.LENGTH_SHORT).show();
                                    }else{
                                        HashMap<String,Object> discount = new HashMap<>();
                                        discount.put("Type",discountType.getSelectedItem());
                                        discount.put("Code",discountCode.getText().toString());
                                        discount.put("OFFP",Integer.parseInt(discountValue.getText().toString()));
                                        discountRef.add(discount).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                Toast.makeText(MyDiscounts.this,"Discount Added !",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        alertDialog.dismiss();
                                    }

                                }
                            }
                        });

                    }
                });
            }
        });


    }
    @Override
    public void onStart()
    {
        super.onStart();
        discountAdapter.startListening();
    }
    @Override
    public void onStop()
    {
        super.onStop();
        discountAdapter.stopListening();
    }
}
