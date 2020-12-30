package com.david.ardfmanager.tracks;

import com.david.ardfmanager.competitors.Competitor;
import com.david.ardfmanager.controlpoint.ControlPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Track implements Serializable {

    private String name;
    private float length;
    private ArrayList<ControlPoint> controlPoints = new ArrayList<ControlPoint>();
    //private ArrayList<Competitor> competitors = new ArrayList<Competitor>();

    public Track(String name, float length, ArrayList<ControlPoint> controlPoints) {
        this.name = name;
        this.length = length;
        this.controlPoints = controlPoints;
    }

    public JSONObject trackToJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("length", this.length);
        JSONArray jsonControlPointsArray = new JSONArray();
        for (int i = 0; i < controlPoints.size(); i++) {
            jsonControlPointsArray.put(controlPoints.get(i).ControlPointToJSON());
        }
        json.put("controlPoints", jsonControlPointsArray);
        return json;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
            this.name = name;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public int getControlPointCount() {
            return controlPoints.size();
    }

    public ArrayList<ControlPoint> getControlPoints(){
        return this.controlPoints;
    }

    public void setControlPoints(ArrayList<ControlPoint> in){
        this.controlPoints = in;
    }


        /*
    public void pridejKontrolu(Kontrola k) {
        kontroly.add(k);
    }

    public void odeberKontrolu(Kontrola k) {
        if (kontroly.contains(k)) {
            kontroly.remove(k);
        } else {
            System.out.println("Kontrola neexistuje");
        }

    }*/
}