package com.example.evcharging;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class fragment_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        Button logButton = findViewById(R.id.logoutButton);

        logButton.setOnClickListener(v -> {
            Intent intent=new Intent(fragment_profile.this, MainActivity2.class);

            onStop();
        });
    }
}
