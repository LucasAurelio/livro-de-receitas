package com.projetoes.livrodereceitas;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecipeListViewAdapter extends  ArrayAdapter  {

    private List items;
    private Activity activity;

    public RecipeListViewAdapter(Activity activity, List items) {
        super(activity, android.R.layout.simple_list_item_1,items );

        this.items = items;
        this.activity = activity;
    }

    @Override
    public List getItem(int position) {
        return (List) items.get(position);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        List currItem = (List) items.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.recipe_list_item, null);
        }

        TextView recipeName = (TextView) convertView.findViewById(R.id.recipe_item_name);
        recipeName.setText(currItem.get(0).toString());



        return convertView;
    }


}