package com.sarita.dashin.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.sarita.dashin.Additems;
import com.sarita.dashin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sarita.dashin.models.ModelMenuItem;
import com.sarita.dashin.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class ItemDataRecyclerView extends FirestoreRecyclerAdapter<ModelMenuItem,ItemDataRecyclerView.ItemsHolder> {
    public ItemDataRecyclerView(@NonNull FirestoreRecyclerOptions<ModelMenuItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ItemsHolder holder, final int position, @NonNull final ModelMenuItem model) {

        holder.price.setText(null);
        holder.price.setText(" ₹ "+model.getPrice());
        holder.name.setText(model.getName());
        holder.desc.setText(model.getDescription());
        if(!model.getVEG())
            holder.veg.setText("Nonveg");
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(holder.itemView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_menu_item_form);
                dialog.setCancelable(false);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ImageView close=dialog.findViewById(R.id.close);
                final TextView type=dialog.findViewById(R.id.menuType);
                final EditText name=dialog.findViewById(R.id.item_name);
                final EditText description=dialog.findViewById(R.id.item_descirption);
                final EditText price =dialog.findViewById(R.id.menu_item_price);
                final CheckBox veg =dialog.findViewById(R.id.vegNonveg);
                ImageView submit=dialog.findViewById(R.id.submitDiscount);
                type.setText(model.getType());
                name.setText(model.getName());
                description.setText(model.getDescription());
                price.setText(model.getPrice()+"");
                if(model.getVEG())
                    veg.setChecked(true);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (name.getText().toString().equals(""))
                        {
                            name.setError("Enter Name");
                            return;
                        }
                        if (price.getText().toString().equals(""))
                        {
                            price.setError("Enter Description");
                            return;
                        }
                        if (description.getText().toString().equals(""))
                        {
                            description.setError("Enter Price");
                            return;
                        }
                        String doc_id = getSnapshots().getSnapshot(position).getId();
                        HashMap<String,Object> discount = new HashMap<>();
                        discount.put("Name",name.getText().toString());
                        discount.put("Description",description.getText().toString());
                        discount.put("Price",Integer.parseInt(price.getText().toString()));
                        discount.put("VEG",veg.isSelected());
                        Constants.mFirestore.collection("VENDORS")
                                .document(Constants.mAuth.getCurrentUser().getPhoneNumber())
                                .collection("MENU").document(doc_id).update(discount);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setMessage("Deleting this item...").setTitle("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String doc_id = getSnapshots().getSnapshot(position).getId();
                                Constants.mFirestore.collection("VENDORS")
                                        .document(Constants.mAuth.getCurrentUser().getPhoneNumber())
                                        .collection("MENU")
                                        .document(doc_id)
                                        .delete();
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

    void addField(final Context context, ModelMenuItem model, final int position)
    {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mess_open_dialog);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView close=dialog.findViewById(R.id.close);
        final TextView type=dialog.findViewById(R.id.menuType);
        final EditText name=dialog.findViewById(R.id.item_name);
        final EditText description=dialog.findViewById(R.id.item_descirption);
        final EditText price =dialog.findViewById(R.id.menu_item_price);
        final CheckBox veg =dialog.findViewById(R.id.vegNonveg);
        ImageView submit=dialog.findViewById(R.id.submitDiscount);
        type.setText(model.getType());
        name.setText(model.getName());
        description.setText(model.getDescription());
        price.setText(model.getPrice()+"");
        if(model.getVEG())
            veg.setActivated(true);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().equals(""))
                {
                    Toast.makeText(context,"Enter Name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (price.getText().toString().equals(""))
                {
                    Toast.makeText(context,"Enter Description",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (description.getText().toString().equals(""))
                {
                    Toast.makeText(context,"Enter Price",Toast.LENGTH_SHORT).show();
                    return;
                }

                String doc_id = getSnapshots().getSnapshot(position).getId();
                HashMap<String,Object> discount = new HashMap<>();
                discount.put("Type",context);
                discount.put("Name",name.getText().toString());
                discount.put("Description",description.getText().toString());
                discount.put("Price",Integer.parseInt(price.getText().toString()));
                discount.put("Veg",veg.isSelected());
                Constants.mFirestore.collection("VENDORS")
                        .document(Constants.mAuth.getCurrentUser().getPhoneNumber())
                        .collection("MENU").document(doc_id).update(discount);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @NonNull
    @Override
    public ItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent,false);
        return new ItemDataRecyclerView.ItemsHolder(view);
    }

    class ItemsHolder extends RecyclerView.ViewHolder{
        TextView name,price,desc,veg;
        ImageButton save,delete;
        public ItemsHolder(@NonNull final View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.item_name);
            price=itemView.findViewById(R.id.menu_item_price);
            desc=itemView.findViewById(R.id.item_desc);
            save=itemView.findViewById(R.id.save_item);
            delete=itemView.findViewById(R.id.deleteItem);
            veg=itemView.findViewById(R.id.menu_vegNonVeg);
        }
    }
}
