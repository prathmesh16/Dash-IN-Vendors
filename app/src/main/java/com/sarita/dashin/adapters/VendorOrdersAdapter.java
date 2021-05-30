package com.sarita.dashin.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sarita.dashin.R;
import com.sarita.dashin.models.ModelOrder;
import com.sarita.dashin.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VendorOrdersAdapter extends FirestoreRecyclerAdapter<ModelOrder, VendorOrdersAdapter.OrdersHolder> {


    private VendorOrdersAdapter.ClickListener listener;
    String Vendordoc= "6KzwVrPx4KUaVtqYaA0ou7FmWVI3";
    String docId = "";

    @Override
    protected void onBindViewHolder(@NonNull final OrdersHolder holder, final int position, @NonNull final ModelOrder model) {

        Log.e("tags", model.getFROM());

        holder.time.setText(String.valueOf(model.getTIME().getSeconds()));

        Long l1 = model.getAMOUNT();
        if(l1 != null) {
            holder.amount.setText("₹" + model.getAMOUNT());
        }

        Long l2 = model.getORDER_ID();
        if(l2 != -1) {
            holder.sub_ll.setVisibility(View.GONE);
            holder.order_id.setText(String.valueOf(model.getORDER_ID()));
            holder.method.setText(model.getMETHOD());
        }
        else{
            holder.order_ll.setVisibility(View.GONE);
            holder.subscription.setText(String.valueOf(model.getSUBSCRIPTION()));
            if(model.isPARCEL())
                holder.type.setText("Parcel");
            else
                holder.type.setText("Pre-order");
        }

       docId = getSnapshots().getSnapshot(holder.getAdapterPosition()).getId();
        Constants.mFirestore.collection("/VENDORS/"+Vendordoc+"/Orders/" + docId + "/DETAILS")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            return;
                        } else {
                            StringBuffer buffer = new StringBuffer("");
                            List<DocumentSnapshot> list = documentSnapshots.getDocuments();
                            for (DocumentSnapshot doc : documentSnapshots) {
                                buffer.append(doc.getString("Name")+" x "+doc.getLong("Quantity").toString()+ ", ");
                            }
                            buffer.deleteCharAt(buffer.length()-2);
                            holder.details.setText(buffer.toString());
                        }
                    }
                });



    }

    @NonNull
    @Override
    public OrdersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_oders_item,parent,false);
        return new OrdersHolder(view);
    }

    public void changeStatusOfOrder(boolean accepted, int position) {
        String docId = getSnapshots().getSnapshot(position).getId();
        Map<String, Object> map = new HashMap<>();
        if(accepted==true) {
            map.put("SERVED", new Boolean(true));
        }else {
            map.put("SERVED", new Boolean(false));
            //notify user the order has been rejected
            //delete this order from collection later
        }
            Constants.mFirestore.collection("VENDORS")
                    .document(Vendordoc)
                    .collection("Orders")
                    .document(docId)
                    .update(map);
        notifyItemRemoved(position);
    }

    public void undo(int position) {
        String docId = getSnapshots().getSnapshot(position).getId();
        Map<String, Object> map = new HashMap<>();
            map.put("SERVED", new Boolean(false));
        Constants.mFirestore.collection("VENDORS")
                .document(Vendordoc)
                .collection("Orders")
                .document(docId)
                .update(map);
        notifyItemInserted(position);
    }

    class OrdersHolder extends RecyclerView.ViewHolder{
        TextView time, order_id, method, subscription, type, amount, details;
        TextView accept, reject;
        LinearLayout order_ll,sub_ll;

        public OrdersHolder(@NonNull final View itemView) {
            super(itemView);
            time=(TextView)itemView.findViewById(R.id.time);
            order_id=(TextView)itemView.findViewById(R.id.order_id);
            method=(TextView)itemView.findViewById(R.id.method);
            subscription=(TextView)itemView.findViewById(R.id.subscription);
            type=(TextView)itemView.findViewById(R.id.type);
            amount=(TextView) itemView.findViewById(R.id.amount);
            details=(TextView)itemView.findViewById(R.id.order_details);
            accept=itemView.findViewById(R.id.order_accept_btn);
            reject=itemView.findViewById(R.id.order_reject_btn);
            order_ll=itemView.findViewById(R.id.order_LL);
            sub_ll=itemView.findViewById(R.id.sub_LL);

        }
    }
    public VendorOrdersAdapter(@NonNull FirestoreRecyclerOptions<ModelOrder> options, VendorOrdersAdapter.ClickListener listener ) {
        super(options);
        this.listener = listener;
        Log.e("Tags", "adapter constr");

    }


    public interface ClickListener {
        void onPositionClicked(int position);

    }


}

