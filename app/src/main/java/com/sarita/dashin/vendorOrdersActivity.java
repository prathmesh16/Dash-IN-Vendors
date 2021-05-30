package com.sarita.dashin;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sarita.dashin.adapters.VendorOrdersAdapter;
import com.sarita.dashin.models.ModelOrder;
import com.sarita.dashin.utils.Constants;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class vendorOrdersActivity extends AppCompatActivity {

    VendorOrdersAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_orders);
        setUpFoodRecyclerView();
    }

    private void setUpFoodRecyclerView() {

        recyclerView = findViewById(R.id.rv_vendor_orders);
        Query query = Constants.mFirestore.collection("/VENDORS/6KzwVrPx4KUaVtqYaA0ou7FmWVI3/Orders").whereEqualTo("SERVED", false);

        FirestoreRecyclerOptions<ModelOrder> options = new FirestoreRecyclerOptions.Builder<ModelOrder>()
                .setQuery(query, ModelOrder.class)
                .build();

        adapter = new VendorOrdersAdapter(options, new VendorOrdersAdapter.ClickListener() {
            @Override public void onPositionClicked(int position) {
                // callback performed on click
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Take action for the swiped item
                final int position = viewHolder.getAdapterPosition();
                boolean accepted = false;
                switch (direction){
                    case ItemTouchHelper.LEFT:
                        accepted = true;
                        break;
                    case ItemTouchHelper.RIGHT:
                        accepted = false;
                        break;
                }
                adapter.changeStatusOfOrder(accepted, position);
                Snackbar snackbar = Snackbar.make(viewHolder.itemView, "Order " + (direction == ItemTouchHelper.RIGHT ? "rejected" : "accepted") + ".", Snackbar.LENGTH_SHORT);
                snackbar.setAction("Undo", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        try {
                            adapter.undo(position);
                            adapter.notifyDataSetChanged();
                        } catch(Exception e) {
                            Log.e("MainActivity", e.getMessage());
                        }
                    }
                });
                snackbar.show();
            }
            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(vendorOrdersActivity.this, R.color.green))
                        .addSwipeLeftActionIcon(R.drawable.ic_check_white_24dp)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(vendorOrdersActivity.this, R.color.quantum_googred))
                        .addSwipeRightActionIcon(R.drawable.ic_delete_black_24dp)
                        .addSwipeRightLabel("Reject")
                        .setSwipeRightLabelColor(Color.WHITE)
                        .addSwipeLeftLabel("Accept")
                        .setSwipeLeftLabelColor(Color.WHITE)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        Log.e("tags", adapter.getItemCount()+"");

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
