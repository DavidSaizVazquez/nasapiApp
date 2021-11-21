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

        AsyncPetition("http://10.0.2.2:9000/Application/androidLogin","GET",params,(result -> {
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

    private interface HandleFunction{
        public void run(String result);
    }

    public void AsyncPetition(String query, String method, String params,HandleFunction function) {


        new Thread(new Runnable() {
            InputStream stream = null;
            String result = null;
            final Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
            public void run() {
                BufferedReader reader;
                String line;

                try {
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 );
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod(method);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(params);
                    writer.flush();
                    writer.close();
                    os.close();

                    stream = conn.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(stream));


                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();

                    //Codi correcte
                    handler.post(()->function.run(result));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}