package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Contato;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class ContatoDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public ContatoDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public String salvarOuAtualizar(Contato contato){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(contato.getId() != null){
            cv.put("_id", contato.getId());
        } else{
            String id = UUID.randomUUID().toString();
            cv.put("_id", id);
            contato.setId(id);
        }

        cv.put("nome", contato.getNome());
        cv.put("telefone", contato.getTelefone());
        cv.put("email", contato.getEmail());
        cv.put("observacao", contato.getObservacao());
        cv.put("status", contato.getStatus());
        cv.put("enviado", contato.getEnviado());

        if(contato.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(contato.getDataCadastro()));
        }

        if(contato.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(contato.getDataRecebimento()));
        }

        cv.put("ultima_alteracao", DataUtils.dataParaBanco(contato.getUltimaAlteracao()));

        if(database.update("contato", cv, "_id = '"+contato.getId()+"'", null) < 1){
            retorno = database.insert("contato", null, cv);
            database.close();
            return contato.getId();
        } else{
            database.close();
            return "";
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("contato", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<Contato> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, telefone, email, observacao " +
                "FROM contato ORDER BY nome COLLATE NOCASE", null);
        List<Contato> contatos = new ArrayList<Contato>();

        if(cursor.moveToFirst()){
            do{
                Contato umContato = new Contato();
                umContato.setId(cursor.getString(0));

                umContato.setNome(cursor.getString(1));
                umContato.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umContato.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umContato.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umContato.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umContato.setTelefone(cursor.getString(6));
                umContato.setEmail(cursor.getString(7));
                umContato.setObservacao(cursor.getString(8));

                contatos.add(umContato);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return contatos;
    }

    public List<Contato> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, telefone, email, observacao " +
                "FROM contato WHERE enviado = -1", null);
        List<Contato> contatos = new ArrayList<Contato>();

        if(cursor.moveToFirst()){
            do{
                Contato umContato = new Contato();
                umContato.setId(cursor.getString(0));

                umContato.setNome(cursor.getString(1));
                umContato.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umContato.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umContato.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umContato.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umContato.setTelefone(cursor.getString(6));
                umContato.setEmail(cursor.getString(7));
                umContato.setObservacao(cursor.getString(8));

                contatos.add(umContato);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return contatos;
    }

    public Contato carregar(Contato contato){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, telefone, email, observacao " +
                "FROM contato WHERE _id = ?", new String[]{contato.getId()});

        Contato umContato = null;

        if(cursor.moveToFirst()){
            do{
                umContato = new Contato();
                umContato.setId(cursor.getString(0));

                umContato.setNome(cursor.getString(1));
                umContato.setStatus(cursor.getInt(2));

                if(cursor.getString(3) != null){
                    umContato.setDataCadastro(DataUtils.bancoParaData(cursor.getString(3)));
                }

                if(cursor.getString(4) != null){
                    umContato.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umContato.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(5)));
                umContato.setTelefone(cursor.getString(6));
                umContato.setEmail(cursor.getString(7));
                umContato.setObservacao(cursor.getString(8));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umContato;
    }

    public boolean jaExiste(Contato contato){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, data_recebimento, ultima_alteracao, telefone " +
                "FROM contato WHERE telefone = ? AND _id != ?", new String[]{contato.getTelefone(), contato.getId() == null ? "-1" : contato.getId()});

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
