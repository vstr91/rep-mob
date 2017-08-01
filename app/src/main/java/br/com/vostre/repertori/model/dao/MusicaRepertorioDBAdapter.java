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
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaRepertorio;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class MusicaRepertorioDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public MusicaRepertorioDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(MusicaRepertorio musicaRepertorio){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(musicaRepertorio.getId() != null){
            cv.put("_id", musicaRepertorio.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("observacao", musicaRepertorio.getObservacao());
        cv.put("ordem", musicaRepertorio.getOrdem());
        cv.put("id_musica", musicaRepertorio.getMusica().getId());
        cv.put("id_repertorio", musicaRepertorio.getRepertorio().getId());
        cv.put("status", musicaRepertorio.getStatus());
        cv.put("enviado", musicaRepertorio.getEnviado());

        if(musicaRepertorio.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(musicaRepertorio.getDataCadastro()));
        }

        if(musicaRepertorio.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(musicaRepertorio.getDataRecebimento()));
        }

        cv.put("ultima_alteracao", DataUtils.dataParaBanco(musicaRepertorio.getUltimaAlteracao()));

        if(database.update("musica_repertorio", cv, "_id = '"+musicaRepertorio.getId()+"'", null) < 1){
            retorno = database.insert("musica_repertorio", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("musica_repertorio", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<MusicaRepertorio> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, ordem, id_musica, id_repertorio, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM musica_repertorio", null);
        List<MusicaRepertorio> musicaRepertorios = new ArrayList<MusicaRepertorio>();

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(context);

            do{
                MusicaRepertorio umMusicaRepertorio = new MusicaRepertorio();
                umMusicaRepertorio.setId(cursor.getString(0));

                umMusicaRepertorio.setObservacao(cursor.getString(1));
                umMusicaRepertorio.setOrdem(cursor.getInt(2));

                Musica musica = new Musica();
                musica.setId(cursor.getString(3));
                musica = musicaDBHelper.carregar(context, musica);
                umMusicaRepertorio.setMusica(musica);

                Repertorio repertorio = new Repertorio();
                repertorio.setId(cursor.getString(4));
                repertorio = repertorioDBHelper.carregar(context, repertorio);
                umMusicaRepertorio.setRepertorio(repertorio);

                umMusicaRepertorio.setStatus(cursor.getInt(5));

                if(cursor.getString(6) != null){
                    umMusicaRepertorio.setDataCadastro(DataUtils.bancoParaData(cursor.getString(6)));
                }

                if(cursor.getString(7) != null){
                    umMusicaRepertorio.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                umMusicaRepertorio.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

                musicaRepertorios.add(umMusicaRepertorio);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicaRepertorios;
    }

    public List<MusicaRepertorio> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, ordem, id_musica, id_repertorio, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM musica_repertorio WHERE enviado = -1", null);
        List<MusicaRepertorio> musicaRepertorios = new ArrayList<MusicaRepertorio>();

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(context);

            do{
                MusicaRepertorio umMusicaRepertorio = new MusicaRepertorio();
                umMusicaRepertorio.setId(cursor.getString(0));

                umMusicaRepertorio.setObservacao(cursor.getString(1));
                umMusicaRepertorio.setOrdem(cursor.getInt(2));

                Musica musica = new Musica();
                musica.setId(cursor.getString(3));
                musica = musicaDBHelper.carregar(context, musica);
                umMusicaRepertorio.setMusica(musica);

                Repertorio repertorio = new Repertorio();
                repertorio.setId(cursor.getString(4));
                repertorio = repertorioDBHelper.carregar(context, repertorio);
                umMusicaRepertorio.setRepertorio(repertorio);

                umMusicaRepertorio.setStatus(cursor.getInt(5));

                if(cursor.getString(6) != null){
                    umMusicaRepertorio.setDataCadastro(DataUtils.bancoParaData(cursor.getString(6)));
                }

                if(cursor.getString(7) != null){
                    umMusicaRepertorio.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                umMusicaRepertorio.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

                musicaRepertorios.add(umMusicaRepertorio);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicaRepertorios;
    }

    public List<Repertorio> listarTodosPorMusica(Musica umaMusica){
        Cursor cursor = database.rawQuery("SELECT id_repertorio FROM musica_repertorio WHERE id_musica = ? AND status != 2", new String[]{umaMusica.getId()});
        List<Repertorio> repertorios = new ArrayList<Repertorio>();

        if(cursor.moveToFirst()){

            RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(context);

            do{

                Repertorio repertorio = new Repertorio();
                repertorio.setId(cursor.getString(0));
                repertorio = repertorioDBHelper.carregar(context, repertorio);

                repertorios.add(repertorio);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return repertorios;
    }

    public List<Musica> listarTodosPorRepertorio(Repertorio umRepertorio){
        Cursor cursor = database.rawQuery("SELECT id_musica, ordem FROM musica_repertorio WHERE id_repertorio = ? AND status != 2 " +
                "ORDER BY ordem ASC", new String[]{umRepertorio.getId()});
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

    public List<MusicaRepertorio> corrigirOrdemPorRepertorio(Repertorio umRepertorio){
        Cursor cursor = database.rawQuery("SELECT _id, id_musica, ordem FROM musica_repertorio WHERE id_repertorio = ? AND status != 2 " +
                "ORDER BY ordem ASC", new String[]{umRepertorio.getId()});
        List<MusicaRepertorio> musicas = new ArrayList<MusicaRepertorio>();

        if(cursor.moveToFirst()){

            MusicaRepertorioDBHelper musicaRepertorioDBHelper = new MusicaRepertorioDBHelper(context);

            do{

                MusicaRepertorio musicaRepertorio = new MusicaRepertorio();
                musicaRepertorio.setId(cursor.getString(0));
                musicaRepertorio = musicaRepertorioDBHelper.carregar(context, musicaRepertorio);

                musicas.add(musicaRepertorio);
            } while (cursor.moveToNext());

        }

        cursor.close();
        database.close();

        return musicas;
    }

    public List<Musica> listarTodosAusentesRepertorio(Repertorio umRepertorio){
        Cursor cursor = database.rawQuery("SELECT DISTINCT m._id FROM musica m LEFT JOIN musica_projeto mp ON mp.id_musica = m._id " +
                        "WHERE mp.id_projeto = ? AND m._id NOT IN (" +
                        "SELECT id_musica FROM musica_repertorio me1 WHERE me1.id_repertorio = ? AND me1.status != 2" +
                        ") AND mp.status IN (0,1) ORDER BY m.nome COLLATE LOCALIZED ASC",
                new String[]{umRepertorio.getProjeto().getId(), umRepertorio.getId()});
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

    public List<Musica> listarTodosAusentesRepertorio(Repertorio umRepertorio, Estilo estilo, Artista artista){

        Cursor cursor;

        if(estilo.getSlug() != null && artista.getSlug() != null){
            cursor = database.rawQuery("SELECT DISTINCT m._id FROM musica m LEFT JOIN musica_projeto mp ON mp.id_musica = m._id " +
                            "WHERE mp.id_projeto = ? AND m._id NOT IN (" +
                            "SELECT id_musica FROM musica_repertorio me1 WHERE me1.id_repertorio = ? AND me1.status != 2" +
                            ") AND mp.status IN (0,1) AND m.id_estilo = ? AND m.id_artista = ? ORDER BY m.nome COLLATE LOCALIZED ASC",
                    new String[]{umRepertorio.getProjeto().getId(), umRepertorio.getId(), estilo.getId(), artista.getId()});
        } else if(estilo.getSlug() != null && artista.getSlug() == null){
            cursor = database.rawQuery("SELECT DISTINCT m._id FROM musica m LEFT JOIN musica_projeto mp ON mp.id_musica = m._id " +
                            "WHERE mp.id_projeto = ? AND m._id NOT IN (" +
                            "SELECT id_musica FROM musica_repertorio me1 WHERE me1.id_repertorio = ? AND me1.status != 2" +
                            ") AND mp.status IN (0,1) AND m.id_estilo = ? ORDER BY m.nome COLLATE LOCALIZED ASC",
                    new String[]{umRepertorio.getProjeto().getId(), umRepertorio.getId(), estilo.getId()});
        } else{
            cursor = database.rawQuery("SELECT DISTINCT m._id FROM musica m LEFT JOIN musica_projeto mp ON mp.id_musica = m._id " +
                            "WHERE mp.id_projeto = ? AND m._id NOT IN (" +
                            "SELECT id_musica FROM musica_repertorio me1 WHERE me1.id_repertorio = ? AND me1.status != 2" +
                            ") AND mp.status IN (0,1) AND m.id_artista = ? ORDER BY m.nome COLLATE LOCALIZED ASC",
                    new String[]{umRepertorio.getProjeto().getId(), umRepertorio.getId(), artista.getId()});
        }

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

    public MusicaRepertorio carregar(MusicaRepertorio musicaRepertorio){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, ordem, id_musica, id_repertorio, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM musica_repertorio WHERE _id = ?",
                new String[]{musicaRepertorio.getId()});

        MusicaRepertorio umMusicaRepertorio = null;

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(context);

            do{
                umMusicaRepertorio = new MusicaRepertorio();
                umMusicaRepertorio.setId(cursor.getString(0));

                umMusicaRepertorio.setObservacao(cursor.getString(1));
                umMusicaRepertorio.setOrdem(cursor.getInt(2));

                Musica musica = new Musica();
                musica.setId(cursor.getString(3));
                musica = musicaDBHelper.carregar(context, musica);
                umMusicaRepertorio.setMusica(musica);

                Repertorio repertorio = new Repertorio();
                repertorio.setId(cursor.getString(4));
                repertorio = repertorioDBHelper.carregar(context, repertorio);
                umMusicaRepertorio.setRepertorio(repertorio);

                umMusicaRepertorio.setStatus(cursor.getInt(5));

                if(cursor.getString(6) != null){
                    umMusicaRepertorio.setDataCadastro(DataUtils.bancoParaData(cursor.getString(6)));
                }

                if(cursor.getString(7) != null){
                    umMusicaRepertorio.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                umMusicaRepertorio.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umMusicaRepertorio;
    }

    public MusicaRepertorio carregarPorMusicaERepertorio(MusicaRepertorio musicaRepertorio){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, ordem, id_musica, id_repertorio, status, data_cadastro, " +
                        "data_recebimento, ultima_alteracao FROM musica_repertorio WHERE id_musica = ? AND id_repertorio = ?",
                new String[]{musicaRepertorio.getMusica().getId(), musicaRepertorio.getRepertorio().getId()});

        MusicaRepertorio umMusicaRepertorio = null;

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(context);

            do{
                umMusicaRepertorio = new MusicaRepertorio();
                umMusicaRepertorio.setId(cursor.getString(0));

                umMusicaRepertorio.setObservacao(cursor.getString(1));
                umMusicaRepertorio.setOrdem(cursor.getInt(2));

                Musica musica = new Musica();
                musica.setId(cursor.getString(3));
                musica = musicaDBHelper.carregar(context, musica);
                umMusicaRepertorio.setMusica(musica);

                Repertorio repertorio = new Repertorio();
                repertorio.setId(cursor.getString(4));
                repertorio = repertorioDBHelper.carregar(context, repertorio);
                umMusicaRepertorio.setRepertorio(repertorio);

                umMusicaRepertorio.setStatus(cursor.getInt(5));

                if(cursor.getString(6) != null){
                    umMusicaRepertorio.setDataCadastro(DataUtils.bancoParaData(cursor.getString(6)));
                }

                if(cursor.getString(7) != null){
                    umMusicaRepertorio.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                umMusicaRepertorio.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umMusicaRepertorio;
    }

}
