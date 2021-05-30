package com.sarita.dashin.models;

import java.util.HashMap;
import java.util.Map;

public class MenuTime {
    String breakfast;
    String dinner;

    public MenuTime()
    {

    }
    public MenuTime(String breakfast, String dinner, String lunch) {
        this.breakfast = breakfast;
        this.dinner = dinner;
        this.lunch = lunch;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    String lunch;


}
