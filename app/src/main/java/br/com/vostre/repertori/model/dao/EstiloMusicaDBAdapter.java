package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.EstiloMusica;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class EstiloMusicaDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public EstiloMusicaDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(EstiloMusica estiloMusica){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(estiloMusica.getId() != null){
            cv.put("_id", estiloMusica.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }
        
        cv.put("id_musica", estiloMusica.getMusica().getId());
        cv.put("id_estilo", estiloMusica.getEstilo().getId());
        cv.put("status", estiloMusica.getStatus());
        cv.put("enviado", estiloMusica.getEnviado());

        if(estiloMusica.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(estiloMusica.getDataCadastro()));
        }

        if(estiloMusica.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(estiloMusica.getDataRecebimento()));
        }

        cv.put("ultima_alteracao", DataUtils.dataParaBanco(estiloMusica.getUltimaAlteracao()));

        if(database.update("estilo_musica", cv, "_id = '"+estiloMusica.getId()+"'", null) < 1){
            retorno = database.insert("estilo_musica", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("estilo_musica", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<EstiloMusica> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, id_musica, id_estilo, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM estilo_musica", null);
        List<EstiloMusica> estiloMusicas = new ArrayList<EstiloMusica>();

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            EstiloDBHelper estiloDBHelper = new EstiloDBHelper(context);

            do{
                EstiloMusica umEstiloMusica = new EstiloMusica();
                umEstiloMusica.setId(cursor.getString(0));

                Musica musica = new Musica();
                musica.setId(cursor.getString(1));
                musica = musicaDBHelper.carregar(context, musica);
                umEstiloMusica.setMusica(musica);

                Estilo estilo = new Estilo();
                estilo.setId(cursor.getString(2));
                estilo = estiloDBHelper.carregar(context, estilo);
                umEstiloMusica.setEstilo(estilo);

                umEstiloMusica.setStatus(cursor.getInt(3));

                if(cursor.getString(6) != null){
                    umEstiloMusica.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(7) != null){
                    umEstiloMusica.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }

                umEstiloMusica.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));

                estiloMusicas.add(umEstiloMusica);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return estiloMusicas;
    }

    public List<EstiloMusica> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, id_musica, id_estilo, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM estilo_musica WHERE enviado = -1", null);
        List<EstiloMusica> estiloMusicas = new ArrayList<EstiloMusica>();

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            EstiloDBHelper estiloDBHelper = new EstiloDBHelper(context);

            do{
                EstiloMusica umEstiloMusica = new EstiloMusica();
                umEstiloMusica.setId(cursor.getString(0));

                Musica musica = new Musica();
                musica.setId(cursor.getString(1));
                musica = musicaDBHelper.carregar(context, musica);
                umEstiloMusica.setMusica(musica);

                Estilo estilo = new Estilo();
                estilo.setId(cursor.getString(2));
                estilo = estiloDBHelper.carregar(context, estilo);
                umEstiloMusica.setEstilo(estilo);

                umEstiloMusica.setStatus(cursor.getInt(3));

                if(cursor.getString(6) != null){
                    umEstiloMusica.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(7) != null){
                    umEstiloMusica.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }

                umEstiloMusica.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));

                estiloMusicas.add(umEstiloMusica);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return estiloMusicas;
    }

    public List<Musica> listarTodosPorEstilo(Estilo umEstilo){
        Cursor cursor = database.rawQuery("SELECT id_musica FROM estilo_musica WHERE id_estilo = ? AND status != 2 " +
                "ORDER BY ordem ASC", new String[]{umEstilo.getId()});
        List<Musica> musicas = new ArrayList<Musica>();

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);

            do{

                Musica musica = new Musica();
                musica.setId(cursor.getString(0));
                musica = musicaDBHelper.carregar(context, musica);

                musica.setNome(musica.getNome());

                musicas.add(musica);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicas;
    }

    public EstiloMusica carregar(EstiloMusica estiloMusica){
        Cursor cursor = database.rawQuery("SELECT _id, id_musica, id_estilo, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM estilo_musica WHERE _id = ?",
                new String[]{estiloMusica.getId()});

        EstiloMusica umEstiloMusica = null;

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            EstiloDBHelper estiloDBHelper = new EstiloDBHelper(context);

            do{
                umEstiloMusica = new EstiloMusica();
                umEstiloMusica.setId(cursor.getString(0));

                Musica musica = new Musica();
                musica.setId(cursor.getString(1));
                musica = musicaDBHelper.carregar(context, musica);
                umEstiloMusica.setMusica(musica);

                Estilo estilo = new Estilo();
                estilo.setId(cursor.getString(2));
                estilo = estiloDBHelper.carregar(context, estilo);
                umEstiloMusica.setEstilo(estilo);

                umEstiloMusica.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    umEstiloMusica.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(5) != null){
                    umEstiloMusica.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }

                umEstiloMusica.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umEstiloMusica;
    }

}
