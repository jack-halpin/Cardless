package com.hackathon.cardless;

import java.util.Date;

/**
 * Created by Jack on 18/02/2017.
 */

public class Transaction {

    private String paymentType;
    private String toSortCode;
    private String paymentReference;
    private double paymentAmount;
    private String currency;
    private Date date;
    private double balance;

    public Transaction(String paymentT, String toSortCode, String paymentRef, double amount, String currency, Date date){
        this.paymentType = paymentT;
        this.toSortCode = toSortCode;
        this.paymentReference = paymentRef;
        this.paymentAmount = amount;
        this.currency = currency;
        this.date = date;
    }

    public Transaction(Date Date, double amount, String paymentRef, String currency, double balance){
        this.paymentReference = paymentRef;
        this.paymentAmount = amount;
        this.currency = currency;
        this.date = Date;
        this.balance = balance;
    }

    public String getPaymentType(){
        return this.paymentType;
    }

    public Date getDate(){
        return this.date;
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

    public double getBalance() { return this.balance; };


}
