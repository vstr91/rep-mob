package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 30/12/2016.
 */

public class MusicaRepertorio extends EntidadeBase {

    private String observacao;
    private Integer ordem;
    private Musica musica;
    private Repertorio repertorio;

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    public Repertorio getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Repertorio repertorio) {
        this.repertorio = repertorio;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        MusicaRepertorioDBHelper musicaRepertorioDBHelper = new MusicaRepertorioDBHelper(context);
        MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
        RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            MusicaRepertorio umMusicaRepertorio = new MusicaRepertorio();
            umMusicaRepertorio.setId(object.getString("id"));
            umMusicaRepertorio.setObservacao(object.getString("observacao"));
            umMusicaRepertorio.setOrdem(object.getInt("ordem"));

            Musica umaMusica = new Musica();
            umaMusica.setId(object.getString("musica"));
            umaMusica = musicaDBHelper.carregar(context, umaMusica);

            umMusicaRepertorio.setMusica(umaMusica);

            Repertorio umRepertorio = new Repertorio();
            umRepertorio.setId(object.getString("repertorio"));
            umRepertorio = repertorioDBHelper.carregar(context, umRepertorio);

            umMusicaRepertorio.setRepertorio(umRepertorio);

            umMusicaRepertorio.setStatus(object.getInt("status"));
            umMusicaRepertorio.setDataRecebimento(Calendar.getInstance());
            umMusicaRepertorio.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));
            umMusicaRepertorio.setEnviado(0);

            musicaRepertorioDBHelper.salvarOuAtualizar(context, umMusicaRepertorio);

        }

    }

    public String toJson(){

        String resultado = "";

        resultado = "{\"id\": \""+this.getId()+"\", \"ordem\": "+this.getOrdem()+", " +
                "\"musica\": \""+this.getMusica().getId()+"\", \"repertorio\": \""+this.getRepertorio().getId()+"\", \"status\": "+this.getStatus()+"}";


        return resultado;
    }

}
