package com.sarita.dashin.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import com.sarita.dashin.R;
import com.sarita.dashin.models.Subscription;
import com.sarita.dashin.utils.Constants;


public class SubscriptionAdapter extends FirestoreRecyclerAdapter<Subscription, SubscriptionAdapter.subscriptionHolder> {
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ObservableSnapshotArray<Subscription> mSnapshots;
    public SubscriptionAdapter(@NonNull FirestoreRecyclerOptions<Subscription> options, Context context) {
        super(options);
        this.context=context;
        mSnapshots=options.getSnapshots();
    }


    @Override
    protected void onBindViewHolder(@NonNull final subscriptionHolder holder, final int position, @NonNull final Subscription model) {
        holder.CustomerName.setText("CustName : "+model.getCustomerName());
        holder.CustomerID.setText("CustID : "+model.getCustomerID());
        holder.FromDate.setText("From : "+model.getFromDate());
        holder.ToDate.setText("To : "+model.getToDate());
        holder.Price.setText("Price : "+model.getPrice());
        holder.Type.setText("Type : "+model.getType());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                db.collection("VENDORS/"+ Constants.mAuth.getCurrentUser().getPhoneNumber() +"/Subscriptions").document(mSnapshots.getSnapshot(holder.getAdapterPosition()).getId())
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context,"Subscription Deleted !",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    @NonNull
    @Override
    public subscriptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_row,parent,false);
        return new subscriptionHolder(v);
    }

    class subscriptionHolder extends RecyclerView.ViewHolder
    {
        TextView CustomerName,CustomerID,Price,ToDate,FromDate,Type;
        ImageView delete;
        public subscriptionHolder(@NonNull View itemView) {
            super(itemView);
            CustomerID=itemView.findViewById(R.id.customerID);
            CustomerName=itemView.findViewById(R.id.customerName);
            Price=itemView.findViewById(R.id.price);
            ToDate=itemView.findViewById(R.id.to);
            FromDate=itemView.findViewById(R.id.from);
            Type=itemView.findViewById(R.id.type);
            delete=itemView.findViewById(R.id.cancel_button);
        }
    }

}
