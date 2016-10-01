package com.projetoes.livrodereceitas.DB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


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

    @Override
    public void onCreate(SQLiteDatabase db) {
        
        //check if db exisist
        if (!checkDataBase()) {
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        } else{
            this.getWritableDatabase();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {
                case 2:
                    try {
                        copyDataBase();
                    } catch (IOException e) {
                        throw new Error("error copying database");
                    }

                    break;
            }
            upgradeTo++;
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


    public boolean checkDataBase(){
        SQLiteDatabase checkDB = null;

        try{
            String testPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(testPath,null,SQLiteDatabase.OPEN_READWRITE);
        }catch(SQLiteException e){
            throw new Error("DataBase does not exist yet");
        }

        if(checkDB != null){
            checkDB.close();
            return true;
        }else{
            return false;
        }

    }


    public void openDataBase() throws SQLException{
        String thePath = DATABASE_PATH + DATABASE_NAME;
        ourDataBase = SQLiteDatabase.openDatabase(thePath,null, SQLiteDatabase.OPEN_READWRITE);
    }

}
