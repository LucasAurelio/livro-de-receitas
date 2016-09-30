package com.projetoes.livrodereceitas;

import android.database.Cursor;
import android.database.SQLException;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.projetoes.livrodereceitas.fragments.HelpFragment;
import com.projetoes.livrodereceitas.fragments.InitialFragment;
import com.projetoes.livrodereceitas.fragments.ListRecipeCategoryFragment;
import com.projetoes.livrodereceitas.fragments.RecipeBookFragment;
import com.projetoes.livrodereceitas.fragments.SearchFragment;
import com.projetoes.livrodereceitas.fragments.ListRecipesFragment;
import com.projetoes.livrodereceitas.fragments.TypeSearchFragment;
import com.projetoes.livrodereceitas.fragments.ViewRecipeFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;

    private InitialFragment initialFragment;
    private SearchFragment searchFragment;
    private ListRecipesFragment listRecipesFragment;
    private ViewRecipeFragment viewRecipeFragment;
    private RecipeBookFragment recipeBookFragment;
    private HelpFragment helpFragment;
    private ListRecipeCategoryFragment listRecipeCategoryFragment;
    private TypeSearchFragment typeSearchFragment;

    private ArrayList<String> resultRecipeList;
    private ArrayList<String> resultRecipeListSimilar;
    private ArrayList viewRecipe;

    public static final String TAG = "MAIN_ACTIVITY";
    private static final String SEARCH_COMPATIBILIDADE = "COMPATIBILIDADE";

    private String typeSearch;


    private static DatabaseHelper ourDB;
    private ArrayList listCategory;
    private int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialFragment = InitialFragment.getInstance();
        searchFragment = SearchFragment.getInstance();
        listRecipesFragment = ListRecipesFragment.getInstance();
        viewRecipeFragment = ViewRecipeFragment.getInstance();
        recipeBookFragment = RecipeBookFragment.getInstance();
        helpFragment = HelpFragment.getInstance();
        listRecipeCategoryFragment = ListRecipeCategoryFragment.getInstance();
        typeSearchFragment = TypeSearchFragment.getInstance();

        // Bottom bar navigation menu
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.noTopOffset();
        mBottomBar.noNavBarGoodness();
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setDefaultTabPosition(0);
        initializeBottomNavigation();

        resultRecipeList = new ArrayList<String>();
        resultRecipeListSimilar = new ArrayList<String>();
        typeSearch = "";

        changeFragment(initialFragment, InitialFragment.TAG, true);



        ourDB = new DatabaseHelper(this);
        try{
            ourDB.createDatabase();
        }catch (IOException e){
            throw new Error("Unable to create Database");
        }
        try{
            ourDB.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    public void onSelectButtonPressed(View view) {
        changeFragment(searchFragment, InitialFragment.TAG, true);


        changeBottomBarItem(SearchFragment.TAG);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,
                searchFragment, SearchFragment.TAG).addToBackStack(SearchFragment.TAG).commit();

        Toast.makeText(getBaseContext(), "I choose you!", Toast.LENGTH_SHORT).show();
    }


    /**
     * Navigate between fragments when clicking the bottom bar navigation
     */
    private void initializeBottomNavigation() {

        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            // The user selected any item.
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case (R.id.homeItem):
                        changeFragment(initialFragment, InitialFragment.TAG, true);
                    case (R.id.searchItem):
                        changeFragment(searchFragment, SearchFragment.TAG, true);
                        break;
                    case (R.id.favoriteItem):
                        changeFragment(recipeBookFragment, RecipeBookFragment.TAG, true);
                        break;
                    case (R.id.helpItem):
                        changeFragment(helpFragment, HelpFragment.TAG, true);
                        break;
                    default:
                        break;
                }

            }

            // The user reselected any item, maybe scroll your content to top.
            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case (R.id.homeItem):
                        changeFragment(initialFragment, InitialFragment.TAG, false);
                        break;
                    case (R.id.searchItem):
                        changeFragment(searchFragment, SearchFragment.TAG, false);
                        break;
                    case (R.id.favoriteItem):
                        changeFragment(recipeBookFragment, RecipeBookFragment.TAG, false);
                        break;
                    case (R.id.helpItem):
                        changeFragment(helpFragment, HelpFragment.TAG, false);
                        break;
                    default:
                        break;
                }
            }
        });

    }


    private void changeBottomBarItem(String currentFragment) {
        switch (currentFragment) {
            case InitialFragment.TAG:
                mBottomBar.selectTabAtPosition(0, true);
                break;
            case SearchFragment.TAG:
                mBottomBar.selectTabAtPosition(1, true);
                break;
        }
    }



    /**
     * Change the current displayed fragment by a new one.
     * - if the fragment is in backstack, it will pop it
     * - if the fragment is already displayed (trying to change the fragment with the same), it will not do anything
     *
     * @param frag            the new fragment to display
     * @param saveInBackstack if we want the fragment to be in backstack
     */

    private void changeFragment(Fragment frag, String tag, boolean saveInBackstack) {


        try {
            FragmentManager manager = getSupportFragmentManager();
           //fragment not in back stack, create it.
            FragmentTransaction transaction = manager.beginTransaction();


            transaction.replace(R.id.content_layout, frag, tag);

            if (saveInBackstack) {
                Log.d(TAG, "Change Fragment: addToBackTack " + tag);
                transaction.addToBackStack(tag);
            } else {
                Log.d(TAG, "Change Fragment: NO addToBackTack");
            }
            transaction.commit();
            // custom effect if fragment is already instanciated

        } catch (IllegalStateException exception) {
            Log.w(TAG, "Unable to commit fragment, could be activity as been killed in background. " + exception.toString());
        }
    }


    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
            return;
        }

        super.onBackPressed();
    }

    //Popular opcoes de busca
    public ArrayList populateCompleteText(){
        Cursor alimentos = ourDB.getAlimentos();
        ArrayList<String> allAlimentos = new ArrayList<>();
        alimentos.moveToFirst();
        while(!alimentos.isAfterLast()){
            allAlimentos.add(alimentos.getString(0));
            alimentos.moveToNext();
        }
        return allAlimentos;
    }
    public ArrayList populateFilterList() {
        Cursor categorias = ourDB.getTipos();
        ArrayList<String> allCategorias = new ArrayList<>();
        categorias.moveToFirst();
        while(!categorias.isAfterLast()){
            allCategorias.add(categorias.getString(0));
            categorias.moveToNext();
        }
        return allCategorias;
    }

    // View resultados de busca
    public ArrayList viewReceitasCompativeis(ArrayList<String> listaIngredientes, ArrayList<String> listaFiltros){
        Cursor receitasCompativeis = ourDB.getReceitasPorCompatibilidade(listaIngredientes, listaFiltros);
        //excluir linha abaixo
        ArrayList<String> allReceitasCompativeis = new ArrayList<>();
        //descomentar linha abaixo
        //ArrayList<String[]> allReceitasCompativeis = new ArrayList<>();

        receitasCompativeis.moveToFirst();
        while(!receitasCompativeis.isAfterLast()){

            //descomentar codigo abaixo
            //String[] receitaEnumber = new String[2];
                    //nome da receita
            //receitaEnumber[0] = receitasCompativeis.getString(0);
                    //quantidade em forma da string 'n/j'
            //receitaEnumber[1] = listaIngredientes.size()+"/"+receitasCompativeis.getString(1);

            //excluir linha abaixo
            allReceitasCompativeis.add(receitasCompativeis.getString(0));

            //descomentar linha abaixo
            //allReceitasCompativeis.add(receitaEnumber);

            //manter linha abaixo
            receitasCompativeis.moveToNext();
        }

        resultRecipeList = allReceitasCompativeis;
        receitasCompativeis.close();
        return allReceitasCompativeis;
    }

    public ArrayList viewReceitasSimilares(ArrayList<String> listaIngredientes, ArrayList<String> listaFiltros){
        Cursor receitasSimilares = ourDB.getReceitasPorSimilaridade(listaIngredientes, listaFiltros);
        //excluir linha abaixo
        ArrayList<String> allReceitasSimilares = new ArrayList<>();
        //descomentar linha abaixo
        //ArrayList<String[]> allReceitasSimilares = new ArrayList<>();

        receitasSimilares.moveToFirst();
        while(!receitasSimilares.isAfterLast()){

            //descomentar código abaixo
            //String receitaEnumber = new String[2];
                    //nome da receita
            //receitaEnumber[0] = receitasSimilares.getString(0);
                    //quantidade de ingredientes presentes na receita
            //receitaEnumber[1] = receitasSimilares.getString(listaIngredientes.size()+2) +"/"+ receitasSimilares.getString(1);

            //excluir linha abaixo
            allReceitasSimilares.add(receitasSimilares.getString(0));

            //descomentar linha abaixo
            //allReceitasSimilares.add(receitaEnumber);

            //manter linha abaixo
            receitasSimilares.moveToNext();
        }

        resultRecipeListSimilar = allReceitasSimilares;
        receitasSimilares.close();
        return allReceitasSimilares;
    }

    public ArrayList viewReceitaSelecionada(String nomeSelecionado){
        Cursor receitaSelecionada = ourDB.getReceitaSelecionada(nomeSelecionado);
        ArrayList aReceita = new ArrayList<>();
        receitaSelecionada.moveToFirst();
        String descricao = null;

        aReceita.add(receitaSelecionada.getString(0));
        ArrayList<String[]> ingredientes = new ArrayList<String[]>();
        while(!receitaSelecionada.isAfterLast()){
            String[] ingrediente = new String[2];
            ingrediente[0] = receitaSelecionada.getString(1);
            ingrediente[1] = receitaSelecionada.getString(2);
            ingredientes.add(ingrediente);
            descricao = receitaSelecionada.getString(3);
            receitaSelecionada.moveToNext();
        }
        aReceita.add(ingredientes);
        aReceita.add(descricao);

        viewRecipe = aReceita;

        receitaSelecionada.close();
        return aReceita;
    }

    public void setTypeSearch(String type){
        typeSearch = type;
    }

    public String getTypeSearch(){
        return typeSearch;
    }

    //view categorias
    public ArrayList viewReceitasFavoritas(){
        Cursor favoritas = ourDB.getReceitasFavoritas();
        ArrayList<String> fvrts = new ArrayList<>();
        favoritas.moveToFirst();

        while(!favoritas.isAfterLast()){
            fvrts.add(favoritas.getString(0));
            favoritas.moveToNext();
        }

        favoritas.close();
        return fvrts;
    }
    public ArrayList viewReceitasJaFiz(){
        Cursor jaFiz = ourDB.getReceitasJaFiz();
        ArrayList<String> fiz = new ArrayList<>();
        jaFiz.moveToFirst();

        while(!jaFiz.isAfterLast()){
            fiz.add(jaFiz.getString(0));
            jaFiz.moveToNext();
        }

        jaFiz.close();
        return fiz;
    }
    public ArrayList viewReceitasQueroFazer(){
        Cursor queroFazer = ourDB.getReceitasQueroFazer();
        ArrayList<String> quero = new ArrayList<>();
        queroFazer.moveToFirst();

        while(!queroFazer.isAfterLast()){
            quero.add(queroFazer.getString(0));
            queroFazer.moveToNext();
        }

        queroFazer.close();
        return quero;
    }


    public void categorizarReceita(int catgSelecionada,String recipe, boolean status){
        byte byteStatus = 0;
        if (status){
            byteStatus = 1;
        }
        ourDB.setReceitaCategoria(catgSelecionada, recipe, byteStatus);
    }
    public ArrayList<String> getCategoriasReceitas(){
        return ourDB.getCategorias();
    }



    private ArrayList getCategoriasPorReceita(String receitaSelecionada){
        Cursor categorias = ourDB.receitaCategorias(receitaSelecionada);
        ArrayList<String> receitaCategorias = new ArrayList<>();

        if (!(categorias.getCount()==0)){
            categorias.moveToFirst();
            if ((categorias.getString(0).equals("1"))){
                receitaCategorias.add("Quero fazer");
            }
            if ((categorias.getString(1).equals("1"))){
                receitaCategorias.add("Já fiz");
            }
            if ((categorias.getString(2).equals("1"))){
                receitaCategorias.add("Favorita");
            }
        }

        categorias.close();
        return receitaCategorias;
    }

    public Recipe getCategoriesByRecipe(Recipe recipe){
        ArrayList categories = getCategoriasPorReceita(recipe.getName());

        if (categories.contains("Quero fazer")){
            recipe.setIsWannaDo(true);
        } if (categories.contains("Já fiz")){
            recipe.setIsDone(true);
        } if (categories.contains("Favorita")){
            recipe.setIsFavorite(true);
        }
        return recipe;
    }
    public ArrayList<String> getResultRecipeList(){

        if(typeSearch.equals(SEARCH_COMPATIBILIDADE)){
            return resultRecipeList;
        }
        else{
            return resultRecipeListSimilar;
        }
    }
    public ArrayList getViewRecipe(){
        return viewRecipe;
    }
    public ArrayList<String> getRecipeListCategory(){
        return listCategory;
    }

    public int getCategory(){
        return category;
    }


    public void onSearchButtonPressed(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,
                typeSearchFragment, TypeSearchFragment.TAG).addToBackStack(TypeSearchFragment.TAG).commit();

    }

    public void onTypeButtonPressed(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,
                listRecipesFragment, ListRecipesFragment.TAG).addToBackStack(ListRecipesFragment.TAG).commit();

    }

    public void onRecipePressed(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,
                viewRecipeFragment, ViewRecipeFragment.TAG).addToBackStack(ViewRecipeFragment.TAG).commit();

    }

    public void onFavoritesPressed(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,
                listRecipeCategoryFragment, ViewRecipeFragment.TAG).addToBackStack(ListRecipeCategoryFragment.TAG).commit();
        listCategory = viewReceitasFavoritas();
        category = R.string.favorite;
    }

    public void onWannaDoPressed(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,
                listRecipeCategoryFragment, ViewRecipeFragment.TAG).addToBackStack(ListRecipeCategoryFragment.TAG).commit();
        listCategory = viewReceitasQueroFazer();
        category = R.string.wannaDo;

    }

    public void onDonePressed(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,
                listRecipeCategoryFragment, ViewRecipeFragment.TAG).addToBackStack(ListRecipeCategoryFragment.TAG).commit();
        listCategory = viewReceitasJaFiz();
        category = R.string.done;

    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter listAdapter = mListView.getAdapter();
            if (listAdapter != null) {

                int numberOfItems = listAdapter.getCount();

                // Get total height of all items.
                int totalItemsHeight = 0;
                for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                    View item = listAdapter.getView(itemPos, null, mListView);
                    float px = 500 * (mListView.getResources().getDisplayMetrics().density);
                    item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    totalItemsHeight += item.getMeasuredHeight();
                }

                // Get total height of all item dividers.
                int totalDividersHeight = mListView.getDividerHeight() *
                        (numberOfItems - 1);
                // Get padding
                int totalPadding = mListView.getPaddingTop() + mListView.getPaddingBottom();

                // Set list height.
                ViewGroup.LayoutParams params = mListView.getLayoutParams();
                params.height = totalItemsHeight + totalDividersHeight + totalPadding;
                mListView.setLayoutParams(params);
                mListView.requestLayout();
            }
        }
    }

}
