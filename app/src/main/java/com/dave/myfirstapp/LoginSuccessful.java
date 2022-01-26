package com.dave.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

/*
 * Screen in case of successful login.
 */
public class LoginSuccessful extends FragmentActivity {

    /**
     * Takes a parameter from the intent and uses it to show the name of the user and welcome him.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_successful);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra("name");
        TextView textView = findViewById(R.id.textView);
        textView.setText("Welcome!, "+ message);
    }
}