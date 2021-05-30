package com.sarita.dashin.adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sarita.dashin.Additems;
import com.sarita.dashin.R;
import com.sarita.dashin.SelectItems;
import com.sarita.dashin.TimeTable;
import com.sarita.dashin.models.TimeTableData;
import com.sarita.dashin.utils.Constants;

import org.w3c.dom.Document;

public class TimeTableRecyclerView extends FirestoreRecyclerAdapter<TimeTableData, TimeTableRecyclerView.TimeTableHolder> {

    public TimeTableRecyclerView(@NonNull FirestoreRecyclerOptions<TimeTableData> options) {
        super(options);
    }
    public static TimeTableData current;
    @Override
    protected void onBindViewHolder(@NonNull final TimeTableHolder holder, int position, @NonNull final TimeTableData model) {
        holder.t.setText(model.getNAME());
        holder.from.setText(model.getSTART_TIME());
        holder.to.setText(model.getEND_TIME());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), SelectItems.class));
                current=model;
            }
        });
        if (model.isLIVE())
            holder.live.setText("LIVE");
        else
            holder.live.setText("");
        holder.live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setMessage("Set this menu as live menu").setTitle("Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseFirestore.getInstance().collection("VENDOR-MENU/"+ Constants.CurrentUser.getOWNER_CONTACT() +"/MENU-LIST").document(model.getNAME()).update("LIVE",true);
                                DeLiven(model.getNAME());
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
    public TimeTableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_menu_list,parent,false);
        return new TimeTableRecyclerView.TimeTableHolder(view);
    }
    class TimeTableHolder extends RecyclerView.ViewHolder{
        TextView t,from,to;
        AppCompatButton live;
        ImageView edit;
        public TimeTableHolder(@NonNull View itemView) {
            super(itemView);
            t=itemView.findViewById(R.id.title);
            edit=itemView.findViewById(R.id.edit);
            live=itemView.findViewById(R.id.live);
            from=itemView.findViewById(R.id.from);
            to=itemView.findViewById(R.id.to);
        }
    }
    void DeLiven(final String id)
    {
        FirebaseFirestore.getInstance().collection("VENDOR-MENU/9998887776/MENU-LIST").whereEqualTo("LIVE",true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot d:queryDocumentSnapshots.getDocuments()) {
                    if(!d.getString("NAME").equals(id))
                    FirebaseFirestore.getInstance().collection("VENDOR-MENU/9998887776/MENU-LIST").document(d.getString("NAME")).update("LIVE", false);
                }
            }
        });
    }
}
