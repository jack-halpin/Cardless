package com.hackathon.cardless;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.hackathon.cardless.Constants.PRIMARY_DEV;
import static com.hackathon.cardless.Constants.TOKEN;

public class SendmoneyActivity extends AppCompatActivity {

    private EditText accountInfoEntry;
    private EditText sortCodeEntry;
    private EditText amountEntry;
    private EditText descriptionEntry;

    private String LOG_TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmoney);

        accountInfoEntry = (EditText) findViewById(R.id.account_number_entry);
        sortCodeEntry = (EditText) findViewById(R.id.sort_code_entry);
        amountEntry = (EditText) findViewById(R.id.amount_entry);
        descriptionEntry = (EditText) findViewById(R.id.description_entry);

        LOG_TAG = "ERROR: ";
    }

    public void makePaymentOnClick(android.view.View view){
        //Get the info from the fields
//        String accountInfo = accountInfoEntry.getText().toString();
//        String sortCode = sortCodeEntry.getText().toString();
//        String amount = amountEntry.getText().toString();
//        String description = descriptionEntry.getText().toString();


        NetworkAsyncTask n = new NetworkAsyncTask();
        n.execute();




    }

    //This method is used to read in the JSON String
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer data = new StringBuffer("");
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }



    class NetworkAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String transJsonStr = null;
            try {

                //Create the JSONObje
                JSONObject payobj = new JSONObject();

                //Define the base url that contains the API key for the application
                String base_url = "https://bluebank.azure-api.net/v0.71/api/Payments";
                Log.e("URL", base_url);


                Uri builtUri = Uri.parse(base_url);
                URL url = new URL(builtUri.toString());

                // Create the request to Eventful, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoInput (true);
                urlConnection.setDoOutput (true);
                urlConnection.setUseCaches (false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                //urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");


                urlConnection.setRequestProperty("Ocp-Apim-Subscription-Key", PRIMARY_DEV);
                urlConnection.setRequestProperty("Authorization", "bearer " + TOKEN);

                try {
                    payobj.put("paymentType", "POS");
                    payobj.put("fromAccountId", "7d50e4f6-1047-476b-a4f9-063e6bb8b396");
                    payobj.put("toSortCode", "839999");
                    payobj.put("toAccountNumber", "10002041");
                    payobj.put("paymentReference", "Test");
                    payobj.put("paymentAmount", 100);
                    payobj.put("paymentCurrency", "GBP");
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }

                Log.e(LOG_TAG, payobj.toString());

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(payobj.toString());


                //urlConnection.connect();
                int re = urlConnection.getResponseCode();
                Log.e(LOG_TAG, Integer.toString(re));



                InputStream inputStream = urlConnection.getInputStream();
                System.out.println(inputStream);
                transJsonStr = readStream(inputStream);


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the event data, there's no point in attempting
                // to parse it.

            }
            return null;
        }
    }
}
