package com.hackathon.cardless;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static com.hackathon.cardless.Constants.TOKEN;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Check if the user has logged in
        if (TOKEN == null){
            //Send to login page
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        else{
            //Send to the menu
            Intent menuIntent = new Intent(this, MenuActivity.class);
            startActivity(menuIntent);
            finish();
        }
    }
}
