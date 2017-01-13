package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 30/12/2016.
 */

public class Musica extends EntidadeBase {

    private String nome;
    private String tom;
    private String slug;
    private Artista artista;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTom() {
        return tom;
    }

    public void setTom(String tom) {
        this.tom = tom;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
        ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            Musica umMusica = new Musica();
            umMusica.setId(object.getString("id"));
            umMusica.setNome(object.getString("nome"));
            umMusica.setSlug(object.getString("slug"));
            umMusica.setTom(object.getString("tom"));

            Artista umArtista = new Artista();
            umArtista.setId(object.getString("artista"));
            umArtista = artistaDBHelper.carregar(context, umArtista);

            umMusica.setEnviado(0);
            umMusica.setArtista(umArtista);
            umMusica.setStatus(object.getInt("status"));
            umMusica.setDataRecebimento(Calendar.getInstance());
            umMusica.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));

            musicaDBHelper.salvarOuAtualizar(context, umMusica);

        }

    }

    public String toJson(){

        String resultado = "";

        resultado = "{\"id\": \""+this.getId()+"\", \"nome\": \""+this.getNome()+"\", \"tom\": \""+this.getTom()+"\", " +
                "\"artista\": \""+this.getArtista().getId()+"\", \"status\": "+this.getStatus()+",  " +
                "\"data_cadastro\": \""+ DataUtils.dataParaBanco(this.getDataCadastro())+"\"}";


        return resultado;
    }

}
