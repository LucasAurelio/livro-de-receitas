package com.projetoes.livrodereceitas.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.projetoes.livrodereceitas.MainActivity;
import com.projetoes.livrodereceitas.R;
import com.projetoes.livrodereceitas.RecipeListViewAdapter;

import java.util.ArrayList;

public class ListRecipeCategoryFragment extends Fragment {
    private static ListRecipeCategoryFragment fragment;
    public static final String TAG = "LIST_RECIPES_FRAGMENT";

    public ListRecipeCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    public static ListRecipeCategoryFragment getInstance() {
        if (fragment == null) {
            fragment = new ListRecipeCategoryFragment();
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
        final View view = inflater.inflate(R.layout.fragment_list_recipe_category, container, false);

        final ArrayList<String> recipeList = ((MainActivity) getActivity()).getRecipeListCategory();

        final ListView recipesListView = (ListView) view.findViewById(R.id.recipes_result_category);
        TextView result = (TextView) view.findViewById(R.id.result_category);


        if (recipeList.isEmpty()){
            result.setText("Ops, não encontramos nenhuma receita nessa categoria :(");
        } else if (((MainActivity) getActivity()).getCategory() == R.string.wannaDo){
            result.setText("Quero fazer:");
        } else if (((MainActivity) getActivity()).getCategory() == R.string.done){
            result.setText("Já Fiz:");
        } else if(((MainActivity) getActivity()).getCategory() == R.string.favorite){
            result.setText("Favoritas:");
        } else {
            result.setText("Receitas:");
        }

        recipesListView.setAdapter(new RecipeListViewAdapter(getActivity(), recipeList));
        MainActivity.ListUtils.setDynamicHeight(recipesListView);

        return view;
    }


}
