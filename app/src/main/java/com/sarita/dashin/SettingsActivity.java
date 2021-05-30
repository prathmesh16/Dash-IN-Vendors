package com.sarita.dashin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.sarita.dashin.utils.Constants;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton backBtn, logoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            }
        });

        logoutBtn = findViewById(R.id.settings_logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.mAuth.signOut();
                finish();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            }
        });
    }
}
