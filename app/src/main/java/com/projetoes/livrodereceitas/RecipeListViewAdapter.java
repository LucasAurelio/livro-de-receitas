package com.projetoes.livrodereceitas;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecipeListViewAdapter extends  ArrayAdapter  {

    private List<String> items;
    private Activity activity;

    public RecipeListViewAdapter(Activity activity, List<String> items) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        String currItem = items.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.recipe_list_item, null);
        }

        TextView recipeName = (TextView) convertView.findViewById(R.id.recipe_item_name);
        TextView recipeDiff = (TextView) convertView.findViewById(R.id.recipe_item_difficulty);
        TextView recipeServe = (TextView) convertView.findViewById(R.id.recipe_item_serve);
        TextView recipeTime = (TextView) convertView.findViewById(R.id.recipe_item_time);

        recipeName.setText(currItem);



        recipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), ((MainActivity) activity).viewReceitaSelecionada(currItem).toString(), Toast.LENGTH_SHORT).show();

            }
        });



        return convertView;
    }


}