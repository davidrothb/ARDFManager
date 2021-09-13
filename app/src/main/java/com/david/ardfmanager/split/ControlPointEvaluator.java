package com.david.ardfmanager.split;

import com.david.ardfmanager.MainActivity;
import com.david.ardfmanager.SI.Punch;
import com.david.ardfmanager.controlpoint.ControlPoint;

import java.util.ArrayList;

public class ControlPointEvaluator {

    /* This class serves as an evaluation mechanism for the punches.
    It's purpose is to calculate the final Fox number and eventually filter the wrong ones.
     */

    //fields section
    //TODO connect them with actual competitors and controlPoints

    private int eventType = MainActivity.event.getType(); //this value is obtained from the Event class: 0-classics, 1-foxoring, 2-sprint, 3-long
    private String category; //TODO: Add a method to get the competitors' category


    private ArrayList<Punch> competitorPunches = new ArrayList<>();
    private ArrayList<ControlPoint> validCPList = new ArrayList<>();


    //This method selects the right evaluation algorithm, depending on the type of the event

    public void evaluationMethodSelector(int eventType) {
        switch (eventType) {
            case 0:
                handleClassics();
                break;
            case 1:
                handleSprint();
                break;
            case 2:
                handleOrienteering();
                break;
        }
    }

    public void handleClassics() {

        //loops through all control points
        for (int i = 0; i < competitorPunches.size(); i++) {
            for (int j = 0; j < validCPList.size(); j++) {

                //checks if the first control point has the same code as one of the valid ones
                if (competitorPunches.get(i).getCode() == validCPList.get(j).getCode()) {
                    competitorPunches.get(i).setCPStatus('+');

                } else {
                    //In this case competitor took CP which he was not supposed to take
                    competitorPunches.get(i).setCPStatus('?');
                }
            }
        }

        //this loops solves the redundancy
        for (int k = 0; k < competitorPunches.size(); k++) {
            for (int l = 0; l < validCPList.size(); l++) {

                if (competitorPunches.get(k).getCode() == competitorPunches.get(l).getCode()) {
                    competitorPunches.get(k).setCPStatus('-');
                }
            }
        }

        //this checks if beacon is last CP
        for (int m = 0; m < competitorPunches.size(); m++) {
        }

    }


    public static void handleSprint() {

    }

    public static void handleOrienteering() {

    }

    public int foxCounter() {
        int counter = 0;


        return counter;

    }
}
