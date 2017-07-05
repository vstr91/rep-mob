package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.EstiloDBHelper;
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
    private Estilo estilo;

    private boolean checked;

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

    public Estilo getEstilo() {
        return estilo;
    }

    public void setEstilo(Estilo estilo) {
        this.estilo = estilo;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
        ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(context);
        EstiloDBHelper estiloDBHelper = new EstiloDBHelper(context);

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

            if(object.getString("estilo") != "null"){
                Estilo umEstilo = new Estilo();
                umEstilo.setId(object.getString("estilo"));
                umEstilo = estiloDBHelper.carregar(context, umEstilo);
                umMusica.setEstilo(umEstilo);
            }

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

        String estilo = this.getEstilo() == null ? "null" : this.getEstilo().getId();

        resultado = "{\"id\": \""+this.getId()+"\", \"nome\": \""+this.getNome()+"\", \"tom\": \""+this.getTom()+"\", " +
                "\"artista\": \""+this.getArtista().getId()+"\",  \"status\": "+this.getStatus()+", \"estilo\": \""+estilo+"\"}";


        return resultado;
    }

    @Override
    public boolean equals(Object o) {

        if(!(o instanceof Musica)){
            return false;
        }

        Musica musica = (Musica) o;
        return musica.getId().equals(this.getId());
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}
