package com.projetoes.livrodereceitas;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class CheckboxListViewAdapter extends ArrayAdapter<String> {

    private final List<String> items, selectedItems;
    private final Activity activity;


    public CheckboxListViewAdapter(Activity activity, List<String> items, List<String> selectedItems) {
        super(activity, android.R.layout.simple_list_item_1,items );
        this.items = items;
        this.selectedItems = selectedItems;
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

        View v = convertView;
        final String currFilter = items.get(position);

        if (v == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            v = inflater.inflate(R.layout.filter_list_item, null);
        }

        final CheckBox checkboxItem = (CheckBox) v.findViewById(R.id.checkbox);
        final TextView checkboxName = (TextView) v.findViewById(R.id.checkbox_name);
        checkboxName.setText(currFilter);
        checkboxItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!checkboxItem.isChecked() && selectedItems.contains(checkboxName.getText().toString())){
                    selectedItems.remove(checkboxName.getText().toString());
                }
                else if(checkboxItem.isChecked() && !selectedItems.contains(checkboxName.getText().toString())) {
                    selectedItems.add(checkboxName.getText().toString());
                }

            }
        });


        return v;
    }

}