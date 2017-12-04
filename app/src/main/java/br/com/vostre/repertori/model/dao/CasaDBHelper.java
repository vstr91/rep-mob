package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Casa;

/**
 * Created by Almir on 24/02/2016.
 */
public class CasaDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE casa (_id text primary key, " +
            "nome text NOT NULL, " +
            "status integer NOT NULL, slug text, data_cadastro text, data_recebimento text, ultima_alteracao text, enviado integer NOT NULL);";
    RepDBHelper repDBHelper;

    public CasaDBHelper(Context context){
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
        Log.w(CasaDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<Casa> listarTodos(Context context){
        CasaDBAdapter adapter = new CasaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<Casa> listarTodosAEnviar(Context context){
        CasaDBAdapter adapter = new CasaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public long salvarOuAtualizar(Context context, Casa casa){
        CasaDBAdapter adapter = new CasaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(casa);
    }

    public long deletarInativos(Context context){
        CasaDBAdapter adapter = new CasaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public Casa carregar(Context context, Casa casa){
        CasaDBAdapter adapter = new CasaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(casa);
    }

    public boolean jaExiste(Context context, Casa casa){
        CasaDBAdapter adapter = new CasaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.jaExiste(casa);
    }

}
