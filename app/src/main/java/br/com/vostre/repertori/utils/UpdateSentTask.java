package br.com.vostre.repertori.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.vostre.repertori.listener.UpdateSentTaskListener;
import br.com.vostre.repertori.listener.UpdateTaskListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.ComentarioEvento;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.TipoEvento;
import br.com.vostre.repertori.model.dao.RepDBHelper;

/**
 * Created by Almir on 02/01/2015.
 */
public class UpdateSentTask extends AsyncTask<String, String, Boolean> {

//    TextView viewLog;
    JSONObject jObj;
    Context ctx;
    ProgressDialog progressDialog;
    int index = 0;
    UpdateSentTaskListener listener;

    public UpdateSentTask(JSONObject obj, Context context){
        this.jObj = obj;
        this.ctx = context;
    }

    public void setOnResultsSentListener(UpdateSentTaskListener listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

//        viewLog.append("Fim da atualização."+System.getProperty("line.separator"));

//        progressDialog.dismiss();
//        progressDialog = null;
        listener.onUpdateSentTaskResultsSucceeded(aBoolean);

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
//        progressDialog.dismiss();
//        progressDialog = null;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        try{

            // Arrays recebidos pela API
            JSONArray processadas = jObj.getJSONArray("processadas");
//            JSONArray tiposEvento = jObj.getJSONArray("tipos_eventos");
//            JSONArray eventos = jObj.getJSONArray("eventos");
//            JSONArray musicas = jObj.getJSONArray("musicas");
//            JSONArray musicasEventos = jObj.getJSONArray("musicas_eventos");
//            JSONArray comentariosEventos = jObj.getJSONArray("comentarios_eventos");

            // Objetos que contem os metodos de atualizacao
//            Artista artista = new Artista();
//            TipoEvento tipoEvento = new TipoEvento();
//            Evento evento = new Evento();
//            Musica musica = new Musica();
//            MusicaEvento musicaEvento = new MusicaEvento();
            ComentarioEvento comentarioEvento = new ComentarioEvento();

            RepDBHelper repDBHelper = new RepDBHelper(ctx);

            int qtdComentariosEventos = processadas.length();

            if(qtdComentariosEventos > 0){
                //mostraProgressBar(progressDialog, qtdComentariosEventos, "Atualizando Comentários Eventos...");

                comentarioEvento.atualizarDadosEnviados(processadas, qtdComentariosEventos, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdComentariosEventos+" Comentário(s) Evento(s) atualizada(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"");
            } else{
                publishProgress("Comentários Eventos já atualizadas."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"");
            }

        } /*catch (ParseException e) {
            e.printStackTrace();
            return false;
        }*/ catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        //super.onProgressUpdate(values);
//        viewLog.append(values[0]);

//        if(!values[1].equals("")){
//            progressDialog.setMessage(values[1]);
//        }

    }

    private void mostraProgressBar(ProgressDialog progressDialog, int qtd, String mensagem){
        //progressDialog.setMessage(mensagem);
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(qtd);
        //progressDialog.show();
    }

    private void escondeProgressBar(ProgressDialog progressDialog){
        progressDialog.setProgress(0);
        //progressDialog.dismiss();
    }

}
