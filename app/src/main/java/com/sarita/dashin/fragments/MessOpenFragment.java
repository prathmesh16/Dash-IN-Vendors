package com.sarita.dashin.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sarita.dashin.MainActivity;
import com.sarita.dashin.R;
import com.sarita.dashin.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessOpenFragment extends Fragment {

    ImageButton breakfast_iv, lunch_iv, dinner_iv;
    TextView breakfast_tv, lunch_tv, dinner_tv,go;

    public MessOpenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_mess_open, container, false);

        breakfast_iv = view.findViewById(R.id.breakfast_iv);
        lunch_iv =  view.findViewById(R.id.lunch_iv);
        dinner_iv = view.findViewById(R.id.dinner_iv);
        breakfast_tv = view.findViewById(R.id.breakfast_text);
        lunch_tv =  view.findViewById(R.id.lunch_text);
        dinner_tv = view.findViewById(R.id.idnner_text);
        go = view.findViewById(R.id.go);

        breakfast_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSetClicked(Constants.BREAKFAST, view);
            }
        });
        lunch_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSetClicked(Constants.LUNCH, view);
            }
        });
        dinner_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSetClicked(Constants.DINNER, view);
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constants.CurrentUser.setOPEN_FROM(Constants.from.get(Constants.BREAKFAST));
                Constants.CurrentUser.setOPEN_TILL(Constants.to.get(Constants.DINNER));

                FragmentTransaction ft1 = getActivity().getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.main_frame_1, new MessTagsFragments(), "");
                ft1.commit();
            }
        });


        return view;
    }

    private void onSetClicked(final String str, View view){

        final Dialog dialog = new Dialog(view.getContext());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mess_open_dialog);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView menuType, fromTo, amPm, next;
        final EditText hour, minute;
        menuType = dialog.findViewById(R.id.menuType);
        fromTo = dialog.findViewById(R.id.fromTo);
        amPm = dialog.findViewById(R.id.ampm);
        next = dialog.findViewById(R.id.mess_menu_nextBtn);
        hour = dialog.findViewById(R.id.hour);
        minute = dialog.findViewById(R.id.minute);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out="";
                String time = hour.getText().toString() + ":" + minute.getText().toString() +" " + amPm.getText().toString();
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");
                try {
                    Date date = dateFormat2.parse(time);
                    out = dateFormat2.format(date);
                    Log.e("Time", date.toString() +"   "+ out);
                } catch (ParseException e) {
                }
                    Constants.from.put(str, out);
                    dialog.dismiss();
                    onNextClicked(str,view);
            }
        });

        amPm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amPm.getText().toString().equals("AM")){
                    amPm.setText("PM");
                }else{
                    amPm.setText("AM");
                }
            }
        });

        menuType.setText(str+" ?");
        fromTo.setText("From");
        next.setText("next");

        dialog.show();
    }

    private void onNextClicked(final String str, View view){

        go.setVisibility(View.VISIBLE);
        final Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mess_open_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView menuType, fromTo, amPm, next;
        final EditText hour, minute;

        menuType = dialog.findViewById(R.id.menuType);
        fromTo = dialog.findViewById(R.id.fromTo);
        amPm = dialog.findViewById(R.id.ampm);
        next = dialog.findViewById(R.id.mess_menu_nextBtn);
        hour = dialog.findViewById(R.id.hour);
        minute = dialog.findViewById(R.id.minute);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out="";
                String time = hour.getText().toString() + ":" + minute.getText().toString() +" " + amPm.getText().toString();
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");
                try {
                    Date date = dateFormat2.parse(time);
                    out = dateFormat2.format(date);
                    Log.e("Time", date.toString() +"   "+ out);
                } catch (ParseException e) {
                }
                    Constants.to.put(str, out);
                    Log.e("dialog", Constants.from.toString() + " and " + Constants.to.toString());
                    dialog.dismiss();
            }
        });

        amPm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amPm.getText().toString().equals("AM")){
                    amPm.setText("PM");
                }else{
                    amPm.setText("AM");
                }
            }
        });
        menuType.setText(str + " ?");
        fromTo.setText("To");
        next.setText("set time");
        dialog.show();
    }

}
