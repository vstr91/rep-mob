package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.repertori.model.Projeto;

/**
 * Created by Almir on 24/02/2016.
 */
public class ProjetoDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE projeto (_id text primary key, " +
            "nome text NOT NULL, " +
            "status integer NOT NULL, slug text, data_cadastro text, data_recebimento text, ultima_alteracao text, enviado integer NOT NULL);";
    RepDBHelper repDBHelper;

    public ProjetoDBHelper(Context context){
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
        Log.w(ProjetoDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<Projeto> listarTodos(Context context){
        ProjetoDBAdapter adapter = new ProjetoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<Projeto> listarTodosAtivos(Context context){
        ProjetoDBAdapter adapter = new ProjetoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAtivos();
    }

    public List<Projeto> listarTodosAEnviar(Context context){
        ProjetoDBAdapter adapter = new ProjetoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public long salvarOuAtualizar(Context context, Projeto projeto){
        ProjetoDBAdapter adapter = new ProjetoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(projeto);
    }

    public long deletarInativos(Context context){
        ProjetoDBAdapter adapter = new ProjetoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public Projeto carregar(Context context, Projeto projeto){
        ProjetoDBAdapter adapter = new ProjetoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(projeto);
    }

    public boolean jaExiste(Context context, Projeto projeto){
        ProjetoDBAdapter adapter = new ProjetoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.jaExiste(projeto);
    }

}
