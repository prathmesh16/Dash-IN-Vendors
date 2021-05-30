package com.sarita.dashin.models;


public class Discount {
    private String Type;

    public int getOFFP() {
        return OFFP;
    }

    public void setOFFP(int OFFP) {
        this.OFFP = OFFP;
    }

    private int OFFP;
    public Discount()
    {

    }
    public Discount(String type, String code,int offp) {
        Type = type;
        Code = code;
        OFFP = offp;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    private String Code;
}

