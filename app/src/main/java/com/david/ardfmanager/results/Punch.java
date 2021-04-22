package com.david.ardfmanager.results;

import java.sql.Time;

public class Punch {

    private Time punchTime;
    private int code;
    private String type;

    //CHK - erase check, ST - start, CP - control point, B - beacon, F - finish


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



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
