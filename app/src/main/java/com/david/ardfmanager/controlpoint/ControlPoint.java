package com.david.ardfmanager.controlpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ControlPoint implements Serializable {

        private int number;
        private int code;
        private boolean obligatory;
        private boolean beacon;

        public ControlPoint(int number, int code, boolean obligatory, boolean beacon) {
                this.number = number;   //cislo
                this.code = code;       //kod
                this.obligatory = obligatory;   //povinna
                this.beacon = beacon;   //majak
        }

    public JSONObject ControlPointToJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("number", this.number);
        json.put("code", this.code);
        json.put("isObligatory", this.obligatory);
        json.put("isBeacon", this.beacon);
        return json;
    }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
                this.number = number;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
                this.code = code;
        }

        public boolean isObligatory() {
            return obligatory;
        }

        public void setObligatory(boolean obligatory) {
            this.obligatory = obligatory;
        }

        public boolean isBeacon() {
            return beacon;
        }

        public void setBeacon(boolean beacon) {
            this.beacon = beacon;
        }

    }
