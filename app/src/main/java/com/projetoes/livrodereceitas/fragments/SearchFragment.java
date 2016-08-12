package com.projetoes.livrodereceitas.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.projetoes.livrodereceitas.CheckboxListViewAdapter;
import com.projetoes.livrodereceitas.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#getInstance} factory method to
 * create or get a instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private static SearchFragment fragment;
    public static final String TAG = "SEARCH_FRAGMENT";


    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment getInstance() {
        if (fragment == null ){
            fragment = new SearchFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Selecionar ingredientes
        //dados para teste
        List ingredientList = new ArrayList();
        ingredientList.add("Farinha");
        ingredientList.add("Arroz");
        ingredientList.add("Chocolate");
        ingredientList.add("Milho");
        ingredientList.add("Ovo");
        ingredientList.add("Feijão");
        ingredientList.add("Filé");
        ingredientList.add("Castanha");


        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (getContext(),android.R.layout.select_dialog_item,ingredientList);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView actv= (AutoCompleteTextView)view.findViewById(R.id.auto_complete_ingredient);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView


        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // recupera o valor do text
                Object item = parent.getItemAtPosition(position).getClass();
                Toast.makeText(getContext(), (CharSequence) parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
            }
        });


        // Selecionar filtro
        //Dados para teste
        List filterList = new ArrayList();
        filterList.add("Carnes");
        filterList.add("Massas");
        filterList.add("Sobremesas");

        ListView checkboxListView = (ListView) view.findViewById(R.id.filter_list);
        checkboxListView.setAdapter(new CheckboxListViewAdapter(getActivity(),filterList));

        return view;

    }

}
