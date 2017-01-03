package br.com.vostre.repertori.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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
        }

        cv.put("id_remoto", musica.getIdRemoto());
        cv.put("nome", musica.getNome());
        cv.put("tom", musica.getTom());
        cv.put("id_artista", musica.getArtista().getId());
        cv.put("status", musica.getStatus());
        cv.put("data_cadastro", DataUtils.dataParaBanco(musica.getDataCadastro()));
        cv.put("data_recebimento", DataUtils.dataParaBanco(musica.getDataRecebimento()));
        cv.put("ultima_alteracao", DataUtils.dataParaBanco(musica.getUltimaAlteracao()));

        if(database.update("musica", cv, "id_remoto = "+musica.getIdRemoto(), null) < 1){
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
                "ultima_alteracao, id_remoto FROM musica", null);
        List<Musica> musicas = new ArrayList<Musica>();

        if(cursor.moveToFirst()){

            ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(context);

            do{
                Musica umMusica = new Musica();
                umMusica.setId(cursor.getInt(0));

                umMusica.setNome(cursor.getString(1));
                umMusica.setTom(cursor.getString(2));

                Artista artista = new Artista();
                artista.setId(cursor.getInt(3));
                artista = artistaDBHelper.carregar(context, artista);
                umMusica.setArtista(artista);

                umMusica.setStatus(cursor.getInt(4));

                umMusica.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                umMusica.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                umMusica.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));
                umMusica.setIdRemoto(cursor.getInt(8));

                musicas.add(umMusica);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return musicas;
    }

    public Musica carregar(Musica musica){
        Cursor cursor = database.rawQuery("SELECT _id, nome, tom, id_artista, status, data_cadastro, data_recebimento, " +
                "ultima_alteracao, id_remoto FROM musica WHERE id_remoto = ?", new String[]{String.valueOf(musica.getIdRemoto())});

        Musica umMusica = null;

        if(cursor.moveToFirst()){

            ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(context);

            do{
                umMusica = new Musica();
                umMusica.setId(cursor.getInt(0));

                umMusica.setNome(cursor.getString(1));
                umMusica.setTom(cursor.getString(2));

                Artista artista = new Artista();
                artista.setId(cursor.getInt(3));
                artista = artistaDBHelper.carregar(context, artista);
                umMusica.setArtista(artista);

                umMusica.setStatus(cursor.getInt(4));

                umMusica.setDataCadastro(DataUtils.bancoParaData(cursor.getString(5)));
                umMusica.setDataRecebimento(DataUtils.bancoParaData(cursor.getString(6)));
                umMusica.setUltimaAlteracao(DataUtils.bancoParaData(cursor.getString(7)));
                umMusica.setIdRemoto(cursor.getInt(8));

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return umMusica;
    }

}
