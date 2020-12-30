package com.david.ardfmanager.event;

import com.david.ardfmanager.competitors.Competitor;
import com.david.ardfmanager.tracks.Track;

import java.util.ArrayList;

public class Event {

    private String name;
    private int level;  //1-national ,2-regional ,3-district ,4-not a championship
    private int band;   //0-combinated, 1-80m, 2-2m
    private int type;   //0-classics, 1-foxoring, 2-sprint, 3-long

    public static ArrayList<Track> tracksList;
    public static ArrayList<Competitor> competitorsList;

    public Event(String name, int level, int band, int type) {
        this.name = name;
        this.level = level;
        this.band = band;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBand() {
        return band;
    }

    public void setBand(int band) {
        this.band = band;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
