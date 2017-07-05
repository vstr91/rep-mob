package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
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
    private Projeto projeto;

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

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        EventoDBHelper eventoDBHelper = new EventoDBHelper(context);
        TipoEventoDBHelper tipoEventoDBHelper = new TipoEventoDBHelper(context);
        ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(context);

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

            Projeto umProjeto = new Projeto();
            umProjeto.setId(object.getString("projeto"));
            umProjeto = projetoDBHelper.carregar(context, umProjeto);

            umEvento.setProjeto(umProjeto);

            umEvento.setStatus(object.getInt("status"));
            umEvento.setDataRecebimento(Calendar.getInstance());
            umEvento.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));
            umEvento.setEnviado(0);

            eventoDBHelper.salvarOuAtualizar(context, umEvento);

        }

    }

    public String toJson(){

        String resultado = "";

        resultado = "{\"id\": \""+this.getId()+"\", \"nome\": \""+this.getNome()+"\", \"data\": \""+DataUtils.dataParaBanco(this.getData())+"\", " +
                "\"tipo_evento\": \""+this.getTipoEvento().getId()+"\", \"status\": "+this.getStatus()+", \"projeto\": \""+this.getProjeto().getId() + "\"}";


        return resultado;
    }
    
}
