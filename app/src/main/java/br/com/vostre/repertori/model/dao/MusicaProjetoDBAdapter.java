package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaExecucao;
import br.com.vostre.repertori.model.MusicaProjeto;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class MusicaProjetoDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public MusicaProjetoDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(MusicaProjeto musicaProjeto){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(musicaProjeto.getId() != null){
            cv.put("_id", musicaProjeto.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }
        
        cv.put("id_musica", musicaProjeto.getMusica().getId());
        cv.put("id_projeto", musicaProjeto.getProjeto().getId());
        cv.put("status", musicaProjeto.getStatus());
        cv.put("enviado", musicaProjeto.getEnviado());

        if(musicaProjeto.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(musicaProjeto.getDataCadastro()));
        }

        if(musicaProjeto.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(musicaProjeto.getDataRecebimento()));
        }

        cv.put("ultima_alteracao", DataUtils.dataParaBanco(musicaProjeto.getUltimaAlteracao()));

        if(database.update("musica_projeto", cv, "_id = '"+musicaProjeto.getId()+"'", null) < 1){
            retorno = database.insert("musica_projeto", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("musica_projeto", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<MusicaProjeto> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, id_musica, id_projeto, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM musica_projeto", null);
        List<MusicaProjeto> musicaProjetos = new ArrayList<MusicaProjeto>();

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(context);

            do{
                MusicaProjeto umMusicaProjeto = new MusicaProjeto();
                umMusicaProjeto.setId(cursor.getString(0));

                Musica musica = new Musica();
                musica.setId(cursor.getString(1));
                musica = musicaDBHelper.carregar(context, musica);
                umMusicaProjeto.setMusica(musica);

                Projeto projeto = new Projeto();
                projeto.setId(cursor.getString(2));
                projeto = projetoDBHelper.carregar(context, projeto);
                umMusicaProjeto.setProjeto(projeto);

                umMusicaProjeto.setStatus(cursor.getInt(3));

                if(cursor.getString(6) != null){
                    umMusicaProjeto.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(7) != null){
                    umMusicaProjeto.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }

                umMusicaProjeto.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));

                musicaProjetos.add(umMusicaProjeto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicaProjetos;
    }

    public List<Projeto> listarTodosDisponiveisMusica(Musica musica){
        Cursor cursor = database.rawQuery("SELECT _id, nome, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao, slug FROM projeto p1 WHERE p1._id NOT IN (" +
                "SELECT id_projeto FROM musica_projeto mp WHERE mp.id_musica = ? AND mp.status  IN(0,1)" +
                ") AND p1.status = 0", new String[]{musica.getId()});
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

    public List<MusicaProjeto> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, id_musica, id_projeto, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM musica_projeto WHERE enviado = -1", null);
        List<MusicaProjeto> musicaProjetos = new ArrayList<MusicaProjeto>();

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(context);

            do{
                MusicaProjeto umMusicaProjeto = new MusicaProjeto();
                umMusicaProjeto.setId(cursor.getString(0));

                Musica musica = new Musica();
                musica.setId(cursor.getString(1));
                musica = musicaDBHelper.carregar(context, musica);
                umMusicaProjeto.setMusica(musica);

                Projeto projeto = new Projeto();
                projeto.setId(cursor.getString(2));
                projeto = projetoDBHelper.carregar(context, projeto);
                umMusicaProjeto.setProjeto(projeto);

                umMusicaProjeto.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    umMusicaProjeto.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(5) != null){
                    umMusicaProjeto.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }

                umMusicaProjeto.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));

                musicaProjetos.add(umMusicaProjeto);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicaProjetos;
    }

    public List<Musica> listarTodosPorProjeto(Projeto umProjeto, int situacao){
        Cursor cursor = database.rawQuery("SELECT id_musica FROM musica_projeto mp INNER JOIN musica m ON m._id = mp.id_musica WHERE id_projeto = ? AND mp.status = ? " +
                        "ORDER BY m.nome COLLATE LOCALIZED",
                new String[]{umProjeto.getId(), String.valueOf(situacao)});
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

    public List<Musica> listarTodosPorProjetoEEstilo(Projeto umProjeto, int situacao){
        Cursor cursor = database.rawQuery("SELECT id_musica FROM musica_projeto mp LEFT JOIN musica m ON m._id = mp.id_musica LEFT JOIN estilo e ON e._id = m.id_estilo " +
                        "WHERE mp.id_projeto = ? AND mp.status = ? ORDER BY e.nome, m.nome",
                new String[]{umProjeto.getId(), String.valueOf(situacao)});
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

    public Map<String, Integer> contarTodosPorProjetoEEstilo(Projeto umProjeto, int situacao){
        Cursor cursor = database.rawQuery("SELECT COUNT(DISTINCT id_musica), e.nome FROM musica_projeto mp LEFT JOIN musica m ON m._id = mp.id_musica LEFT JOIN estilo e ON e._id = m.id_estilo " +
                        "WHERE mp.id_projeto = ? AND mp.status = ? GROUP BY e.nome ORDER BY COUNT(DISTINCT id_musica), e.nome",
                new String[]{umProjeto.getId(), String.valueOf(situacao)});
        Map<String, Integer> musicas = new HashMap<>();

        if(cursor.moveToFirst()){

            do{
                musicas.put(cursor.getString(1), cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicas;
    }

    public Map<String, Integer> contarTodosPorProjetoEArtista(Projeto umProjeto, int situacao){
        Cursor cursor = database.rawQuery("SELECT COUNT(DISTINCT id_musica), a.nome FROM musica_projeto mp LEFT JOIN musica m ON m._id = mp.id_musica LEFT JOIN artista a ON a._id = m.id_artista " +
                        "WHERE mp.id_projeto = ? AND mp.status = ? GROUP BY a.nome ORDER BY COUNT(DISTINCT id_musica) COLLATE NOCASE DESC",
                new String[]{umProjeto.getId(), String.valueOf(situacao)});
        Map<String, Integer> musicas = new HashMap<>();

        if(cursor.moveToFirst()){

            do{
                musicas.put(cursor.getString(1), cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicas;
    }

    public MusicaProjeto carregar(MusicaProjeto musicaProjeto){
        Cursor cursor = database.rawQuery("SELECT _id, id_musica, id_projeto, status, data_cadastro, " +
                        "data_recebimento, ultima_alteracao FROM musica_projeto WHERE _id = ?",
                new String[]{musicaProjeto.getId()});

        MusicaProjeto umMusicaProjeto = null;

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(context);

            do{
                umMusicaProjeto = new MusicaProjeto();
                umMusicaProjeto.setId(cursor.getString(0));

                Musica musica = new Musica();
                musica.setId(cursor.getString(1));
                musica = musicaDBHelper.carregar(context, musica);
                umMusicaProjeto.setMusica(musica);

                Projeto projeto = new Projeto();
                projeto.setId(cursor.getString(2));
                projeto = projetoDBHelper.carregar(context, projeto);
                umMusicaProjeto.setProjeto(projeto);

                umMusicaProjeto.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    umMusicaProjeto.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(5) != null){
                    umMusicaProjeto.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }

                umMusicaProjeto.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umMusicaProjeto;
    }

    public MusicaProjeto carregarPorMusicaEProjeto(MusicaProjeto musicaProjeto){
        Cursor cursor = database.rawQuery("SELECT _id, id_musica, id_projeto, status, data_cadastro, " +
                        "data_recebimento, ultima_alteracao FROM musica_projeto WHERE id_musica = ? AND id_projeto = ?",
                new String[]{musicaProjeto.getMusica().getId(), musicaProjeto.getProjeto().getId()});

        MusicaProjeto umMusicaProjeto = null;

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(context);

            do{
                umMusicaProjeto = new MusicaProjeto();
                umMusicaProjeto.setId(cursor.getString(0));

                Musica musica = new Musica();
                musica.setId(cursor.getString(1));
                musica = musicaDBHelper.carregar(context, musica);
                umMusicaProjeto.setMusica(musica);

                Projeto projeto = new Projeto();
                projeto.setId(cursor.getString(2));
                projeto = projetoDBHelper.carregar(context, projeto);
                umMusicaProjeto.setProjeto(projeto);

                umMusicaProjeto.setStatus(cursor.getInt(3));

                if(cursor.getString(4) != null){
                    umMusicaProjeto.setDataCadastro(DataUtils.bancoParaData(cursor.getString(4)));
                }

                if(cursor.getString(5) != null){
                    umMusicaProjeto.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(5)));
                }

                umMusicaProjeto.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(6)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umMusicaProjeto;
    }

    public List<MusicaExecucao> ultimaExecucaoPorProjeto(Projeto projeto){
        Cursor cursor = database.rawQuery("SELECT id_musica, " +
                        "(" +
                        "SELECT MAX(data) FROM musica_evento me INNER JOIN evento e ON e._id = me.id_evento " +
                        "WHERE me.id_musica = mp.id_musica AND e.id_projeto = ? AND me.status <> 2 AND e.data <= datetime()" +
                        ") " +
                        "FROM musica_projeto mp WHERE mp.id_projeto = ? AND mp.status = 0 GROUP BY id_musica ORDER BY " +
                        "(" +
                        "SELECT MAX(data) FROM musica_evento me INNER JOIN evento e ON e._id = me.id_evento " +
                        "WHERE me.id_musica = mp.id_musica AND e.id_projeto = ? AND me.status <> 2 AND e.data <= datetime()" +
                        ") DESC",
                new String[]{projeto.getId(), projeto.getId()});

        List<MusicaExecucao> musicas = new ArrayList<>();

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);

            do{
                MusicaExecucao umaMusica = new MusicaExecucao();
                Musica musica = new Musica();
                musica.setId(cursor.getString(0));
                musica = musicaDBHelper.carregar(context, musica);

                Calendar execucao = null;

                if(null != cursor.getString(1)){
                    execucao = DataUtils.bancoParaData(cursor.getString(1));
                }

                umaMusica.setMusica(musica);
                umaMusica.setExecucao(execucao);
                musicas.add(umaMusica);

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicas;
    }

    public List<Musica> listarTodosAusentesProjeto(Projeto projeto){
        Cursor cursor = database.rawQuery("SELECT _id, nome " +
                        "FROM musica WHERE _id NOT IN (SELECT id_musica " +
                        "FROM musica_projeto WHERE id_projeto = ? AND status != 2) AND status != 2 ORDER BY nome COLLATE LOCALIZED",
                new String[]{projeto.getId()});
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

}
