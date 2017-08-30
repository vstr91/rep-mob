package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;
import java.util.Map;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;

/**
 * Created by Almir on 24/02/2016.
 */
public class MusicaEventoDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE musica_evento (_id text primary key, observacao text, " +
            "ordem integer NOT NULL, id_musica integer NOT NULL, id_evento integer NOT NULL, status integer NOT NULL, " +
            "data_cadastro text, data_recebimento text, ultima_alteracao text, enviado integer NOT NULL);";
    RepDBHelper repDBHelper;

    public MusicaEventoDBHelper(Context context){
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
        Log.w(MusicaEventoDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<MusicaEvento> listarTodos(Context context){
        MusicaEventoDBAdapter adapter = new MusicaEventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<MusicaEvento> listarTodosAEnviar(Context context){
        MusicaEventoDBAdapter adapter = new MusicaEventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public List<Evento> listarTodosPorMusica(Context context, Musica musica){
        MusicaEventoDBAdapter adapter = new MusicaEventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorMusica(musica);
    }

    public List<Musica> listarTodosPorEvento(Context context, Evento evento){
        MusicaEventoDBAdapter adapter = new MusicaEventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorEvento(evento);
    }

    public List<MusicaEvento> corrigirOrdemPorEvento(Context context, Evento evento){
        MusicaEventoDBAdapter adapter = new MusicaEventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.corrigirOrdemPorEvento(evento);
    }

    public List<Musica> listarTodosAusentesEvento(Context context, Evento evento){
        MusicaEventoDBAdapter adapter = new MusicaEventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAusentesEvento(evento);
    }

    public List<Musica> listarTodosAusentesEvento(Context context, Evento umEvento, Estilo estilo, Artista artista){
        MusicaEventoDBAdapter adapter = new MusicaEventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAusentesEvento(umEvento, estilo, artista);
    }

    public long salvarOuAtualizar(Context context, MusicaEvento musicaEvento){
        MusicaEventoDBAdapter adapter = new MusicaEventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(musicaEvento);
    }

    public long deletarInativos(Context context){
        MusicaEventoDBAdapter adapter = new MusicaEventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public MusicaEvento carregar(Context context, MusicaEvento musicaEvento){
        MusicaEventoDBAdapter adapter = new MusicaEventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(musicaEvento);
    }

    public MusicaEvento carregarPorMusicaEEvento(Context context, MusicaEvento musicaEvento){
        MusicaEventoDBAdapter adapter = new MusicaEventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregarPorMusicaEEvento(musicaEvento);
    }

    public Map<String, Integer> contarTodosPorEventoEEstilo(Context context, Evento evento, int situacao){
        MusicaEventoDBAdapter adapter = new MusicaEventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.contarTodosPorEventoEEstilo(evento, situacao);
    }

}
