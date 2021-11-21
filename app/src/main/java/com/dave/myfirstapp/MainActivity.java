package com.dave.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void register(View view) {
        Intent intent = new Intent(this, register.class);
        EditText editText = (EditText) findViewById(R.id.username);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void login(View view){
        EditText userText = findViewById(R.id.username);
        String username= userText.getText().toString();
        EditText pswText = findViewById(R.id.Password);
        String psw = pswText.getText().toString();
        String params = "uname="+username+"&psw="+psw;

        Utils.AsyncPetition("http://10.0.2.2:9000/Application/androidLogin","GET",params,(result -> {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if(jsonObject.has("login")) {
                    if (jsonObject.getBoolean("login")) {
                        Intent intent = new Intent(this, LoginSuccessful.class);
                        String message = userText.getText().toString();
                        intent.putExtra(EXTRA_MESSAGE, message);
                        startActivity(intent);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }));
    }



}