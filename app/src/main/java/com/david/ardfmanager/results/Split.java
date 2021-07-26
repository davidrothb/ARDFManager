package com.david.ardfmanager.results;

import java.sql.Time;

//Class for computing the time of the run and the split times from punches

// punch format Time punchTime, int code, String type
//split format  int code, char CPStatus, String type, Time punchTime, time relTime, time splitTime

public class Split {
    private int code; // the SI code number
    private char CPStatus; // The status of the control + valid control, - control already taken before, ? - invalid control for the category
    private boolean beacon; // 0 - not a beacon, 1 - beacon
    private Time punchTime; // The TOD of the punch
    private Time relTime;   // The time from the start
    private Time splitTime; // The split time between two Punches

    //constructors
    public Split(int code, char CPStatus, boolean beacon, Time punchTime, Time relTime, Time splitTime) {
        this.code = code;
        this.CPStatus = CPStatus;
        this.beacon = beacon;
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

    //getters and setters
    public char getCPStatus() {
        return CPStatus;
    }

    public void setCPStatus(char CPStatus) {
        this.CPStatus = CPStatus;
    }

    public boolean getType() {
        return beacon;
    }

    public void setType(boolean beacon) {
        this.beacon = beacon;
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
