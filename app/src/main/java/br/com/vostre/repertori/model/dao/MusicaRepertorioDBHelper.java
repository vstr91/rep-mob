package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaRepertorio;

/**
 * Created by Almir on 24/02/2016.
 */
public class MusicaRepertorioDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE musica_repertorio (_id text primary key, observacao text, " +
            "ordem integer NOT NULL, id_musica integer NOT NULL, id_repertorio integer NOT NULL, status integer NOT NULL, " +
            "data_cadastro text, data_recebimento text, ultima_alteracao text, enviado integer NOT NULL);";
    RepDBHelper repDBHelper;

    public MusicaRepertorioDBHelper(Context context){
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
        Log.w(MusicaRepertorioDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<MusicaRepertorio> listarTodos(Context context){
        MusicaRepertorioDBAdapter adapter = new MusicaRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<MusicaRepertorio> listarTodosAEnviar(Context context){
        MusicaRepertorioDBAdapter adapter = new MusicaRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public List<Repertorio> listarTodosPorMusica(Context context, Musica musica){
        MusicaRepertorioDBAdapter adapter = new MusicaRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorMusica(musica);
    }

    public List<Musica> listarTodosPorRepertorio(Context context, Repertorio repertorio){
        MusicaRepertorioDBAdapter adapter = new MusicaRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorRepertorio(repertorio);
    }

    public List<MusicaRepertorio> corrigirOrdemPorRepertorio(Context context, Repertorio repertorio){
        MusicaRepertorioDBAdapter adapter = new MusicaRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.corrigirOrdemPorRepertorio(repertorio);
    }

    public List<Musica> listarTodosAusentesRepertorio(Context context, Repertorio repertorio){
        MusicaRepertorioDBAdapter adapter = new MusicaRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAusentesRepertorio(repertorio);
    }

    public List<Musica> listarTodosAusentesRepertorio(Context context, Repertorio umRepertorio, Estilo estilo, Artista artista){
        MusicaRepertorioDBAdapter adapter = new MusicaRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAusentesRepertorio(umRepertorio, estilo, artista);
    }

    public long salvarOuAtualizar(Context context, MusicaRepertorio musicaRepertorio){
        MusicaRepertorioDBAdapter adapter = new MusicaRepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(musicaRepertorio);
    }

    public long deletarInativos(Context context){
        MusicaRepertorioDBAdapter adapter = new MusicaRepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public MusicaRepertorio carregar(Context context, MusicaRepertorio musicaRepertorio){
        MusicaRepertorioDBAdapter adapter = new MusicaRepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(musicaRepertorio);
    }

    public MusicaRepertorio carregarPorMusicaERepertorio(Context context, MusicaRepertorio musicaRepertorio){
        MusicaRepertorioDBAdapter adapter = new MusicaRepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregarPorMusicaERepertorio(musicaRepertorio);
    }

}
