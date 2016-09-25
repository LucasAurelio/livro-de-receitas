package com.projetoes.livrodereceitas.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.projetoes.livrodereceitas.IngredientsRecipeViewAdapter;
import com.projetoes.livrodereceitas.MainActivity;
import com.projetoes.livrodereceitas.R;
import com.projetoes.livrodereceitas.Recipe;

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

        recipe = new Recipe((String) myRecipe.get(0));
        recipe = ((MainActivity) getActivity()).getCategoriesByRecipe(recipe);
        recipe.setDescription((String) myRecipe.get(2));
        recipe.setIngredients((ArrayList) myRecipe.get(1));

        TextView recipeName = (TextView) view.findViewById(R.id.view_recipe_name);
        recipeName.setText((String) myRecipe.get(0));

        TextView recipeInstructions = (TextView) view.findViewById(R.id.view_recipe_instruction);
        recipeInstructions.setText(recipe.getDescription());

        final ArrayList recipeIngredients = recipe.getIngredients();

        final ListView ingredientsListView = (ListView) view.findViewById(R.id.view_recipe_ingredients_list);

        ingredientsListView.setAdapter(new IngredientsRecipeViewAdapter(getActivity(), recipeIngredients));
        MainActivity.ListUtils.setDynamicHeight(ingredientsListView);

        initializeCategories(view, R.string.category_favorite, R.mipmap.ic_favorite_color,R.drawable.ic_favorite);
        initializeCategories(view, R.string.category_wannaDo, R.mipmap.ic_pot_color, R.drawable.pot_black);
        initializeCategories(view, R.string.category_done, R.mipmap.ic_chef_color, R.drawable.cooker_black);

        return view;
    }

    private void initializeCategories(final View view, final int stringId, final int colorTrue, final int colorFalse){
        ImageButton btn = (ImageButton) view.findViewById(R.id.favorite_recipe_btn);
        boolean initialStatus = false;

        if (stringId == R.string.category_favorite){
            btn = (ImageButton) view.findViewById(R.id.favorite_recipe_btn);

        } else if (stringId == R.string.category_wannaDo){
            btn = (ImageButton) view.findViewById(R.id.wannaDo_recipe_btn);

        } else if (stringId == R.string.category_done) {
            btn = (ImageButton) view.findViewById(R.id.done_recipe_btn);

        }

        setImgBtn(btn, initialStatus, colorTrue, colorFalse);

        try {
            final ImageButton finalBtn = btn;

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Only to check before change
                    Recipe categories = ((MainActivity) getActivity()).getCategoriesByRecipe(recipe);
                    Log.d(TAG, categories.toString());
                    //
                    boolean status = false;

                    if (stringId == R.string.category_favorite) {
                        recipe.setIsFavorite(!recipe.isFavorite());
                        status = recipe.isFavorite();

                    } else if (stringId == R.string.category_wannaDo) {
                        recipe.setIsWannaDo(!recipe.isWannaDo());
                        status = recipe.isWannaDo();

                    } else if (stringId == R.string.category_done) {
                        recipe.setIsDone(!recipe.isDone());
                        status = recipe.isDone();

                    }

                    setImgBtn(finalBtn, status, colorTrue, colorFalse);

                    ((MainActivity) getActivity()).categorizarReceita(stringId, recipe.getName(), status);

                    //only to check change
                    categories = ((MainActivity) getActivity()).getCategoriesByRecipe(recipe);
                    Log.d(TAG, categories.toString());

                }
            });

        } catch (Exception e){
            Log.e(TAG, String.valueOf(e));
        }

    }

    private void setImgBtn(ImageButton btn, Boolean status, int colorTrue, int colorFalse){
        if (status){
            btn.setImageResource(colorTrue);
        } else {
            btn.setImageResource(colorFalse);
        }
    }

}
