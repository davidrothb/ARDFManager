package com.david.ardfmanager.split;

public class ControlPointEvaluator {

    /* This class serves as an evaluation mechanism for the punches.
    It's purpose is to calculate the final Fox number and eventually filter the wrong ones.
     */

    //fields section
    //TODO connect them with actual competitors and controlPoints

    public static int eventType; //this value is obtained from the Event class: 0-classics, 1-foxoring, 2-sprint, 3-long


    //This method selects the right evaluation algorithm, depending on the type of the event

    public static void evaluationMethodSelector() {
        switch (eventType) {
            case 0:
                handleClassics();
                break;
            case 1:
                handleSprint()
                break;
            case 2:
                handleOrienteering()
                break;
        }
    }
}
