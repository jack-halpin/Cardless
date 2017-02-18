package com.hackathon.cardless;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jack on 18/02/2017.
 */

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private int layoutResource;

    //Constructor
    public TransactionAdapter(Context context, int layoutResource, List<Transaction> listOfTrans){
        super(context, layoutResource, listOfTrans);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        //If the view is null then inflate it
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        //For each item in the listOfEvents object set the content of the view
        Transaction trans = getItem(position);

        if (trans != null) {
            //Get the various views from the layout

            TextView transDate = (TextView) view.findViewById(R.id.trans_date);
            TextView transRef = (TextView) view.findViewById(R.id.trans_ref);
            TextView transAmount = (TextView) view.findViewById(R.id.trans_amount);

            //Populate them based on information stored in the currevent EventListing object
            transDate.setText(trans.getDate());
            transRef.setText(trans.getReference());
            transAmount.setText(Double.toString(trans.getAmount()));
        }

        return view;
    }
}
