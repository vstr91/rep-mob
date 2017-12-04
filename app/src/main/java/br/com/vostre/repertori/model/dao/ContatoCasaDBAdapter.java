package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Casa;
import br.com.vostre.repertori.model.Contato;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.ContatoCasa;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class ContatoCasaDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public ContatoCasaDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(ContatoCasa contatoCasa){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(contatoCasa.getId() != null){
            cv.put("_id", contatoCasa.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("observacao", contatoCasa.getObservacao());
        cv.put("cargo", contatoCasa.getCargo());
        cv.put("id_contato", contatoCasa.getContato().getId());
        cv.put("id_casa", contatoCasa.getCasa().getId());
        cv.put("status", contatoCasa.getStatus());
        cv.put("enviado", contatoCasa.getEnviado());

        if(contatoCasa.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(contatoCasa.getDataCadastro()));
        }

        if(contatoCasa.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(contatoCasa.getDataRecebimento()));
        }

        if(contatoCasa.getUltimaAlteracao() != null){
            cv.put("ultima_alteracao", DataUtils.dataParaBanco(contatoCasa.getUltimaAlteracao()));
        }

        if(database.update("contato_casa", cv, "_id = '"+contatoCasa.getId()+"'", null) < 1){
            retorno = database.insert("contato_casa", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("contato_casa", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<ContatoCasa> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, cargo, id_contato, id_casa, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM contato_casa", null);
        List<ContatoCasa> contatoCasas = new ArrayList<ContatoCasa>();

        if(cursor.moveToFirst()){

            ContatoDBHelper contatoDBHelper = new ContatoDBHelper(context);
            CasaDBHelper casaDBHelper = new CasaDBHelper(context);

            do{
                ContatoCasa umContatoCasa = new ContatoCasa();
                umContatoCasa.setId(cursor.getString(0));

                umContatoCasa.setObservacao(cursor.getString(1));
                umContatoCasa.setCargo(cursor.getString(2));

                Contato contato = new Contato();
                contato.setId(cursor.getString(3));
                contato = contatoDBHelper.carregar(context, contato);
                umContatoCasa.setContato(contato);

                Casa casa = new Casa();
                casa.setId(cursor.getString(4));
                casa = casaDBHelper.carregar(context, casa);
                umContatoCasa.setCasa(casa);

                umContatoCasa.setStatus(cursor.getInt(5));

                if(cursor.getString(6) != null){
                    umContatoCasa.setDataCadastro(DataUtils.bancoParaData(cursor.getString(6)));
                }

                if(cursor.getString(7) != null){
                    umContatoCasa.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                umContatoCasa.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

                contatoCasas.add(umContatoCasa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return contatoCasas;
    }

    public List<ContatoCasa> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, cargo, id_contato, id_casa, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM contato_casa WHERE enviado = -1", null);
        List<ContatoCasa> contatoCasas = new ArrayList<ContatoCasa>();

        if(cursor.moveToFirst()){

            ContatoDBHelper contatoDBHelper = new ContatoDBHelper(context);
            CasaDBHelper casaDBHelper = new CasaDBHelper(context);

            do{
                ContatoCasa umContatoCasa = new ContatoCasa();
                umContatoCasa.setId(cursor.getString(0));

                umContatoCasa.setObservacao(cursor.getString(1));
                umContatoCasa.setCargo(cursor.getString(2));

                Contato contato = new Contato();
                contato.setId(cursor.getString(3));
                contato = contatoDBHelper.carregar(context, contato);
                umContatoCasa.setContato(contato);

                Casa casa = new Casa();
                casa.setId(cursor.getString(4));
                casa = casaDBHelper.carregar(context, casa);
                umContatoCasa.setCasa(casa);

                umContatoCasa.setStatus(cursor.getInt(5));

                if(cursor.getString(6) != null){
                    umContatoCasa.setDataCadastro(DataUtils.bancoParaData(cursor.getString(6)));
                }

                if(cursor.getString(7) != null){
                    umContatoCasa.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                if(cursor.getString(8) != null){
                    umContatoCasa.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));
                }

                contatoCasas.add(umContatoCasa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return contatoCasas;
    }

    public List<Casa> listarTodosPorContato(Contato contato){
        Cursor cursor = database.rawQuery("SELECT id_casa FROM contato_casa me INNER JOIN casa e ON e._id = me.id_casa WHERE id_contato = ? AND me.status != 2 ORDER BY e.nome DESC",
                new String[]{contato.getId()});
        List<Casa> casas = new ArrayList<Casa>();

        if(cursor.moveToFirst()){

            CasaDBHelper casaDBHelper = new CasaDBHelper(context);

            do{

                Casa casa = new Casa();
                casa.setId(cursor.getString(0));
                casa = casaDBHelper.carregar(context, casa);

                casas.add(casa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return casas;
    }

    public List<Contato> listarTodosPorCasa(Casa casa){
        Cursor cursor = database.rawQuery("SELECT id_contato FROM contato_casa WHERE id_casa = ? AND status != 2 " +
                "ORDER BY id_contato ASC", new String[]{casa.getId()});
        List<Contato> contatos = new ArrayList<Contato>();

        if(cursor.moveToFirst()){

            ContatoDBHelper contatoDBHelper = new ContatoDBHelper(context);

            do{

                Contato contato = new Contato();
                contato.setId(cursor.getString(0));
                contato = contatoDBHelper.carregar(context, contato);

                contatos.add(contato);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return contatos;
    }

    public List<ContatoCasa> listarTodosPorCasa(Casa casa, Boolean isContatoCasa){
        Cursor cursor = database.rawQuery("SELECT _id FROM contato_casa WHERE id_casa = ? AND status != 2 " +
                "ORDER BY _id ASC", new String[]{casa.getId()});
        List<ContatoCasa> contatos = new ArrayList<ContatoCasa>();

        if(cursor.moveToFirst()){

            ContatoCasaDBHelper contatoCasaDBHelper = new ContatoCasaDBHelper(context);

            do{

                ContatoCasa contatoCasa = new ContatoCasa();
                contatoCasa.setId(cursor.getString(0));
                contatoCasa = contatoCasaDBHelper.carregar(context, contatoCasa);

                contatos.add(contatoCasa);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return contatos;
    }

    public List<Contato> listarTodosAusentesCasa(Casa casa){
        Cursor cursor = database.rawQuery("SELECT DISTINCT m._id FROM contato m WHERE m._id NOT IN (" +
                        "SELECT id_contato FROM contato_casa me1 WHERE me1.id_casa = ? AND me1.status != 2" +
                        ") AND m.status IN (0,1) ORDER BY m.nome COLLATE LOCALIZED ASC",
                new String[]{casa.getId()});
        List<Contato> contatos = new ArrayList<Contato>();

        if(cursor.moveToFirst()){

            ContatoDBHelper contatoDBHelper = new ContatoDBHelper(context);

            do{

                Contato contato = new Contato();
                contato.setId(cursor.getString(0));
                contato = contatoDBHelper.carregar(context, contato);

                contatos.add(contato);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return contatos;
    }

    public ContatoCasa carregar(ContatoCasa contatoCasa){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, cargo, id_contato, id_casa, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM contato_casa WHERE _id = ?",
                new String[]{contatoCasa.getId()});

        ContatoCasa umContatoCasa = null;

        if(cursor.moveToFirst()){

            ContatoDBHelper contatoDBHelper = new ContatoDBHelper(context);
            CasaDBHelper casaDBHelper = new CasaDBHelper(context);

            do{
                umContatoCasa = new ContatoCasa();
                umContatoCasa.setId(cursor.getString(0));

                umContatoCasa.setObservacao(cursor.getString(1));
                umContatoCasa.setCargo(cursor.getString(2));

                Contato contato = new Contato();
                contato.setId(cursor.getString(3));
                contato = contatoDBHelper.carregar(context, contato);
                umContatoCasa.setContato(contato);

                Casa casa = new Casa();
                casa.setId(cursor.getString(4));
                casa = casaDBHelper.carregar(context, casa);
                umContatoCasa.setCasa(casa);

                umContatoCasa.setStatus(cursor.getInt(5));

                if(cursor.getString(6) != null){
                    umContatoCasa.setDataCadastro(DataUtils.bancoParaData(cursor.getString(6)));
                }

                if(cursor.getString(7) != null){
                    umContatoCasa.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                umContatoCasa.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umContatoCasa;
    }

}
