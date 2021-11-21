package com.dave.myfirstapp;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    public static void AsyncPetition(String query, String method, String params, HandleFunction function) {


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
