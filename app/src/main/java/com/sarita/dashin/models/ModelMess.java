package com.sarita.dashin.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;

@IgnoreExtraProperties
public class ModelMess {
    String BUSI_NAME, FRONT_PIC, OPEN_FROM, OPEN_TILL;
    String OWNER_NAME, OWNER_CONTACT, OWNER_EMAIL, OWNER_PASSWORD, OWNER_IMAGE;
    @PropertyName("MESS_DESCRIPTION")
    public String BUSI_DESCRIPTION;
    @PropertyName("MESS_ADDRESS")
    public String ADDRESS;
    Timestamp TimeStamp;
    GeoPoint LOCATION;
    int COSTING, DISCOUNT;
    double RATING;
    ArrayList<String> FACILITIES = new ArrayList<>();
    ArrayList<String> Mess_IMAGES = new ArrayList<>();

    public ModelMess(String BUSI_NAME, String BUSI_DESCRIPTION, String OWNER, String ADDRESS, int COSTING, String FRONT_PIC, String OPEN_FROM, String OPEN_TILL, Timestamp timeStamp, GeoPoint LOCATION, double RATING) {
        this.BUSI_NAME = BUSI_NAME;
        this.BUSI_DESCRIPTION = BUSI_DESCRIPTION;
        this.ADDRESS = ADDRESS;
        this.COSTING = COSTING;
        this.FRONT_PIC = FRONT_PIC;
        this.OPEN_FROM = OPEN_FROM;
        this.OPEN_TILL = OPEN_TILL;
        this.LOCATION = LOCATION;
        this.RATING = RATING;
    }

    public void setMESS_IMAGES(ArrayList<String> FACILITIES) {
        this.Mess_IMAGES = FACILITIES;
    }

    public ArrayList<String> getMess_IMAGES() {
        return Mess_IMAGES;
    }

    public void addMESS_IMAGES(String facilities) {
        this.Mess_IMAGES.add(facilities);
    }

    public void removeMESS_IMAGES(String facilities) {
        this.Mess_IMAGES.remove(facilities);
    }

    public String getOWNER_PASSWORD() {
        return OWNER_PASSWORD;
    }

    public void setOWNER_PASSWORD(String OWNER_PASSWORD) {
        this.OWNER_PASSWORD = OWNER_PASSWORD;
    }

    public String getOWNER_IMAGE() {
        return OWNER_IMAGE;
    }

    public void setOWNER_IMAGE(String OWNER_IMAGE) {
        this.OWNER_IMAGE = OWNER_IMAGE;
    }

    public void setFACILITIES(ArrayList<String> FACILITIES) {
        this.FACILITIES = FACILITIES;
    }

    public ArrayList<String> getFACILITIES() {
        return FACILITIES;
    }

    public void addFACILITIES(String facilities) {
        this.FACILITIES.add(facilities);
    }

    public void removeFACILITIES(String facilities) {
        this.FACILITIES.remove(facilities);
    }

    public ModelMess(){}

    public String getOWNER_NAME() {
        return OWNER_NAME;
    }

    public void setOWNER_NAME(String OWNER_NAME) {
        this.OWNER_NAME = OWNER_NAME;
    }

    public String getOWNER_CONTACT() {
        return OWNER_CONTACT;
    }

    public void setOWNER_CONTACT(String OWNER_CONTACT) {
        this.OWNER_CONTACT = OWNER_CONTACT;
    }

    public String getOWNER_EMAIL() {
        return OWNER_EMAIL;
    }

    public void setOWNER_EMAIL(String OWNER_EMAIL) {
        this.OWNER_EMAIL = OWNER_EMAIL;
    }

    @Override
    public String toString() {
        return "ModelMess{" +
                "BUSI_NAME='" + BUSI_NAME + '\'' +
                ", BUSI_DESCRIPTION='" + BUSI_DESCRIPTION + '\'' +
                ", ADDRESS='" + ADDRESS + '\'' +
                ", COSTING='" + COSTING + '\'' +
                ", FRONT_PIC='" + FRONT_PIC + '\'' +
                ", OPEN_FROM='" + OPEN_FROM + '\'' +
                ", OPEN_TILL='" + OPEN_TILL + '\'' +
                ", TimeStamp=" + TimeStamp +
                ", LOCATION=" + LOCATION +
                ", RATING=" + RATING +
                '}';
    }

    public int getDISCOUNT() {
        return DISCOUNT;
    }

    public void setDISCOUNT(int DISCOUNT) {
        this.DISCOUNT = DISCOUNT;
    }

    public String getBUSI_NAME() {
        return BUSI_NAME;
    }


    public void setBUSI_NAME(String BUSI_NAME) {
        this.BUSI_NAME = BUSI_NAME;
    }


    @PropertyName("MESS_DESCRIPTION")
    public String getBUSI_DESCRIPTION() {
        return BUSI_DESCRIPTION;
    }

    @PropertyName("MESS_DESCRIPTION")
    public void setBUSI_DESCRIPTION(String BUSI_DESCRIPTION) {
        this.BUSI_DESCRIPTION = BUSI_DESCRIPTION;
    }

    @PropertyName("MESS_ADDRESS")
    public String getADDRESS() {
        return ADDRESS;
    }

    @PropertyName("MESS_ADDRESS")
    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public int getCOSTING() {
        return COSTING;
    }

    public void setCOSTING(int COSTING) {
        this.COSTING = COSTING;
    }

    public String getFRONT_PIC() {
        return FRONT_PIC;
    }

    public void setFRONT_PIC(String FRONT_PIC) {
        this.FRONT_PIC = FRONT_PIC;
    }

    public String getOPEN_FROM() {
        return OPEN_FROM;
    }

    public void setOPEN_FROM(String OPEN_FROM) {
        this.OPEN_FROM = OPEN_FROM;
    }

    public String getOPEN_TILL() {
        return OPEN_TILL;
    }

    public void setOPEN_TILL(String OPEN_TILL) {
        this.OPEN_TILL = OPEN_TILL;
    }

    public Timestamp getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        TimeStamp = timeStamp;
    }

    public GeoPoint getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(GeoPoint LOCATION) {
        this.LOCATION = LOCATION;
    }

    public double getRATING() {
        return RATING;
    }

    public void setRATING(double RATING) {
        this.RATING = RATING;
    }
}