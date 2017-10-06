package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.BlocoRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.MusicaBlocoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 30/12/2016.
 */

public class MusicaBloco extends EntidadeBase {

    private String observacao;
    private Integer ordem;
    private Musica musica;
    private BlocoRepertorio blocoRepertorio;

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

    public BlocoRepertorio getBlocoRepertorio() {
        return blocoRepertorio;
    }

    public void setBlocoRepertorio(BlocoRepertorio blocoRepertorio) {
        this.blocoRepertorio = blocoRepertorio;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        MusicaBlocoDBHelper musicaBlocoDBHelper = new MusicaBlocoDBHelper(context);
        MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
        BlocoRepertorioDBHelper blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            MusicaBloco umMusicaBloco = new MusicaBloco();
            umMusicaBloco.setId(object.getString("id"));
            umMusicaBloco.setObservacao(object.getString("observacao"));
            umMusicaBloco.setOrdem(object.getInt("ordem"));

            Musica umaMusica = new Musica();
            umaMusica.setId(object.getString("musica"));
            umaMusica = musicaDBHelper.carregar(context, umaMusica);

            umMusicaBloco.setMusica(umaMusica);

            BlocoRepertorio umBlocoRepertorio = new BlocoRepertorio();
            umBlocoRepertorio.setId(object.getString("bloco_repertorio"));
            umBlocoRepertorio = blocoRepertorioDBHelper.carregar(context, umBlocoRepertorio);

            umMusicaBloco.setBlocoRepertorio(umBlocoRepertorio);

            umMusicaBloco.setStatus(object.getInt("status"));
            umMusicaBloco.setDataRecebimento(Calendar.getInstance());
            umMusicaBloco.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));
            umMusicaBloco.setEnviado(0);

            musicaBlocoDBHelper.salvarOuAtualizar(context, umMusicaBloco);

        }

    }

    public String toJson(){

        String resultado = "";

        resultado = "{\"id\": \""+this.getId()+"\", \"ordem\": "+this.getOrdem()+", " +
                "\"musica\": \""+this.getMusica().getId()+"\", \"bloco_repertorio\": \""+this.getBlocoRepertorio().getId()+"\", \"status\": "+this.getStatus()+"}";


        return resultado;
    }

}
