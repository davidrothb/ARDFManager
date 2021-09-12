package com.david.ardfmanager.SI;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Punch implements Parcelable, Serializable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Punch createFromParcel(Parcel in) {
            return new Punch(in);
        }

        public Punch[] newArray(int size) {
            return new Punch[size];
        }
    };
    public int code;
    public long time;

    public Punch()
    {
    }

    public Punch(int code, long time){
        this.code = code;
        this.time = time;
    }

    public Punch(Parcel in) {
        this.code = in.readInt();
        this.time = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeLong(this.time);
    }
}


/*public class Punch {

    private Time punchTime;
    private int code;
    private String type;   //CHK - erase check, ST - start, CP - control point, B - beacon, F - finish
    private char CPStatus; // + valid control, - control already taken before, ? - invalid control for the category

    public Punch(Time punchTime, int code, String type, char CPStatus) {
        this.punchTime = punchTime;
        this.code = code;
        this.type = type;
        this.CPStatus = CPStatus;

    }
    public Time getPunchTime() {
        return punchTime;
    }

    public void setPunchTime(Time punchTime) {
        this.punchTime = punchTime;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}*/
