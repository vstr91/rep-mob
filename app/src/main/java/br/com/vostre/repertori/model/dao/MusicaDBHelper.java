package br.com.vostre.repertori.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Musica;

/**
 * Created by Almir on 24/02/2016.
 */
public class MusicaDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION = RepDBHelper.DBVERSION;
    private static final String DBNAME = RepDBHelper.DBNAME;
    public static final String DBCREATE = "CREATE TABLE musica (_id text primary key, nome text NOT NULL, " +
            "tom text NOT NULL, id_artista integer NOT NULL, status integer NOT NULL, data_cadastro text, " +
            "data_recebimento text, ultima_alteracao text, slug text, enviado integer NOT NULL, id_estilo integer NOT NULL);";
    RepDBHelper repDBHelper;

    public MusicaDBHelper(Context context){
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
        Log.w(MusicaDBHelper.class.getName(), "Atualizacaoo da Versao " + oldVersion + " para a Versao " + newVersion);
        //db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        //onCreate(db);
    }

    public List<Musica> listarTodos(Context context){
        MusicaDBAdapter adapter = new MusicaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodos();
    }

    public List<Musica> listarTodosPorSituacao(Context context, int situacao){
        MusicaDBAdapter adapter = new MusicaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosPorSituacao(situacao);
    }

    public HashMap<Integer, Artista> contarTodosPorArtista(Context context){
        MusicaDBAdapter adapter = new MusicaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.contarTodosPorArtista();
    }

    public List<Musica> listarTodosAEnviar(Context context){
        MusicaDBAdapter adapter = new MusicaDBAdapter(context, repDBHelper.getReadableDatabase());
        return adapter.listarTodosAEnviar();
    }

    public long salvarOuAtualizar(Context context, Musica musica){
        MusicaDBAdapter adapter = new MusicaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.salvarOuAtualizar(musica);
    }

    public long deletarInativos(Context context){
        MusicaDBAdapter adapter = new MusicaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.deletarInativos();
    }

    public Musica carregar(Context context, Musica musica){
        MusicaDBAdapter adapter = new MusicaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.carregar(musica);
    }

    public boolean jaExiste(Context context, Musica musica){
        MusicaDBAdapter adapter = new MusicaDBAdapter(context, repDBHelper.getWritableDatabase());
        return adapter.jaExiste(musica);
    }

}
