package com.david.ardfmanager.competitors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.david.ardfmanager.R;
import com.david.ardfmanager.competitors.Competitor;

import java.util.ArrayList;

/**
 * Created by David on 17. 7. 2017.
 */

public class CompetitorsListAdapter extends ArrayAdapter<Competitor> {

    private Context mContext;
    int mResource;

    public CompetitorsListAdapter(Context context, int resource, ArrayList<Competitor> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();

        Competitor o = new Competitor(name);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView text = (TextView) convertView.findViewById(R.id.text_name);
        text.setText(name);

        return convertView;
    }
}
