package com.hackathon.cardless;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.CookieManager;

import com.microsoft.aad.adal.AuthenticationContext;

import static com.hackathon.cardless.Constants.TOKEN;

public class LogoutActivity extends AppCompatActivity {


    private SharedPreferences sp;
    private AuthenticationContext mAuthContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);



        //Set the token to null for now

        TOKEN = null;

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
