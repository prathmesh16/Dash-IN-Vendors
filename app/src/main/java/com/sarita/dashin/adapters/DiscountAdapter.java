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
import com.sarita.dashin.models.Discount;
import com.sarita.dashin.utils.Constants;


public class DiscountAdapter extends FirestoreRecyclerAdapter<Discount, DiscountAdapter.discountHolder> {
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ObservableSnapshotArray<Discount> mSnapshots;
    public DiscountAdapter(@NonNull FirestoreRecyclerOptions<Discount> options, Context context) {
        super(options);
        this.context=context;
        mSnapshots=options.getSnapshots();
    }


    @Override
    protected void onBindViewHolder(@NonNull final discountHolder holder, final int position, @NonNull final Discount model) {
        holder.type.setText("Type : "+model.getType());
        holder.code.setText("Code : "+model.getCode());
        holder.value.setText("Value : "+model.getOFFP());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                db.collection("VENDORS/"+ Constants.mAuth.getCurrentUser().getPhoneNumber() +"/Discounts").document(mSnapshots.getSnapshot(holder.getAdapterPosition()).getId())
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context,"Discount Deleted !",Toast.LENGTH_SHORT).show();
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
    public discountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_row,parent,false);
        return new discountHolder(v);
    }

    class discountHolder extends RecyclerView.ViewHolder
    {
        TextView type,code,value;
        ImageView delete;
        public discountHolder(@NonNull View itemView) {
            super(itemView);
            type=itemView.findViewById(R.id.discountType);
            code=itemView.findViewById(R.id.discountCode);
            delete=itemView.findViewById(R.id.deleteDiscount);
            value=itemView.findViewById(R.id.discountValue);
        }
    }

}
