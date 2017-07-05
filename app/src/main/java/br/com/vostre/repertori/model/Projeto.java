package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 27/06/2017.
 */

public class Projeto extends EntidadeBase {

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

        ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            Projeto umProjeto = new Projeto();
            umProjeto.setId(object.getString("id"));
            umProjeto.setNome(object.getString("nome"));
            umProjeto.setSlug(object.getString("slug"));
            umProjeto.setStatus(object.getInt("status"));
            umProjeto.setEnviado(0);
            umProjeto.setDataRecebimento(Calendar.getInstance());
            umProjeto.setUltimaAlteracao(DataUtils.apiParaData(object.getString("ultima_alteracao")));

            projetoDBHelper.salvarOuAtualizar(context, umProjeto);

        }

    }

    public String toJson(){

        String resultado = "";

        resultado = "{\"id\": \""+this.getId()+"\", \"nome\": \""+this.getNome()+"\", \"status\": "+this.getStatus()+"\"}";


        return resultado;
    }

    @Override
    public String toString() {
        return this.getNome();
    }

    @Override
    public boolean equals(Object o) {

        if(!(o instanceof Projeto)){
            return false;
        }

        Projeto projeto = (Projeto) o;
        return projeto.getId().equals(this.getId());
    }
    
}
