package com.projetoes.livrodereceitas;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class IngredientsRecipeViewAdapter extends  ArrayAdapter  {

    private List<String[]> items;
    private Activity activity;

    public IngredientsRecipeViewAdapter(Activity activity, List items) {
        super(activity, android.R.layout.simple_list_item_1,items );

        this.items = items;
        this.activity = activity;
    }

    @Override
    public String[] getItem(int position) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final String[] currItem = items.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ingredient_recipe_item, null);
        }

        TextView ingName = (TextView) convertView.findViewById(R.id.name_ing);
        TextView ingQuantity = (TextView) convertView.findViewById(R.id.quantity_ing);



        ingName.setText(currItem[1]);
        ingQuantity.setText(currItem[0]);




        return convertView;
    }


}