package com.dave.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

/**
 * Main activity of the application, it contains the login screen and has access to the register page
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * executed with the onClick of the register button, opens the register menu
     */
    public void register(View view) {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

    /**
     * executed with the onClick of the login button, connects to the server and if successful opens the login successfull page
     */
    public void login(View view){

        EditText userText = findViewById(R.id.username);
        String username= userText.getText().toString();
        EditText pswText = findViewById(R.id.Password);
        String psw = pswText.getText().toString();
        String params = "uname="+username+"&psw="+psw;
        /**
         * Executes a GET query to the http://10.0.2.2:9000/Android/androidLogin with the given params.
         */
        Utils.AsyncPetition("http://10.0.2.2:9000/Android/androidLogin","GET",params,(result -> {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if(jsonObject.has("login")) {
                    if (jsonObject.getBoolean("login")) {
                        Intent intent = new Intent(this, LoginSuccessful.class);
                        String message = userText.getText().toString();
                        intent.putExtra("name", message);
                        startActivity(intent);
                    }else{
                        TextView textView = findViewById(R.id.errorMessage);
                        textView.setTextColor(Color.RED);
                        textView.setText(jsonObject.getString("error"));
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }));
    }

}