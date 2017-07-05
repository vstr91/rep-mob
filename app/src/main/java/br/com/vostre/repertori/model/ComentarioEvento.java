package br.com.vostre.repertori.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.ComentarioEventoDBHelper;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

/**
 * Created by Almir on 30/12/2016.
 */

public class ComentarioEvento extends EntidadeBase {

    private String texto;
    private Evento evento;

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public void atualizarDados(JSONArray dados, int qtdDados, ProgressDialog progressDialog, Context context) throws JSONException {

        ComentarioEventoDBHelper comentarioEventoDBHelper = new ComentarioEventoDBHelper(context);
        EventoDBHelper eventoDBHelper = new EventoDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            JSONObject object =  dados.getJSONObject(i);
            ComentarioEvento umComentarioEvento = new ComentarioEvento();
            umComentarioEvento.setId(object.getString("id"));
            umComentarioEvento.setTexto(object.getString("texto"));

            Evento umEvento = new Evento();
            umEvento.setId(object.getString("evento"));
            umEvento = eventoDBHelper.carregar(context, umEvento);

            umComentarioEvento.setEvento(umEvento);
            umComentarioEvento.setStatus(object.getInt("status"));
            umComentarioEvento.setDataRecebimento(Calendar.getInstance());
            umComentarioEvento.setUltimaAlteracao(DataUtils.bancoParaData(object.getString("ultima_alteracao")));
            umComentarioEvento.setEnviado(0);

            comentarioEventoDBHelper.salvarOuAtualizar(context, umComentarioEvento);

        }

    }

    public void atualizarDadosEnviados(JSONArray dados, int qtdDados, Context context) throws JSONException {

        ComentarioEventoDBHelper comentarioEventoDBHelper = new ComentarioEventoDBHelper(context);
        EventoDBHelper eventoDBHelper = new EventoDBHelper(context);

        for(int i = 0; i < qtdDados; i++){

            //progressDialog.setProgress(i+1);

            String id =  dados.getString(i);
            ComentarioEvento umComentarioEvento = new ComentarioEvento();
            umComentarioEvento.setId(id);

            umComentarioEvento = comentarioEventoDBHelper.carregar(context, umComentarioEvento);
            umComentarioEvento.setEnviado(0);

            comentarioEventoDBHelper.salvarOuAtualizar(context, umComentarioEvento);

        }

    }

    public String toJson(){

        String resultado = "";

        resultado = "{\"id\": \""+this.getId()+"\", \"texto\": \""+this.getTexto()+"\", \"evento\": \""+this.getEvento().getId()+"\"}";


        return resultado;
    }

}
