package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.vostre.repertori.model.ContatoCasa;
import br.com.vostre.repertori.model.MusicaBloco;
import br.com.vostre.repertori.model.MusicaRepertorio;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.TempoBlocoRepertorio;
import br.com.vostre.repertori.model.TipoEvento;

/**
 * Created by Almir on 24/02/2016.
 */
public class RepDBHelper extends SQLiteOpenHelper {

    public static final int DBVERSION = 3;
    public static final String DBNAME = "rep.db";

    public RepDBHelper(Context context){
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // versao 1
        db.execSQL(ArtistaDBHelper.DBCREATE);
        db.execSQL(TipoEventoDBHelper.DBCREATE);
        db.execSQL(EventoDBHelper.DBCREATE);
        db.execSQL(MusicaDBHelper.DBCREATE);
        db.execSQL(MusicaEventoDBHelper.DBCREATE);
        db.execSQL(ComentarioEventoDBHelper.DBCREATE);
        db.execSQL(ParametroDBHelper.DBCREATE);

        db.execSQL(MusicaProjetoDBHelper.DBCREATE);
        db.execSQL(ProjetoDBHelper.DBCREATE);
        db.execSQL(EstiloDBHelper.DBCREATE);
        db.execSQL(TempoMusicaEventoDBHelper.DBCREATE);

        db.execSQL(RepertorioDBHelper.DBCREATE);
        db.execSQL(MusicaRepertorioDBHelper.DBCREATE);

        db.execSQL(BlocoRepertorioDBHelper.DBCREATE);
        db.execSQL(MusicaBlocoDBHelper.DBCREATE);
        db.execSQL(TempoBlocoRepertorioDBHelper.DBCREATE);
        db.execSQL(CasaDBHelper.DBCREATE);
        db.execSQL(ContatoDBHelper.DBCREATE);
        db.execSQL(ContatoCasaDBHelper.DBCREATE);

        db.execSQL(ParametroDBHelper.DBPOPULATE);

        // versao 2
        // nada ainda
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch(oldVersion){
            case 1:
                db.execSQL("DROP TABLE IF EXISTS artista");
                db.execSQL("DROP TABLE IF EXISTS tipo_evento");
                db.execSQL("DROP TABLE IF EXISTS evento");
                db.execSQL("DROP TABLE IF EXISTS musica");
                db.execSQL("DROP TABLE IF EXISTS musica_evento");
                db.execSQL("DROP TABLE IF EXISTS comentario_evento");
                db.execSQL("DROP TABLE IF EXISTS parametro");
                db.execSQL("DROP TABLE IF EXISTS musica_projeto");
                db.execSQL("DROP TABLE IF EXISTS projeto");
                db.execSQL("DROP TABLE IF EXISTS estilo");
                db.execSQL("DROP TABLE IF EXISTS tempo_musica_evento");
                db.execSQL("DROP TABLE IF EXISTS repertorio");
                db.execSQL("DROP TABLE IF EXISTS musica_repertorio");
                db.execSQL("DROP TABLE IF EXISTS bloco_repertorio");
                db.execSQL("DROP TABLE IF EXISTS musica_bloco");
                db.execSQL("DROP TABLE IF EXISTS tempo_bloco_repertorio");
                db.execSQL("DROP TABLE IF EXISTS casa");
                db.execSQL("DROP TABLE IF EXISTS contato");
                db.execSQL("DROP TABLE IF EXISTS contato_casa");
                onCreate(db);
            case 2:
                db.execSQL("DROP TABLE IF EXISTS artista");
                db.execSQL("DROP TABLE IF EXISTS tipo_evento");
                db.execSQL("DROP TABLE IF EXISTS evento");
                db.execSQL("DROP TABLE IF EXISTS musica");
                db.execSQL("DROP TABLE IF EXISTS musica_evento");
                db.execSQL("DROP TABLE IF EXISTS comentario_evento");
                db.execSQL("DROP TABLE IF EXISTS parametro");
                db.execSQL("DROP TABLE IF EXISTS musica_projeto");
                db.execSQL("DROP TABLE IF EXISTS projeto");
                db.execSQL("DROP TABLE IF EXISTS estilo");
                db.execSQL("DROP TABLE IF EXISTS tempo_musica_evento");
                db.execSQL("DROP TABLE IF EXISTS repertorio");
                db.execSQL("DROP TABLE IF EXISTS musica_repertorio");
                db.execSQL("DROP TABLE IF EXISTS bloco_repertorio");
                db.execSQL("DROP TABLE IF EXISTS musica_bloco");
                db.execSQL("DROP TABLE IF EXISTS tempo_bloco_repertorio");
                db.execSQL("DROP TABLE IF EXISTS casa");
                db.execSQL("DROP TABLE IF EXISTS contato");
                db.execSQL("DROP TABLE IF EXISTS contato_casa");
                onCreate(db);
                break;
        }

    }

}
