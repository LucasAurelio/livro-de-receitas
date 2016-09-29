package com.projetoes.livrodereceitas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;
    private static String DATABASE_PATH = "/data/data/com.projetoes.livrodereceitas/databases/";
    public static final String DATABASE_NAME = "repositorioDeReceitas.db";

    private static SQLiteDatabase ourDataBase;
    private static Context ourContext;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ourContext = context;
    }

    public void createDatabase() throws IOException{
        boolean dbExists = checkDataBase();

        if(dbExists){
            //lembrar de excluir
            //ourDataBase.close();
            //ourContext.deleteDatabase(DATABASE_NAME);
            this.getWritableDatabase();
            try{
                copyDataBase();
            }catch (IOException e){
                throw new Error("error copying database");
            }
        }else{
            this.getWritableDatabase();
            try{
                copyDataBase();
            }catch (IOException e){
                throw new Error("error copying database");
            }
        }
    }

    public boolean checkDataBase(){
        SQLiteDatabase checkDB = null;

        try{
            String testPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(testPath,null,SQLiteDatabase.OPEN_READWRITE);
        }catch(SQLiteException e){
            // DataBase does not exist yet
        }

        if(checkDB != null){
            checkDB.close();
            return true;
        }else{
            return false;
        }

    }

    private void copyDataBase() throws IOException{
        InputStream ourInput = ourContext.getAssets().open(DATABASE_NAME);

        String outFile = DATABASE_PATH + DATABASE_NAME;

        OutputStream ourOutput = new FileOutputStream(outFile);

        byte[] buffer = new byte[1024];
        int length;
        while ((length=ourInput.read(buffer))>0){
            ourOutput.write(buffer,0,length);
        }

        ourOutput.flush();
        ourOutput.close();
        ourInput.close();
    }

    public void openDataBase() throws SQLException{
        String thePath = DATABASE_PATH + DATABASE_NAME;
        ourDataBase = SQLiteDatabase.openDatabase(thePath,null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close(){
        if(ourDataBase != null){
            ourDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db){

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //QUERIES PESQUISA
    public Cursor getAlimentos() {
        Cursor cursor = ourDataBase.rawQuery("SELECT nome FROM alimento",null);
        return cursor;
    }

    public Cursor getTipos() {
        Cursor cursor = ourDataBase.rawQuery("Select DISTINCT tipo FROM receita",null);
        return cursor;
    }

    public Cursor getReceitasPorCompatibilidade(ArrayList<String> listaIngredientes, ArrayList<String> listaFiltros){
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

        Cursor cursor = ourDataBase.rawQuery(
                "SELECT nome" + allSelections+" "+
                "FROM(SELECT p.nome as nome, g.nome" + allCases +" "+
                        "FROM receita p, ingrediente g, receita_ingredientes f "+
                "WHERE p._id = f.id_receita " +
                "AND g._id = f.id_ingrediente " +
                "AND (p.tipo = " + allFiltros + ")) " +
                "GROUP BY nome " +
                "HAVING "+allHavings+" ",null);

        return cursor;
    }

    public Cursor getReceitasPorSimilaridade(ArrayList<String> listaIngredientes, ArrayList<String> listaFiltros){
        String allIngredientes = "'%" + listaIngredientes.get(0) +"%'";
        for(int i=1;i<listaIngredientes.size();i++){
            allIngredientes += " OR g.nome LIKE '%" + listaIngredientes.get(i) +"%'";
        }

        String allFiltros = "'" + listaFiltros.get(0) +"'";
        for(int i=1;i<listaFiltros.size();i++){
            allFiltros += " OR p.tipo = '" + listaFiltros.get(i) +"'";
        }

        Cursor cursor = ourDataBase.rawQuery(
                "SELECT p.nome, COUNT(g.nome) as ranker " +
                "FROM receita p, ingrediente g, receita_ingredientes f " +
                "WHERE p._id = f.id_receita " +
                "AND g._id = f.id_ingrediente " +
                "AND (p.tipo = " + allFiltros + ") " +
                "AND (g.nome LIKE " + allIngredientes + ") "+
                "GROUP BY p._id " +
                "HAVING ranker < " + listaIngredientes.size() +" "+
                "ORDER BY ranker DESC ",null);

        return cursor;
    }

    // QUERY VIEW RECEITA
    public Cursor getReceitaSelecionada(String nomeDaReceita){
        Cursor cursor = ourDataBase.rawQuery(
                "SELECT p.nome, f.quantidade, g.nome, p.descricao " +
                "FROM receita p, ingrediente g, receita_ingredientes f " +
                "WHERE p.nome =  '" + nomeDaReceita  +
                "' AND p._id = f.id_receita " +
                "AND g._id = f.id_ingrediente",null);

        return cursor;
    }


    //QUERIES CATEGORIAS
    public ArrayList<String> getCategorias(){
        ArrayList<String> categorias = new ArrayList<>();
        categorias.add("favoritas");
        categorias.add("já fiz");
        categorias.add("quero fazer");

        return categorias;
    }

    public Cursor getReceitasFavoritas(){
        Cursor cursor = ourDataBase.rawQuery(
                "SELECT nome_receita " +
                "FROM receita_categorias " +
                "WHERE favoritas = 1",null);
        return cursor;
    }

    public Cursor getReceitasJaFiz(){
        Cursor cursor = ourDataBase.rawQuery(
                "SELECT nome_receita " +
                "FROM receita_categorias " +
                "WHERE ja_fiz = 1 ",null);
        return cursor;
    }

    public Cursor getReceitasQueroFazer(){
        Cursor cursor = ourDataBase.rawQuery(
                "SELECT nome_receita " +
                "FROM receita_categorias " +
                "WHERE quero_fazer = 1 ",null);
        return cursor;
    }

    public void setReceitaCategoria(int catgr, String nomeDaReceita, byte status){
        if (catgr == (R.string.category_wannaDo)){
            setReceitaQueroFazer(nomeDaReceita,status);
        }else if(catgr == (R.string.category_done)){
            setReceitaJaFiz(nomeDaReceita,status);
        }else if(catgr == (R.string.category_favorite)){
            setReceitaFavoritas(nomeDaReceita, status);
        }
    }

    public void setReceitaFavoritas(String receitaSelecionada, byte status){
        ContentValues values = new ContentValues();

        values.put("nome_receita", receitaSelecionada);
        values.put("favoritas", status);
        try{
            ourDataBase.update("receita_categorias",values,"nome_receita = "+receitaSelecionada,null);
        }catch (SQLException e){
            ourDataBase.insert("receita_categorias", null, values);
        }
        checkForReceitaSemCategoria();
    }

    public void setReceitaQueroFazer(String receitaSelecionada, byte status){
        ContentValues values = new ContentValues();

        values.put("nome_receita", receitaSelecionada);
        values.put("quero_fazer", status);
        try{
            ourDataBase.update("receita_categorias",values,"nome_receita = "+receitaSelecionada,null);
        }catch (SQLException e){
            ourDataBase.insert("receita_categorias", null, values);
        }
        checkForReceitaSemCategoria();
    }

    public void setReceitaJaFiz(String receitaSelecionada,byte status){
        ContentValues values = new ContentValues();

        values.put("nome_receita", receitaSelecionada);
        values.put("ja_fiz", status);
        try{
            ourDataBase.update("receita_categorias",values,"nome_receita = "+receitaSelecionada,null);
        }catch (SQLException e){
            ourDataBase.insert("receita_categorias", null, values);
        }
        checkForReceitaSemCategoria();
    }

    public void checkForReceitaSemCategoria(){
        ourDataBase.rawQuery(
                "DELETE FROM receita_categorias " +
                        "WHERE quero_fazer = 0 " +
                        "AND ja_fiz = 0 " +
                        "AND favoritas = 0 ",null);
    }

    public Cursor receitaCategorias(String receitaSelecionada) {
        Cursor cursor = ourDataBase.rawQuery(
                "SELECT quero_fazer, ja_fiz, favoritas " +
                        "FROM receita_categorias " +
                        "WHERE nome_receita = '"+receitaSelecionada+"'", null);

        return  cursor;
    }
}
