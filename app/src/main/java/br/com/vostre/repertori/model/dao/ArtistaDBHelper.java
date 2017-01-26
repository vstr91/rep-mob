package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.repertori.model.Artista;

/**
 * Created by Almir on 24/02/2016.
 */
public class ArtistaDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE artista (_id text primary key, " +
            "nome text NOT NULL, " +
            "status integer NOT NULL, slug text, data_cadastro text, data_recebimento text, ultima_alteracao text, enviado integer NOT NULL);";
    RepDBHelper repDBHelper;

    public ArtistaDBHelper(Context context){
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
        Log.w(ArtistaDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<Artista> listarTodos(Context context){
        ArtistaDBAdapter adapter = new ArtistaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<Artista> listarTodosAEnviar(Context context){
        ArtistaDBAdapter adapter = new ArtistaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public long salvarOuAtualizar(Context context, Artista artista){
        ArtistaDBAdapter adapter = new ArtistaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(artista);
    }

    public long deletarInativos(Context context){
        ArtistaDBAdapter adapter = new ArtistaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public Artista carregar(Context context, Artista artista){
        ArtistaDBAdapter adapter = new ArtistaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(artista);
    }

    public boolean jaExiste(Context context, Artista artista){
        ArtistaDBAdapter adapter = new ArtistaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.jaExiste(artista);
    }

}
