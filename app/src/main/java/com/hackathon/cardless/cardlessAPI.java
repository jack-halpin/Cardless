package com.hackathon.cardless;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by barry on 15/06/2017.
 */

public class cardlessAPI extends AsyncTask<String, Void, String> {


    private Exception exception;

    protected void onPreExecute() {
//        progressBar.setVisibility(View.VISIBLE);
//        responseView.setText("");
    }

    protected String doInBackground(String... params) {

        try {
            URL url = new URL("http://ec2-52-209-200-49.eu-west-1.compute.amazonaws.com/tagid/"+params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            System.out.println(params[0]);
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
//        progressBar.setVisibility(View.GONE);
        Log.i("INFO", response);
//        responseView.setText(response);
    }
}
