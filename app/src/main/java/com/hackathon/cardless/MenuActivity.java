package com.hackathon.cardless;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void paymentOnClick(View view){
        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }

    public void transactionOnClick(View view){
        Intent intent = new Intent(this, TransactionActivity.class);
        startActivity(intent);
    }

    public void accountOnClick(View view){

    }

    public void sendmoneyOnClick(View view){
        Intent intent = new Intent(this, SendmoneyActivity.class);
        startActivity(intent);
        finish();
    }
}
