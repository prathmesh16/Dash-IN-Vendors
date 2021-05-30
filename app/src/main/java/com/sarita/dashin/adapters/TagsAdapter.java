package com.sarita.dashin.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.sarita.dashin.R;
import com.sarita.dashin.models.ModelTag;
import com.sarita.dashin.utils.Constants;
import com.sarita.dashin.utils.GlideApp;

import java.lang.ref.WeakReference;

public class TagsAdapter extends FirestoreRecyclerAdapter<ModelTag, TagsAdapter.TagsHolder> {


    private TagsAdapter.ClickListener listener;
    Context context;



    public TagsAdapter(@NonNull FirestoreRecyclerOptions<ModelTag> options, TagsAdapter.ClickListener listener, Context context ) {
        super(options);
        this.listener = listener;
        this.context = context;

        Log.e("Tags", "adapter constr");

    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        Log.e("Adapter error", e.getMessage());
    }

    @Override
    protected void onBindViewHolder(@NonNull final TagsAdapter.TagsHolder holder, final int position, @NonNull final ModelTag model) {

        holder.tag.setText(model.getTag());

        //set front pic here
        if(model.getImage()!=null) {
            Constants.mStorage.getReference().child("Tags").child(model.getImage()).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(final Uri uri) {
                    if(uri!=null) {
//                        Picasso.get()
//                                .load(uri)
//                                .transform(new CircleTransform())
//                                .into(holder.image);

                    GlideApp.with(context)
                            .load(uri)
                            .circleCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.image);



                    }
                }
            });
        }
        //calculate open status here


        Log.e("Tags", "bindviewholder");
    }


    @Override
    public int getItemCount() {
        return super.getItemCount() ;
    }

    @NonNull
    @Override
    public TagsAdapter.TagsHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sorting_item, parent, false);
        Log.e("Tags", "createviewholder");
        return new TagsAdapter.TagsHolder(v, listener);
    }

    class TagsHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        TextView tag;
        ImageView image;
        Context context;
        RelativeLayout selected;

        private WeakReference<ClickListener> listenerRef;

        public TagsHolder(View v, TagsAdapter.ClickListener listener){
            super(v);
            listenerRef = new WeakReference<>(listener);
            v.setOnClickListener(this);
            context = itemView.getContext();
            tag = v.findViewById(R.id.sort_tv);
            image = v.findViewById(R.id.sort_iv);
            selected = v.findViewById(R.id.selected);


            Log.e("Tags", "holder constr");


        }

        @Override
        public void onClick(View v) {

            if(selected.getVisibility()==View.GONE){
                selected.setVisibility(View.VISIBLE);
                Constants.CurrentUser.addFACILITIES(tag.getText().toString());
            }else{
                selected.setVisibility(View.GONE);
                Constants.CurrentUser.removeFACILITIES(tag.getText().toString());
            }

        }

    }



    public interface ClickListener {
        void onPositionClicked(int position);
    }



}
