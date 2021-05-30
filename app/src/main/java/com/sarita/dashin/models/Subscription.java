package com.sarita.dashin.models;

public class Subscription {
    private String ToDate;
    private String FromDate;
    private int Price;
    private String MessName;

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    private String CustomerName;
    private String CustomerID;

    public Subscription(String toDate, String fromDate, int price, String messName, String customerName, String customerID, String type) {
        ToDate = toDate;
        FromDate = fromDate;
        Price = price;
        MessName = messName;
        CustomerName = customerName;
        CustomerID = customerID;
        Type = type;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getMessName() {
        return MessName;
    }

    public void setMessName(String messName) {
        MessName = messName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    private String Type;

    Subscription(){

    }
}
