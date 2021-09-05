package com.david.ardfmanager.controlpoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ControlPoint implements Serializable {

        private String name;
        private int code;
        private int type; //0-classic, 1-beacon, 2-spectator

        public ControlPoint(String number, int code, int type) {
                this.name = number;   //cislo
                this.code = code;       //kod
                this.type = type;       //typ
        }

    public JSONObject ControlPointToJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("number", this.name);
        json.put("code", this.code);
        json.put("type", this.type);
        return json;
    }

        public String getName() {
            return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
                this.code = code;
        }

        public int getType() {
        return type;
    }

        public void setType(int type) {
        this.type = type;
    }
}
