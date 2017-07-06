package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.TempoMusicaEvento;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class TempoMusicaEventoDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public TempoMusicaEventoDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(TempoMusicaEvento tme){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(tme.getId() != null){
            cv.put("_id", tme.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("tempo", DataUtils.dataParaBanco(tme.getTempo()));
        cv.put("id_musica_evento", tme.getMusicaEvento().getId());
        cv.put("status", tme.getStatus());
        cv.put("enviado", tme.getEnviado());

        if(tme.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(tme.getDataCadastro()));
        }

        if(tme.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(tme.getDataRecebimento()));
        }

        cv.put("ultima_alteracao", DataUtils.dataParaBanco(tme.getUltimaAlteracao()));

        if(database.update("tempo_musica_evento", cv, "_id = '"+tme.getId()+"'", null) < 1){
            retorno = database.insert("tempo_musica_evento", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("tempo_musica_evento", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<TempoMusicaEvento> listarTodosPorMusica(Musica musica){
        Cursor cursor = database.rawQuery("SELECT tme._id, tme.tempo, tme.id_musica_evento, tme.status, tme.data_cadastro, tme.data_recebimento, " +
                "tme.ultima_alteracao FROM tempo_musica_evento tme INNER JOIN musica_evento me ON me._id = tme.id_musica_evento WHERE me.id_musica = ? AND tme.status = 0 " +
                "ORDER BY tme.data_cadastro DESC", new String[]{musica.getId()});
        List<TempoMusicaEvento> tmes = new ArrayList<TempoMusicaEvento>();

        if(cursor.moveToFirst()){

            MusicaEventoDBHelper musicaEventoDBHelper = new MusicaEventoDBHelper(context);

            do{
                TempoMusicaEvento tme = new TempoMusicaEvento();
                tme.setId(cursor.getString(0));

                tme.setTempo(DataUtils.bancoParaData(cursor.getString(1)));

                MusicaEvento musicaEvento = new MusicaEvento();
                musicaEvento.setId(cursor.getString(2));
                musicaEvento = musicaEventoDBHelper.carregar(context, musicaEvento);
                tme.setMusicaEvento(musicaEvento);

                tme.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    tme.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(5) != null){
                    tme.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }

                if(cursor.getString(6) != null){
                    tme.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));
                }

                tmes.add(tme);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return tmes;
    }

    public List<TempoMusicaEvento> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, tempo, id_musica_evento, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao FROM tempo_musica_evento WHERE enviado = -1 ORDER BY data_cadastro DESC", null);
        List<TempoMusicaEvento> tmes = new ArrayList<TempoMusicaEvento>();

        if(cursor.moveToFirst()){

            MusicaEventoDBHelper musicaEventoDBHelper = new MusicaEventoDBHelper(context);

            do{
                TempoMusicaEvento tme = new TempoMusicaEvento();
                tme.setId(cursor.getString(0));

                tme.setTempo(DataUtils.bancoParaData(cursor.getString(1)));

                MusicaEvento musicaEvento = new MusicaEvento();
                musicaEvento.setId(cursor.getString(2));
                musicaEvento = musicaEventoDBHelper.carregar(context, musicaEvento);
                tme.setMusicaEvento(musicaEvento);

                tme.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    tme.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(5) != null){
                    tme.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }

                if(cursor.getString(6) != null){
                    tme.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));
                }

                tmes.add(tme);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return tmes;
    }

}
