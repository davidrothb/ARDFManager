package com.david.ardfmanager.results;

import com.david.ardfmanager.competitors.Competitor;
import com.david.ardfmanager.controlpoint.ControlPoint;

import java.sql.Time;
import java.util.ArrayList;

public class Results {


    // ALL TO BE UPDATED!!! - dynamic reference to the CP ArrayList and to the readout

    /*
    ArrayList<Punch> punches - punches read from the SI  ......Last index should be finish. In case of no
	finish, last control can be used, but the competitor should be DQ.

    (time, code, type, status)
    (10:00:05; 41; CHK)
    (10:20:45; 42; CP, +)
    (10:25:45; 45; CP, +)
    (10:36:45; 43; CP, +)
    (10:45:25; 99; BCN, +)
    (10:46:10; 100; FNS)


    control point status
    + valid control
    - control taken before
    ? - invalid control for category


*//*
    private static int eventType;  // Get from the Category class 0 -classic, 1 - sprint, 2 - orienteering
    private static int status = 0; // 0 - valid, 1 - after time limit, 2- DQ, 3 - not evaluated

    private static ArrayList<Punch> punches = new ArrayList<>();    //punches imported from readout
    private static ArrayList<Punch> processedPunches = new ArrayList<>();   // punches already processed
    private static ArrayList<ControlPoint> validCP = new ArrayList<>(); //event.getTrack(ZAVODNIK.getTrack).getControlPoints(); //competitor.get...  // arraylist of valid controlpoints

    //Trat t = event.getTrat();
    //Kontroly k = t.getKontroly();
    //Kontroly k = event.getTrat().getKontroly();

    public void makeResults(Competitor competitor){
        punches = competitor.getPunches();
        validCP = MainActivity.event.getTrack()
    }

    //this method returns time from SI
    public static Time getTimefromSI(int type){

        switch(type){

            case 0:
                //noexc pri checktime null
                return //returns checkTime

            case 1:

                return //return startTime

            case 2:

                Time finishTime;
                if (finishTime!= null){
                    return  finishTime
                }

                else {
                    //Error - competitor does not have finish time
                    //vokno

                }
        }

    }





	/*public static String printTextOutput(ArrayList<Punches> punches ){
		String textOutput ="";


		Something that should be later used for printing via the bluetooth Printer
	}}*/

    ArrayList<Punch> punches = new ArrayList<Punch>(); //Competitor punches


    ArrayList<ControlPoint> controlPoints = new ArrayList<ControlPoint>(); //Control points for the category
    Time startTime;
    Time finishTime;
    String status;

    int allFoxNum;
    int validFoxNum;


    private int eventType;


    //Time section

    //This method is responsible for setting the correct times of check, start, and finish
    public void setTimes() {


    }


    public static Time setStartTime() {
        return null;
    }

    // This method sets the finish Time of the competitor to the value read from the SI
    public Time setFinishTime(ArrayList<Punch> punches) {

        if (punches.get(punches.size() - 1).getType().equals("F")) {
            return punches.get(punches.size() - 1).getPunchTime();
        } else {
            return null; //Error - the competitor did not punch finish/fail of SI
        }
    }


    public Time calculateRunTime(Time startTime, Time finishTime) {
        if (finishTime != null && finishTime.compareTo(startTime) > 0) {
            return new Time(finishTime.getTime() - startTime.getTime());
        } else {
            return null;
        }
    }

    //Handle classics
    public void handleClassics(ArrayList<Punch> punches, ArrayList<ControlPoint> controlPoints) {
        //alespon 1 kontrola, majak, finish,
        classicsCPStatus();
        allFoxNum();
        validFoxNumber();

    }

    //Handling Sprint event
    public static void handleCSprint() {
        //alespoň jedna kontrola na pomalém, S, kontrola na rychlém, maják

    }

    //Handling Orienteering event
    public void handleOrienteering() {


    }

    // returns the number of Foxes taken, no matter if correct or not
    public void allFoxNum() {
        ArrayList<Punch> punches = this.punches;
        int allFox = 0;

        for (int i = 0; i < punches.size(); i++) {
            if (punches.get(i).getType().equals("CP") || punches.get(i).getType().equals("B")) {
                allFox++;
            }
        }
        this.allFoxNum = allFox;
    }

    //the method calculates the number of VALID foxes for the category - valid fox with + in status
    public void validFoxNumber() {
        ArrayList<Punch> punches = this.punches;
        int validFox = 0;

        for (int i = 0; i < punches.size(); i++) {
            if (punches.get(i).getCPStatus() == '+') {
                validFox++;
            }

        }

        //checking if the beacon has been punched as a last CP

        if (punches.get(punches.size() - 1).getType().equals('F') && !punches.get(punches.size() - 2).getType().equals("B")) {

            validFox--;
        }
        this.validFoxNum = validFox;

    }


    public void classicsCPStatus() {
        /*Projet všechny kontroly v listu závodníka, porovnat je
         * 1) vzít kontrolu - ověřit duplicitu
         * 2) změnit status
         *   */
        ArrayList<Punch> punches = this.punches;
        ArrayList<ControlPoint> controlPoints = this.controlPoints;


        for (int i = 0; i < punches.size(); i++) {
            for (int j = 0; j < controlPoints.size(); j++) {
                if (controlPoints.get(j).getCode() == punches.get(i).getCode() && punches.get(i).getType().equals("CP")) {

                    punches.get(i).setCPStatus('+');

                } else {
                    punches.get(i).setCPStatus('?');

                }

            }
            /* Need to solve the duplicate fox situation !!!!!!!
             * Multiple control needs to be marked with minus
             * */
        }
    }

    public void orienteeringCPStatus() {

        // checks if the punches codes are the same as the Control reference list
        ArrayList<Punch> punches = this.punches;
        ArrayList<ControlPoint> controlPoints = this.controlPoints;

        for (int i = 0; i < punches.size(); i++) {
            if (punches.get(i).getCode() == controlPoints.get(i).getCode()) {
                punches.get(i).setCPStatus('+');
            } else {
                punches.get(i).setCPStatus('-');
            }
        }

    }


    public boolean eligible(int eventType) {
        boolean eligible = true;

        switch (eventType) {

            case 0: //classics
                if (this.validFoxNum < 2) {
                    eligible = false;
                }

            case 1: //foxoring
                ;

            case 2: //orienteering
                ;

        }


        return eligible;

    }

    public void sortResults(ArrayList<Competitor> competitors) {


    }

    //Main part
    public static void main(String[] args) {
        //setTimes();

       /* switch (eventType) {

            case 0:
                handleClassics();
                break;

        case 1:
            handleSprint()
            break;

        case 2:
            handleOrienteering()
            break;

    }*/

    }


}
