package com.david.ardfmanager.results;


import java.sql.Time;
import java.util.ArrayList;


public class SplitsProcessor {


    private ArrayList<Punch> punches = new ArrayList<Punch>(); //TODO: Reference to the actual arrayList
    private ArrayList<Split> splits = new ArrayList<Split>();

    private Time startTime; //TODO: get the actual start time from the SIReadout
    private Time finishTime; //TODO: get the actual finish time from the SIReadout

    //This method is responsible for setting the correct times of check, start, and finish
    public void setTimes() {

    }

    public void setStartTime() {


    }

    /* // This method sets the finish Time of the competitor to the value read from the SI
    public Time setFinishTime(ArrayList<Punch> punches) {

        if (punches.get(punches.size() - 1).getType().equals("F")) {
            return punches.get(punches.size() - 1).getPunchTime();
        } else {
            return null; //Error - the competitor did not punch finish/fail of SI
        }
    }
*/

    public Time calculateRunTime(Time startTime, Time finishTime) {
        if (finishTime != null && finishTime.compareTo(startTime) > 0) {
            return new Time(finishTime.getTime() - startTime.getTime());
        } else {
            return null;
        }
    }

    public void splitConventor() {

        long relTimeCounter = 0;

        //The following loop goes through the array of punches and converts them to Splits
        for (int i = 0; i < punches.size(); i++) {
            Punch p = punches.get(i);
            Time punchOneTime; //The time of the first Punch

            //In case of index 0, punchOneTime is the start Time
            if (i != 0) {
                punchOneTime = punches.get(i - 1).getPunchTime();

            } else {
                punchOneTime = startTime;
            }
            Time split = calculateSplit(punchOneTime, p.getPunchTime()); // Gets the split value
            relTimeCounter = relTimeCounter + split.getTime(); //Adds the split Time to the relTime
            Split s = new Split(p.getCode(), p.getPunchTime(), new Time(relTimeCounter), split); // creates and adds the new split to the Array list
            splits.add(s);
        }
    }

    // This method calculates the split time between two punches.
    public Time calculateSplit(Time punchOneTime, Time punchTwoTime) {
        long diff = (punchTwoTime.getTime() - punchOneTime.getTime());
        return new Time(diff);
    }


}
