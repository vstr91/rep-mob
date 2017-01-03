package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class MusicaEventoDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public MusicaEventoDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(MusicaEvento musicaEvento){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(musicaEvento.getId() != null){
            cv.put("_id", musicaEvento.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("observacao", musicaEvento.getObservacao());
        cv.put("ordem", musicaEvento.getOrdem());
        cv.put("id_musica", musicaEvento.getMusica().getId());
        cv.put("id_evento", musicaEvento.getEvento().getId());
        cv.put("status", musicaEvento.getStatus());

        if(musicaEvento.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(musicaEvento.getDataCadastro()));
        }

        cv.put("data_recebimento", DataUtils.dataParaBanco(musicaEvento.getDataRecebimento()));
        cv.put("ultima_alteracao", DataUtils.dataParaBanco(musicaEvento.getUltimaAlteracao()));

        if(database.update("musica_evento", cv, "_id = '"+musicaEvento.getId()+"'", null) < 1){
            retorno = database.insert("musica_evento", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("musica_evento", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<MusicaEvento> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, ordem, id_musica, id_evento, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM musica_evento", null);
        List<MusicaEvento> musicaEventos = new ArrayList<MusicaEvento>();

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            EventoDBHelper eventoDBHelper = new EventoDBHelper(context);

            do{
                MusicaEvento umMusicaEvento = new MusicaEvento();
                umMusicaEvento.setId(cursor.getString(0));

                umMusicaEvento.setObservacao(cursor.getString(1));
                umMusicaEvento.setOrdem(cursor.getInt(2));

                Musica musica = new Musica();
                musica.setId(cursor.getString(3));
                musica = musicaDBHelper.carregar(context, musica);
                umMusicaEvento.setMusica(musica);

                Evento evento = new Evento();
                evento.setId(cursor.getString(4));
                evento = eventoDBHelper.carregar(context, evento);
                umMusicaEvento.setEvento(evento);

                umMusicaEvento.setStatus(cursor.getInt(5));

                if(cursor.getString(6) != null){
                    umMusicaEvento.setDataCadastro(DataUtils.bancoParaData(cursor.getString(6)));
                }

                umMusicaEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                umMusicaEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

                musicaEventos.add(umMusicaEvento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicaEventos;
    }

    public MusicaEvento carregar(MusicaEvento musicaEvento){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, ordem, id_musica, id_evento, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM musica_evento WHERE _id = ?",
                new String[]{musicaEvento.getId()});

        MusicaEvento umMusicaEvento = null;

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            EventoDBHelper eventoDBHelper = new EventoDBHelper(context);

            do{
                umMusicaEvento = new MusicaEvento();
                umMusicaEvento.setId(cursor.getString(0));

                umMusicaEvento.setObservacao(cursor.getString(1));
                umMusicaEvento.setOrdem(cursor.getInt(2));

                Musica musica = new Musica();
                musica.setId(cursor.getString(3));
                musica = musicaDBHelper.carregar(context, musica);
                umMusicaEvento.setMusica(musica);

                Evento evento = new Evento();
                evento.setId(cursor.getString(4));
                evento = eventoDBHelper.carregar(context, evento);
                umMusicaEvento.setEvento(evento);

                umMusicaEvento.setStatus(cursor.getInt(5));

                if(cursor.getString(6) != null){
                    umMusicaEvento.setDataCadastro(DataUtils.bancoParaData(cursor.getString(6)));
                }

                umMusicaEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                umMusicaEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umMusicaEvento;
    }

}
