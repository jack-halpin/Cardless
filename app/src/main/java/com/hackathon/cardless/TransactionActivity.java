package com.hackathon.cardless;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.hackathon.cardless.Constants.PRIMARY_DEV;
import static com.hackathon.cardless.Constants.TOKEN;

public class TransactionActivity extends AppCompatActivity {


    private TransactionAdapter transactionListAdapter; //Adapter used for occupying fragment Listview
    private FetchTransInfo fetch; //AsyncTask used to get info from the EventFul API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);


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
            String eventJsonStr = null;


            //Next need to construct the URL string used to query the API
            try {
                //Define the base url that contains the API key for the application
                String base_url = "https://bluebank.azure-api.net/api/v0.7/accounts/7d50e4f6-1047-476b-a4f9-063e6bb8b396";



                Log.e("URL", base_url);


                Uri builtUri = Uri.parse(base_url);
                URL url = new URL(builtUri.toString());

                // Create the request to Eventful, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Ocp-Apim-Subscription-Key", "c1c6b0e768fe4ef4a510da6dd9f4f666");
                urlConnection.setRequestProperty("Authorization", "bearer " + TOKEN);
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                System.out.println(inputStream);
                eventJsonStr = readStream(inputStream);
                Log.e("JSON", eventJsonStr);

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

//            try {
//                //return getEventInfoFromJson(eventJsonStr);
//            } catch (JSONException e) {
//                Log.e(LOG_TAG, e.getMessage(), e);
//                e.printStackTrace();
//            }

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

//        public EventListing[] getEventInfoFromJson(String eventInfo) throws JSONException{
//            //Create the JSONObject from the string returned from the API query
//            JSONObject eventJson = new JSONObject(eventInfo);
//            JSONObject eventlistings = eventJson.getJSONObject("events");
//
//            //Get a JSON array of all the events that are listed from the query
//            JSONArray events = eventlistings.getJSONArray("event");
//
//            //We're going to parse each event and store the information as an EventListing object
//            //So they can be fed into the EventAdapter and displayed on the screen
//            EventListing[] resultStrs = new EventListing[events.length()];
//            //Iterate through each event in the JSONArray
//            for(int i = 0; i < events.length(); i++) {
//                //Create a new event object and a new EventListing Object to store the information in
//                JSONObject currEvent = events.getJSONObject(i);
//                EventListing newEvent = new EventListing();
//
//                //Need to make sure that an image is supplied for the event
//                //If no image is supplied a default image will be used
//                String img_url;
//                if (currEvent.isNull("image")) {
//                    img_url = "";
//                } else {
//                    //Get the url for the image to be displayed
//                    img_url = currEvent.getJSONObject("image").getJSONObject("block200").getString("url");
//                }
//
//                //Next get all the required event details required from the JSONObject
//                String title = currEvent.getString("title");
//                Log.e("title", title);
//                String date = currEvent.getString("start_time");
//                String endTime = currEvent.getString("stop_time");
//                String venue = currEvent.getString("venue_name");
//                int allDay = currEvent.getInt("all_day");
//                double lat = currEvent.getDouble("latitude");
//                double lng = currEvent.getDouble("longitude");
//                String id = currEvent.getString("id");
//                String url = currEvent.getString("url");
//                String venueAdd = currEvent.getString("venue_address");
//
//                //Some events do not have a performer field filled in and this field is used
//                //When searching for music later on. If the field isn't supplied it will be
//                //set to 'Unknown' in the EventListing object
//                String name;
//                Log.e("performer:", currEvent.getString("performers"));
//                if(currEvent.isNull("performers") || currEvent.getJSONObject("performers").getString("performer").charAt(0) == '['){
//                    name = "Unknown Artist";
//                } else {
//                    name = currEvent.getJSONObject("performers").getJSONObject("performer").getString("name");
//                }
//
//                //If no description of the event is supplied just set it to "No Information available."
//                String description;
//                if (currEvent.getString("description") == "null") {
//                    description = "No information available.";
//                } else {
//                    description = android.text.Html.fromHtml(currEvent.getString("description")).toString();
//                }
//
//                //Use the gathered variables to set the information for the current EventListing object
//                newEvent.setEventInfo(title, img_url, date, allDay, description, venue, venueAdd, lat, lng, id, url, name, endTime);
//                newEvent.setBitmapFromURL(img_url, getResources());
//
//                //Add the object to eh array.
//                resultStrs[i] = newEvent;
//            }
//            return resultStrs;
//        }


        @Override
        protected void onPostExecute(Transaction[] results) {

//            //If results were found
//            if (results != null) {
//                //Hide the loading text
//                TextView screenText = (TextView) getActivity().findViewById(R.id.loading_text);
//                screenText.setVisibility(View.GONE);
//
//                //Occupy the EventAdapter with the results from the query
//                for (int i = 0; i < results.length; i++) {
//                    eventLists.add(results[i]);
//                }
//            }
//            //Else set the loading TextView to display that no results have been found
//            else{
//                TextView screenText = (TextView) getActivity().findViewById(R.id.loading_text);
//                screenText.setText("Sorry your search returned no results.");
//            }

        }
    }
}
