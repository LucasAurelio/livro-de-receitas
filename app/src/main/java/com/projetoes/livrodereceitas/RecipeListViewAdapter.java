package com.projetoes.livrodereceitas;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.StringTokenizer;

public class RecipeListViewAdapter extends  ArrayAdapter  {

    private List<String[]> items;
    private Activity activity;

    public RecipeListViewAdapter(Activity activity, List<String[]> items) {
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

        final String currItem = items.get(position)[0];
        final String currNumber = items.get(position)[1];
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.recipe_list_item, null);
        }

        TextView recipeName = (TextView) convertView.findViewById(R.id.recipe_item_name);
        TextView numberIngr = (TextView) convertView.findViewById(R.id.have);

        recipeName.setText(currItem);
        numberIngr.setText(currNumber);

        recipeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) activity).viewReceitaSelecionada(getContext(),currItem);
                ((MainActivity) activity).onRecipePressed(view);
            }
        });



        return convertView;
    }


}