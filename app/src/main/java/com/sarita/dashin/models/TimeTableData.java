package com.sarita.dashin.models;

import com.sarita.dashin.SelectItems;

import java.util.ArrayList;
import java.util.Map;

public class TimeTableData {
    Map<String, ArrayList<String>> MENU;
    String START_TIME;
    String END_TIME;
    String NAME;
    boolean LIVE;

    public TimeTableData(Map MENU, String START_TIME, String END_TIME, String NAME, boolean LIVE) {
        this.MENU = MENU;
        this.START_TIME = START_TIME;
        this.END_TIME = END_TIME;
        this.NAME = NAME;
        this.LIVE = LIVE;
    }

    public TimeTableData() {
    }

    public Map getMENU() {
        return MENU;
    }

    public void setMENU(Map MENU) {
        this.MENU = MENU;
    }

    public String getSTART_TIME() {
        return START_TIME;
    }

    public void setSTART_TIME(String START_TIME) {
        this.START_TIME = START_TIME;
    }

    public String getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(String END_TIME) {
        this.END_TIME = END_TIME;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public boolean isLIVE() {
        return LIVE;
    }

    public void setLIVE(boolean LIVE) {
        this.LIVE = LIVE;
    }
}
