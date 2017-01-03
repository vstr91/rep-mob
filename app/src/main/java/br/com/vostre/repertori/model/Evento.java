package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.TipoEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 30/12/2016.
 */

public class Evento extends EntidadeBase {

    private String nome;
    private Calendar data;
    private TipoEvento tipoEvento;
    private String slug;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        EventoDBHelper eventoDBHelper = new EventoDBHelper(context);
        TipoEventoDBHelper tipoEventoDBHelper = new TipoEventoDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            Evento umEvento = new Evento();
            umEvento.setId(object.getString("id"));
            umEvento.setNome(object.getString("nome"));
            umEvento.setSlug(object.getString("slug"));
            umEvento.setData(DataUtils.bancoParaData(object.getString("data")));

            TipoEvento umTipoEvento = new TipoEvento();
            umTipoEvento.setId(object.getString("tipo_evento"));
            umTipoEvento = tipoEventoDBHelper.carregar(context, umTipoEvento);

            umEvento.setTipoEvento(umTipoEvento);

            umEvento.setStatus(object.getInt("status"));
            umEvento.setDataRecebimento(Calendar.getInstance());
            umEvento.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));

            eventoDBHelper.salvarOuAtualizar(context, umEvento);

        }

    }
    
}
