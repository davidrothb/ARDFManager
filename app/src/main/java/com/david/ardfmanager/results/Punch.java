package com.david.ardfmanager.results;

import java.sql.Time;

public class Punch {

    private Time punchTime;
    private int code;


    public Punch(Time punchTime, int code) {
        this.punchTime = punchTime;
        this.code = code;

    }

    public Time getPunchTime() {
        return punchTime;
    }

    public void setPunchTime(Time punchTime) {
        this.punchTime = punchTime;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


/*
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

   /* public char getCPStatus() {
        return CPStatus;
    }

    public void setCPStatus(char CPStatus) {
        this.CPStatus = CPStatus;
    }
*/
}