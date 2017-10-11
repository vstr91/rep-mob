package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.BlocoRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.model.dao.TempoBlocoRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.TempoMusicaEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 06/07/2017.
 */

public class TempoBlocoRepertorio extends EntidadeBase {

    private Calendar tempo;
    private BlocoRepertorio blocoRepertorio;
    private String audio;
    private Integer audioEnviado;
    private Integer audioRecebido;

    public Calendar getTempo() {
        return tempo;
    }

    public void setTempo(Calendar tempo) {
        this.tempo = tempo;
    }

    public BlocoRepertorio getBlocoRepertorio() {
        return blocoRepertorio;
    }

    public void setBlocoRepertorio(BlocoRepertorio blocoRepertorio) {
        this.blocoRepertorio = blocoRepertorio;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public Integer getAudioEnviado() {
        return audioEnviado;
    }

    public void setAudioEnviado(Integer audioEnviado) {
        this.audioEnviado = audioEnviado;
    }

    public Integer getAudioRecebido() {
        return audioRecebido;
    }

    public void setAudioRecebido(Integer audioRecebido) {
        this.audioRecebido = audioRecebido;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        TempoBlocoRepertorioDBHelper tbrDBHelper = new TempoBlocoRepertorioDBHelper(context);
        BlocoRepertorioDBHelper blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            TempoBlocoRepertorio umTbr = new TempoBlocoRepertorio();
            umTbr.setId(object.getString("id"));
            umTbr.setTempo(DataUtils.bancoParaData(object.getString("tempo")));

            BlocoRepertorio umBlocoRepertorio = new BlocoRepertorio();
            umBlocoRepertorio.setId(object.getString("bloco_repertorio"));
            umBlocoRepertorio = blocoRepertorioDBHelper.carregar(context, umBlocoRepertorio);

            umTbr.setBlocoRepertorio(umBlocoRepertorio);
            umTbr.setEnviado(0);
            umTbr.setAudio(object.getString("audio"));
            umTbr.setAudioEnviado(-1);
            umTbr.setAudioRecebido(-1);

            umTbr.setStatus(object.getInt("status"));
            umTbr.setDataRecebimento(Calendar.getInstance());
            umTbr.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));

            tbrDBHelper.salvarOuAtualizar(context, umTbr);

        }

    }

    public String toJson(){

        String resultado = "{\"id\": \""+this.getId()+"\", \"tempo\": \""+DataUtils.dataParaBanco(this.getTempo())+"\", " +
                "\"bloco_repertorio\": \""+this.getBlocoRepertorio().getId()+"\",  \"status\": "+this.getStatus()+", \"audio\": \""+this.getAudio()+"\"}";


        return resultado;
    }

}
