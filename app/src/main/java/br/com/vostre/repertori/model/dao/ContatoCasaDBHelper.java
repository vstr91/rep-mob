package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;
import java.util.Map;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Casa;
import br.com.vostre.repertori.model.Contato;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.ContatoCasa;

/**
 * Created by Almir on 24/02/2016.
 */
public class ContatoCasaDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE contato_casa (_id text primary key, observacao text, " +
            "cargo text, id_contato integer NOT NULL, id_casa integer NOT NULL, status integer NOT NULL, " +
            "data_cadastro text, data_recebimento text, ultima_alteracao text, enviado integer NOT NULL);";
    RepDBHelper repDBHelper;

    public ContatoCasaDBHelper(Context context){
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
        Log.w(ContatoCasaDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<ContatoCasa> listarTodos(Context context){
        ContatoCasaDBAdapter adapter = new ContatoCasaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<ContatoCasa> listarTodosAEnviar(Context context){
        ContatoCasaDBAdapter adapter = new ContatoCasaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public List<Casa> listarTodosPorContato(Context context, Contato contato){
        ContatoCasaDBAdapter adapter = new ContatoCasaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorContato(contato);
    }

    public List<Contato> listarTodosPorCasa(Context context, Casa casa){
        ContatoCasaDBAdapter adapter = new ContatoCasaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorCasa(casa);
    }

    public List<ContatoCasa> listarTodosPorCasa(Context context, Casa casa, Boolean contatoCasa){
        ContatoCasaDBAdapter adapter = new ContatoCasaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorCasa(casa, true);
    }

    public List<Contato> listarTodosAusentesCasa(Context context, Casa casa){
        ContatoCasaDBAdapter adapter = new ContatoCasaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAusentesCasa(casa);
    }

    public long salvarOuAtualizar(Context context, ContatoCasa contatoCasa){
        ContatoCasaDBAdapter adapter = new ContatoCasaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(contatoCasa);
    }

    public long deletarInativos(Context context){
        ContatoCasaDBAdapter adapter = new ContatoCasaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public ContatoCasa carregar(Context context, ContatoCasa contatoCasa){
        ContatoCasaDBAdapter adapter = new ContatoCasaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(contatoCasa);
    }

}
