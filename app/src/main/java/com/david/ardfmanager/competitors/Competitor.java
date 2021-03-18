package com.david.ardfmanager.competitors;

import com.david.ardfmanager.Punch;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;

public class Competitor {
    private String name;
    private String surname;
    private int tagNumber;
    private String index;
    private int year;
    private boolean gender;

    private String track;
    private Time startTime;
    private Time finishTime;
    private Time checkTime;
    private Time totalTime;
    private int totalFoxNum;
    private int validFoxNum;

    private static ArrayList<Punch> punches = new ArrayList<>();

    public static void setPunches(ArrayList<Punch> punches) {
        Competitor.punches = punches;
    }

    public static ArrayList<Punch> getPunches() {
        return punches;
    }


    public boolean hasStartTime(){
        return (startTime != null);
    }

    public void setTotalFoxNum(int foxNum){
        this.totalFoxNum = foxNum;
    }

    public Competitor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
            this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
            this.surname = surname;
    }

    public int getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(int tagNumber) {
            this.tagNumber = tagNumber;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
            this.index = index;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
            this.year = year;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
            this.track = track;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Time finishTime) {
        this.finishTime = finishTime;
    }

    public JSONObject competitorToJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("surname", this.surname);
        json.put("year", this.year);
        json.put("tagNumber", this.tagNumber);
        json.put("gender", this.gender);
        return json;
    }
}
