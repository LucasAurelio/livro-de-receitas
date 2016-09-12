package com.projetoes.livrodereceitas;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH = "/data/data/com.projetoes.livrodereceitas/databases/";
    public static final String DATABASE_NAME = "repositorioDeReceitas.db";

    private static SQLiteDatabase ourDataBase;
    private final Context ourContext;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ourContext = context;
    }

    public void createDatabase() throws IOException{
        boolean dbExists = checkDataBase();

        if(dbExists){
            //lembrar de excluir
            //ourDataBase.close();
            ourContext.deleteDatabase(DATABASE_NAME);
            this.getReadableDatabase();
            try{
                copyDataBase();
            }catch (IOException e){
                throw new Error("error copying database");
            }
        }else{
            this.getReadableDatabase();
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
        ourDataBase = SQLiteDatabase.openDatabase(thePath,null, SQLiteDatabase.OPEN_READONLY);
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


    //QUERIES
    public Cursor getAlimentos() {
        Cursor cursor = ourDataBase.rawQuery("SELECT nome FROM alimento",null);
        return cursor;
    }

    public Cursor getTipos() {
        Cursor cursor = ourDataBase.rawQuery("Select DISTINCT tipo FROM receita",null);
        return cursor;
    }

    public Cursor getReceitasPorCompatibilidade(ArrayList<String> listaIngredientes, ArrayList<String> listaFiltros){
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
                "HAVING ranker > " + (listaIngredientes.size()-1) + " AND ranker <  "+ (listaIngredientes.size()+1)+" "+
                "ORDER BY ranker DESC",null);

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

    public Cursor getReceitaSelecionada(String nomeDaReceita){
        Cursor cursor = ourDataBase.rawQuery(
                "SELECT p.nome, f.quantidade, g.nome, p.descricao " +
                "FROM receita p, ingrediente g, receita_ingredientes f " +
                "WHERE p.nome =  '" + nomeDaReceita  +
                "' AND p._id = f.id_receita " +
                "AND g._id = f.id_ingrediente",null);

        return cursor;
    }

    public ArrayList<String> getCategorias(){
        ArrayList<String> categorias = new ArrayList<>();
        categorias.add("favoritas");
        categorias.add("já fiz");
        categorias.add("quero fazer");

        return categorias;
    }

    public Cursor getReceitasFavoritas(){
        Cursor cursor = ourDataBase.rawQuery(
                "SELECT nome " +
                "FROM receita " +
                "WHERE categoria = 'favoritas' ",null);
        return cursor;
    }

    public Cursor getReceitasJaFiz(){
        Cursor cursor = ourDataBase.rawQuery(
                "SELECT nome " +
                "FROM receita " +
                "WHERE categoria = 'já fiz' ",null);
        return cursor;
    }

    public Cursor getReceitasQueroFazer(){
        Cursor cursor = ourDataBase.rawQuery(
                "SELECT nome " +
                "FROM receita " +
                "WHERE categoria = 'quero fazer' ",null);
        return cursor;
    }

    public void setReceitaCategoria(String ctgra, String receitaSelecionada){
        ourDataBase.rawQuery(
                "UPDATE receita " +
                "SET categoria = '" + ctgra + "' " +
                "WHERE nome = '" + receitaSelecionada + "'",null);
    }

}
