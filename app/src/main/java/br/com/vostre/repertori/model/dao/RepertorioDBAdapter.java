package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class RepertorioDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public RepertorioDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(Repertorio repertorio){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(repertorio.getId() != null){
            cv.put("_id", repertorio.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("nome", repertorio.getNome());
        cv.put("slug", repertorio.getSlug());
        cv.put("id_projeto", repertorio.getProjeto().getId());
        cv.put("status", repertorio.getStatus());
        cv.put("enviado", repertorio.getEnviado());

        if(repertorio.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(repertorio.getDataCadastro()));
        }

        if(repertorio.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(repertorio.getDataRecebimento()));
        }

        cv.put("ultima_alteracao", DataUtils.dataParaBanco(repertorio.getUltimaAlteracao()));

        if(database.update("repertorio", cv, "_id = '"+repertorio.getId()+"'", null) < 1){
            retorno = database.insert("repertorio", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("repertorio", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<Repertorio> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, id_projeto, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, slug FROM repertorio", null);
        List<Repertorio> repertorios = new ArrayList<Repertorio>();

        if(cursor.moveToFirst()){

            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(context);

            do{
                Repertorio umRepertorio = new Repertorio();
                umRepertorio.setId(cursor.getString(0));

                umRepertorio.setNome(cursor.getString(1));

                Projeto projeto = new Projeto();
                projeto.setId(cursor.getString(2));
                projeto = projetoDBHelper.carregar(context, projeto);
                umRepertorio.setProjeto(projeto);

                umRepertorio.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    umRepertorio.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(5) != null){
                    umRepertorio.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }


                umRepertorio.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));
                umRepertorio.setSlug(cursor.getString(7));

                repertorios.add(umRepertorio);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return repertorios;
    }

    public List<Repertorio> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, slug, id_projeto FROM repertorio WHERE enviado = -1", null);
        List<Repertorio> repertorios = new ArrayList<Repertorio>();

        if(cursor.moveToFirst()){

            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(context);

            do{
                Repertorio umRepertorio = new Repertorio();
                umRepertorio.setId(cursor.getString(0));

                umRepertorio.setNome(cursor.getString(1));

                umRepertorio.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umRepertorio.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umRepertorio.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }


                umRepertorio.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umRepertorio.setSlug(cursor.getString(6));

                Projeto projeto = new Projeto();
                projeto.setId(cursor.getString(7));
                projeto = projetoDBHelper.carregar(context, projeto);
                umRepertorio.setProjeto(projeto);

                repertorios.add(umRepertorio);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return repertorios;
    }

    public List<Repertorio> listarTodosPorProjeto(Projeto projeto){
        Cursor cursor = database.rawQuery("SELECT _id, nome, id_projeto, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, slug FROM repertorio WHERE id_projeto = ?", new String[]{projeto.getId()});
        List<Repertorio> repertorios = new ArrayList<Repertorio>();

        if(cursor.moveToFirst()){

            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(context);

            do{
                Repertorio umRepertorio = new Repertorio();
                umRepertorio.setId(cursor.getString(0));

                umRepertorio.setNome(cursor.getString(1));

                Projeto umProjeto = new Projeto();
                umProjeto.setId(cursor.getString(2));
                umProjeto = projetoDBHelper.carregar(context, umProjeto);
                umRepertorio.setProjeto(umProjeto);

                umRepertorio.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    umRepertorio.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(5) != null){
                    umRepertorio.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }


                umRepertorio.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));
                umRepertorio.setSlug(cursor.getString(7));

                repertorios.add(umRepertorio);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return repertorios;
    }

    public Repertorio carregar(Repertorio repertorio){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, slug, id_projeto FROM repertorio WHERE _id = ?", new String[]{repertorio.getId()});

        Repertorio umRepertorio = null;

        if(cursor.moveToFirst()){

            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(context);

            do{
                umRepertorio = new Repertorio();
                umRepertorio.setId(cursor.getString(0));

                umRepertorio.setNome(cursor.getString(1));

                umRepertorio.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umRepertorio.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umRepertorio.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umRepertorio.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umRepertorio.setSlug(cursor.getString(6));

                Projeto projeto = new Projeto();
                projeto.setId(cursor.getString(7));
                projeto = projetoDBHelper.carregar(context, projeto);
                umRepertorio.setProjeto(projeto);

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umRepertorio;
    }

}
