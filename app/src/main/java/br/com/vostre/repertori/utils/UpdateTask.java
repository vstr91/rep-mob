package br.com.vostre.repertori.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import br.com.vostre.repertori.listener.UpdateTaskListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.ComentarioEvento;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.TipoEvento;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.RepDBHelper;

/**
 * Created by Almir on 02/01/2015.
 */
public class UpdateTask extends AsyncTask<String, String, Boolean> {

//    TextView viewLog;
    JSONObject jObj;
    Context ctx;
    ProgressDialog progressDialog;
    int index = 0;
    UpdateTaskListener listener;

    public UpdateTask(/*TextView umaViewLog,*/ JSONObject obj, Context context){
//        this.viewLog = umaViewLog;
        this.jObj = obj;
        this.ctx = context;
        //this.progressDialog = progressDialog;
    }

    public void setOnResultsListener(UpdateTaskListener listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {

        //super.onPreExecute();
//        progressDialog = new ProgressDialog(ctx);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setMessage("Progresso");
//        progressDialog.setCancelable(false);
//
//        if(!progressDialog.isShowing()){
//            progressDialog.show();
//        }

//        viewLog.setText("");
//        viewLog.append("Iniciando atualização."+System.getProperty("line.separator")+System.getProperty("line.separator"));
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

//        viewLog.append("Fim da atualização."+System.getProperty("line.separator"));

//        progressDialog.dismiss();
//        progressDialog = null;
        listener.onUpdateTaskResultsSucceeded(aBoolean);

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressDialog.dismiss();
        progressDialog = null;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        try{

            // Arrays recebidos pela API
            JSONArray artistas = jObj.getJSONArray("artistas");
            JSONArray tiposEvento = jObj.getJSONArray("tipos_eventos");
            JSONArray eventos = jObj.getJSONArray("eventos");
            JSONArray musicas = jObj.getJSONArray("musicas");
            JSONArray musicasEventos = jObj.getJSONArray("musicas_eventos");
            JSONArray comentariosEventos = jObj.getJSONArray("comentarios_eventos");

            // Objetos que contem os metodos de atualizacao
            Artista artista = new Artista();
            TipoEvento tipoEvento = new TipoEvento();
            Evento evento = new Evento();
            Musica musica = new Musica();
            MusicaEvento musicaEvento = new MusicaEvento();
            ComentarioEvento comentarioEvento = new ComentarioEvento();

            RepDBHelper repDBHelper = new RepDBHelper(ctx);

            int qtdArtistas = artistas.length();

            if(qtdArtistas > 0){
                //mostraProgressBar(progressDialog, qtdArtistas, "Atualizando Artistas...");

                artista.atualizarDados(artistas, qtdArtistas, progressDialog, ctx);

                //escondeProgressBar(progressDialog);
                publishProgress(qtdArtistas+" artista(s) atualizado(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Tipos de Evento");
            } else{
                publishProgress("Artistas já atualizados."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Tipos de Evento");
            }

            int qtdTiposEvento = tiposEvento.length();

            if(qtdTiposEvento > 0){
                //mostraProgressBar(progressDialog, qtdTiposEvento, "Atualizando Tipos de Evento...");

                tipoEvento.atualizarDados(tiposEvento, qtdTiposEvento, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdTiposEvento+ " tipo(s) de evento atualizado(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Eventos");
            } else{
                publishProgress("Tipos de Evento já atualizados."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Eventos");
            }

            int qtdEventos = eventos.length();

            if(qtdEventos > 0){
                //mostraProgressBar(progressDialog, qtdEventos, "Atualizando Eventos...");

                evento.atualizarDados(eventos, qtdEventos, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdEventos+ " evento(s) atualizado(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Músicas");
            } else{
                publishProgress("Eventos já atualizados."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Músicas");
            }

            int qtdMusicas = musicas.length();

            if(qtdMusicas > 0){
                //mostraProgressBar(progressDialog, qtdMusicas, "Atualizando Musicas...");

                musica.atualizarDados(musicas, qtdMusicas, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdMusicas+" música(s) atualizada(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Músicas Eventos");
            } else{
                publishProgress("Músicas já atualizados."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Músicas Eventos");
            }

            int qtdMusicasEventos = musicasEventos.length();

            if(qtdMusicasEventos > 0){
                //mostraProgressBar(progressDialog, qtdMusicasEventos, "Atualizando Músicas Eventos...");

                musicaEvento.atualizarDados(musicasEventos, qtdMusicasEventos, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdMusicasEventos+" música(s) evento(s) atualizada(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Comentários Eventos");
            } else{
                publishProgress("Músicas Eventos já atualizadas."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Comentários Eventos");
            }

            int qtdComentariosEventos = comentariosEventos.length();

            if(qtdComentariosEventos > 0){
                //mostraProgressBar(progressDialog, qtdComentariosEventos, "Atualizando Comentários Eventos...");

                comentarioEvento.atualizarDados(comentariosEventos, qtdComentariosEventos, progressDialog, ctx);

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
