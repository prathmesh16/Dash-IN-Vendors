package com.sarita.dashin.models;

import com.google.firebase.firestore.PropertyName;

public class ModelMenuItem {

    String Name, Description, Type;
    Long Price;
    @PropertyName("VEG")
    Boolean VEG;

    ModelMenuItem(){}

    public Boolean getVEG() {
        return VEG;
    }

    public void setVEG(Boolean VEG) {
        this.VEG = VEG;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Long getPrice() {
        return Price;
    }

    public void setPrice(Long price) {
        Price = price;
    }

}
