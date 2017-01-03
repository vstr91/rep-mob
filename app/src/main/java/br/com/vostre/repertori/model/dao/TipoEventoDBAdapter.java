package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.TipoEvento;
import br.com.vostre.repertori.model.TipoEvento;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class TipoEventoDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public TipoEventoDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(TipoEvento tipoEvento){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(tipoEvento.getId() != null){
            cv.put("_id", tipoEvento.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("nome", tipoEvento.getNome());
        cv.put("slug", tipoEvento.getSlug());
        cv.put("status", tipoEvento.getStatus());

        if(tipoEvento.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(tipoEvento.getDataCadastro()));
        }

        cv.put("data_recebimento", DataUtils.dataParaBanco(tipoEvento.getDataRecebimento()));
        cv.put("ultima_alteracao", DataUtils.dataParaBanco(tipoEvento.getUltimaAlteracao()));

        if(database.update("tipo_evento", cv, "_id = '"+tipoEvento.getId()+"'", null) < 1){
            retorno = database.insert("tipo_evento", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("tipo_evento", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<TipoEvento> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM tipo_evento", null);
        List<TipoEvento> tipoEventos = new ArrayList<TipoEvento>();

        if(cursor.moveToFirst()){
            do{
                TipoEvento umTipoEvento = new TipoEvento();
                umTipoEvento.setId(cursor.getString(0));

                umTipoEvento.setNome(cursor.getString(1));
                umTipoEvento.setStatus(cursor.getInt(2));

                umTipoEvento.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                umTipoEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                umTipoEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umTipoEvento.setSlug(cursor.getString(6));

                tipoEventos.add(umTipoEvento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return tipoEventos;
    }

    public TipoEvento carregar(TipoEvento tipoEvento){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM tipo_evento WHERE _id = ?", new String[]{tipoEvento.getId()});

        TipoEvento umTipoEvento = null;

        if(cursor.moveToFirst()){
            do{
                umTipoEvento = new TipoEvento();
                umTipoEvento.setId(cursor.getString(0));

                umTipoEvento.setNome(cursor.getString(1));
                umTipoEvento.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umTipoEvento.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }


                umTipoEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                umTipoEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umTipoEvento.setSlug(cursor.getString(6));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umTipoEvento;
    }

}
