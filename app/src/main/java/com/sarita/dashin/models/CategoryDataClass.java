package com.sarita.dashin.models;

public class CategoryDataClass {
    String CAT_NAME,CAT_ID;
    boolean IS_THALI;

    public CategoryDataClass(String CAT_NAME, boolean IS_THALI, String CAT_ID) {
        this.CAT_NAME = CAT_NAME;
        this.IS_THALI = IS_THALI;
        this.CAT_ID=CAT_ID;
    }

    public CategoryDataClass() {
    }

    public String getCAT_NAME() {
        return CAT_NAME;
    }

    public void setCAT_NAME(String CAT_NAME) {
        this.CAT_NAME = CAT_NAME;
    }

    public boolean isIS_THALI() {
        return IS_THALI;
    }

    public void setIS_THALI(boolean IS_THALI) {
        this.IS_THALI = IS_THALI;
    }

    public String getCAT_ID() {
        return CAT_ID;
    }

    public void setCAT_ID(String CAT_ID) {
        this.CAT_ID = CAT_ID;
    }
}
