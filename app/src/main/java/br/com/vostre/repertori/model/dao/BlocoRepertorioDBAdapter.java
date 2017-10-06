package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class BlocoRepertorioDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public BlocoRepertorioDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(BlocoRepertorio blocoRepertorio){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(blocoRepertorio.getId() != null){
            cv.put("_id", blocoRepertorio.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("nome", blocoRepertorio.getNome());
        cv.put("ordem", blocoRepertorio.getOrdem());
        cv.put("id_repertorio", blocoRepertorio.getRepertorio().getId());
        cv.put("status", blocoRepertorio.getStatus());
        cv.put("enviado", blocoRepertorio.getEnviado());

        if(blocoRepertorio.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(blocoRepertorio.getDataCadastro()));
        }

        if(blocoRepertorio.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(blocoRepertorio.getDataRecebimento()));
        }

        cv.put("ultima_alteracao", DataUtils.dataParaBanco(blocoRepertorio.getUltimaAlteracao()));

        if(database.update("bloco_repertorio", cv, "_id = '"+blocoRepertorio.getId()+"'", null) < 1){
            retorno = database.insert("bloco_repertorio", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("bloco_repertorio", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<BlocoRepertorio> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, ordem, id_repertorio, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM bloco_repertorio", null);
        List<BlocoRepertorio> blocoRepertorios = new ArrayList<BlocoRepertorio>();

        if(cursor.moveToFirst()){
            RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(context);

            do{
                BlocoRepertorio umBlocoRepertorio = new BlocoRepertorio();
                umBlocoRepertorio.setId(cursor.getString(0));

                umBlocoRepertorio.setNome(cursor.getString(1));
                umBlocoRepertorio.setOrdem(cursor.getInt(2));

                Repertorio repertorio = new Repertorio();
                repertorio.setId(cursor.getString(3));
                repertorio = repertorioDBHelper.carregar(context, repertorio);
                umBlocoRepertorio.setRepertorio(repertorio);

                umBlocoRepertorio.setStatus(cursor.getInt(4));

                if(cursor.getString(5) != null){
                    umBlocoRepertorio.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                }

                if(cursor.getString(6) != null){
                    umBlocoRepertorio.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                }

                umBlocoRepertorio.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));

                blocoRepertorios.add(umBlocoRepertorio);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return blocoRepertorios;
    }

    public List<BlocoRepertorio> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, ordem, id_repertorio, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM bloco_repertorio WHERE enviado = -1", null);
        List<BlocoRepertorio> blocoRepertorios = new ArrayList<BlocoRepertorio>();

        if(cursor.moveToFirst()){

            RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(context);

            do{
                BlocoRepertorio umBlocoRepertorio = new BlocoRepertorio();
                umBlocoRepertorio.setId(cursor.getString(0));

                umBlocoRepertorio.setNome(cursor.getString(1));
                umBlocoRepertorio.setOrdem(cursor.getInt(2));

                Repertorio repertorio = new Repertorio();
                repertorio.setId(cursor.getString(3));
                repertorio = repertorioDBHelper.carregar(context, repertorio);
                umBlocoRepertorio.setRepertorio(repertorio);

                umBlocoRepertorio.setStatus(cursor.getInt(4));

                if(cursor.getString(5) != null){
                    umBlocoRepertorio.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                }

                if(cursor.getString(6) != null){
                    umBlocoRepertorio.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                }

                umBlocoRepertorio.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));

                blocoRepertorios.add(umBlocoRepertorio);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return blocoRepertorios;
    }

    public List<BlocoRepertorio> listarTodosPorRepertorio(Repertorio umRepertorio, int situacao){
        Cursor cursor = database.rawQuery("SELECT _id, nome, ordem, id_repertorio, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM bloco_repertorio WHERE id_repertorio = ? AND status = ? " +
                "ORDER BY ordem ASC", new String[]{umRepertorio.getId(), String.valueOf(situacao)});
        List<BlocoRepertorio> blocoRepertorios = new ArrayList<BlocoRepertorio>();

        if(cursor.moveToFirst()){

            RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(context);

            do{
                BlocoRepertorio umBlocoRepertorio = new BlocoRepertorio();
                umBlocoRepertorio.setId(cursor.getString(0));

                umBlocoRepertorio.setNome(cursor.getString(1));
                umBlocoRepertorio.setOrdem(cursor.getInt(2));

                Repertorio repertorio = new Repertorio();
                repertorio.setId(cursor.getString(3));
                repertorio = repertorioDBHelper.carregar(context, repertorio);
                umBlocoRepertorio.setRepertorio(repertorio);

                umBlocoRepertorio.setStatus(cursor.getInt(4));

                if(cursor.getString(5) != null){
                    umBlocoRepertorio.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                }

                if(cursor.getString(6) != null){
                    umBlocoRepertorio.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                }

                umBlocoRepertorio.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));

                blocoRepertorios.add(umBlocoRepertorio);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return blocoRepertorios;
    }

    public List<BlocoRepertorio> corrigirOrdemPorRepertorio(Repertorio umRepertorio){
        Cursor cursor = database.rawQuery("SELECT _id, nome, ordem, id_repertorio, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM bloco_repertorio WHERE id_repertorio = ? AND status != 2 " +
                "ORDER BY ordem ASC", new String[]{umRepertorio.getId()});
        List<BlocoRepertorio> blocoRepertorios = new ArrayList<BlocoRepertorio>();

        if(cursor.moveToFirst()){

            BlocoRepertorioDBHelper blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(context);

            do{

                BlocoRepertorio blocoRepertorio = new BlocoRepertorio();
                blocoRepertorio.setId(cursor.getString(0));
                blocoRepertorio = blocoRepertorioDBHelper.carregar(context, blocoRepertorio);

                blocoRepertorios.add(blocoRepertorio);
            } while (cursor.moveToNext());

        }

        cursor.close();
        database.close();

        return blocoRepertorios;
    }

    public BlocoRepertorio carregar(BlocoRepertorio blocoRepertorio){
        Cursor cursor = database.rawQuery("SELECT _id, nome, ordem, id_repertorio, status, data_cadastro, " +
                        "data_recebimento, ultima_alteracao FROM bloco_repertorio WHERE _id = ?",
                new String[]{blocoRepertorio.getId()});

        BlocoRepertorio umBlocoRepertorio = null;

        if(cursor.moveToFirst()){

            RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(context);

            do{
                umBlocoRepertorio = new BlocoRepertorio();
                umBlocoRepertorio.setId(cursor.getString(0));

                umBlocoRepertorio.setNome(cursor.getString(1));
                umBlocoRepertorio.setOrdem(cursor.getInt(2));

                Repertorio repertorio = new Repertorio();
                repertorio.setId(cursor.getString(3));
                repertorio = repertorioDBHelper.carregar(context, repertorio);
                umBlocoRepertorio.setRepertorio(repertorio);

                umBlocoRepertorio.setStatus(cursor.getInt(4));

                if(cursor.getString(5) != null){
                    umBlocoRepertorio.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                }

                if(cursor.getString(6) != null){
                    umBlocoRepertorio.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                }

                umBlocoRepertorio.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umBlocoRepertorio;
    }

}
