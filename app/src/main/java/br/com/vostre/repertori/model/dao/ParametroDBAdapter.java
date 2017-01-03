package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Almir on 11/06/2015.
 */
public class ParametroDBAdapter {

    private SQLiteDatabase database;
    private ParametroDBHelper parametroDBHelper;
    private Context context;

    public ParametroDBAdapter(Context context, SQLiteDatabase database){
        parametroDBHelper = new ParametroDBHelper(context);
        this.database = database;
        this.context = context;
    }

    public String carregarUltimoAcesso(){
        Cursor cursor = database.rawQuery("SELECT ultimo_acesso FROM parametro", null);

        String ultimoAcesso = null;

        if(cursor.moveToFirst()){
            do{
                ultimoAcesso = cursor.getString(0);

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return ultimoAcesso;
    }

    public void gravarUltimoAcesso(String acesso){

        Cursor cursor = database.rawQuery("UPDATE parametro SET ultimo_acesso = ?", new String[]{acesso});
        cursor.moveToFirst();
        cursor.close();

        database.close();

    }


}
