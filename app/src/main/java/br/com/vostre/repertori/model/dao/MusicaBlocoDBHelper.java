package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaBloco;
import br.com.vostre.repertori.model.Repertorio;

/**
 * Created by Almir on 24/02/2016.
 */
public class MusicaBlocoDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE musica_bloco (_id text primary key, observacao text, " +
            "ordem integer NOT NULL, id_musica integer NOT NULL, id_bloco_repertorio integer NOT NULL, status integer NOT NULL, " +
            "data_cadastro text, data_recebimento text, ultima_alteracao text, enviado integer NOT NULL);";
    RepDBHelper repDBHelper;

    public MusicaBlocoDBHelper(Context context){
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
        Log.w(MusicaBlocoDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<MusicaBloco> listarTodos(Context context){
        MusicaBlocoDBAdapter adapter = new MusicaBlocoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<MusicaBloco> listarTodosAEnviar(Context context){
        MusicaBlocoDBAdapter adapter = new MusicaBlocoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public List<Musica> listarTodosPorBloco(Context context, BlocoRepertorio blocoRepertorio, int situacao){
        MusicaBlocoDBAdapter adapter = new MusicaBlocoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorBloco(blocoRepertorio, situacao);
    }

    public List<MusicaBloco> corrigirOrdemPorBloco(Context context, BlocoRepertorio blocoRepertorio){
        MusicaBlocoDBAdapter adapter = new MusicaBlocoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.corrigirOrdemPorBloco(blocoRepertorio);
    }

    public List<Musica> listarTodosAusentesBloco(Context context, BlocoRepertorio blocoRepertorio){
        MusicaBlocoDBAdapter adapter = new MusicaBlocoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAusentesBloco(blocoRepertorio);
    }

    public List<Musica> listarTodosAusentesBloco(Context context, BlocoRepertorio umBlocoRepertorio, Estilo estilo, Artista artista){
        MusicaBlocoDBAdapter adapter = new MusicaBlocoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAusentesBloco(umBlocoRepertorio, estilo, artista);
    }

    public long salvarOuAtualizar(Context context, MusicaBloco musicaBloco){
        MusicaBlocoDBAdapter adapter = new MusicaBlocoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(musicaBloco);
    }

    public long deletarInativos(Context context){
        MusicaBlocoDBAdapter adapter = new MusicaBlocoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public MusicaBloco carregar(Context context, MusicaBloco musicaBloco){
        MusicaBlocoDBAdapter adapter = new MusicaBlocoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(musicaBloco);
    }

    public MusicaBloco carregarPorMusicaEBloco(Context context, MusicaBloco musicaBloco){
        MusicaBlocoDBAdapter adapter = new MusicaBlocoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregarPorMusicaEBloco(musicaBloco);
    }

}
