package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaExecucao;
import br.com.vostre.repertori.model.MusicaProjeto;
import br.com.vostre.repertori.model.Projeto;

/**
 * Created by Almir on 24/02/2016.
 */
public class MusicaProjetoDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE musica_projeto (_id text primary key, id_musica integer NOT NULL, id_projeto integer NOT NULL, status integer NOT NULL, " +
            "data_cadastro text, data_recebimento text, ultima_alteracao text, enviado integer NOT NULL);";
    RepDBHelper repDBHelper;

    public MusicaProjetoDBHelper(Context context){
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
        Log.w(MusicaProjetoDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<MusicaProjeto> listarTodos(Context context){
        MusicaProjetoDBAdapter adapter = new MusicaProjetoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<Projeto> listarTodosDisponiveisMusica(Context context, Musica musica){
        MusicaProjetoDBAdapter adapter = new MusicaProjetoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosDisponiveisMusica(musica);
    }

    public List<MusicaProjeto> listarTodosAEnviar(Context context){
        MusicaProjetoDBAdapter adapter = new MusicaProjetoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public List<Musica> listarTodosPorProjeto(Context context, Projeto projeto, int situacao){
        MusicaProjetoDBAdapter adapter = new MusicaProjetoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorProjeto(projeto, situacao);
    }

    public List<Musica> listarTodosPorProjetoEEstilo(Context context, Projeto projeto, int situacao){
        MusicaProjetoDBAdapter adapter = new MusicaProjetoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorProjetoEEstilo(projeto, situacao);
    }

    public Map<String, Integer> contarTodosPorProjetoEEstilo(Context context, Projeto projeto, int situacao){
        MusicaProjetoDBAdapter adapter = new MusicaProjetoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.contarTodosPorProjetoEEstilo(projeto, situacao);
    }

    public long salvarOuAtualizar(Context context, MusicaProjeto musicaEvento){
        MusicaProjetoDBAdapter adapter = new MusicaProjetoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(musicaEvento);
    }

    public long deletarInativos(Context context){
        MusicaProjetoDBAdapter adapter = new MusicaProjetoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public MusicaProjeto carregar(Context context, MusicaProjeto musicaEvento){
        MusicaProjetoDBAdapter adapter = new MusicaProjetoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(musicaEvento);
    }

    public MusicaProjeto carregarPorMusicaEProjeto(Context context, MusicaProjeto musicaEvento){
        MusicaProjetoDBAdapter adapter = new MusicaProjetoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregarPorMusicaEProjeto(musicaEvento);
    }

    public List<MusicaExecucao> ultimaExecucaoPorProjeto(Context context, Projeto projeto){
        MusicaProjetoDBAdapter adapter = new MusicaProjetoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.ultimaExecucaoPorProjeto(projeto);
    }

    public List<Musica> listarTodosAusentesProjeto(Context context, Projeto projeto){
        MusicaProjetoDBAdapter adapter = new MusicaProjetoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAusentesProjeto(projeto);
    }

}
