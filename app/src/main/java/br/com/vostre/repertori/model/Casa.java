package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.CasaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 30/12/2016.
 */

public class Casa extends EntidadeBase {

    private String nome;
    private ArrayList<Evento> eventos;
    private String slug;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(ArrayList<Evento> eventos) {
        this.eventos = eventos;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public String toString() {
        return this.getNome();
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        CasaDBHelper casaDBHelper = new CasaDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            Casa umCasa = new Casa();
            umCasa.setId(object.getString("id"));
            umCasa.setNome(object.getString("nome"));
            umCasa.setSlug(object.getString("slug"));
            umCasa.setStatus(object.getInt("status"));
            umCasa.setEnviado(0);
            umCasa.setDataRecebimento(Calendar.getInstance());
            umCasa.setUltimaAlteracao(DataUtils.apiParaData(object.getString("ultima_alteracao")));

            casaDBHelper.salvarOuAtualizar(context, umCasa);

        }

    }

    public String toJson(){

        String resultado = "";

        resultado = "{\"id\": \""+this.getId()+"\", \"nome\": \""+this.getNome()+"\", \"status\": "+this.getStatus()+"}";


        return resultado;
    }

    @Override
    public boolean equals(Object o) {

        if(!(o instanceof Casa)){
            return false;
        }

        Casa casa = (Casa) o;
        return casa.getId().equals(this.getId());
    }
}
