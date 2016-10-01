package com.projetoes.livrodereceitas.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.projetoes.livrodereceitas.R;
import com.projetoes.livrodereceitas.Recipe;
import com.projetoes.livrodereceitas.RecipeSearch;

import java.util.ArrayList;

public class DBUtils {

    public static SQLiteDatabase getReadableDatabase(Context context) {
        DatabaseHelper openHelper = new DatabaseHelper(context);
        return openHelper.getReadableDatabase();
    }

    public static SQLiteDatabase getWritableDatabase(Context context) {
        DatabaseHelper openHelper = new DatabaseHelper(context);
        return openHelper.getWritableDatabase();
    }


    public static ArrayList getAlimentos(Context context) {
        SQLiteDatabase db = getReadableDatabase(context);

        ArrayList<String> allAlimentos = new ArrayList<>();

        String query = "SELECT nome FROM alimento";

        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            allAlimentos.add(c.getString(0));
            c.moveToNext();
        }

        c.close();
        db.close();

        return allAlimentos;
    }

    public static ArrayList getTipos(Context context) {

        SQLiteDatabase db = getReadableDatabase(context);

        ArrayList<String> allCategorias = new ArrayList<>();

        String query = "Select DISTINCT tipo FROM receita";

        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            allCategorias.add(c.getString(0));
            c.moveToNext();
        }

        c.close();
        db.close();

        return allCategorias;

    }

    public static ArrayList getReceitasPorCompatibilidade(Context context, ArrayList<String> listaIngredientes, ArrayList<String> listaFiltros) {

        SQLiteDatabase db = getReadableDatabase(context);

        ArrayList<String[]> allReceitasCompativeis = new ArrayList<>();


        String allSelections = "";
        for(int i=0;i<listaIngredientes.size();i++){
            allSelections += ", sum(ss"+(i+1)+") as pp"+(i+1);
        }

        String allCases = "";
        for(int i=0;i<listaIngredientes.size();i++){
            allCases += ", CASE WHEN g.nome LIKE '%" + listaIngredientes.get(i) +"%' THEN 1 ELSE 0 END AS ss"+(i+1);
        }

        String allFiltros = "'" + listaFiltros.get(0) +"'";
        for(int i=1;i<listaFiltros.size();i++){
            allFiltros += " OR p.tipo = '" + listaFiltros.get(i) +"'";
        }

        String allHavings = "pp1>0";
        for(int i=1;i<listaIngredientes.size();i++){
            allHavings += " AND pp"+(i+1) +">0";
        }

        String query = "SELECT nome, count(ingr)" + allSelections+" "+
                "FROM(SELECT p.nome as nome, g.nome as ingr" + allCases +" "+
                "FROM receita p, ingrediente g, receita_ingredientes f "+
                "WHERE p._id = f.id_receita " +
                "AND g._id = f.id_ingrediente " +
                "AND (p.tipo = " + allFiltros + ")) " +
                "GROUP BY nome " +
                "HAVING "+allHavings+" ";

        Cursor c = db.rawQuery(query, null);


        if (listaFiltros.isEmpty()) {
            listaFiltros.add("");
        }
        c.moveToFirst();
        while (!c.isAfterLast()) {

            Recipe recipe = new Recipe(c.getString(c.getColumnIndex("nome")));
            RecipeSearch recipeSearch = new RecipeSearch(recipe, listaIngredientes.size(), Integer.parseInt(c.getString(1)));

            //String[] receitaEnumber = new String[2];
            //nome da receita
            //receitaEnumber[0] = receitasCompativeis.getString(0);
            //quantidade em forma da string 'n/j'
            //receitaEnumber[1] = listaIngredientes.size()+"/"+receitasCompativeis.getString(1);
            allReceitasCompativeis.add(new String[]{recipe.getName(), recipeSearch.ingredientsToString()});


            c.moveToNext();
        }

        c.close();
        db.close();

        return allReceitasCompativeis;

    }


    //AJUSTAR PARA FICAR IGUAL AO QUE TA NO MASTER
    public static ArrayList<String[]> getReceitasPorSimilaridade(Context context, ArrayList<String> listaIngredientes, ArrayList<String> listaFiltros) {

        SQLiteDatabase db = getReadableDatabase(context);

        ArrayList<String[]> allReceitasSimilares = new ArrayList<>();

        String allSelections = "";
        for(int i=0;i<listaIngredientes.size();i++){
            allSelections += ", sum(ss"+(i+1)+") as pp"+(i+1);
        }

        String allSums = ",(sum(ss1) ";
        for(int i=1;i<listaIngredientes.size();i++){
            allSums += "+ sum(ss"+(i+1)+")";
        }
        allSums = allSums + ")";


        String allCases = "";
        for(int i=0;i<listaIngredientes.size();i++){
            allCases += ", CASE WHEN g.nome LIKE '%" + listaIngredientes.get(i) +"%' THEN 1 ELSE 0 END AS ss"+(i+1);
        }

        String allFiltros = "'" + listaFiltros.get(0) +"'";
        for(int i=1;i<listaFiltros.size();i++){
            allFiltros += " OR p.tipo = '" + listaFiltros.get(i) +"'";
        }

        String allHavings = "(pp1";
        for(int i=1;i<listaIngredientes.size();i++){
            allHavings += "+ pp"+(i+1);
        }
        allHavings = allHavings +") <"+listaIngredientes.size();


        String query = "SELECT nome, count(ingr)" + allSelections+allSums+" "+
                "FROM(SELECT p.nome as nome, g.nome as ingr" + allCases +" "+
                "FROM receita p, ingrediente g, receita_ingredientes f "+
                "WHERE p._id = f.id_receita " +
                "AND g._id = f.id_ingrediente " +
                "AND (p.tipo = " + allFiltros + ")) " +
                "GROUP BY nome " +
                "HAVING "+allHavings+"<" + listaIngredientes.size() + " AND " + allHavings + ">0";

        Cursor cursor = db.rawQuery(query, null);


        if (listaFiltros.isEmpty()) {
            listaFiltros.add("");
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Recipe recipe = new Recipe(cursor.getString(cursor.getColumnIndex("nome")));
            RecipeSearch recipeSearch = new RecipeSearch(recipe, listaIngredientes.size()+2, Integer.parseInt(cursor.getString(1)));

            //String [] receitaEnumber = new String[2];
            //nome da receita
            //receitaEnumber[0] = recipe.getName();
            //quantidade de ingredientes presentes na receita
            //receitaEnumber[1] = receitasSimilares.getString(listaIngredientes.size()+2) +"/"+ receitasSimilares.getString(1);

            allReceitasSimilares.add(new String[]{recipe.getName(), recipeSearch.ingredientsToString()});

            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return allReceitasSimilares;
    }


    // QUERY VIEW RECEITA
    public static ArrayList getReceitaSelecionada(Context context, String nomeDaReceita) {

        SQLiteDatabase db = getReadableDatabase(context);

        ArrayList aReceita = new ArrayList<>();

        String query = "SELECT p.nome, f.quantidade, g.nome, p.descricao " +
                "FROM receita p, ingrediente g, receita_ingredientes f " +
                "WHERE p.nome =  '" + nomeDaReceita +
                "' AND p._id = f.id_receita " +
                "AND g._id = f.id_ingrediente";

        Cursor cursor = db.rawQuery(query, null);


        cursor.moveToFirst();

        Recipe recipe = new Recipe(cursor.getString(0));

        while (!cursor.isAfterLast()) {
            recipe.addIngredient(new String[]{cursor.getString(1), cursor.getString(2)});
            recipe.setDescription(cursor.getString(3));
            cursor.moveToNext();
        }

        aReceita.add(recipe.getName());
        aReceita.add(recipe.getIngredients());
        aReceita.add(recipe.getDescription());


        //ArrayList: sendo o primeiro elemento o título da receita,
        //todos os seguintes na sequencia -> quantidade do ingrediente e logo após o ingrediente
        //e o último elemento é o modo de fazer.

        cursor.close();
        db.close();

        return aReceita;
    }


    public static ArrayList receitaCategorias(Context context, String receitaSelecionada) {

        SQLiteDatabase db = getReadableDatabase(context);

        ArrayList receitaCategorias = new ArrayList<>();

        String query = "SELECT quero_fazer, ja_fiz, favoritas " +
                "FROM receita_categorias " +
                "WHERE nome_receita ='"+receitaSelecionada+"'";

        Cursor cursor = db.rawQuery( query, null);


        if (!(cursor.getCount()==0)){
            cursor.moveToFirst();
            if ((cursor.getString(0) == "1")){
                receitaCategorias.add("Quero fazer");
            }
            if ((cursor.getString(1) == "1")){
                receitaCategorias.add("Já fiz");
            }
            if ((cursor.getString(2) == "1")){
                receitaCategorias.add("Favorita");
            }
        }

        cursor.close();
        return receitaCategorias;
    }



    public static void setReceitaCategoria(Context context, int catgr, String nomeDaReceita, byte status){
        if (catgr == (R.string.category_wannaDo)){
            setReceitaQueroFazer(context, nomeDaReceita,status);
        }else if(catgr == (R.string.category_done)){
            setReceitaJaFiz(context, nomeDaReceita,status);
        }else if(catgr == (R.string.category_favorite)){
            setReceitaFavoritas(context, nomeDaReceita, status);
        }
    }

    public static void setReceitaFavoritas(Context context, String receitaSelecionada, byte status){

        SQLiteDatabase db = getWritableDatabase(context);

        ContentValues values = new ContentValues();

        values.put("nome_receita", receitaSelecionada);
        values.put("favoritas", status);
        try{
            db.update("receita_categorias",values,"nome_receita = "+receitaSelecionada,null);
        }catch (SQLException e){
            values.put("quero_fazer",0);
            values.put("ja_fiz",0);

            db.insert("receita_categorias", null, values);
        }
        checkForReceitaSemCategoria(context);
    }



    public static void setReceitaQueroFazer(Context context, String receitaSelecionada, byte status){
        SQLiteDatabase db = getWritableDatabase(context);

        ContentValues values = new ContentValues();

        values.put("nome_receita", receitaSelecionada);
        values.put("quero_fazer", status);

        if(status == 1){
            values.put("ja_fiz",0);
        }

        try{
            db.update("receita_categorias",values,"nome_receita = "+receitaSelecionada,null);
        }catch (SQLException e){

            values.put("favoritas",0);
            db.insert("receita_categorias", null, values);
        }
        checkForReceitaSemCategoria(context);
    }

    public static void setReceitaJaFiz(Context context, String receitaSelecionada, byte status){

        SQLiteDatabase db = getWritableDatabase(context);

        ContentValues values = new ContentValues();

        values.put("nome_receita", receitaSelecionada);
        values.put("ja_fiz", status);

        if(status == 1){
            values.put("quero_fazer",0);
        }


        try{
            db.update("receita_categorias",values,"nome_receita = "+receitaSelecionada,null);
        }catch (SQLException e){
            values.put("favoritas",0);
            db.insert("receita_categorias", null, values);
        }
        checkForReceitaSemCategoria(context);
    }

    public static void checkForReceitaSemCategoria(Context context){
        SQLiteDatabase db = getWritableDatabase(context);
        db.delete("receita_categorias","quero_fazer = 0 AND ja_fiz = 0 AND favoritas = 0 ",null);
        db.close();
    }

    public static ArrayList<String> getReceitasFavoritas(Context context) {

        SQLiteDatabase db = getReadableDatabase(context);

        ArrayList<String> fvrts = new ArrayList<>();

        String query = "SELECT nome_receita " +
                 "FROM receita_categorias " +
                 "WHERE favoritas = 1";


        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            fvrts.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return fvrts;
    }


    public static ArrayList<String> getReceitasJaFiz(Context context){

        SQLiteDatabase db = getReadableDatabase(context);

        ArrayList<String> fiz = new ArrayList<>();

        String query =  "SELECT nome_receita " +
                "FROM receita_categorias " +
                "WHERE ja_fiz = 1 ";

        Cursor cursor = db.rawQuery(query,null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            fiz.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return fiz;
    }


    public static ArrayList<String> getReceitasQueroFazer(Context context){
        SQLiteDatabase db = getReadableDatabase(context);

        ArrayList<String> quero = new ArrayList<>();

        String query = "SELECT nome_receita " +
                "FROM receita_categorias " +
                "WHERE quero_fazer = 1 ";

        Cursor cursor = db.rawQuery(query ,null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            quero.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return quero;

    }


}
