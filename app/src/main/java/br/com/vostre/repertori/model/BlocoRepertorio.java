package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.BlocoRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 30/12/2016.
 */

public class BlocoRepertorio extends EntidadeBase {

    private String nome;
    private Integer ordem;
    private Repertorio repertorio;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Repertorio getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Repertorio repertorio) {
        this.repertorio = repertorio;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        BlocoRepertorioDBHelper blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(context);
        RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            BlocoRepertorio umBlocoRepertorio = new BlocoRepertorio();
            umBlocoRepertorio.setId(object.getString("id"));
            umBlocoRepertorio.setNome(object.getString("nome"));
            umBlocoRepertorio.setOrdem(object.getInt("ordem"));

            Repertorio umRepertorio = new Repertorio();
            umRepertorio.setId(object.getString("repertorio"));
            umRepertorio = repertorioDBHelper.carregar(context, umRepertorio);

            umBlocoRepertorio.setRepertorio(umRepertorio);

            umBlocoRepertorio.setStatus(object.getInt("status"));
            umBlocoRepertorio.setDataRecebimento(Calendar.getInstance());
            umBlocoRepertorio.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));
            umBlocoRepertorio.setEnviado(0);

            blocoRepertorioDBHelper.salvarOuAtualizar(context, umBlocoRepertorio);

        }

    }

    public String toJson(){

        String resultado = "";

        resultado = "{\"id\": \""+this.getId()+"\", \"ordem\": "+this.getOrdem()+", " +
                "\"nome\": \""+this.getNome()+"\", \"repertorio\": \""+this.getRepertorio().getId()+"\", \"status\": "+this.getStatus()+"}";


        return resultado;
    }

}
