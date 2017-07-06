package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
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
        cv.put("enviado", musicaEvento.getEnviado());

        if(musicaEvento.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(musicaEvento.getDataCadastro()));
        }

        if(musicaEvento.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(musicaEvento.getDataRecebimento()));
        }

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

                if(cursor.getString(7) != null){
                    umMusicaEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                umMusicaEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

                musicaEventos.add(umMusicaEvento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicaEventos;
    }

    public List<MusicaEvento> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, ordem, id_musica, id_evento, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM musica_evento WHERE enviado = -1", null);
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

                if(cursor.getString(7) != null){
                    umMusicaEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                umMusicaEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

                musicaEventos.add(umMusicaEvento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicaEventos;
    }

    public List<Evento> listarTodosPorMusica(Musica umaMusica){
        Cursor cursor = database.rawQuery("SELECT id_evento FROM musica_evento WHERE id_musica = ? AND status != 2", new String[]{umaMusica.getId()});
        List<Evento> eventos = new ArrayList<Evento>();

        if(cursor.moveToFirst()){

            EventoDBHelper eventoDBHelper = new EventoDBHelper(context);

            do{

                Evento evento = new Evento();
                evento.setId(cursor.getString(0));
                evento = eventoDBHelper.carregar(context, evento);

                eventos.add(evento);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return eventos;
    }

    public List<Musica> listarTodosPorEvento(Evento umEvento){
        Cursor cursor = database.rawQuery("SELECT id_musica, ordem FROM musica_evento WHERE id_evento = ? AND status != 2 " +
                "ORDER BY ordem ASC", new String[]{umEvento.getId()});
        List<Musica> musicas = new ArrayList<Musica>();

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);

            do{

                Musica musica = new Musica();
                musica.setId(cursor.getString(0));
                musica = musicaDBHelper.carregar(context, musica);

                musica.setNome(musica.getNome());

                musicas.add(musica);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicas;
    }

    public List<MusicaEvento> corrigirOrdemPorEvento(Evento umEvento){
        Cursor cursor = database.rawQuery("SELECT _id, id_musica, ordem FROM musica_evento WHERE id_evento = ? AND status != 2 " +
                "ORDER BY ordem ASC", new String[]{umEvento.getId()});
        List<MusicaEvento> musicas = new ArrayList<MusicaEvento>();

        if(cursor.moveToFirst()){

            MusicaEventoDBHelper musicaEventoDBHelper = new MusicaEventoDBHelper(context);

            do{

                MusicaEvento musicaEvento = new MusicaEvento();
                musicaEvento.setId(cursor.getString(0));
                musicaEvento = musicaEventoDBHelper.carregar(context, musicaEvento);

                musicas.add(musicaEvento);
            } while (cursor.moveToNext());

        }

        cursor.close();
        database.close();

        return musicas;
    }

    public List<Musica> listarTodosAusentesEvento(Evento umEvento){
        Cursor cursor = database.rawQuery("SELECT DISTINCT me.id_musica " +
                "FROM musica_evento me INNER JOIN evento e ON e._id = me.id_evento INNER JOIN musica m ON m._id = me.id_musica WHERE e.id_projeto = ? AND e._id NOT IN (SELECT id_musica " +
                "FROM musica_evento me1 WHERE me1.id_evento = ? AND me1.status != 2) AND me.status != 2 ORDER BY m.nome COLLATE LOCALIZED ASC",
                new String[]{umEvento.getProjeto().getId(), umEvento.getId()});
        List<Musica> musicas = new ArrayList<Musica>();

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);

            do{

                Musica musica = new Musica();
                musica.setId(cursor.getString(0));
                musica = musicaDBHelper.carregar(context, musica);

                musicas.add(musica);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicas;
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

                if(cursor.getString(7) != null){
                    umMusicaEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                umMusicaEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umMusicaEvento;
    }

    public MusicaEvento carregarPorMusicaEEvento(MusicaEvento musicaEvento){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, ordem, id_musica, id_evento, status, data_cadastro, " +
                        "data_recebimento, ultima_alteracao FROM musica_evento WHERE id_musica = ? AND id_evento = ?",
                new String[]{musicaEvento.getMusica().getId(), musicaEvento.getEvento().getId()});

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

                if(cursor.getString(7) != null){
                    umMusicaEvento.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                umMusicaEvento.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umMusicaEvento;
    }

}
