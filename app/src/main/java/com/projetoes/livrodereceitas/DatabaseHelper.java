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

/**
 * Created by lucas on 08/08/2016.
 */
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
            //all good
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

    public Cursor getAlimentos() {
        Cursor cursor = ourDataBase.rawQuery("SELECT nome FROM alimento",null);
        return cursor;
    }

    public Cursor getFiltros() {
        Cursor cursor = ourDataBase.rawQuery("Select DISTINCT categoria FROM receita",null);
        return cursor;
    }

    //Queries

}
