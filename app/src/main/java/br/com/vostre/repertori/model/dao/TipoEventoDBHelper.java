package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.repertori.model.TipoEvento;

/**
 * Created by Almir on 24/02/2016.
 */
public class TipoEventoDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE tipo_evento (_id text primary key, nome text NOT NULL, " +
            "status integer NOT NULL, data_cadastro text, data_recebimento text, ultima_alteracao text, slug text, cor text NOT NULL);";
    RepDBHelper repDBHelper;

    public TipoEventoDBHelper(Context context){
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
        Log.w(TipoEventoDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<TipoEvento> listarTodos(Context context){
        TipoEventoDBAdapter adapter = new TipoEventoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public long salvarOuAtualizar(Context context, TipoEvento tipoEvento){
        TipoEventoDBAdapter adapter = new TipoEventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(tipoEvento);
    }

    public long deletarInativos(Context context){
        TipoEventoDBAdapter adapter = new TipoEventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public TipoEvento carregar(Context context, TipoEvento tipoEvento){
        TipoEventoDBAdapter adapter = new TipoEventoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(tipoEvento);
    }

}
