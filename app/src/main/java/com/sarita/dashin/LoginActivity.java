package com.sarita.dashin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.sarita.dashin.fragments.MessOpenFragment;
import com.sarita.dashin.fragments.MessTagsFragments;
import com.sarita.dashin.fragments.OwnerInfoFragment;
import com.sarita.dashin.fragments.PhoneNumberVerification;
import com.sarita.dashin.fragments.SetMessNameFragment;
import com.sarita.dashin.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));




        //go to otp verification..
        //it is working so this part is commented
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.main_frame_1, new PhoneNumberVerification(), "");
        ft1.commit();

        //this is temporary code
//        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
//        ft1.replace(R.id.main_frame_1, new MessTagsFragments(), "");
//        ft1.commit();

    }
}
