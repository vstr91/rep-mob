package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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
        }

        cv.put("id_remoto", artista.getIdRemoto());
        cv.put("nome", artista.getNome());
        cv.put("status", artista.getStatus());
        cv.put("data_cadastro", DataUtils.dataParaBanco(artista.getDataCadastro()));
        cv.put("data_recebimento", DataUtils.dataParaBanco(artista.getDataRecebimento()));
        cv.put("ultima_alteracao", DataUtils.dataParaBanco(artista.getUltimaAlteracao()));

        if(database.update("artista", cv, "id_remoto = "+artista.getIdRemoto(), null) < 1){
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
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, id_remoto " +
                "FROM artista", null);
        List<Artista> artistas = new ArrayList<Artista>();

        if(cursor.moveToFirst()){
            do{
                Artista umArtista = new Artista();
                umArtista.setId(cursor.getInt(0));

                umArtista.setNome(cursor.getString(1));
                umArtista.setStatus(cursor.getInt(2));

                umArtista.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                umArtista.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                umArtista.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umArtista.setIdRemoto(cursor.getInt(6));

                artistas.add(umArtista);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return artistas;
    }

    public Artista carregar(Artista artista){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, id_remoto " +
                "FROM artista WHERE id_remoto = ?", new String[]{String.valueOf(artista.getIdRemoto())});

        Artista umArtista = null;

        if(cursor.moveToFirst()){
            do{
                umArtista = new Artista();
                umArtista.setId(cursor.getInt(0));

                umArtista.setNome(cursor.getString(1));
                umArtista.setStatus(cursor.getInt(2));

                umArtista.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                umArtista.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                umArtista.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umArtista.setIdRemoto(cursor.getInt(6));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umArtista;
    }

}
