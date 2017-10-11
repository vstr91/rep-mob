package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.model.dao.BlocoRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;
import br.com.vostre.repertori.model.dao.TempoBlocoRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.TempoMusicaEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 30/12/2016.
 */

public class BlocoRepertorio extends EntidadeBase {

    private String nome;
    private Integer ordem;
    private Repertorio repertorio;
    private String slug;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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
            umBlocoRepertorio.setSlug(object.getString("slug"));

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

    public Calendar calcularMedia(Context context){
        TempoBlocoRepertorioDBHelper tbrDBHelper = new TempoBlocoRepertorioDBHelper(context);
        List<TempoBlocoRepertorio> tbrs = tbrDBHelper.listarTodosPorBlocoRepertorio(context, this, 10);

        if(tbrs.size() > 0){
            long millis = 0;

            for(TempoBlocoRepertorio tbr : tbrs){
                Calendar tempo = tbr.getTempo();
                tempo.set(Calendar.DAY_OF_MONTH, 1);
                tempo.set(Calendar.MONTH, 1);
                tempo.set(Calendar.YEAR, 2000);

                millis += tempo.getTimeInMillis();
            }

            long result = millis / tbrs.size();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(result);

//            c.set(Calendar.DAY_OF_MONTH, 1);
//            c.set(Calendar.MONTH, 1);
//            c.set(Calendar.YEAR, 2000);
            return c;
        } else{
            return null;
        }

    }

}
