package com.sarita.dashin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sarita.dashin.adapters.SubscriptionAdapter;
import com.sarita.dashin.models.Subscription;
import com.sarita.dashin.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class SubscriptionsList extends AppCompatActivity {

    private RecyclerView subscriptionView;
    private FirebaseFirestore db;
    private CollectionReference subscriptionRef;
    private SubscriptionAdapter subscriptionAdapter;
    private FloatingActionButton addSubscrition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions_list);
        getSupportActionBar().setTitle("Subscriptions");
        subscriptionView=findViewById(R.id.subscriptionList);
        db= FirebaseFirestore.getInstance();

        subscriptionRef=db.collection("VENDORS/"+ Constants.mAuth.getCurrentUser().getPhoneNumber() +"/Subscriptions");
        Query query = subscriptionRef.orderBy("Type",Query.Direction.ASCENDING);;

        FirestoreRecyclerOptions<Subscription> options=new FirestoreRecyclerOptions.Builder<Subscription>()
                .setQuery(query, Subscription.class)
                .build();
        subscriptionAdapter = new SubscriptionAdapter(options,this);
        subscriptionView.setLayoutManager(new LinearLayoutManager(this));
        subscriptionView.setAdapter(subscriptionAdapter);
        addSubscrition = findViewById(R.id.addSubscrition);
        addSubscrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SubscriptionsList.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(SubscriptionsList.this).inflate(R.layout.add_subscription_form, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                ImageView close=dialogView.findViewById(R.id.close);
                final TextView From = dialogView.findViewById(R.id.From);
                final TextView To = dialogView.findViewById(R.id.To);
                final EditText Name=dialogView.findViewById(R.id.subscriptionCustomerName);
                final EditText ID = dialogView.findViewById(R.id.subscriptionCustomerID);
                final EditText Price = dialogView.findViewById(R.id.subscriptionPrice);
                final Spinner Type = dialogView.findViewById(R.id.subscriptionType);
                ImageView submit=dialogView.findViewById(R.id.submitSubscription);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                final Calendar myCalendar = Calendar.getInstance();


                final DatePickerDialog.OnDateSetListener dateFrom = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd/MM/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        From.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                final DatePickerDialog.OnDateSetListener dateTo = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd/MM/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        To.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                From.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(SubscriptionsList.this, dateFrom, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                To.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(SubscriptionsList.this, dateTo, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Type.getSelectedItemPosition()==0)
                        {
                            Toast.makeText(SubscriptionsList.this,"Select Subscription Type",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (Price.getText().toString().equals(""))
                        {
                            Toast.makeText(SubscriptionsList.this,"Enter Price",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (Name.getText().toString().equals(""))
                        {
                            Toast.makeText(SubscriptionsList.this,"Enter Customer Name",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (ID.getText().toString().equals(""))
                        {
                            Toast.makeText(SubscriptionsList.this,"Enter Customer Number",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (From.getText().toString().equals("Start Date"))
                        {
                            Toast.makeText(SubscriptionsList.this,"Enter Start Date",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (To.getText().toString().equals("End Date"))
                        {
                            Toast.makeText(SubscriptionsList.this,"Enter End Date",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Query query1=db.collection("CUSTOMER").whereEqualTo("NUMBER", ID.getText().toString());
                        query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    QuerySnapshot documentSnapshot = task.getResult();
                                    if(documentSnapshot != null && !documentSnapshot.isEmpty()) {
                                         HashMap<String,Object> discount = new HashMap<>();
                                        discount.put("Type",Type.getSelectedItem());
                                        discount.put("FromDate",From.getText().toString());
                                        discount.put("ToDate",To.getText().toString());
                                        discount.put("MessName",Constants.CurrentUser.getBUSI_NAME());
                                        discount.put("CustomerName",Name.getText().toString());
                                        discount.put("CustomerID",ID.getText().toString());
                                        discount.put("Price",Integer.parseInt(Price.getText().toString()));
                                        subscriptionRef.add(discount).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                Toast.makeText(SubscriptionsList.this,"Subscription Added !",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        alertDialog.dismiss();
                                    }else{
                                        Toast.makeText(SubscriptionsList.this,"Customer not exits!",Toast.LENGTH_SHORT).show();
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
        subscriptionAdapter.startListening();
    }
    @Override
    public void onStop()
    {
        super.onStop();
        subscriptionAdapter.stopListening();
    }
}
