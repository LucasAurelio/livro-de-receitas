package com.projetoes.livrodereceitas;


import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CheckboxListViewAdapter extends ArrayAdapter<String> {

    private final List<String> items;
    private final Activity activity;

    public CheckboxListViewAdapter(Activity activity, List<String> items) {
        super(activity, android.R.layout.simple_list_item_1,items );
        this.items = items;
        this.activity = activity;
    }


    @Override
    public String getItem(int position) {
        return items.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        String currFilter = items.get(position);


        if (v == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            v = inflater.inflate(R.layout.filter_list_item, null);
        }

        TextView filterItemName = (TextView) v.findViewById(R.id.filter_item_name);
        filterItemName.setText(currFilter);

        return v;
    }

}