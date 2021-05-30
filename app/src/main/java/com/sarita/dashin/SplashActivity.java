package com.sarita.dashin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sarita.dashin.models.ModelMess;
import com.sarita.dashin.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    Thread objectthread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.button_color));


        try {

            Animation objectAnimation = AnimationUtils.loadAnimation(this,R.anim.translate);
            objectAnimation.reset();
            ImageView objectImageView = findViewById(R.id.logo);
            objectImageView.clearAnimation();
            objectImageView.startAnimation(objectAnimation);
            objectthread = new Thread()
            {
                @Override
                public void run() {
                    int pasueIt = 0;
                    while (pasueIt<6000){
                        try {
                            sleep(90);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        pasueIt+=100;
                    }

                    finish();
                    if(Constants.mAuth.getCurrentUser() != null){

                        Constants.mFirestore.collection("VENDORS")
                                .document(Constants.mAuth.getCurrentUser().getPhoneNumber()).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            Constants.CurrentUser = task.getResult().toObject(ModelMess.class);
                                    }
                                });

                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }else
                    {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                }
            };
            objectthread.start();
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
