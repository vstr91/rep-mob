package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 30/12/2016.
 */

public class Repertorio extends EntidadeBase {

    private String nome;
    private Projeto projeto;
    private ArrayList<Musica> musicas;
    private String slug;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Musica> getMusicas() {
        return musicas;
    }

    public void setMusicas(ArrayList<Musica> musicas) {
        this.musicas = musicas;
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

    @Override
    public String toString() {
        return this.getNome();
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(context);
        ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            Repertorio umRepertorio = new Repertorio();
            umRepertorio.setId(object.getString("id"));
            umRepertorio.setNome(object.getString("nome"));
            umRepertorio.setSlug(object.getString("slug"));
            umRepertorio.setStatus(object.getInt("status"));

            Projeto umProjeto = new Projeto();
            umProjeto.setId(object.getString("projeto"));
            umProjeto = projetoDBHelper.carregar(context, umProjeto);

            umRepertorio.setProjeto(umProjeto);
            umRepertorio.setEnviado(0);
            umRepertorio.setDataRecebimento(Calendar.getInstance());
            umRepertorio.setUltimaAlteracao(DataUtils.apiParaData(object.getString("ultima_alteracao")));

            repertorioDBHelper.salvarOuAtualizar(context, umRepertorio);

        }

    }

    public String toJson(){

        String resultado = "";

        resultado = "{\"id\": \""+this.getId()+"\", \"nome\": \""+this.getNome()+"\", \"status\": "+this.getStatus()+", \"projeto\": \""+this.getProjeto().getId()+"\"}";


        return resultado;
    }

    @Override
    public boolean equals(Object o) {

        if(!(o instanceof Repertorio)){
            return false;
        }

        Repertorio repertorio = (Repertorio) o;
        return repertorio.getId().equals(this.getId());
    }
}
