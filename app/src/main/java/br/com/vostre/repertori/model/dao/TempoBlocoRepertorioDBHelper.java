package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.TempoBlocoRepertorio;

/**
 * Created by Almir on 24/02/2016.
 */
public class TempoBlocoRepertorioDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE tempo_bloco_repertorio (_id text primary key, tempo text NOT NULL, " +
            "id_bloco_repertorio integer NOT NULL, status integer NOT NULL, data_cadastro text, " +
            "data_recebimento text, ultima_alteracao text, enviado integer NOT NULL, audio text, audio_enviado integer NOT NULL, audio_recebido integer NOT NULL);";
    RepDBHelper repDBHelper;

    public TempoBlocoRepertorioDBHelper(Context context){
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
        Log.w(TempoBlocoRepertorioDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<TempoBlocoRepertorio> listarTodosPorBlocoRepertorio(Context context, BlocoRepertorio blocoRepertorio, int limite){
        TempoBlocoRepertorioDBAdapter adapter = new TempoBlocoRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorBlocoRepertorio(blocoRepertorio, limite);
    }

    public List<TempoBlocoRepertorio> listarTodosAEnviar(Context context){
        TempoBlocoRepertorioDBAdapter adapter = new TempoBlocoRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public List<TempoBlocoRepertorio> listarTodosAEnviarAudio(Context context){
        TempoBlocoRepertorioDBAdapter adapter = new TempoBlocoRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviarAudio();
    }

    public List<TempoBlocoRepertorio> listarTodosAReceberAudio(Context context){
        TempoBlocoRepertorioDBAdapter adapter = new TempoBlocoRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAReceberAudio();
    }

    public long salvarOuAtualizar(Context context, TempoBlocoRepertorio tme){
        TempoBlocoRepertorioDBAdapter adapter = new TempoBlocoRepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(tme);
    }

    public long deletarInativos(Context context){
        TempoBlocoRepertorioDBAdapter adapter = new TempoBlocoRepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public long sinalizaEnvioAudio(Context context, String audio){
        TempoBlocoRepertorioDBAdapter adapter = new TempoBlocoRepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.sinalizaEnvioAudio(audio);
    }

    public TempoBlocoRepertorio carregarPorAudio(Context context, String audio){
        TempoBlocoRepertorioDBAdapter adapter = new TempoBlocoRepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregarPorAudio(audio);
    }

}
