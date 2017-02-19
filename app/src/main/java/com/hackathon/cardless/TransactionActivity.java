package com.hackathon.cardless;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hackathon.cardless.Constants.PRIMARY_DEV;
import static com.hackathon.cardless.Constants.TOKEN;

public class TransactionActivity extends AppCompatActivity {


    private TransactionAdapter transactionListAdapter; //Adapter used for occupying fragment Listview
    private FetchTransInfo fetch; //AsyncTask used to get info from the EventFul API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);


        String start_dt = "2016-04-27T09:00:00.000Z";


        transactionListAdapter = new TransactionAdapter(this, R.layout.layout_transaction, new ArrayList<Transaction>());

        Log.e("onCreateView: ", "Called");
        View rootView = findViewById(R.id.activity_transaction);
        // Get ListView object from xml
        ListView listView = (ListView) rootView.findViewById(R.id.trans_list);

        listView.setAdapter(transactionListAdapter);
        fetch = new FetchTransInfo();
        fetch.execute();
    }

    //The class represents an asynchronous task to be carried out when the activity is loaded
    //In here we must define the URL, perform a JSON request with the parameters and parse it.
    //objects that have been defined in the parent class can be accessed here!
    public class FetchTransInfo extends AsyncTask<Void, Void, Transaction[]> {

        private final String LOG_TAG = FetchTransInfo.class.getSimpleName();


        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        @Override
        protected void onPreExecute() {


            //If there is currently anything in the evenLists object we want to delete them
            transactionListAdapter.clear();
            //Notify the user that the task is loading up the relevant information


        }

        @Override
        protected Transaction[] doInBackground(Void... params) {
            //NOTE: This code is based off of the Sunshine Part 3 tutorial

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String transJsonStr = null;


            //Next need to construct the URL string used to query the API
            try {
                //Define the base url that contains the API key for the application
                String base_url = "https://bluebank.azure-api.net/api/v0.7/accounts/7d50e4f6-1047-476b-a4f9-063e6bb8b396/transactions?limit=20";



                Log.e("URL", base_url);


                Uri builtUri = Uri.parse(base_url);
                URL url = new URL(builtUri.toString());

                // Create the request to Eventful, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Ocp-Apim-Subscription-Key", PRIMARY_DEV);
                urlConnection.setRequestProperty("Authorization", "bearer " + TOKEN);
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                System.out.println(inputStream);
                transJsonStr = readStream(inputStream);


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the event data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getTransactionsFromJson(transJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the data.
            return null;
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

        public Transaction[] getTransactionsFromJson(String transInfo) throws JSONException{
            //Create the JSONObject from the string returned from the API query
            JSONObject transactionJson = new JSONObject(transInfo);


            //Get a JSON array of all the events that are listed from the query
            JSONArray transactions = transactionJson.getJSONArray("results");
            Log.e("JSON: ", transactions.toString());

            //We're going to parse each event and store the information as an EventListing object
            //So they can be fed into the EventAdapter and displayed on the screen
            Transaction[] resultStrs = new Transaction[transactions.length()];

            //Iterate through each event in the JSONArray
            for(int i = 0; i < transactions.length(); i++) {
                //Create a new event object and a new EventListing Object to store the information in
                JSONObject currTransaction = transactions.getJSONObject(i);


                //Next get all the required transaction details required from the JSONObject
                String dateString = currTransaction.getString("transactionDateTime");

                try {
                    Date date = (Date) dateFormatter.parse(dateString);
                    String description = currTransaction.getString("transactionDescription");
                    double amount = currTransaction.getInt("transactionAmount");
                    String currency = currTransaction.getString("transactionCurrency");
                    double balance = currTransaction.getDouble("accountBalance");
                    Transaction newTrans = new Transaction(date, amount, description, currency, balance);
                    //Add the object to eh array.
                    resultStrs[i] = newTrans;
                }
                catch (ParseException a){
                    Log.e("ERROR:", a.getMessage());
                }






            }
            return resultStrs;
        }



        @Override
        protected void onPostExecute(Transaction[] results) {

            //Create the chart to occupy
//            BarChart chart = (BarChart) findViewById(R.id.spend_chart);
//
//            //List of Entries
//            List<Entry> entries = new ArrayList<Entry>();
           //Occupy the EventAdapter with the results from the query
            for (int i = 0; i < results.length; i++) {
//                double day = results[i].getDate().getDate();
//                double am = results[i].getBalance();
//                float b = (float) am;
//                float a = (float) day;
//                entries.add(new Entry(a, b));
//                LineDataSet dataSet = new LineDataSet(entries, "Label");
//
//                BarData lineData = new BarData(dataSet);
//                chart.setData(lineData);
//                chart.invalidate();

                transactionListAdapter.add(results[i]);
            }


        }
    }
}
