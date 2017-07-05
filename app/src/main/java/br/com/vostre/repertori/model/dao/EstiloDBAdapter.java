package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class EstiloDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public EstiloDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(Estilo estilo){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(estilo.getId() != null){
            cv.put("_id", estilo.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("nome", estilo.getNome());
        cv.put("slug", estilo.getSlug());
        cv.put("status", estilo.getStatus());
        cv.put("enviado", estilo.getEnviado());

        if(estilo.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(estilo.getDataCadastro()));
        }

        if(estilo.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(estilo.getDataRecebimento()));
        }

        cv.put("ultima_alteracao", DataUtils.dataParaBanco(estilo.getUltimaAlteracao()));

        if(database.update("estilo", cv, "_id = '"+estilo.getId()+"'", null) < 1){
            retorno = database.insert("estilo", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("estilo", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<Estilo> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM estilo ORDER BY nome COLLATE NOCASE", null);
        List<Estilo> estilos = new ArrayList<Estilo>();

        if(cursor.moveToFirst()){
            do{
                Estilo umEstilo = new Estilo();
                umEstilo.setId(cursor.getString(0));

                umEstilo.setNome(cursor.getString(1));
                umEstilo.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umEstilo.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umEstilo.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umEstilo.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umEstilo.setSlug(cursor.getString(6));

                estilos.add(umEstilo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return estilos;
    }

    public List<Estilo> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM estilo WHERE enviado = -1", null);
        List<Estilo> estilos = new ArrayList<Estilo>();

        if(cursor.moveToFirst()){
            do{
                Estilo umEstilo = new Estilo();
                umEstilo.setId(cursor.getString(0));

                umEstilo.setNome(cursor.getString(1));
                umEstilo.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umEstilo.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umEstilo.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umEstilo.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umEstilo.setSlug(cursor.getString(6));

                estilos.add(umEstilo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return estilos;
    }

    public Estilo carregar(Estilo estilo){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM estilo WHERE _id = ?", new String[]{estilo.getId()});

        Estilo umEstilo = null;

        if(cursor.moveToFirst()){
            do{
                umEstilo = new Estilo();
                umEstilo.setId(cursor.getString(0));

                umEstilo.setNome(cursor.getString(1));
                umEstilo.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umEstilo.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umEstilo.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umEstilo.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umEstilo.setSlug(cursor.getString(6));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umEstilo;
    }

    public boolean jaExiste(Estilo estilo){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM estilo WHERE nome = ? AND _id != ?", new String[]{estilo.getNome(), estilo.getId() == null ? "-1" : estilo.getId()});

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
