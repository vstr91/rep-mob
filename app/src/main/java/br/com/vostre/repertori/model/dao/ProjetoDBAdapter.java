package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class ProjetoDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public ProjetoDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(Projeto projeto){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(projeto.getId() != null){
            cv.put("_id", projeto.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("nome", projeto.getNome());
        cv.put("slug", projeto.getSlug());
        cv.put("status", projeto.getStatus());
        cv.put("enviado", projeto.getEnviado());

        if(projeto.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(projeto.getDataCadastro()));
        }

        if(projeto.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(projeto.getDataRecebimento()));
        }

        cv.put("ultima_alteracao", DataUtils.dataParaBanco(projeto.getUltimaAlteracao()));

        if(database.update("projeto", cv, "_id = '"+projeto.getId()+"'", null) < 1){
            retorno = database.insert("projeto", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("projeto", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<Projeto> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM projeto", null);
        List<Projeto> projetos = new ArrayList<Projeto>();

        if(cursor.moveToFirst()){
            do{
                Projeto umProjeto = new Projeto();
                umProjeto.setId(cursor.getString(0));

                umProjeto.setNome(cursor.getString(1));
                umProjeto.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umProjeto.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umProjeto.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umProjeto.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umProjeto.setSlug(cursor.getString(6));

                projetos.add(umProjeto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return projetos;
    }

    public List<Projeto> listarTodosAtivos(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM projeto WHERE status = 0", null);
        List<Projeto> projetos = new ArrayList<Projeto>();

        if(cursor.moveToFirst()){
            do{
                Projeto umProjeto = new Projeto();
                umProjeto.setId(cursor.getString(0));

                umProjeto.setNome(cursor.getString(1));
                umProjeto.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umProjeto.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umProjeto.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umProjeto.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umProjeto.setSlug(cursor.getString(6));

                projetos.add(umProjeto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return projetos;
    }

    public List<Projeto> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM projeto WHERE enviado = -1", null);
        List<Projeto> projetos = new ArrayList<Projeto>();

        if(cursor.moveToFirst()){
            do{
                Projeto umProjeto = new Projeto();
                umProjeto.setId(cursor.getString(0));

                umProjeto.setNome(cursor.getString(1));
                umProjeto.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umProjeto.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umProjeto.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umProjeto.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umProjeto.setSlug(cursor.getString(6));

                projetos.add(umProjeto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return projetos;
    }

    public Projeto carregar(Projeto projeto){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM projeto WHERE _id = ?", new String[]{projeto.getId()});

        Projeto umProjeto = null;

        if(cursor.moveToFirst()){
            do{
                umProjeto = new Projeto();
                umProjeto.setId(cursor.getString(0));

                umProjeto.setNome(cursor.getString(1));
                umProjeto.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umProjeto.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umProjeto.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umProjeto.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umProjeto.setSlug(cursor.getString(6));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umProjeto;
    }

    public boolean jaExiste(Projeto projeto){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, slug " +
                "FROM projeto WHERE _id = ?", new String[]{projeto.getId()});

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
