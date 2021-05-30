package com.sarita.dashin.models;

import com.google.firebase.firestore.PropertyName;

public class ItemData {
    @PropertyName("Name")
    String ITEM_NAME;
    @PropertyName("Price")
    long ITEM_PRICE;
    @PropertyName("Descirption")
    String ITEM_DESC;
    @PropertyName("Type")
    String Type;
    @PropertyName("VEG")
    Boolean Veg;

    ItemData(){}

    public String getITEM_NAME() {
        return ITEM_NAME;
    }

    public void setITEM_NAME(String ITEM_NAME) {
        this.ITEM_NAME = ITEM_NAME;
    }

    public long getITEM_PRICE() {
        return ITEM_PRICE;
    }

    public void setITEM_PRICE(long ITEM_PRICE) {
        this.ITEM_PRICE = ITEM_PRICE;
    }

    public String getITEM_DESC() {
        return ITEM_DESC;
    }

    public void setITEM_DESC(String ITEM_DESC) {
        this.ITEM_DESC = ITEM_DESC;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Boolean getVeg() {
        return Veg;
    }

    public void setVeg(Boolean veg) {
        Veg = veg;
    }
}


