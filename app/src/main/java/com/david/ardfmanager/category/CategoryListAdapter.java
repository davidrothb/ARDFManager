package com.david.ardfmanager.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.david.ardfmanager.R;
import com.david.ardfmanager.category.Category;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by David on 17. 7. 2017.
 */

public class CategoryListAdapter extends ArrayAdapter<Category> {

    private Context mContext;
    int mResource;

    public CategoryListAdapter(Context context, int resource, ArrayList<Category> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name = getItem(position).getName();
        int minAge = getItem(position).getMinAge();
        int maxAge = getItem(position).getMaxAge();
        float length = getItem(position).getLength();
        int cpCount = getItem(position).getControlPointCount();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView textName = (TextView) convertView.findViewById(R.id.nameText);
        TextView textAges = (TextView) convertView.findViewById(R.id.agesText);
        TextView textLength = (TextView) convertView.findViewById(R.id.lengthText);
        TextView textCpCount = (TextView) convertView.findViewById(R.id.cpCountText);

        textName.setText(name);
        String ages = minAge + "/" + maxAge;
        textAges.setText(ages);
        textLength.setText(String.valueOf(length));
        textCpCount.setText(String.valueOf(cpCount));

        return convertView;
    }
}
