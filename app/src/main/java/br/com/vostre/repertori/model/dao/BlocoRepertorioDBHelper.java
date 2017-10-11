package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaRepertorio;
import br.com.vostre.repertori.model.Repertorio;

/**
 * Created by Almir on 24/02/2016.
 */
public class BlocoRepertorioDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE bloco_repertorio (_id text primary key, nome text NOT NULL, " +
            "ordem integer NOT NULL, id_repertorio integer NOT NULL, status integer NOT NULL, " +
            "data_cadastro text, data_recebimento text, ultima_alteracao text, enviado integer NOT NULL);";
    RepDBHelper repDBHelper;

    public BlocoRepertorioDBHelper(Context context){
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
        Log.w(BlocoRepertorioDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<BlocoRepertorio> listarTodos(Context context){
        BlocoRepertorioDBAdapter adapter = new BlocoRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<BlocoRepertorio> listarTodosAEnviar(Context context){
        BlocoRepertorioDBAdapter adapter = new BlocoRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public List<BlocoRepertorio> listarTodosPorRepertorio(Context context, Repertorio repertorio, int situacao){
        BlocoRepertorioDBAdapter adapter = new BlocoRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorRepertorio(repertorio, situacao);
    }

    public List<BlocoRepertorio> corrigirOrdemPorRepertorio(Context context, Repertorio repertorio){
        BlocoRepertorioDBAdapter adapter = new BlocoRepertorioDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.corrigirOrdemPorRepertorio(repertorio);
    }

    public long salvarOuAtualizar(Context context, BlocoRepertorio blocoRepertorio){
        BlocoRepertorioDBAdapter adapter = new BlocoRepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(blocoRepertorio);
    }

    public long deletarInativos(Context context){
        BlocoRepertorioDBAdapter adapter = new BlocoRepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public BlocoRepertorio carregar(Context context, BlocoRepertorio blocoRepertorio){
        BlocoRepertorioDBAdapter adapter = new BlocoRepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(blocoRepertorio);
    }

    public boolean jaExiste(Context context, BlocoRepertorio blocoRepertorio){
        BlocoRepertorioDBAdapter adapter = new BlocoRepertorioDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.jaExiste(blocoRepertorio);
    }

}
