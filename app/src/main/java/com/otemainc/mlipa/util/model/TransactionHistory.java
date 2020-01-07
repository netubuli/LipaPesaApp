package com.otemainc.mlipa.util.model;

public class TransactionHistory {
    private int id;
    private String code;
    private String tdate;
    private String sender;
    private double amount;
    private String receiver;

    public TransactionHistory(int id, String code, String tdate, String sender, double amount, String receiver) {
        this.id = id;
        this.code = code;
        this.tdate = tdate;
        this.sender = sender;
        this.amount = amount;
        this.receiver = receiver;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getTdate() {
        return tdate;
    }

    public String getSender() {
        return sender;
    }

    public double getAmount() {
        return amount;
    }

    public String getReceiver() {
        return receiver;
    }
}