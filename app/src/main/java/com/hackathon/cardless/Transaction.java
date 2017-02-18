package com.hackathon.cardless;

/**
 * Created by Jack on 18/02/2017.
 */

public class Transaction {

    private String paymentType;
    private String toSortCode;
    private String paymentReference;
    private double paymentAmount;
    private String currency;
    private String Date;

    public Transaction(String paymentT, String toSortCode, String paymentRef, double amount, String currency, String Date){
        this.paymentType = paymentT;
        this.toSortCode = toSortCode;
        this.paymentReference = paymentRef;
        this.paymentAmount = amount;
        this.currency = currency;
        this.Date = Date;
    }

    public String getPaymentType(){
        return this.paymentType;
    }

    public String getDate(){
        return this.Date;
    }
    public String getSortCode(){
        return this.toSortCode;
    }

    public String getReference(){
        return this.paymentReference;
    }

    public double getAmount(){
        return this.paymentAmount;
    }

    public String getCurrency(){
        return this.currency;
    }


}
