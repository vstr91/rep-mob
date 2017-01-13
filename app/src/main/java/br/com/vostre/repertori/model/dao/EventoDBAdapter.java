package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.TipoEvento;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class EventoDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public EventoDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(Evento evento){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(evento.getId() != null){
            cv.put("_id", evento.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("nome", evento.getNome());
        cv.put("slug", evento.getSlug());
        cv.put("data", DataUtils.dataParaBanco(evento.getData()));
        cv.put("id_tipo_evento", evento.getTipoEvento().getId());
        cv.put("status", evento.getStatus());

        if(evento.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(evento.getDataCadastro()));
        }

        cv.put("data_recebimento", DataUtils.dataParaBanco(evento.getDataRecebimento()));
        cv.put("ultima_alteracao", DataUtils.dataParaBanco(evento.getUltimaAlteracao()));

        if(database.update("evento", cv, "_id = '"+evento.getId()+"'", null) < 1){
            retorno = database.insert("evento", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("evento", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<Evento> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, data, id_tipo_evento, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, slug FROM evento", null);
        List<Evento> eventos = new ArrayList<Evento>();

        if(cursor.moveToFirst()){

            TipoEventoDBHelper tipoEventoDBHelper = new TipoEventoDBHelper(context);

            do{
                Evento umEvento = new Evento();
                umEvento.setId(cursor.getString(0));

                umEvento.setNome(cursor.getString(1));
                umEvento.setData(DataUtils.bancoParaData(cursor.getString(2)));

                TipoEvento tipoEvento = new TipoEvento();
                tipoEvento.setId(cursor.getString(3));
                tipoEvento = tipoEventoDBHelper.carregar(context, tipoEvento);
                umEvento.setTipoEvento(tipoEvento);

                umEvento.setStatus(cursor.getInt(4));

                if(cursor.getString(5) != null){
                    umEvento.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                }

                umEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                umEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));
                umEvento.setSlug(cursor.getString(8));

                eventos.add(umEvento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return eventos;
    }

    public List<Evento> listarTodosAteHoje(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, data, id_tipo_evento, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, slug FROM evento WHERE data < ? AND status != 2 ORDER BY data ASC", new String[]{DataUtils.dataParaBanco(Calendar.getInstance())});
        List<Evento> eventos = new ArrayList<Evento>();

        if(cursor.moveToFirst()){

            TipoEventoDBHelper tipoEventoDBHelper = new TipoEventoDBHelper(context);

            do{
                Evento umEvento = new Evento();
                umEvento.setId(cursor.getString(0));

                umEvento.setNome(cursor.getString(1));
                umEvento.setData(DataUtils.bancoParaData(cursor.getString(2)));

                TipoEvento tipoEvento = new TipoEvento();
                tipoEvento.setId(cursor.getString(3));
                tipoEvento = tipoEventoDBHelper.carregar(context, tipoEvento);
                umEvento.setTipoEvento(tipoEvento);

                umEvento.setStatus(cursor.getInt(4));

                if(cursor.getString(5) != null){
                    umEvento.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                }

                umEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                umEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));
                umEvento.setSlug(cursor.getString(8));

                eventos.add(umEvento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return eventos;
    }

    public List<Evento> listarTodosAPartirDeHoje(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, data, id_tipo_evento, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, slug FROM evento WHERE data >= ? AND status != 2 ORDER BY data ASC", new String[]{DataUtils.dataParaBanco(Calendar.getInstance())});
        List<Evento> eventos = new ArrayList<Evento>();

        if(cursor.moveToFirst()){

            TipoEventoDBHelper tipoEventoDBHelper = new TipoEventoDBHelper(context);

            do{
                Evento umEvento = new Evento();
                umEvento.setId(cursor.getString(0));

                umEvento.setNome(cursor.getString(1));
                umEvento.setData(DataUtils.bancoParaData(cursor.getString(2)));

                TipoEvento tipoEvento = new TipoEvento();
                tipoEvento.setId(cursor.getString(3));
                tipoEvento = tipoEventoDBHelper.carregar(context, tipoEvento);
                umEvento.setTipoEvento(tipoEvento);

                umEvento.setStatus(cursor.getInt(4));

                if(cursor.getString(5) != null){
                    umEvento.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                }

                umEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                umEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));
                umEvento.setSlug(cursor.getString(8));

                eventos.add(umEvento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return eventos;
    }

    public List<Evento> listarTodosPorData(Calendar data){

        String umaData = DataUtils.dataParaBancoSemHora(data);
        data.add(Calendar.DATE, 1);
        String amanha = DataUtils.dataParaBancoSemHora(data);

        Cursor cursor = database.rawQuery("SELECT _id, nome, data, id_tipo_evento, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, slug FROM evento WHERE data >= ? AND data < ? AND status != 2 ORDER BY data ASC",
                new String[]{umaData, amanha});
        List<Evento> eventos = new ArrayList<Evento>();

        if(cursor.moveToFirst()){

            TipoEventoDBHelper tipoEventoDBHelper = new TipoEventoDBHelper(context);

            do{
                Evento umEvento = new Evento();
                umEvento.setId(cursor.getString(0));

                umEvento.setNome(cursor.getString(1));
                umEvento.setData(DataUtils.bancoParaData(cursor.getString(2)));

                TipoEvento tipoEvento = new TipoEvento();
                tipoEvento.setId(cursor.getString(3));
                tipoEvento = tipoEventoDBHelper.carregar(context, tipoEvento);
                umEvento.setTipoEvento(tipoEvento);

                umEvento.setStatus(cursor.getInt(4));

                if(cursor.getString(5) != null){
                    umEvento.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                }

                umEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                umEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));
                umEvento.setSlug(cursor.getString(8));

                eventos.add(umEvento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return eventos;
    }

    public Evento carregar(Evento evento){
        Cursor cursor = database.rawQuery("SELECT _id, nome, data, id_tipo_evento, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, slug FROM evento WHERE _id = ?", new String[]{evento.getId()});

        Evento umEvento = null;

        if(cursor.moveToFirst()){

            TipoEventoDBHelper tipoEventoDBHelper = new TipoEventoDBHelper(context);

            do{
                umEvento = new Evento();
                umEvento.setId(cursor.getString(0));

                umEvento.setNome(cursor.getString(1));
                umEvento.setData(DataUtils.bancoParaData(cursor.getString(2)));

                TipoEvento tipoEvento = new TipoEvento();
                tipoEvento.setId(cursor.getString(3));
                tipoEvento = tipoEventoDBHelper.carregar(context, tipoEvento);
                umEvento.setTipoEvento(tipoEvento);

                umEvento.setStatus(cursor.getInt(4));

                if(cursor.getString(5) != null){
                    umEvento.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                }

                umEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                umEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));
                umEvento.setSlug(cursor.getString(8));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umEvento;
    }

}
