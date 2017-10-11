package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaBloco;
import br.com.vostre.repertori.model.MusicaRepertorio;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class MusicaBlocoDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public MusicaBlocoDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(MusicaBloco musicaBloco){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(musicaBloco.getId() != null){
            cv.put("_id", musicaBloco.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("observacao", musicaBloco.getObservacao());
        cv.put("ordem", musicaBloco.getOrdem());
        cv.put("id_musica", musicaBloco.getMusica().getId());
        cv.put("id_bloco_repertorio", musicaBloco.getBlocoRepertorio().getId());
        cv.put("status", musicaBloco.getStatus());
        cv.put("enviado", musicaBloco.getEnviado());

        if(musicaBloco.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(musicaBloco.getDataCadastro()));
        }

        if(musicaBloco.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(musicaBloco.getDataRecebimento()));
        }

        cv.put("ultima_alteracao", DataUtils.dataParaBanco(musicaBloco.getUltimaAlteracao()));

        if(database.update("musica_bloco", cv, "_id = '"+musicaBloco.getId()+"'", null) < 1){
            retorno = database.insert("musica_bloco", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("musica_bloco", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<MusicaBloco> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, ordem, id_musica, id_bloco_repertorio, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM musica_bloco", null);
        List<MusicaBloco> musicaBlocos = new ArrayList<MusicaBloco>();

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            BlocoRepertorioDBHelper blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(context);

            do{
                MusicaBloco umMusicaBloco = new MusicaBloco();
                umMusicaBloco.setId(cursor.getString(0));

                umMusicaBloco.setObservacao(cursor.getString(1));
                umMusicaBloco.setOrdem(cursor.getInt(2));

                Musica musica = new Musica();
                musica.setId(cursor.getString(3));
                musica = musicaDBHelper.carregar(context, musica);
                umMusicaBloco.setMusica(musica);

                BlocoRepertorio blocoRepertorio = new BlocoRepertorio();
                blocoRepertorio.setId(cursor.getString(4));
                blocoRepertorio = blocoRepertorioDBHelper.carregar(context, blocoRepertorio);
                umMusicaBloco.setBlocoRepertorio(blocoRepertorio);

                umMusicaBloco.setStatus(cursor.getInt(5));

                if(cursor.getString(6) != null){
                    umMusicaBloco.setDataCadastro(DataUtils.bancoParaData(cursor.getString(6)));
                }

                if(cursor.getString(7) != null){
                    umMusicaBloco.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                umMusicaBloco.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

                musicaBlocos.add(umMusicaBloco);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicaBlocos;
    }

    public List<MusicaBloco> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, ordem, id_musica, id_bloco_repertorio, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM musica_bloco WHERE enviado = -1", null);
        List<MusicaBloco> musicaBlocos = new ArrayList<MusicaBloco>();

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            BlocoRepertorioDBHelper blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(context);

            do{
                MusicaBloco umMusicaBloco = new MusicaBloco();
                umMusicaBloco.setId(cursor.getString(0));

                umMusicaBloco.setObservacao(cursor.getString(1));
                umMusicaBloco.setOrdem(cursor.getInt(2));

                Musica musica = new Musica();
                musica.setId(cursor.getString(3));
                musica = musicaDBHelper.carregar(context, musica);
                umMusicaBloco.setMusica(musica);

                BlocoRepertorio blocoRepertorio = new BlocoRepertorio();
                blocoRepertorio.setId(cursor.getString(4));
                blocoRepertorio = blocoRepertorioDBHelper.carregar(context, blocoRepertorio);
                umMusicaBloco.setBlocoRepertorio(blocoRepertorio);

                umMusicaBloco.setStatus(cursor.getInt(5));

                if(cursor.getString(6) != null){
                    umMusicaBloco.setDataCadastro(DataUtils.bancoParaData(cursor.getString(6)));
                }

                if(cursor.getString(7) != null){
                    umMusicaBloco.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                umMusicaBloco.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

                musicaBlocos.add(umMusicaBloco);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicaBlocos;
    }

    public List<Musica> listarTodosPorBloco(BlocoRepertorio umBlocoRepertorio, int situacao){
        Cursor cursor = database.rawQuery("SELECT id_musica, ordem FROM musica_bloco WHERE id_bloco_repertorio = ? AND status = ? " +
                "ORDER BY ordem ASC", new String[]{umBlocoRepertorio.getId(), String.valueOf(situacao)});
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

    public List<MusicaBloco> corrigirOrdemPorBloco(BlocoRepertorio umBlocoRepertorio){
        Cursor cursor = database.rawQuery("SELECT _id, id_musica, ordem FROM musica_bloco WHERE id_bloco_repertorio = ? AND status != 2 " +
                "ORDER BY ordem ASC", new String[]{umBlocoRepertorio.getId()});
        List<MusicaBloco> musicas = new ArrayList<MusicaBloco>();

        if(cursor.moveToFirst()){

            MusicaBlocoDBHelper musicaBlocoDBHelper = new MusicaBlocoDBHelper(context);

            do{

                MusicaBloco musicaBloco = new MusicaBloco();
                musicaBloco.setId(cursor.getString(0));
                musicaBloco = musicaBlocoDBHelper.carregar(context, musicaBloco);

                musicas.add(musicaBloco);
            } while (cursor.moveToNext());

        }

        cursor.close();
        database.close();

        return musicas;
    }

    public List<Musica> listarTodosAusentesBloco(BlocoRepertorio umBlocoRepertorio){
        Cursor cursor = database.rawQuery("SELECT DISTINCT m._id FROM musica m LEFT JOIN musica_repertorio mr ON mr.id_musica = m._id LEFT JOIN " +
                        "repertorio r ON r._id = mr.id_repertorio " +
                        "WHERE r._id = ? AND m._id NOT IN (" +
                        "SELECT id_musica FROM musica_bloco me1 INNER JOIN bloco_repertorio br ON br._id = me1.id_bloco_repertorio WHERE br.id_repertorio = ? AND me1.status != 2" +
                        ") AND mr.status = 0 ORDER BY m.nome COLLATE LOCALIZED ASC",
                new String[]{umBlocoRepertorio.getRepertorio().getId(), umBlocoRepertorio.getRepertorio().getId()});
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

    public List<Musica> listarTodosAusentesBloco(BlocoRepertorio umBlocoRepertorio, Estilo estilo, Artista artista){

        Cursor cursor;

        if(estilo.getSlug() != null && artista.getSlug() != null){
//            cursor = database.rawQuery("SELECT DISTINCT m._id FROM musica m LEFT JOIN musica_bloco mp ON mp.id_musica = m._id " +
//                            "WHERE mp.id_projeto = ? AND m._id NOT IN (" +
//                            "SELECT id_musica FROM musica_bloco me1 WHERE me1.id_bloco_repertorio = ? AND me1.status != 2" +
//                            ") AND mp.status IN (0,1) AND m.id_estilo = ? AND m.id_artista = ? ORDER BY m.nome COLLATE LOCALIZED ASC",
//                    new String[]{umBlocoRepertorio.getRepertorio().getProjeto().getId(), umBlocoRepertorio.getId(), estilo.getId(), artista.getId()});
            cursor = database.rawQuery("SELECT DISTINCT m._id FROM musica m LEFT JOIN musica_repertorio mr ON mr.id_musica = m._id LEFT JOIN " +
                            "repertorio r ON r._id = mr.id_repertorio " +
                            "WHERE r._id = ? AND m._id NOT IN (" +
                            "SELECT id_musica FROM musica_bloco me1 INNER JOIN bloco_repertorio br ON br._id = me1.id_bloco_repertorio WHERE br.id_repertorio = ? AND me1.status != 2" +
                            ") AND mr.status IN (0,1) AND m.id_estilo = ? AND m.id_artista = ? ORDER BY m.nome COLLATE LOCALIZED ASC",
                    new String[]{umBlocoRepertorio.getRepertorio().getId(), umBlocoRepertorio.getRepertorio().getId(), estilo.getId(), artista.getId()});
        } else if(estilo.getSlug() != null && artista.getSlug() == null){
            cursor = database.rawQuery("SELECT DISTINCT m._id FROM musica m LEFT JOIN musica_repertorio mr ON mr.id_musica = m._id LEFT JOIN " +
                            "repertorio r ON r._id = mr.id_repertorio " +
                            "WHERE r._id = ? AND m._id NOT IN (" +
                            "SELECT id_musica FROM musica_bloco me1 INNER JOIN bloco_repertorio br ON br._id = me1.id_bloco_repertorio WHERE br.id_repertorio = ? AND me1.status != 2" +
                            ") AND mr.status IN (0,1) AND m.id_estilo = ? ORDER BY m.nome COLLATE LOCALIZED ASC",
                    new String[]{umBlocoRepertorio.getRepertorio().getId(), umBlocoRepertorio.getRepertorio().getId(), estilo.getId()});
        } else{
            cursor = database.rawQuery("SELECT DISTINCT m._id FROM musica m LEFT JOIN musica_repertorio mr ON mr.id_musica = m._id LEFT JOIN " +
                            "repertorio r ON r._id = mr.id_repertorio " +
                            "WHERE r._id = ? AND m._id NOT IN (" +
                            "SELECT id_musica FROM musica_bloco me1 INNER JOIN bloco_repertorio br ON br._id = me1.id_bloco_repertorio WHERE br.id_repertorio = ? AND me1.status != 2" +
                            ") AND mr.status IN (0,1) AND m.id_artista = ? ORDER BY m.nome COLLATE LOCALIZED ASC",
                    new String[]{umBlocoRepertorio.getRepertorio().getId(), umBlocoRepertorio.getRepertorio().getId(), artista.getId()});
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

    public MusicaBloco carregar(MusicaBloco musicaBloco){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, ordem, id_musica, id_bloco_repertorio, status, data_cadastro, " +
                "data_recebimento, ultima_alteracao FROM musica_bloco WHERE _id = ?",
                new String[]{musicaBloco.getId()});

        MusicaBloco umMusicaBloco = null;

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            BlocoRepertorioDBHelper blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(context);

            do{
                umMusicaBloco = new MusicaBloco();
                umMusicaBloco.setId(cursor.getString(0));

                umMusicaBloco.setObservacao(cursor.getString(1));
                umMusicaBloco.setOrdem(cursor.getInt(2));

                Musica musica = new Musica();
                musica.setId(cursor.getString(3));
                musica = musicaDBHelper.carregar(context, musica);
                umMusicaBloco.setMusica(musica);

                BlocoRepertorio blocoRepertorio = new BlocoRepertorio();
                blocoRepertorio.setId(cursor.getString(4));
                blocoRepertorio = blocoRepertorioDBHelper.carregar(context, blocoRepertorio);
                umMusicaBloco.setBlocoRepertorio(blocoRepertorio);

                umMusicaBloco.setStatus(cursor.getInt(5));

                if(cursor.getString(6) != null){
                    umMusicaBloco.setDataCadastro(DataUtils.bancoParaData(cursor.getString(6)));
                }

                if(cursor.getString(7) != null){
                    umMusicaBloco.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                umMusicaBloco.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umMusicaBloco;
    }

    public MusicaBloco carregarPorMusicaEBloco(MusicaBloco musicaBloco){
        Cursor cursor = database.rawQuery("SELECT _id, observacao, ordem, id_musica, id_bloco_repertorio, status, data_cadastro, " +
                        "data_recebimento, ultima_alteracao FROM musica_bloco WHERE id_musica = ? AND id_bloco_repertorio = ?",
                new String[]{musicaBloco.getMusica().getId(), musicaBloco.getBlocoRepertorio().getId()});

        MusicaBloco umMusicaBloco = null;

        if(cursor.moveToFirst()){

            MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
            BlocoRepertorioDBHelper blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(context);

            do{
                umMusicaBloco = new MusicaBloco();
                umMusicaBloco.setId(cursor.getString(0));

                umMusicaBloco.setObservacao(cursor.getString(1));
                umMusicaBloco.setOrdem(cursor.getInt(2));

                Musica musica = new Musica();
                musica.setId(cursor.getString(3));
                musica = musicaDBHelper.carregar(context, musica);
                umMusicaBloco.setMusica(musica);

                BlocoRepertorio blocoRepertorio = new BlocoRepertorio();
                blocoRepertorio.setId(cursor.getString(4));
                blocoRepertorio = blocoRepertorioDBHelper.carregar(context, blocoRepertorio);
                umMusicaBloco.setBlocoRepertorio(blocoRepertorio);

                umMusicaBloco.setStatus(cursor.getInt(5));

                if(cursor.getString(6) != null){
                    umMusicaBloco.setDataCadastro(DataUtils.bancoParaData(cursor.getString(6)));
                }

                if(cursor.getString(7) != null){
                    umMusicaBloco.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(7)));
                }

                umMusicaBloco.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(8)));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umMusicaBloco;
    }

}
