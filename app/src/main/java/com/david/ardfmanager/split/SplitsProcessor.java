package com.david.ardfmanager.split;

import com.david.ardfmanager.MainActivity;
import com.david.ardfmanager.SI.Punch;
import com.david.ardfmanager.readouts.SIReadout;

import java.util.ArrayList;

public class SplitsProcessor {


    //private ArrayList<Punch> punches = new ArrayList<Punch>(); //TODO: Reference to the actual arrayList
    private ArrayList<Split> splits = new ArrayList<Split>();

    private long startTime;
    private long finishTime;

    //This method is responsible for setting the correct times of check, start, and finish
    public void setTimes() {

    }

    public void setStartTime() {


    }

    //THE IMPORTANT SHIT

    public ArrayList<Split> readoutToSplits(SIReadout siReadout){
        startTime = siReadout.getStartTime();
        finishTime = siReadout.getFinishTime();

        splitConventor(siReadout.getPunches());

        return splits;
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

    public long calculateRunTime(long startTime, long finishTime) {
        if (finishTime != 0 && finishTime > startTime) {
            return finishTime - startTime;
        } else {
            return -1;
        }
    }

    public void splitConventor(ArrayList<Punch> punches) {

        long relTimeCounter = 0;

        //The following loop goes through the array of punches and converts them to Splits
        for (int i = 0; i < punches.size(); i++) {
            Punch p = punches.get(i);
            long punchOneTime; //The time of the first Punch

            //In case of index 0, punchOneTime is the start Time
            if (i != 0) {
                punchOneTime = punches.get(i - 1).time;

            } else {
                punchOneTime = startTime;
            }
            long split = calculateSplit(punchOneTime, p.time); // Gets the split value
            relTimeCounter = relTimeCounter + split; //Adds the split Time to the relTime
            Split s = new Split(p.code, p.time, relTimeCounter, split); // creates and adds the new split to the Array list
            splits.add(s);
        }
    }

    // This method calculates the split time between two punches.
    public long calculateSplit(long punchOneTime, long punchTwoTime) {
        return punchTwoTime - punchOneTime;
    }


}
