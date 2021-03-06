package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 24/02/2016.
 */
public class MusicaDBAdapter {

    private SQLiteDatabase database;
    private Context context;

    public MusicaDBAdapter(Context context, SQLiteDatabase database){
        this.database = database;
        this.context = context;
    }

    public long salvarOuAtualizar(Musica musica){
        Long retorno;
        ContentValues cv = new ContentValues();

        if(musica.getId() != null){
            cv.put("_id", musica.getId());
        } else{
            cv.put("_id", UUID.randomUUID().toString());
        }

        cv.put("nome", musica.getNome());
        cv.put("tom", musica.getTom());
        cv.put("slug", musica.getSlug());
        cv.put("id_artista", musica.getArtista().getId());
        cv.put("status", musica.getStatus());
        cv.put("enviado", musica.getEnviado());

        if(musica.getDataCadastro() != null){
            cv.put("data_cadastro", DataUtils.dataParaBanco(musica.getDataCadastro()));
        }

        if(musica.getDataRecebimento() != null){
            cv.put("data_recebimento", DataUtils.dataParaBanco(musica.getDataRecebimento()));
        }

        cv.put("ultima_alteracao", DataUtils.dataParaBanco(musica.getUltimaAlteracao()));

        if(database.update("musica", cv, "_id = '"+musica.getId()+"'", null) < 1){
            retorno = database.insert("musica", null, cv);
            database.close();
            return retorno;
        } else{
            database.close();
            return -1;
        }

    }

    public int deletarInativos(){
        int retorno = database.delete("musica", "status = "+2, null);
        database.close();
        return retorno;
    }

    public List<Musica> listarTodos(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, tom, id_artista, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, slug FROM musica", null);
        List<Musica> musicas = new ArrayList<Musica>();

        if(cursor.moveToFirst()){

            ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(context);

            do{
                Musica umMusica = new Musica();
                umMusica.setId(cursor.getString(0));

                umMusica.setNome(cursor.getString(1));
                umMusica.setTom(cursor.getString(2));

                Artista artista = new Artista();
                artista.setId(cursor.getString(3));
                artista = artistaDBHelper.carregar(context, artista);
                umMusica.setArtista(artista);

                umMusica.setStatus(cursor.getInt(4));

                if(cursor.getString(5) != null){
                    umMusica.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                }

                umMusica.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                umMusica.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));
                umMusica.setSlug(cursor.getString(8));

                musicas.add(umMusica);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicas;
    }

    public List<Musica> listarTodosAEnviar(){
        Cursor cursor = database.rawQuery("SELECT _id, nome, tom, id_artista, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, slug FROM musica WHERE enviado = -1", null);
        List<Musica> musicas = new ArrayList<Musica>();

        if(cursor.moveToFirst()){

            ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(context);

            do{
                Musica umMusica = new Musica();
                umMusica.setId(cursor.getString(0));

                umMusica.setNome(cursor.getString(1));
                umMusica.setTom(cursor.getString(2));

                Artista artista = new Artista();
                artista.setId(cursor.getString(3));
                artista = artistaDBHelper.carregar(context, artista);
                umMusica.setArtista(artista);

                umMusica.setStatus(cursor.getInt(4));

                if(cursor.getString(5) != null){
                    umMusica.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                }

                if(cursor.getString(6) != null){
                    umMusica.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                }

                umMusica.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));
                umMusica.setSlug(cursor.getString(8));

                musicas.add(umMusica);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicas;
    }

    public List<Musica> listarTodosPorSituacao(int situacao){
        Cursor cursor = database.rawQuery("SELECT _id, nome, tom, id_artista, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, slug FROM musica WHERE status = ?", new String[]{String.valueOf(situacao)});
        List<Musica> musicas = new ArrayList<Musica>();

        if(cursor.moveToFirst()){

            ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(context);

            do{
                Musica umMusica = new Musica();
                umMusica.setId(cursor.getString(0));

                umMusica.setNome(cursor.getString(1));
                umMusica.setTom(cursor.getString(2));

                Artista artista = new Artista();
                artista.setId(cursor.getString(3));
                artista = artistaDBHelper.carregar(context, artista);
                umMusica.setArtista(artista);

                umMusica.setStatus(cursor.getInt(4));

                if(cursor.getString(5) != null){
                    umMusica.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                }

                if(cursor.getString(6) != null){
                    umMusica.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                }

                umMusica.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));
                umMusica.setSlug(cursor.getString(8));

                musicas.add(umMusica);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicas;
    }

    public HashMap<Integer, Artista> contarTodosPorArtista(){
        Cursor cursor = database.rawQuery("SELECT COUNT(_id), id_artista FROM musica WHERE status != 2 " +
                "GROUP BY id_artista ORDER BY COUNT(_id)", null);
        List<Musica> musicas = new ArrayList<Musica>();
        HashMap<Integer, Artista> vetor = new HashMap<>();

        if(cursor.moveToFirst()){

            ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(context);

            do{
                Artista artista = new Artista();
                artista.setId(cursor.getString(1));
                artista = artistaDBHelper.carregar(context, artista);

                vetor.put(cursor.getInt(0), artista);

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return vetor;
    }

    public Musica carregar(Musica musica){
        Cursor cursor = database.rawQuery("SELECT _id, nome, tom, id_artista, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, slug FROM musica WHERE _id = ?", new String[]{musica.getId()});

        Musica umMusica = null;

        if(cursor.moveToFirst()){

            ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(context);

            do{
                umMusica = new Musica();
                umMusica.setId(cursor.getString(0));

                umMusica.setNome(cursor.getString(1));
                umMusica.setTom(cursor.getString(2));

                Artista artista = new Artista();
                artista.setId(cursor.getString(3));
                artista = artistaDBHelper.carregar(context, artista);
                umMusica.setArtista(artista);

                umMusica.setStatus(cursor.getInt(4));

                if(cursor.getString(5) != null){
                    umMusica.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                }

                if(cursor.getString(6) != null){
                    umMusica.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                }

                umMusica.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));
                umMusica.setSlug(cursor.getString(8));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umMusica;
    }

    public boolean jaExiste(Musica musica){
        Cursor cursor = database.rawQuery("SELECT _id, nome, tom, id_artista, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, slug FROM musica WHERE nome = ? AND id_artista = ?", new String[]{musica.getNome(), musica.getArtista().getId()});

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
