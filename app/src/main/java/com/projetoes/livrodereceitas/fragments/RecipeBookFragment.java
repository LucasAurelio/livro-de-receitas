package com.projetoes.livrodereceitas.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projetoes.livrodereceitas.R;

import java.util.ArrayList;

public class RecipeBookFragment extends Fragment {

    private static RecipeBookFragment fragment;
    public static final String TAG = "RECIPE_BOOK_FRAGMENT";
    private boolean isFavorite = true;
    private boolean isDone = false;
    private boolean isWant = true;

    public RecipeBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    public static RecipeBookFragment getInstance() {
        if (fragment == null) {
            fragment = new RecipeBookFragment();
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
        final View view = inflater.inflate(R.layout.fragment_recipe_book, container, false);

        return view;
    }
    
}
