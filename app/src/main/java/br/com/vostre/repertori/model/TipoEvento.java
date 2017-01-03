package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.TipoEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 30/12/2016.
 */

public class TipoEvento extends EntidadeBase {

    private String nome;
    private String cor;
    private String slug;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        TipoEventoDBHelper tipoEventoDBHelper = new TipoEventoDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            TipoEvento umTipoEvento = new TipoEvento();
            umTipoEvento.setId(object.getString("id"));
            umTipoEvento.setNome(object.getString("nome"));
            umTipoEvento.setCor(object.getString("cor"));
            umTipoEvento.setSlug(object.getString("slug"));
            umTipoEvento.setStatus(object.getInt("status"));
            umTipoEvento.setDataRecebimento(Calendar.getInstance());
            umTipoEvento.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));

            tipoEventoDBHelper.salvarOuAtualizar(context, umTipoEvento);

        }

    }

}
