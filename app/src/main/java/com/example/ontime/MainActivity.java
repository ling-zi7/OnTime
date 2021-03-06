package com.example.ontime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.ontime.R;

public class MainActivity extends AppCompatActivity {

    Button signupMainButton;
    Button loginMainButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signupMainButton = findViewById(R.id.signup_main);
        loginMainButton = findViewById(R.id.login_main);


        signupMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignupActivity.class));
            }
        });

        loginMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

    }
}