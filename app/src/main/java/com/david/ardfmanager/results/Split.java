package com.david.ardfmanager.results;

import java.sql.Time;

// punch format Time punchTime, int code, String type
//split format  int code, char CPStatus, String type, Time punchTime, time relTime, time splitTime
public class Split {
    private int code;
    private char CPStatus;
    private String type;
    private Time punchTime;
    private Time relTime;
    private Time splitTime;

    public Split(int code, char CPStatus, String type, Time punchTime, Time relTime, Time splitTime) {
        this.code = code;
        this.CPStatus = CPStatus;
        this.type = type;
        this.punchTime = punchTime;
        this.relTime = relTime;
        this.splitTime = splitTime;
    }

    public Split(int code, Time punchTime, Time relTime, Time splitTime) {
        this.code = code;
        this.punchTime = punchTime;
        this.relTime = relTime;
        this.splitTime = splitTime;
    }

    public char getCPStatus() {
        return CPStatus;
    }

    public void setCPStatus(char CPStatus) {
        this.CPStatus = CPStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Time getPunchTime() {
        return punchTime;
    }

    public void setPunchTime(Time punchTime) {
        this.punchTime = punchTime;
    }

    public Time getRelTime() {
        return relTime;
    }

    public void setRelTime(Time relTime) {
        this.relTime = relTime;
    }

    public Time getSplitTime() {
        return splitTime;
    }

    public void setSplitTime(Time splitTime) {
        this.splitTime = splitTime;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
