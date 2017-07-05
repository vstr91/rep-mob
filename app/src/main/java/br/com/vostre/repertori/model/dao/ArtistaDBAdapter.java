package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class ArtistaDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public ArtistaDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(Artista artista){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(artista.getId() != null){
            cv.put("_id", artista.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("nome", artista.getNome());
        cv.put("slug", artista.getSlug());
        cv.put("status", artista.getStatus());
        cv.put("enviado", artista.getEnviado());

        if(artista.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(artista.getDataCadastro()));
        }

        if(artista.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(artista.getDataRecebimento()));
        }

        cv.put("ultima_alteracao", DataUtils.dataParaBanco(artista.getUltimaAlteracao()));

        if(database.update("artista", cv, "_id = '"+artista.getId()+"'", null) < 1){
            retorno = database.insert("artista", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("artista", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<Artista> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM artista ORDER BY nome COLLATE NOCASE", null);
        List<Artista> artistas = new ArrayList<Artista>();

        if(cursor.moveToFirst()){
            do{
                Artista umArtista = new Artista();
                umArtista.setId(cursor.getString(0));

                umArtista.setNome(cursor.getString(1));
                umArtista.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umArtista.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umArtista.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umArtista.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umArtista.setSlug(cursor.getString(6));

                artistas.add(umArtista);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return artistas;
    }

    public List<Artista> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM artista WHERE enviado = -1", null);
        List<Artista> artistas = new ArrayList<Artista>();

        if(cursor.moveToFirst()){
            do{
                Artista umArtista = new Artista();
                umArtista.setId(cursor.getString(0));

                umArtista.setNome(cursor.getString(1));
                umArtista.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umArtista.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umArtista.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umArtista.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umArtista.setSlug(cursor.getString(6));

                artistas.add(umArtista);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return artistas;
    }

    public Artista carregar(Artista artista){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM artista WHERE _id = ?", new String[]{artista.getId()});

        Artista umArtista = null;

        if(cursor.moveToFirst()){
            do{
                umArtista = new Artista();
                umArtista.setId(cursor.getString(0));

                umArtista.setNome(cursor.getString(1));
                umArtista.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umArtista.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umArtista.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umArtista.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umArtista.setSlug(cursor.getString(6));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umArtista;
    }

    public boolean jaExiste(Artista artista){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM artista WHERE nome = ? AND _id != ?", new String[]{artista.getNome(), artista.getId() == null ? "-1" : artista.getId()});

        if(cursor.moveToFirst()){
            cursor.close();
            database.close();
            return true;
        } else{
            cursor.close();
            database.close();
            return false;
        }

    }

}
