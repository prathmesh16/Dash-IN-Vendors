package com.sarita.dashin.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sarita.dashin.MainActivity;
import com.sarita.dashin.R;
import com.sarita.dashin.adapters.TagsAdapter;
import com.sarita.dashin.adapters.TagsAdapterNew;
import com.sarita.dashin.models.ModelTag;
import com.sarita.dashin.utils.CircleTransform;
import com.sarita.dashin.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MessTagsFragments extends Fragment {

    View view;
    RecyclerView recyclerView2;
    TagsAdapter tagsAdapter;
    ImageButton next;
    Source source;

    public MessTagsFragments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_mess_tags, container, false);

        source=Source.CACHE;

        setUpFoodRecyclerView();
        next = view.findViewById(R.id.mess_tags_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  uploadToFirebase();

            }
        });

        return view;
    }

    private void setUpFoodRecyclerView() {

        Log.e("Tags", "rv setup");
        recyclerView2 = view.findViewById(R.id.rv_tags);
        Query query =
                Constants.mFirestore.collection("Tags");

        FirestoreRecyclerOptions<ModelTag> options = new FirestoreRecyclerOptions.Builder<ModelTag>()
                .setQuery(query, ModelTag.class)
                .build();

        tagsAdapter = new TagsAdapter(options, new TagsAdapter.ClickListener() {
            @Override public void onPositionClicked(int position) {
                // callback performed on click
            }
        }, getActivity());

//        try {
//            Thread.sleep(2000);
//            Log.e("sleep", "over");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        recyclerView2.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView2.setAdapter(tagsAdapter);

//        final List<Uri> images = new ArrayList<>();
//        final List<String> names = new ArrayList<>();
//        Constants.mFirestore.collection("Tags").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()) {
//                    for (QueryDocumentSnapshot doc : task.getResult()) {
//                        names.add(doc.getString("Tag"));
//                    }
//                    for (QueryDocumentSnapshot doc : task.getResult()) {
//                        //load image from storage
//                        Constants.mStorage.getReference().child("Tags").child(doc.getString("Image"))
//                                .getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Uri> task) {
//                                Uri uri = task.getResult();
//                                if (uri != null) {
//                                    images.add(uri);
//                                    Log.e("iamge", images.size()+ "");
//                                }
//                                if(images.size()==names.size()) {
//                                    tagsAdapter = new TagsAdapterNew(names, images, getActivity(), new TagsAdapterNew.ClickListener() {
//                                        @Override
//                                        public void onPositionClicked(int position) {
//                                            // callback performed on click
//                                        }
//                                    });
//                                    recyclerView2.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
//                                    recyclerView2.setAdapter(tagsAdapter);
//                                }
//                            }
//
//                        });
//                    }
//
//
//
//                }else{
//                    Log.e("tag", "error");
//                }
//            }
//        });

    }

    @Override
    public void onStart() {
        Log.e("ModelBooks", "on start");
        super.onStart();
        tagsAdapter.startListening();
    }

    @Override
    public void onStop() {
        Log.e("ModelBooks", "on stop");
        super.onStop();
        tagsAdapter.stopListening();
    }


    private void uploadToFirebase() {

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Uploading...");
        dialog.setCancelable(false);
        dialog.show();
        //upload currentuser data to firebase
        Constants.mFirestore.collection("VENDORS").document(Constants.CurrentUser.getOWNER_CONTACT()).set(Constants.CurrentUser);
        Constants.mFirestore.collection("VENDORS").document(Constants.CurrentUser.getOWNER_CONTACT())
                .collection("MENU_TIME").document("from").set(Constants.from);
        Constants.mFirestore.collection("VENDORS").document(Constants.CurrentUser.getOWNER_CONTACT())
                .collection("MENU_TIME").document("to").set(Constants.to);

        //TODO
        //upload image links to storage
        //there are 2 lists in Constants mess images, file names..

        //directory structure ==> /vendor's ID/ ... (owner image)
        //directory structure ==> /vendor's ID/mess images/ ... (mess images)
        //directory structure ==> /vendor's ID/review images/ ... (review images)
        //directory structure ==> /vendor's ID/menu images/ ...
            StorageReference mStorageRef;
            mStorageRef = FirebaseStorage.getInstance().getReference();

            for (int i=0;i<Constants.MessImages.size();i++)
            {
                StorageReference Ref = mStorageRef.child(Constants.CurrentUser.getOWNER_CONTACT()+"/mess_images/"+Constants.MessImagenames.get(i));

                final int finalI = i;
                Ref.putFile(Constants.MessImages.get(i))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while(!uri.isComplete());
                                Uri url = uri.getResult();
                                HashMap<String,String> tmp= new HashMap<>();
                                tmp.put("ImageUri",url.toString());
                                Constants.mFirestore.collection("VENDORS").document(Constants.CurrentUser.getOWNER_CONTACT()).collection("mess_IMAGES").add(tmp);
                                if (finalI == Constants.MessImagenames.size()-1)
                                {
                                    dialog.dismiss();
                                    getActivity().finish();
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });
            }

    }


}
