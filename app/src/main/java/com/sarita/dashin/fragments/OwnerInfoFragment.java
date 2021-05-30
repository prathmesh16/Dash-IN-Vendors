package com.sarita.dashin.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sarita.dashin.R;
import com.sarita.dashin.utils.Constants;

public class OwnerInfoFragment extends Fragment {

    View view;
    ImageButton next;
    EditText name, email, password, confirmPassword, contact;
    ImageView image;
    Intent imageData;
    public OwnerInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_owner_info, container, false);
        next = view.findViewById(R.id.owner_info_next);
        name = view.findViewById(R.id.owner_name);
        contact = view.findViewById(R.id.owner_contact);
        email = view.findViewById(R.id.owner_email);
        password = view.findViewById(R.id.owner_password);
        confirmPassword = view.findViewById(R.id.owner_password_confirm);
        image = view.findViewById(R.id.ownerImage);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose();
            }
        });
         next.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(checkEmpty()) {
                     final ProgressDialog dialog = new ProgressDialog(getContext());
                     dialog.setMessage("Uploading...");
                     dialog.setCancelable(false);
                     dialog.show();
                     Constants.CurrentUser.setOWNER_NAME(name.getText().toString());
                     Constants.CurrentUser.setOWNER_EMAIL(email.getText().toString());
                     Constants.CurrentUser.setOWNER_PASSWORD(password.getText().toString());
                     Constants.CurrentUser.setOWNER_CONTACT("+91" + contact.getText().toString());
                     Uri fileUri = imageData.getData();
                     String fileName = getFileName(fileUri);
                     //set image
                     Constants.CurrentUser.setOWNER_IMAGE(fileName);
                     //image upload code
                     StorageReference mStorageRef;
                     mStorageRef = FirebaseStorage.getInstance().getReference();

                     StorageReference Ref = mStorageRef.child(Constants.CurrentUser.getOWNER_CONTACT()+"/"+fileName);

                     Ref.putFile(fileUri)
                             .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                 @Override
                                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                     // Get a URL to the uploaded content
                                    dialog.dismiss();
                                     FragmentTransaction ft1 = getActivity().getSupportFragmentManager().beginTransaction();
                                     ft1.replace(R.id.main_frame_1, new SetMessNameFragment(), "");
                                     ft1.commit();
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
         });

         return view;
    }

    private boolean checkEmpty() {

        if( TextUtils.isEmpty(name.getText())) {
            name.setError("Name is required!");
        }else if( TextUtils.isEmpty(email.getText())) {
            email.setError("email is required!");
        }else if( TextUtils.isEmpty(password.getText())) {
            password.setError("enter a valid password!");
        }else if( password.length()<6) {
            confirmPassword.setError("Password must have 6 characters!");
        }else if( TextUtils.isEmpty(confirmPassword.getText())) {
            confirmPassword.setError("enter a valid password!");
        }else if( TextUtils.isEmpty(contact.getText())) {
            confirmPassword.setError("Contact is required!");
        }else if( !confirmPassword.getText().toString().equals(password.getText().toString())) {
            confirmPassword.setError("passwords do not match!");
        }else{
            return true;
        }
        return false;
    }

    private void choose(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == getActivity().RESULT_OK) {

        if (data.getData() != null) {

                Toast.makeText(getActivity(), "Selected Profile picture", Toast.LENGTH_SHORT).show();
                Uri fileUri = data.getData();
                String fileName = getFileName(fileUri);
                imageData=data;
                image.setImageURI(fileUri);
                //TODO
                //set user image to image view
                //add image link to firebase storage


                //directory structure ==> /vendor's ID/ ... (owner image)
                //directory structure ==> /vendor's ID/mess images/ ... (mess images)
                //directory structure ==> /vendor's ID/review images/ ... (review images)
                //directory structure ==> /vendor's ID/menu images/ ...


            }

        }

    }


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }



}
