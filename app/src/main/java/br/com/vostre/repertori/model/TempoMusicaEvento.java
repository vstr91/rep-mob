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
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.model.dao.TempoMusicaEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 06/07/2017.
 */

public class TempoMusicaEvento extends EntidadeBase {

    private Calendar tempo;
    private MusicaEvento musicaEvento;
    private String audio;
    private Integer audioEnviado;
    private Integer audioRecebido;

    public Calendar getTempo() {
        return tempo;
    }

    public void setTempo(Calendar tempo) {
        this.tempo = tempo;
    }

    public MusicaEvento getMusicaEvento() {
        return musicaEvento;
    }

    public void setMusicaEvento(MusicaEvento musicaEvento) {
        this.musicaEvento = musicaEvento;
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

        TempoMusicaEventoDBHelper tmeDBHelper = new TempoMusicaEventoDBHelper(context);
        MusicaEventoDBHelper musicaEventoDBHelper = new MusicaEventoDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            TempoMusicaEvento umTme = new TempoMusicaEvento();
            umTme.setId(object.getString("id"));
            umTme.setTempo(DataUtils.bancoParaData(object.getString("tempo")));

            MusicaEvento umMusicaEvento = new MusicaEvento();
            umMusicaEvento.setId(object.getString("musica_evento"));
            umMusicaEvento = musicaEventoDBHelper.carregar(context, umMusicaEvento);

            umTme.setMusicaEvento(umMusicaEvento);
            umTme.setEnviado(0);
            umTme.setAudio(object.getString("audio"));
            umTme.setAudioEnviado(-1);
            umTme.setAudioRecebido(-1);

            umTme.setStatus(object.getInt("status"));
            umTme.setDataRecebimento(Calendar.getInstance());
            umTme.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));

            tmeDBHelper.salvarOuAtualizar(context, umTme);

        }

    }

    public String toJson(){

        String resultado = "{\"id\": \""+this.getId()+"\", \"tempo\": \""+DataUtils.dataParaBanco(this.getTempo())+"\", " +
                "\"musica_evento\": \""+this.getMusicaEvento().getId()+"\",  \"status\": "+this.getStatus()+", \"audio\": \""+this.getAudio()+"\"}";


        return resultado;
    }

}
