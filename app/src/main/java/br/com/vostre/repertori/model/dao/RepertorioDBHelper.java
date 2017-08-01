package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.Projeto;

/**
 * Created by Almir on 24/02/2016.
 */
public class RepertorioDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE repertorio (_id text primary key, nome text NOT NULL, " +
            " id_projeto integer NOT NULL, status integer NOT NULL, data_cadastro text, " +
            "data_recebimento text, ultima_alteracao text, slug text, enviado integer NOT NULL);";
    RepDBHelper repDBHelper;

    public RepertorioDBHelper(Context context){
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
        Log.w(RepertorioDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<Repertorio> listarTodos(Context context){
        RepertorioDBAdapter adapter = new RepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<Repertorio> listarTodosAEnviar(Context context){
        RepertorioDBAdapter adapter = new RepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public List<Repertorio> listarTodosPorProjeto(Context context, Projeto projeto){
        RepertorioDBAdapter adapter = new RepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorProjeto(projeto);
    }

    public long salvarOuAtualizar(Context context, Repertorio repertorio){
        RepertorioDBAdapter adapter = new RepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(repertorio);
    }

    public long deletarInativos(Context context){
        RepertorioDBAdapter adapter = new RepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public Repertorio carregar(Context context, Repertorio repertorio){
        RepertorioDBAdapter adapter = new RepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(repertorio);
    }

}
