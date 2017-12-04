package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.repertori.model.Contato;

/**
 * Created by Almir on 24/02/2016.
 */
public class ContatoDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE contato (_id text primary key, " +
            "nome text NOT NULL, " +
            "status integer NOT NULL, telefone text, email text, observacao text, data_cadastro text, data_recebimento text, ultima_alteracao text, enviado integer NOT NULL);";
    RepDBHelper repDBHelper;

    public ContatoDBHelper(Context context){
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
        Log.w(ContatoDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<Contato> listarTodos(Context context){
        ContatoDBAdapter adapter = new ContatoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<Contato> listarTodosAEnviar(Context context){
        ContatoDBAdapter adapter = new ContatoDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public String salvarOuAtualizar(Context context, Contato contato){
        ContatoDBAdapter adapter = new ContatoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(contato);
    }

    public long deletarInativos(Context context){
        ContatoDBAdapter adapter = new ContatoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public Contato carregar(Context context, Contato contato){
        ContatoDBAdapter adapter = new ContatoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(contato);
    }

    public boolean jaExiste(Context context, Contato contato){
        ContatoDBAdapter adapter = new ContatoDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.jaExiste(contato);
    }

}
