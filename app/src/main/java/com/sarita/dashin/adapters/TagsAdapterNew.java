package com.sarita.dashin.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.sarita.dashin.R;
import com.sarita.dashin.utils.CircleTransform;
import com.sarita.dashin.utils.Constants;
import com.sarita.dashin.utils.GlideApp;
import com.sarita.dashin.utils.MyGlideModule;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

public class TagsAdapterNew extends RecyclerView.Adapter<TagsAdapterNew.ViewHolder>{

    public List<String> fileNameList;
    public List<Uri> filePreviewList;
    Context context;

    private TagsAdapterNew.ClickListener listener;


    public TagsAdapterNew(List<String> fileNameList, List<Uri> filePreviewList, Context context, TagsAdapterNew.ClickListener listener ){

        this.fileNameList = fileNameList;
        this.filePreviewList = filePreviewList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sorting_item, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String fileName = fileNameList.get(position);
        holder.tag.setText(fileName);
        try {
            Uri filePath = filePreviewList.get(position);
//            Picasso.get()
//                    .load(filePath)
//                    .transform(new CircleTransform())
//                    .into(holder.upload_preview);

//            GlideApp.with(context)
//                    .load(filePath)
//                    .transform((Transformation<Bitmap>) new CircleTransform())
//                    .into(holder.upload_preview);

        }catch(Exception e){
            Log.e("upload error" , e.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public interface ClickListener {
        void onPositionClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mView;
        public ImageView upload_preview;
        TextView tag;
        RelativeLayout selected;

        private WeakReference<TagsAdapter.ClickListener> listenerRef;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            upload_preview = (ImageView) mView.findViewById(R.id.sort_iv);
            tag = mView.findViewById(R.id.sort_tv);
            selected = mView.findViewById(R.id.selected);
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



}
