package com.sarita.dashin.adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.google.android.gms.tasks.OnSuccessListener;
import com.sarita.dashin.Additems;
import com.sarita.dashin.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sarita.dashin.SelectItems;
import com.sarita.dashin.models.CategoryDataClass;
import com.sarita.dashin.utils.Constants;

import java.util.HashMap;
import java.util.Map;


public class CategoriesRecyclerView extends FirestoreRecyclerAdapter<CategoryDataClass, CategoriesRecyclerView.CategoryHolder> {

    public static CategoryDataClass currID;

    public CategoriesRecyclerView(@NonNull FirestoreRecyclerOptions<CategoryDataClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final CategoryHolder holder, int position, @NonNull final CategoryDataClass model) {
        holder.CategoryName.setText(model.getCAT_NAME());
        final boolean[] typing = {false};
        holder.CategoryName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    typing[0] = true;
                }
                if (!hasFocus && typing[0]) {
                    typing[0] = false;
                    FirebaseFirestore.getInstance().collection("VENDOR-MENU/"+ Constants.mAuth.getCurrentUser().getPhoneNumber() +"/CAT").document(model.getCAT_ID()).update("CAT_NAME", holder.CategoryName.getText().toString());
                }
            }
        });
        holder.aSwitch.setChecked(model.isIS_THALI());
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FirebaseFirestore.getInstance().collection("VENDOR-MENU/"+ Constants.mAuth.getCurrentUser().getPhoneNumber() +"/CAT").document(model.getCAT_ID()).update("IS_THALI", isChecked);
                if (isChecked)
                    holder.aSwitch.setText("Thali menu");
                else
                    holder.aSwitch.setText("Simple menu");
            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currID=model;
                holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), Additems.class));
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setMessage("Deleting this category will delete all items in it...").setTitle("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseFirestore.getInstance().collection("VENDOR-MENU/"+ Constants.mAuth.getCurrentUser().getPhoneNumber() +"/CAT").document(model.getCAT_ID()).delete();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cat,parent,false);
        return new CategoryHolder(view);
    }

    class CategoryHolder extends RecyclerView.ViewHolder{
        EditText CategoryName;
        Switch aSwitch;
        AppCompatButton button,deleteButton;
        public CategoryHolder(@NonNull final View itemView) {
            super(itemView);
            CategoryName=itemView.findViewById(R.id.category_name);
            aSwitch=itemView.findViewById(R.id.switchType);
            button=itemView.findViewById(R.id.details_button);
            deleteButton=itemView.findViewById(R.id.deleteButton);
        }
    }
}
