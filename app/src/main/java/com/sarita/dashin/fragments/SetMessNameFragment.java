package com.sarita.dashin.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sarita.dashin.R;
import com.sarita.dashin.utils.Constants;


public class SetMessNameFragment extends Fragment {


    private EditText name;
    private ImageButton next;

    public SetMessNameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_set_mess_name, container, false);

        name = view.findViewById(R.id.mess_name);
        next =view.findViewById(R.id.mess_name_next_btn);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    next.setBackground(getResources().getDrawable(R.drawable.next_button));
                }else{
                    next.setBackground(getResources().getDrawable(R.drawable.disabled_button));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.CurrentUser.setBUSI_NAME(name.getText().toString());
                FragmentTransaction ft1 = getActivity().getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.main_frame_1, new SetLocationFragment(), "");
                ft1.commit();
            }
        });

        return view;
    }
}
