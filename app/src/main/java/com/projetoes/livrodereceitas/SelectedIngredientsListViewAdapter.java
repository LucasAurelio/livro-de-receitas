package com.projetoes.livrodereceitas;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import java.util.List;

public class SelectedIngredientsListViewAdapter extends  ArrayAdapter<String>  {

    private List<String> items;
    private Activity activity;

    public SelectedIngredientsListViewAdapter(Activity activity, List<String> items) {
        super(activity, android.R.layout.simple_list_item_1,items );

        this.items = items;
        this.activity = activity;
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount(){
        return items.size();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String currItem = items.get(position);

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.selected_list_item, null);
        }

        TextView listItem = (TextView) convertView.findViewById(R.id.selected_element);
        listItem.setText(currItem);


        return convertView;
    }

}