package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 30/12/2016.
 */

public class MusicaEvento extends EntidadeBase {

    private String observacao;
    private Integer ordem;
    private Musica musica;
    private Evento evento;

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

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        MusicaEventoDBHelper musicaEventoDBHelper = new MusicaEventoDBHelper(context);
        MusicaDBHelper musicaDBHelper = new MusicaDBHelper(context);
        EventoDBHelper eventoDBHelper = new EventoDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            MusicaEvento umMusicaEvento = new MusicaEvento();
            umMusicaEvento.setIdRemoto(object.getInt("id"));
            umMusicaEvento.setObservacao(object.getString("observacao"));
            umMusicaEvento.setOrdem(object.getInt("ordem"));

            Musica umaMusica = new Musica();
            umaMusica.setIdRemoto(object.getInt("id_musica"));
            umaMusica = musicaDBHelper.carregar(context, umaMusica);

            umMusicaEvento.setMusica(umaMusica);

            Evento umEvento = new Evento();
            umEvento.setIdRemoto(object.getInt("id_evento"));
            umEvento = eventoDBHelper.carregar(context, umEvento);

            umMusicaEvento.setEvento(umEvento);

            umMusicaEvento.setStatus(object.getInt("status"));
            umMusicaEvento.setDataRecebimento(Calendar.getInstance());
            umMusicaEvento.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));

            musicaEventoDBHelper.salvarOuAtualizar(context, umMusicaEvento);

        }

    }

}
