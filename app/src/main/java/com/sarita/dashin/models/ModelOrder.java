package com.sarita.dashin.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;

public class ModelOrder {
    String FROM, OFFER_CODE, METHOD;
    Timestamp TIME;
     long AMOUNT, ORDER_ID, SUBSCRIPTION;
    boolean PARCEL, SERVED;

    //TYPE IS 0=PARCEL PICK UP 1=DELIVERY
    public ModelOrder()
    {

    }

    public String getFROM() {
        return FROM;
    }

    public void setFROM(String FROM) {
        this.FROM = FROM;
    }

    public String getOFFER_CODE() {
        return OFFER_CODE;
    }

    public void setOFFER_CODE(String OFFER_CODE) {
        this.OFFER_CODE = OFFER_CODE;
    }

    public String getMETHOD() {
        return METHOD;
    }

    public void setMETHOD(String METHOD) {
        this.METHOD = METHOD;
    }

    public Timestamp getTIME() {
        return TIME;
    }

    public void setTIME(Timestamp TIME) {
        this.TIME = TIME;
    }

    public long getORDER_ID() {
        return ORDER_ID;
    }

    public void setORDER_ID(long ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }

    public long getSUBSCRIPTION() {
        return SUBSCRIPTION;
    }

    public void setSUBSCRIPTION(long SUBSCRIPTION) {
        this.SUBSCRIPTION = SUBSCRIPTION;
    }

    public boolean isPARCEL() {
        return PARCEL;
    }

    public void setPARCEL(boolean PARCEL) {
        this.PARCEL = PARCEL;
    }

    public boolean isSERVED() {
        return SERVED;
    }

    public void setSERVED(boolean SERVED) {
        this.SERVED = SERVED;
    }

    @PropertyName("AMOUNT")
    public long getAMOUNT() {
        return AMOUNT;
    }

    @PropertyName("AMOUNT")
    public void setAMOUNT(long AMOUNT) {
        this.AMOUNT = AMOUNT;
    }
}
