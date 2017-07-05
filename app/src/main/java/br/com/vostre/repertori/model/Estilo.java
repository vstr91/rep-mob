package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.EstiloDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 27/06/2017.
 */

public class Estilo extends EntidadeBase {

    private String nome;
    private String slug;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        EstiloDBHelper estiloDBHelper = new EstiloDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            Estilo umEstilo = new Estilo();
            umEstilo.setId(object.getString("id"));
            umEstilo.setNome(object.getString("nome"));
            umEstilo.setSlug(object.getString("slug"));
            umEstilo.setStatus(object.getInt("status"));
            umEstilo.setEnviado(0);
            umEstilo.setDataRecebimento(Calendar.getInstance());
            umEstilo.setUltimaAlteracao(DataUtils.apiParaData(object.getString("ultima_alteracao")));

            estiloDBHelper.salvarOuAtualizar(context, umEstilo);

        }

    }

    public String toJson(){

        String resultado = "";

        resultado = "{\"id\": \""+this.getId()+"\", \"nome\": \""+this.getNome()+"\", \"status\": "+this.getStatus()+"}";


        return resultado;
    }

    @Override
    public String toString() {
        return this.getNome();
    }

    @Override
    public boolean equals(Object o) {

        if(!(o instanceof Estilo)){
            return false;
        }

        Estilo estilo = (Estilo) o;
        return estilo.getId().equals(this.getId());
    }
    
}
