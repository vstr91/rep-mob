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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        TipoEventoDBHelper tipoEventoDBHelper = new TipoEventoDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            TipoEvento umTipoEvento = new TipoEvento();
            umTipoEvento.setIdRemoto(object.getInt("id"));
            umTipoEvento.setNome(object.getString("nome"));
            umTipoEvento.setStatus(object.getInt("status"));
            umTipoEvento.setDataRecebimento(Calendar.getInstance());
            umTipoEvento.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));

            tipoEventoDBHelper.salvarOuAtualizar(context, umTipoEvento);

        }

    }

}
