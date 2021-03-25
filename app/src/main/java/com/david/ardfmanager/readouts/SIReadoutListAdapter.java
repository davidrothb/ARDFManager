package com.david.ardfmanager.readouts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.david.ardfmanager.R;
import com.david.ardfmanager.controlpoint.ControlPoint;

import java.util.ArrayList;

public class SIReadoutListAdapter extends ArrayAdapter<SIReadout> {

    private Context mContext;
    int mResource;
    TextView textNumber;

    public SIReadoutListAdapter(Context context, int resource, ArrayList<SIReadout> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        long cardId = getItem(position).getCardId();
        long startTime = getItem(position).getStartTime();
        long finishTime = getItem(position).getFinishTime();
        long checkTime = getItem(position).getCheckTime();


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        textNumber = (TextView) convertView.findViewById(R.id.textNumber);

        textNumber.setText(String.valueOf(cardId));

        return convertView;
    }
}
