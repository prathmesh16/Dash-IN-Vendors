package com.sarita.dashin.fragments;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sarita.dashin.R;
import com.sarita.dashin.utils.Constants;

public class SetLocationFragment extends Fragment {

    ImageButton setLocation;

    SetLocationFragment(){

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_location_fragment, container, false);

        setLocation = view.findViewById(R.id.setLocationBtn);
        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get coordinates
                //set to current user location
                //Constants.CurrentUser.setLOCATION();
                //get address from geocoder
                //Constants.CurrentUser.setADDRESS();
                FragmentTransaction ft1 = getActivity().getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.main_frame_1, new MessDescriptionFragment(), "");
                ft1.commit();
            }
        });

        return view;
    }



}
