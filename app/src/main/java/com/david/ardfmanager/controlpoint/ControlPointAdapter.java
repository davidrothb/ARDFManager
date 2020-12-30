package com.david.ardfmanager.controlpoint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.david.ardfmanager.R;

import java.util.ArrayList;

public class ControlPointAdapter extends ArrayAdapter<ControlPoint> {

    private Context mContext;
    int mResource;
    TextView textNumber, textCode, textObligatory, textBeacon;

    public ControlPointAdapter(Context context, int resource, ArrayList<ControlPoint> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int number = getItem(position).getNumber();
        int code = getItem(position).getCode();
        boolean obligatory = getItem(position).isObligatory();
        boolean beacon = getItem(position).isBeacon();


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        textNumber = (TextView) convertView.findViewById(R.id.textNumber);
        textCode = (TextView) convertView.findViewById(R.id.textCode);
        textObligatory  = (TextView) convertView.findViewById(R.id.textObligatory);
        textBeacon  = (TextView) convertView.findViewById(R.id.textBeacon);


        textNumber.setText(String.valueOf(number));
        textCode.setText(String.valueOf(code));

        if(obligatory){
            textObligatory.setText(R.string.obligatory);
        }else{
            textObligatory.setText(R.string.non_obligatory);
        }

        if(beacon){
            textBeacon.setTextColor(getContext().getResources().getColor(R.color.colorOrange));
            textBeacon.setText(R.string.beacon);
        }else{
            textBeacon.setText("");
        }

        return convertView;
    }
}
