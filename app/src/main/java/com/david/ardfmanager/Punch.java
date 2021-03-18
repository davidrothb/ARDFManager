package com.david.ardfmanager;

import java.sql.Time;

public class Punch {
    private Time time;
    int code;
    String type;
    boolean valid;

    public Punch(Time time, int code, String type, boolean valid){
        this.time = time;
        this.code = code;
        this.type = type;
        this.valid = valid;
    }

    public void setTime(Time time){
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    public void setCode(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    /*
    (time, code, type, valid)

            (10:00:05; 41; CHK, 1)
            (10:20:45; 42; CP, 0)
            (10:25:45; 45; CP, 0)
            (10:36:45; 43; CP, 0)
            (10:45:25; 99; BCN, 0)
            (10:46:10; 100; FNS, 1)*/
}
