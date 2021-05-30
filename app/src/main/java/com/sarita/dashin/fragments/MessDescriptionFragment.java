package com.sarita.dashin.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.sarita.dashin.R;
import com.sarita.dashin.adapters.UploadListAdapter;
import com.sarita.dashin.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MessDescriptionFragment extends Fragment {

    private RecyclerView mUploadList;
    private View view;
    private EditText desc;
    private List<String> fileDoneList;
    private UploadListAdapter uploadListAdapter;
    private ProgressDialog progressDialog;
    private ImageButton next,upload;

    public MessDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_mess_description, container, false);

        progressDialog = new ProgressDialog(getContext());
        mUploadList = (RecyclerView) view.findViewById(R.id.upload_list);
        desc = view.findViewById(R.id.mess_description);

        fileDoneList = new ArrayList<>();

        uploadListAdapter = new UploadListAdapter(Constants.MessImagenames, fileDoneList, Constants.MessImages, getActivity());
        next = view.findViewById(R.id.mess_desc_next_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set list of files names
                Constants.CurrentUser.setFRONT_PIC(Constants.MessImagenames.get(0));
                Constants.CurrentUser.setMESS_IMAGES((ArrayList<String>) Constants.MessImagenames);
                if(TextUtils.isEmpty(desc.getText())){
                    Constants.CurrentUser.setBUSI_DESCRIPTION(desc.getHint().toString());
                }else {
                    Constants.CurrentUser.setBUSI_DESCRIPTION(desc.getText().toString());
                }
                FragmentTransaction ft1 = getActivity().getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.main_frame_1, new MessOpenFragment(), "");
                ft1.commit();
            }
        });

        upload = view.findViewById(R.id.add_mess_image);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose();
            }
        });
        mUploadList.setLayoutManager(new GridLayoutManager(getActivity(),3, GridLayoutManager.VERTICAL,false));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdapter);

        return view;
    }

    private void choose(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mUploadList.setVisibility(View.VISIBLE);

        if (requestCode == 101 && resultCode == getActivity().RESULT_OK) {

            if (data.getClipData() != null) {

                int totalItemsSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemsSelected; i++) {

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    String fileName = getFileName(fileUri);

                    Constants.MessImagenames.add(fileName);
                    fileDoneList.add("uploading");
                    Constants.MessImages.add(fileUri);
                    uploadListAdapter.notifyDataSetChanged();
                }

            } else if (data.getData() != null) {

                Toast.makeText(getActivity(), "Selected Single File", Toast.LENGTH_SHORT).show();
                Uri fileUri = data.getData();
                String fileName = getFileName(fileUri);

                Constants.MessImagenames.add(fileName);
                fileDoneList.add("uploading");
                Constants.MessImages.add(fileUri);
                uploadListAdapter.notifyDataSetChanged();

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
