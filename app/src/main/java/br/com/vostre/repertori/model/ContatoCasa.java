package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.CasaDBHelper;
import br.com.vostre.repertori.model.dao.ContatoCasaDBHelper;
import br.com.vostre.repertori.model.dao.ContatoDBHelper;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 30/12/2016.
 */

public class ContatoCasa extends EntidadeBase {

    private String observacao;
    private String cargo;
    private Contato contato;
    private Casa casa;

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public Casa getCasa() {
        return casa;
    }

    public void setCasa(Casa casa) {
        this.casa = casa;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        ContatoCasaDBHelper contatoCasaDBHelper = new ContatoCasaDBHelper(context);
        ContatoDBHelper contatoDBHelper = new ContatoDBHelper(context);
        CasaDBHelper casaDBHelper = new CasaDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            ContatoCasa umContatoCasa = new ContatoCasa();
            umContatoCasa.setId(object.getString("id"));
            umContatoCasa.setObservacao(object.getString("observacao"));
            umContatoCasa.setCargo(object.getString("cargo"));

            Contato umContato = new Contato();
            umContato.setId(object.getString("contato"));
            umContato = contatoDBHelper.carregar(context, umContato);

            umContatoCasa.setContato(umContato);

            Casa umCasa = new Casa();
            umCasa.setId(object.getString("casa"));
            umCasa = casaDBHelper.carregar(context, umCasa);

            umContatoCasa.setCasa(umCasa);

            umContatoCasa.setStatus(object.getInt("status"));
            umContatoCasa.setDataRecebimento(Calendar.getInstance());
            umContatoCasa.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));
            umContatoCasa.setEnviado(0);

            contatoCasaDBHelper.salvarOuAtualizar(context, umContatoCasa);

        }

    }

    public String toJson(){

        String resultado = "";

        String cargo = this.getCargo().isEmpty() || this.getCargo() == null ? "" : this.getCargo();
        String observacao = this.getObservacao().isEmpty() ? "" : this.getObservacao();

        resultado = "{\"id\": \""+this.getId()+"\", \"cargo\": \""+cargo+"\", " +
                "\"contato\": \""+this.getContato().getId()+"\", \"casa\": \""+this.getCasa().getId()+"\", \"observacao\": \"" + observacao + "\", \"status\": "+this.getStatus()+"}";


        return resultado;
    }

}
