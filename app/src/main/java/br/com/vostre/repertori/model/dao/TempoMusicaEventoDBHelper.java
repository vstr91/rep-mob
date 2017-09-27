package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.TempoMusicaEvento;

/**
 * Created by Almir on 24/02/2016.
 */
public class TempoMusicaEventoDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE tempo_musica_evento (_id text primary key, tempo text NOT NULL, " +
            "id_musica_evento integer NOT NULL, status integer NOT NULL, data_cadastro text, " +
            "data_recebimento text, ultima_alteracao text, enviado integer NOT NULL, audio text);";
    RepDBHelper repDBHelper;

    public TempoMusicaEventoDBHelper(Context context){
        super(context, DBNAME, null, DBVERSION);
        repDBHelper = new RepDBHelper(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //db.execSQL(DBCREATE);
        //db.execSQL("INSERT INTO pais (nome, iso3, status) VALUES ('Brasil', 'BRA', 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w(TempoMusicaEventoDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<TempoMusicaEvento> listarTodosPorMusica(Context context, Musica musica, int limite){
        TempoMusicaEventoDBAdapter adapter = new TempoMusicaEventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorMusica(musica, limite);
    }

    public List<TempoMusicaEvento> listarTodosAEnviar(Context context){
        TempoMusicaEventoDBAdapter adapter = new TempoMusicaEventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public long salvarOuAtualizar(Context context, TempoMusicaEvento tme){
        TempoMusicaEventoDBAdapter adapter = new TempoMusicaEventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(tme);
    }

    public long deletarInativos(Context context){
        TempoMusicaEventoDBAdapter adapter = new TempoMusicaEventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

}
