package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Evento;

/**
 * Created by Almir on 24/02/2016.
 */
public class EventoDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE evento (_id text primary key, nome text NOT NULL, " +
            "data text NOT NULL, id_tipo_evento integer NOT NULL, status integer NOT NULL, data_cadastro text, " +
            "data_recebimento text, ultima_alteracao text, slug text, enviado integer NOT NULL);";
    RepDBHelper repDBHelper;

    public EventoDBHelper(Context context){
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
        Log.w(EventoDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<Evento> listarTodos(Context context){
        EventoDBAdapter adapter = new EventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<Evento> listarTodosAEnviar(Context context){
        EventoDBAdapter adapter = new EventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public List<Evento> listarTodosAteHoje(Context context){
        EventoDBAdapter adapter = new EventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAteHoje();
    }

    public List<Evento> listarTodosAPartirDeHoje(Context context){
        EventoDBAdapter adapter = new EventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAPartirDeHoje();
    }

    public List<Evento> listarTodosPorData(Context context, Calendar data){
        EventoDBAdapter adapter = new EventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorData(data);
    }

    public long salvarOuAtualizar(Context context, Evento evento){
        EventoDBAdapter adapter = new EventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(evento);
    }

    public long deletarInativos(Context context){
        EventoDBAdapter adapter = new EventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public Evento carregar(Context context, Evento evento){
        EventoDBAdapter adapter = new EventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(evento);
    }

    public boolean jaExiste(Context context, Evento evento){
        EventoDBAdapter adapter = new EventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.jaExiste(evento);
    }

}
