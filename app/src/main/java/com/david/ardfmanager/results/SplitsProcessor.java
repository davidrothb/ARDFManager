package com.david.ardfmanager.results;


import java.sql.Time;
import java.util.ArrayList;



public class SplitsProcessor {
    //TESTING DATA
    private ArrayList<Punch> t = new ArrayList<Punch>();

    public void main (String[]args){
        t.add(new Punch(null,32));

    }

    private ArrayList<Punch> punches = new ArrayList<Punch>(); //TODO: Reference to the actual arrayList
    private ArrayList<Split> splits = new ArrayList<Split>();

    private Time startTime; //TODO: get the actual start time

    public void calculateTimes() {
        long relTimeCounter = 0;
        for (int i = 0; i < punches.size(); i++) {
            Punch p = punches.get(i);
            Time cpOne;

            if (i != 0) {
                cpOne = punches.get(i - 1).getPunchTime();

            } else {
                cpOne = startTime;
            }
            Time split = calculateSplit(cpOne, p.getPunchTime());
            relTimeCounter = relTimeCounter + split.getTime();
            Split s = new Split(p.getCode(), p.getPunchTime(), new Time(relTimeCounter), split);
            splits.add(s);
        }
    }

    public Time calculateSplit(Time cpOne, Time cpTwo) {
        long diff = (cpTwo.getTime() - cpOne.getTime());
        return new Time(diff);
    }

    public Time stringToTime(){
        Time time;

        return null;
    }


}
