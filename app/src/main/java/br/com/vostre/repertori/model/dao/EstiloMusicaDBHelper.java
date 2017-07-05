package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.EstiloMusica;
import br.com.vostre.repertori.model.Projeto;

/**
 * Created by Almir on 24/02/2016.
 */
public class EstiloMusicaDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE estilo_musica (_id text primary key, id_musica integer NOT NULL, id_estilo integer NOT NULL, status integer NOT NULL, " +
            "data_cadastro text, data_recebimento text, ultima_alteracao text, enviado integer NOT NULL);";
    RepDBHelper repDBHelper;

    public EstiloMusicaDBHelper(Context context){
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
        Log.w(EstiloMusicaDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<EstiloMusica> listarTodos(Context context){
        EstiloMusicaDBAdapter adapter = new EstiloMusicaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<EstiloMusica> listarTodosAEnviar(Context context){
        EstiloMusicaDBAdapter adapter = new EstiloMusicaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public List<Musica> listarTodosPorEstilo(Context context, Estilo estilo){
        EstiloMusicaDBAdapter adapter = new EstiloMusicaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorEstilo(estilo);
    }

    public long salvarOuAtualizar(Context context, EstiloMusica musicaEvento){
        EstiloMusicaDBAdapter adapter = new EstiloMusicaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(musicaEvento);
    }

    public long deletarInativos(Context context){
        EstiloMusicaDBAdapter adapter = new EstiloMusicaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public EstiloMusica carregar(Context context, EstiloMusica musicaEvento){
        EstiloMusicaDBAdapter adapter = new EstiloMusicaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(musicaEvento);
    }

}
