package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.EstiloMusicaDBHelper;
import br.com.vostre.repertori.model.dao.EstiloDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 27/06/2017.
 */

public class EstiloMusica extends EntidadeBase {

    private String observacao;
    private Estilo estilo;
    private Musica musica;

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Estilo getEstilo() {
        return estilo;
    }

    public void setEstilo(Estilo estilo) {
        this.estilo = estilo;
    }

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        EstiloMusicaDBHelper estiloMusicaDBHelper = new EstiloMusicaDBHelper(context);
        MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
        EstiloDBHelper estiloDBHelper = new EstiloDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            EstiloMusica umEstiloMusica = new EstiloMusica();
            umEstiloMusica.setId(object.getString("id"));

            Musica umaMusica = new Musica();
            umaMusica.setId(object.getString("musica"));
            umaMusica = musicaDBHelper.carregar(context, umaMusica);

            umEstiloMusica.setMusica(umaMusica);

            Estilo umEstilo = new Estilo();
            umEstilo.setId(object.getString("estilo"));
            umEstilo = estiloDBHelper.carregar(context, umEstilo);

            umEstiloMusica.setEstilo(umEstilo);

            umEstiloMusica.setStatus(object.getInt("status"));
            umEstiloMusica.setDataRecebimento(Calendar.getInstance());
            umEstiloMusica.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));
            umEstiloMusica.setEnviado(0);

            estiloMusicaDBHelper.salvarOuAtualizar(context, umEstiloMusica);

        }

    }

    public String toJson(){

        String resultado = "";

        resultado = "{\"id\": \""+this.getId()+"\", \"ordem\": "+
                "\"musica\": \""+this.getMusica().getId()+"\", \"estilo\": \""+this.getEstilo().getId()+"\", \"status\": "+this.getStatus()+"}";


        return resultado;
    }
    
}
