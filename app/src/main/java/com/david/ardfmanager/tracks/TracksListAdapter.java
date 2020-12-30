package com.david.ardfmanager.tracks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.david.ardfmanager.R;

import java.util.ArrayList;

/**
 * Created by David on 17. 7. 2017.
 */

public class TracksListAdapter extends ArrayAdapter<Track> {

    private Context mContext;
    int mResource;

    public TracksListAdapter(Context context, int resource, ArrayList<Track> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name = getItem(position).getName();
        float length = getItem(position).getLength();
        int controlPointCount = getItem(position).getControlPointCount();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView textName = (TextView) convertView.findViewById(R.id.nameText);
        TextView textLength = (TextView) convertView.findViewById(R.id.lenghtText);
        TextView textControlPointCount = (TextView) convertView.findViewById(R.id.controlPointCountText);

        textName.setText(name);
        textLength.setText(String.valueOf(length) + "Km");
        textControlPointCount.setText(String.valueOf(controlPointCount));

        return convertView;
    }
}
