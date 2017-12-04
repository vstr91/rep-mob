package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Casa;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class CasaDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public CasaDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(Casa casa){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(casa.getId() != null){
            cv.put("_id", casa.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("nome", casa.getNome());
        cv.put("slug", casa.getSlug());
        cv.put("status", casa.getStatus());
        cv.put("enviado", casa.getEnviado());

        if(casa.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(casa.getDataCadastro()));
        }

        if(casa.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(casa.getDataRecebimento()));
        }

        cv.put("ultima_alteracao", DataUtils.dataParaBanco(casa.getUltimaAlteracao()));

        if(database.update("casa", cv, "_id = '"+casa.getId()+"'", null) < 1){
            retorno = database.insert("casa", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("casa", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<Casa> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM casa ORDER BY nome COLLATE NOCASE", null);
        List<Casa> casas = new ArrayList<Casa>();

        if(cursor.moveToFirst()){
            do{
                Casa umCasa = new Casa();
                umCasa.setId(cursor.getString(0));

                umCasa.setNome(cursor.getString(1));
                umCasa.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umCasa.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umCasa.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umCasa.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umCasa.setSlug(cursor.getString(6));

                casas.add(umCasa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return casas;
    }

    public List<Casa> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM casa WHERE enviado = -1", null);
        List<Casa> casas = new ArrayList<Casa>();

        if(cursor.moveToFirst()){
            do{
                Casa umCasa = new Casa();
                umCasa.setId(cursor.getString(0));

                umCasa.setNome(cursor.getString(1));
                umCasa.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umCasa.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umCasa.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umCasa.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umCasa.setSlug(cursor.getString(6));

                casas.add(umCasa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return casas;
    }

    public Casa carregar(Casa casa){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM casa WHERE _id = ?", new String[]{casa.getId()});

        Casa umCasa = null;

        if(cursor.moveToFirst()){
            do{
                umCasa = new Casa();
                umCasa.setId(cursor.getString(0));

                umCasa.setNome(cursor.getString(1));
                umCasa.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umCasa.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umCasa.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umCasa.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umCasa.setSlug(cursor.getString(6));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umCasa;
    }

    public boolean jaExiste(Casa casa){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM casa WHERE nome = ? AND _id != ?", new String[]{casa.getNome(), casa.getId() == null ? "-1" : casa.getId()});

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
