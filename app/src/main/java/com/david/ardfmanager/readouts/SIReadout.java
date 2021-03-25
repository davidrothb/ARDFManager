package com.david.ardfmanager.readouts;

import com.david.ardfmanager.SI.CardReader;

import java.util.ArrayList;

public class SIReadout {
    private long cardId;
    private long startTime;
    private long finishTime;
    private long checkTime;
    private ArrayList<CardReader.CardEntry.Punch> punches;


    public SIReadout(long cardId, long startTime, long finishTime, long checkTime) { //ArrayList<CardReader.CardEntry.Punch> punches
        this.cardId = cardId;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.checkTime = checkTime;
        //this.punches = punches;
    }


    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(long checkTime) {
        this.checkTime = checkTime;
    }

    public ArrayList<CardReader.CardEntry.Punch> getPunches() {
        return punches;
    }

    public void setPunches(ArrayList<CardReader.CardEntry.Punch> punches) {
        this.punches = punches;
    }
}

