package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.ComentarioEvento;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class ComentarioEventoDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public ComentarioEventoDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(ComentarioEvento comentarioEvento){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(comentarioEvento.getId() != null){
            cv.put("_id", comentarioEvento.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("texto", comentarioEvento.getTexto());
        cv.put("id_evento", comentarioEvento.getEvento().getId());
        cv.put("status", comentarioEvento.getStatus());

        if(comentarioEvento.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(comentarioEvento.getDataCadastro()));
        }

        cv.put("data_recebimento", DataUtils.dataParaBanco(comentarioEvento.getDataRecebimento()));
        cv.put("ultima_alteracao", DataUtils.dataParaBanco(comentarioEvento.getUltimaAlteracao()));

        if(database.update("comentario_evento", cv, "_id = '"+comentarioEvento.getId()+"'", null) < 1){
            retorno = database.insert("comentario_evento", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("comentario_evento", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<ComentarioEvento> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, texto, id_evento, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM comentario_evento", null);
        List<ComentarioEvento> comentarioEventos = new ArrayList<ComentarioEvento>();

        if(cursor.moveToFirst()){

            EventoDBHelper eventoDBHelper = new EventoDBHelper(context);

            do{
                ComentarioEvento umComentarioEvento = new ComentarioEvento();
                umComentarioEvento.setId(cursor.getString(0));

                umComentarioEvento.setTexto(cursor.getString(1));

                Evento evento = new Evento();
                evento.setId(cursor.getString(2));
                evento = eventoDBHelper.carregar(context, evento);
                umComentarioEvento.setEvento(evento);

                umComentarioEvento.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    umComentarioEvento.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umComentarioEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                umComentarioEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));

                comentarioEventos.add(umComentarioEvento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return comentarioEventos;
    }

    public List<ComentarioEvento> listarTodosPorEvento(Evento umEvento){
        Cursor cursor = database.rawQuery("SELECT _id, texto, id_evento, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM comentario_evento WHERE id_evento = ? AND status != 2",
                new String[]{umEvento.getId()});
        List<ComentarioEvento> comentarios = new ArrayList<ComentarioEvento>();

        if(cursor.moveToFirst()){

            EventoDBHelper eventoDBHelper = new EventoDBHelper(context);

            do{
                ComentarioEvento umComentarioEvento = new ComentarioEvento();
                umComentarioEvento.setId(cursor.getString(0));

                umComentarioEvento.setTexto(cursor.getString(1));

                Evento evento = new Evento();
                evento.setId(cursor.getString(2));
                evento = eventoDBHelper.carregar(context, evento);
                umComentarioEvento.setEvento(evento);

                umComentarioEvento.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    umComentarioEvento.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umComentarioEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                umComentarioEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));

                comentarios.add(umComentarioEvento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return comentarios;
    }

    public ComentarioEvento carregar(ComentarioEvento comentarioEvento){
        Cursor cursor = database.rawQuery("SELECT _id, texto, id_evento, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao FROM comentario_evento WHERE _id = ?",
                new String[]{comentarioEvento.getId()});

        ComentarioEvento umComentarioEvento = null;

        if(cursor.moveToFirst()){

            EventoDBHelper eventoDBHelper = new EventoDBHelper(context);

            do{
                umComentarioEvento = new ComentarioEvento();
                umComentarioEvento.setId(cursor.getString(0));

                umComentarioEvento.setTexto(cursor.getString(1));

                Evento evento = new Evento();
                evento.setId(cursor.getString(2));
                evento = eventoDBHelper.carregar(context, evento);
                umComentarioEvento.setEvento(evento);

                umComentarioEvento.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    umComentarioEvento.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                umComentarioEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                umComentarioEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umComentarioEvento;
    }

}
