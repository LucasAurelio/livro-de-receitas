package com.projetoes.livrodereceitas.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.projetoes.livrodereceitas.R;
import com.projetoes.livrodereceitas.Recipe;
import com.projetoes.livrodereceitas.RecipeSearch;

import java.util.ArrayList;

public class DBUtils {
    public static final String TAG = "DB_Utils";
    private static DatabaseHelper openHelper;
    private static SQLiteDatabase db;

    public static void instancializaBanco(Context cont){
        openHelper = new DatabaseHelper(cont);
    }

    public static SQLiteDatabase getReadableDatabase() {
        openHelper.openDataBase();
        return openHelper.getWritableDatabase();
    }

    public static SQLiteDatabase getWritableDatabase() {
        openHelper.openDataBase();
        return openHelper.getWritableDatabase();
    }


    public static ArrayList getAlimentos() {
        db = getReadableDatabase();
        ArrayList<String> allAlimentos = new ArrayList<>();

        db.beginTransaction();

        try {
            String query = "SELECT nome FROM alimento";

            Cursor c = db.rawQuery(query, null);

            c.moveToFirst();
            while (!c.isAfterLast()) {
                allAlimentos.add(c.getString(0));
                c.moveToNext();
            }

            c.close();
            db.setTransactionSuccessful();

        }finally {
            db.endTransaction();
        }

        db.close();

        return allAlimentos;
    }

    public static ArrayList getTipos() {
        db = getReadableDatabase();
        ArrayList<String> allCategorias = new ArrayList<>();

        db.beginTransaction();
        try {
            String query = "Select DISTINCT tipo FROM receita";

            Cursor c = db.rawQuery(query, null);

            c.moveToFirst();
            while (!c.isAfterLast()) {
                allCategorias.add(c.getString(0));
                c.moveToNext();
            }

            c.close();
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
        db.close();

        return allCategorias;
    }

    public static ArrayList getReceitasPorCompatibilidade(ArrayList<String> listaIngredientes, ArrayList<String> listaFiltros) {

        db = getReadableDatabase();

        ArrayList<String[]> allReceitasCompativeis = new ArrayList<>();

        db.beginTransaction();

        try {
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
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();

        return allReceitasCompativeis;
    }


    //AJUSTAR PARA FICAR IGUAL AO QUE TA NO MASTER
    public static ArrayList<String[]> getReceitasPorSimilaridade(ArrayList<String> listaIngredientes, ArrayList<String> listaFiltros) {

        db = getReadableDatabase();

        ArrayList<String[]> allReceitasSimilares = new ArrayList<>();

        db.beginTransaction();

        try {
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
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();

        return allReceitasSimilares;
    }


    // QUERY VIEW RECEITA
    public static ArrayList getReceitaSelecionada(String nomeDaReceita) {

        db = getReadableDatabase();

        ArrayList aReceita = new ArrayList<>();

        db.beginTransaction();
        try {
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
            cursor.close();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();
        return aReceita;
    }


    public static ArrayList receitaCategorias(String receitaSelecionada) {

        db = getReadableDatabase();

        ArrayList receitaCategorias = new ArrayList<>();

        db.beginTransaction();

        try{
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
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();
        return receitaCategorias;
    }



    public static void setReceitaCategoria(int catgr, String nomeDaReceita, byte status){
        if (catgr == (R.string.category_wannaDo)){
            setReceitaQueroFazer(nomeDaReceita, status);
        }else if(catgr == (R.string.category_done)){
            setReceitaJaFiz(nomeDaReceita,status);
        }else if(catgr == (R.string.category_favorite)){
            setReceitaFavoritas(nomeDaReceita, status);
        }
    }

    public static void setReceitaFavoritas(String receitaSelecionada, byte status){

        db = getWritableDatabase();

        db.beginTransaction();

        try{
            ContentValues values = new ContentValues();

            values.put("nome_receita", receitaSelecionada);
            values.put("favoritas", status);

            if(checkForReceitaInCategorias(receitaSelecionada) > 0){
                db.update("receita_categorias",values,"nome_receita = '"+receitaSelecionada+"'",null);
            }else{
                values.put("quero_fazer",0);
                values.put("ja_fiz",0);
                db.insert("receita_categorias", null, values);
            }
            checkForReceitaSemCategoria();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();

    }

    public static void setReceitaQueroFazer(String receitaSelecionada, byte status){
        db = getWritableDatabase();

        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();

            values.put("nome_receita", receitaSelecionada);
            values.put("quero_fazer", status);

            if(status == 1){
                values.put("ja_fiz",0);
            }

            if(checkForReceitaInCategorias(receitaSelecionada) > 0){
                db.update("receita_categorias",values,"nome_receita = '"+receitaSelecionada+"'",null);
            }else{
                values.put("favoritas",0);
                db.insert("receita_categorias", null, values);
            }
            checkForReceitaSemCategoria();

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();
    }

    public static void setReceitaJaFiz(String receitaSelecionada, byte status){

        db = getWritableDatabase();

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();

            values.put("nome_receita", receitaSelecionada);
            values.put("ja_fiz", status);

            if(status == 1){
                values.put("quero_fazer", 0);
            }
            if(checkForReceitaInCategorias(receitaSelecionada) > 0){
                db.update("receita_categorias", values, "nome_receita = '" + receitaSelecionada+"'", null);
            }else{
                values.put("favoritas",0);
                db.insert("receita_categorias", null, values);
            }
            checkForReceitaSemCategoria();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();

    }

    private static void checkForReceitaSemCategoria(){
            db.delete("receita_categorias", "quero_fazer = 0 AND ja_fiz = 0 AND favoritas = 0 ", null);
    }

    private static int checkForReceitaInCategorias(String nomeDaReceita){
        int count = 0;
            String query = "SELECT nome_receita " + "FROM receita_categorias " +
                    "WHERE nome_receita = '"+nomeDaReceita+"' ";

            Cursor cursor = db.rawQuery(query, null);
            count = cursor.getCount();

            cursor.close();

        return count;
    }

    public static ArrayList<String> getReceitasFavoritas() {
        db = getReadableDatabase();
        ArrayList<String> fvrts = new ArrayList<>();

        db.beginTransaction();

        try{
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
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();

        return fvrts;
    }


    public static ArrayList<String> getReceitasJaFiz() {
        db = getReadableDatabase();

        ArrayList<String> fiz = new ArrayList<>();

        db.beginTransaction();

        try{
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
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
        db.close();
        return fiz;
    }


    public static ArrayList<String> getReceitasQueroFazer(){
        db = getReadableDatabase();

        ArrayList<String> quero = new ArrayList<>();

        db.beginTransaction();
        try {
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
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
        db.close();
        return quero;

    }

}
