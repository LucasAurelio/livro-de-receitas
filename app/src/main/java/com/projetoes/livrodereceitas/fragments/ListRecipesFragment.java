package com.projetoes.livrodereceitas.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.projetoes.livrodereceitas.R;

public class ListRecipesFragment extends Fragment {

    private static ListRecipesFragment fragment;
    public static final String TAG = "LIST_RECIPES_FRAGMENT";

    public ListRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    public static ListRecipesFragment getInstance() {
        if (fragment == null) {
            fragment = new ListRecipesFragment();
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
        final View view = inflater.inflate(R.layout.fragment_list_recipes, container, false);





        return view;
    }
}
