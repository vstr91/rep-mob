package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.ContatoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 30/12/2016.
 */

public class Contato extends EntidadeBase {

    private String nome;
    private String telefone;
    private String email;
    private String observacao;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return this.getNome();
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        ContatoDBHelper contatoDBHelper = new ContatoDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            Contato umContato = new Contato();
            umContato.setId(object.getString("id"));
            umContato.setNome(object.getString("nome"));
            umContato.setTelefone(object.getString("telefone"));
            umContato.setEmail(object.getString("email"));
            umContato.setObservacao(object.getString("observacao"));
            umContato.setStatus(object.getInt("status"));
            umContato.setEnviado(0);
            umContato.setDataRecebimento(Calendar.getInstance());
            umContato.setUltimaAlteracao(DataUtils.apiParaData(object.getString("ultima_alteracao")));

            contatoDBHelper.salvarOuAtualizar(context, umContato);

        }

    }

    public String toJson(){

        String resultado = "";

        resultado = "{\"id\": \""+this.getId()+"\", \"nome\": \""+this.getNome()+"\", \"telefone\": \"" + this.getTelefone() + "\", \"email\": \"" + this.getEmail() + "\", " +
                "\"observacao\": \"" + this.getObservacao() + "\", " +
                "\"status\": "+this.getStatus()+"}";


        return resultado;
    }

    @Override
    public boolean equals(Object o) {

        if(!(o instanceof Contato)){
            return false;
        }

        Contato contato = (Contato) o;
        return contato.getId().equals(this.getId());
    }
}
