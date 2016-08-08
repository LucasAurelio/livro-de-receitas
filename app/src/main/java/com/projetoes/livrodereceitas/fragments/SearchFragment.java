package com.projetoes.livrodereceitas.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

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

        List listFilter = new ArrayList();
        listFilter.add("Carnes");
        listFilter.add("Massas");
        listFilter.add("Sobremesas");

        ListView checkboxListView = (ListView) view.findViewById(R.id.filter_list);
        checkboxListView.setAdapter(new CheckboxListViewAdapter(getActivity(),listFilter));

        return view;

    }

}
