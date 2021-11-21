package com.dave.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONObject;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
    }

    public void onRegister(View view){
        EditText userText = findViewById(R.id.name);
        String username= userText.getText().toString();
        EditText pswText = findViewById(R.id.password);
        String psw = pswText.getText().toString();
        EditText vpswText = findViewById(R.id.verifyPassword);
        String vpsw = vpswText.getText().toString();
        EditText ageText = findViewById(R.id.editTextNumber);
        String age = ageText.getText().toString();

        RadioButton rd1 = findViewById(R.id.dexter1);
        RadioButton rd2 = findViewById(R.id.dexter2);
        RadioButton rd3 = findViewById(R.id.dexter3);
        RadioButton rd4 = findViewById(R.id.dexter4);
        String dexter="";
        if(rd1.isChecked()){
            dexter ="dexter1";
        }else if(rd2.isChecked()){
            dexter="dexter2";
        }else if(rd3.isChecked()){
            dexter="dexter3";
        }else if(rd4.isChecked()){
            dexter="dexter4";
        }

        String params = "name="+username+"&password="+psw+"&verifyPassword="+vpsw+"&age="+age+"&dexter="+dexter;

        Utils.AsyncPetition("http://10.0.2.2:9000/Application/androidRegister","POST",params,(result -> {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if(jsonObject.has("register")) {
                    if (jsonObject.getBoolean("register")) {
                        finish();
                    }else {
                        TextView textView = findViewById(R.id.textView2);
                        textView.setText(jsonObject.getString("error"));
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }));

    }

}