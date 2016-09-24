package com.projetoes.livrodereceitas.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.projetoes.livrodereceitas.IngredientsRecipeViewAdapter;
import com.projetoes.livrodereceitas.MainActivity;
import com.projetoes.livrodereceitas.R;
import com.projetoes.livrodereceitas.Recipe;
import com.projetoes.livrodereceitas.RecipeListViewAdapter;

import java.util.ArrayList;

public class ViewRecipeFragment extends Fragment {

    private static ViewRecipeFragment fragment;
    public static final String TAG = "VIEW_RECIPE_FRAGMENT";
    private Recipe recipe;

    public ViewRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    public static ViewRecipeFragment getInstance() {
        if (fragment == null) {
            fragment = new ViewRecipeFragment();
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
        final View view = inflater.inflate(R.layout.fragment_view_recipe, container, false);
        final ArrayList myRecipe = ((MainActivity) getActivity()).getViewRecipe();

        recipe = ((MainActivity) getActivity()).getCategoriesByRecipe((String) myRecipe.get(0));
        recipe.setDescription((String) myRecipe.get(2));
        recipe.setIngredients((ArrayList) myRecipe.get(1));

        TextView recipeName = (TextView) view.findViewById(R.id.view_recipe_name);
        recipeName.setText((String) myRecipe.get(0));

        TextView recipeInstructions = (TextView) view.findViewById(R.id.view_recipe_instruction);
        recipeInstructions.setText(recipe.getDescription());

        final ArrayList recipeIngredients = recipe.getIngredients();

        final ListView ingredientsListView = (ListView) view.findViewById(R.id.view_recipe_ingredients_list);

        ingredientsListView.setAdapter(new IngredientsRecipeViewAdapter(getActivity(), recipeIngredients));
        ListUtils.setDynamicHeight(ingredientsListView);


        ImageButton favoriteBtn = (ImageButton) view.findViewById(R.id.favorite_btn);

        /*if (!isFavorite){
            favoriteBtn.setBackgroundColor(Color.GRAY);
        } else {
            favoriteBtn.setBackgroundColor(Color.RED);
        }

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite = !isFavorite;
                Log.d(TAG, "É favorita?: " + isFavorite);
                if (!isFavorite){
                    v.setBackgroundColor(Color.GRAY);
                } else {
                    v.setBackgroundColor(Color.RED);
                }

            }
        }); */


        ImageButton madeBtn = (ImageButton) view.findViewById(R.id.made_btn);

        ImageButton wantBtn = (ImageButton) view.findViewById(R.id.want_btn);

        return view;
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount()));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}
