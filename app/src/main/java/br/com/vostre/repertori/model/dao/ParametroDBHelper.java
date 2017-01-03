package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Almir on 11/06/2015.
 */
public class ParametroDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String TABELA = "parametro";
    public static final String DBCREATE = "CREATE TABLE parametro (ultimo_acesso text);";
    public static final String DBPOPULATE = "INSERT INTO parametro (ultimo_acesso) VALUES ('-');";
    RepDBHelper repDBHelper;

    public ParametroDBHelper(Context context){
        super(context, DBNAME, null, DBVERSION);
        repDBHelper = new RepDBHelper(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public String carregarUltimoAcesso(Context context){
        ParametroDBAdapter adapter = new ParametroDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.carregarUltimoAcesso();
    }

    public void gravarUltimoAcesso(Context context, String ultimoAcesso){
        ParametroDBAdapter adapter = new ParametroDBAdapter(context, repDBHelper.getReadableDatabase());
        adapter.gravarUltimoAcesso(ultimoAcesso);
    }

}
