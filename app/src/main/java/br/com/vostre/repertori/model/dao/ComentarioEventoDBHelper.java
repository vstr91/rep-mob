package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.repertori.model.ComentarioEvento;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;

/**
 * Created by Almir on 24/02/2016.
 */
public class ComentarioEventoDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE comentario_evento (_id text primary key, texto text NOT NULL, " +
            "id_evento integer NOT NULL, status integer NOT NULL, enviado integer NOT NULL, " +
            "data_cadastro text, data_recebimento text, ultima_alteracao text);";
    RepDBHelper repDBHelper;

    public ComentarioEventoDBHelper(Context context){
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
        Log.w(ComentarioEventoDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<ComentarioEvento> listarTodos(Context context){
        ComentarioEventoDBAdapter adapter = new ComentarioEventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<ComentarioEvento> listarTodosAEnviar(Context context){
        ComentarioEventoDBAdapter adapter = new ComentarioEventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public List<ComentarioEvento> listarTodosPorEvento(Context context, Evento evento){
        ComentarioEventoDBAdapter adapter = new ComentarioEventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorEvento(evento);
    }

    public long salvarOuAtualizar(Context context, ComentarioEvento comentarioEvento){
        ComentarioEventoDBAdapter adapter = new ComentarioEventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(comentarioEvento);
    }

    public long deletarInativos(Context context){
        ComentarioEventoDBAdapter adapter = new ComentarioEventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public ComentarioEvento carregar(Context context, ComentarioEvento comentarioEvento){
        ComentarioEventoDBAdapter adapter = new ComentarioEventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(comentarioEvento);
    }

}
