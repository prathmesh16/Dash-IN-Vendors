package com.sarita.dashin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.sarita.dashin.MenuCaller;
import com.sarita.dashin.SelectItems;
import com.sarita.dashin.models.CategoryDataClass;
import com.sarita.dashin.models.ItemData;
import com.sarita.dashin.R;
import java.util.ArrayList;

public class SelectCategoryAdapter extends FirestoreRecyclerAdapter<CategoryDataClass,SelectCategoryAdapter.NameHolder> {

    ArrayList<String> arrayList;
    RelativeLayout.LayoutParams rule ;
    public SelectCategoryAdapter(@NonNull FirestoreRecyclerOptions<CategoryDataClass> options) {
        super(options);
        rule =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onBindViewHolder(@NonNull final NameHolder holder, int position, @NonNull CategoryDataClass model) {
        final RelativeLayout.LayoutParams rule ;
        rule =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        rule.addRule(RelativeLayout.BELOW,holder.cat.getId());
        holder.cat.setText(model.getCAT_NAME());
        holder.adapter=new AddItemAdapter(makeItemRecyclerView("9998887776",model.getCAT_ID()));
        holder.adapter.startListening();
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerView.setAdapter(holder.adapter);
        SelectItems.menu.put(model.getCAT_NAME(),new ArrayList<String>());
        holder.cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.expand){
                    holder.cat.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.expand_arrow, 0);
                    holder.expand=false;
                    holder.recyclerView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,0));
                }
                else {
                    holder.cat.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.collapse_arrow, 0);
                    holder.expand=true;
                    holder.recyclerView.setLayoutParams(rule);
                    holder.adapter.startListening();
                }
            }
        });
    }

    @NonNull
    @Override
    public NameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.select_item_tickmark,parent,false);
        return new SelectCategoryAdapter.NameHolder(view);
    }

    class NameHolder extends RecyclerView.ViewHolder{
        TextView cat;
        boolean expand;
        RecyclerView recyclerView;
        AddItemAdapter adapter;
        public NameHolder(@NonNull View itemView) {
            super(itemView);
            cat=itemView.findViewById(R.id.cat_name);
            expand=false;
            recyclerView = itemView.findViewById(R.id.item);
        }

    }

    FirestoreRecyclerOptions<ItemData> makeItemRecyclerView(String number, String CAT_ID)
    {
        Query query = FirebaseFirestore.getInstance().collection("VENDOR-MENU/"+number+"/"+CAT_ID);
        FirestoreRecyclerOptions<ItemData> options = new FirestoreRecyclerOptions
                .Builder<ItemData>()
                .setQuery(query,ItemData.class)
                .build();
        return options;
    }

}
