package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.vostre.repertori.model.TipoEvento;

/**
 * Created by Almir on 24/02/2016.
 */
public class RepDBHelper extends SQLiteOpenHelper {

    public static final int DBVERSION = 1;
    public static final String DBNAME = "rep.db";

    public RepDBHelper(Context context){
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ArtistaDBHelper.DBCREATE);
        db.execSQL(TipoEventoDBHelper.DBCREATE);
        db.execSQL(EventoDBHelper.DBCREATE);
        db.execSQL(MusicaDBHelper.DBCREATE);
        db.execSQL(MusicaEventoDBHelper.DBCREATE);
        db.execSQL(ComentarioEventoDBHelper.DBCREATE);
        db.execSQL(ParametroDBHelper.DBCREATE);

        db.execSQL(ParametroDBHelper.DBPOPULATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nada ainda
    }

}
