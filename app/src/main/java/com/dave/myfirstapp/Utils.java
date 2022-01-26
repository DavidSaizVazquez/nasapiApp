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

    /**
     * Wrapper to the thread code that simplifies the petitions to the server.
     * @param query: Query url
     * @param method: HTTP method to apply
     * @param params: parameters already in string form to add them to the query
     * @param function: function lamba to execute inside of the handler once the data is retreived
     */
    public static void AsyncPetition(String query, String method, String params, HandleFunction function) {


        new Thread(new Runnable() {
            InputStream stream = null;
            String result = null;
            final Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
            public void run() {
                BufferedReader reader;
                String line;

                try {
                    //get the url and create a http conneciton
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod(method);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();

                    //buffer the params
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(params);
                    writer.flush();
                    writer.close();
                    os.close();
                    //get the data from the input
                    stream = conn.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();

                    //execute the lambda in function
                    handler.post(()->function.run(result));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
