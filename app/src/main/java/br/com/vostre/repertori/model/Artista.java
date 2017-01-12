package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 30/12/2016.
 */

public class Artista extends EntidadeBase {

    private String nome;
    private ArrayList<Musica> musicas;
    private String slug;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Musica> getMusicas() {
        return musicas;
    }

    public void setMusicas(ArrayList<Musica> musicas) {
        this.musicas = musicas;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public String toString() {
        return this.getNome();
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            Artista umArtista = new Artista();
            umArtista.setId(object.getString("id"));
            umArtista.setNome(object.getString("nome"));
            umArtista.setSlug(object.getString("slug"));
            umArtista.setStatus(object.getInt("status"));
            umArtista.setDataRecebimento(Calendar.getInstance());
            umArtista.setUltimaAlteracao(DataUtils.apiParaData(object.getString("ultima_alteracao")));

            artistaDBHelper.salvarOuAtualizar(context, umArtista);

        }

    }

}
