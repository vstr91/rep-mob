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
import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.ComentarioEvento;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.EstiloMusica;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaBloco;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.MusicaProjeto;
import br.com.vostre.repertori.model.MusicaRepertorio;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.TempoBlocoRepertorio;
import br.com.vostre.repertori.model.TempoMusicaEvento;
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

            JSONArray estilos = jObj.getJSONArray("estilos");
            //JSONArray estilosMusicas = jObj.getJSONArray("estilos_musicas");
            JSONArray projetos = jObj.getJSONArray("projetos");
            JSONArray musicasProjetos = jObj.getJSONArray("musicas_projetos");
            JSONArray temposMusicasEventos = jObj.getJSONArray("tempos_musicas_eventos");

            JSONArray repertorios = jObj.getJSONArray("repertorios");
            JSONArray musicasRepertorios = jObj.getJSONArray("musicas_repertorios");

            JSONArray blocosRepertorios = jObj.getJSONArray("blocos_repertorios");
            JSONArray musicasBlocos = jObj.getJSONArray("musicas_blocos_repertorios");
            JSONArray temposBlocosRepertorios = jObj.getJSONArray("tempos_blocos_repertorios");

            // Objetos que contem os metodos de atualizacao
            Artista artista = new Artista();
            TipoEvento tipoEvento = new TipoEvento();
            Evento evento = new Evento();
            Musica musica = new Musica();
            MusicaEvento musicaEvento = new MusicaEvento();
            ComentarioEvento comentarioEvento = new ComentarioEvento();

            Estilo estilo = new Estilo();
            Projeto projeto = new Projeto();
            EstiloMusica estiloMusica = new EstiloMusica();
            MusicaProjeto musicaProjeto = new MusicaProjeto();
            TempoMusicaEvento tempoMusicaEvento = new TempoMusicaEvento();

            Repertorio repertorio = new Repertorio();
            MusicaRepertorio musicaRepertorio = new MusicaRepertorio();

            BlocoRepertorio blocoRepertorio = new BlocoRepertorio();
            MusicaBloco musicaBloco = new MusicaBloco();
            TempoBlocoRepertorio tempoBlocoRepertorio = new TempoBlocoRepertorio();

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
                        +System.getProperty("line.separator"),"Atualizando Projetos");
            } else{
                publishProgress("Tipos de Evento já atualizados."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Projetos");
            }

            int qtdProjetos = projetos.length();

            if(qtdProjetos > 0){
                //mostraProgressBar(progressDialog, qtdTiposEvento, "Atualizando Tipos de Evento...");

                projeto.atualizarDados(projetos, qtdProjetos, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdTiposEvento+ " projeto(s) atualizado(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Eventos");
            } else{
                publishProgress("Projetos já atualizados."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Eventos");
            }

            int qtdEventos = eventos.length();

            if(qtdEventos > 0){
                //mostraProgressBar(progressDialog, qtdEventos, "Atualizando Eventos...");

                evento.atualizarDados(eventos, qtdEventos, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdEventos+ " evento(s) atualizado(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Estilos");
            } else{
                publishProgress("Eventos já atualizados."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Estilos");
            }

            int qtdEstilos = estilos.length();

            if(qtdEstilos > 0){
                //mostraProgressBar(progressDialog, qtdEventos, "Atualizando Eventos...");

                estilo.atualizarDados(estilos, qtdEstilos, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdEventos+ " estilo(s) atualizado(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Músicas");
            } else{
                publishProgress("Estilos já atualizados."+System.getProperty("line.separator")
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
                        +System.getProperty("line.separator"),"Atualizando Tempos Músicas Eventos");
            } else{
                publishProgress("Músicas Eventos já atualizadas."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Tempos Músicas Eventos");
            }

            int qtdTemposMusicasEventos = temposMusicasEventos.length();

            if(qtdTemposMusicasEventos > 0){
                //mostraProgressBar(progressDialog, qtdMusicasEventos, "Atualizando Músicas Eventos...");

                tempoMusicaEvento.atualizarDados(temposMusicasEventos, qtdTemposMusicasEventos, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdMusicasEventos+" tempo(s) música(s) evento(s) atualizada(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Comentários Eventos");
            } else{
                publishProgress("Tempos Músicas Eventos já atualizadas."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Comentários Eventos");
            }

            int qtdComentariosEventos = comentariosEventos.length();

            if(qtdComentariosEventos > 0){
                //mostraProgressBar(progressDialog, qtdComentariosEventos, "Atualizando Comentários Eventos...");

                comentarioEvento.atualizarDados(comentariosEventos, qtdComentariosEventos, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdComentariosEventos+" Comentário(s) Evento(s) atualizada(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Músicas Projetos");
            } else{
                publishProgress("Comentários Eventos já atualizadas."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"");
            }

            int qtdMusicasProjetos = musicasProjetos.length();

            if(qtdMusicasProjetos > 0){
                //mostraProgressBar(progressDialog, qtdComentariosEventos, "Atualizando Comentários Eventos...");

                musicaProjeto.atualizarDados(musicasProjetos, qtdMusicasProjetos, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdComentariosEventos+" Música(s) Projeto(s) atualizada(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"");
            } else{
                publishProgress("Músicas Projetos já atualizadas."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"");
            }

            int qtdRepertorios = repertorios.length();

            if(qtdRepertorios > 0){
                //mostraProgressBar(progressDialog, qtdEventos, "Atualizando Eventos...");

                repertorio.atualizarDados(repertorios, qtdRepertorios, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdEventos+ " repertorio(s) atualizado(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Estilos");
            } else{
                publishProgress("Repertorios já atualizados."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Estilos");
            }

            int qtdMusicasRepertorios = musicasRepertorios.length();

            if(qtdMusicasRepertorios > 0){
                //mostraProgressBar(progressDialog, qtdEventos, "Atualizando Eventos...");

                musicaRepertorio.atualizarDados(musicasRepertorios, qtdMusicasRepertorios, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdEventos+ " musica(s)-repertorio atualizada(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Estilos");
            } else{
                publishProgress("Músicas-Repertório já atualizadas."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Estilos");
            }

            // novos

            int qtdBlocosRepertorios = blocosRepertorios.length();

            if(qtdBlocosRepertorios > 0){
                //mostraProgressBar(progressDialog, qtdEventos, "Atualizando Eventos...");

                blocoRepertorio.atualizarDados(blocosRepertorios, qtdBlocosRepertorios, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdEventos+ " musica(s)-repertorio atualizada(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Estilos");
            } else{
                publishProgress("Músicas-Repertório já atualizadas."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Estilos");
            }

            int qtdMusicasBlocos = musicasBlocos.length();

            if(qtdMusicasBlocos > 0){
                //mostraProgressBar(progressDialog, qtdEventos, "Atualizando Eventos...");

                musicaBloco.atualizarDados(musicasBlocos, qtdMusicasBlocos, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdEventos+ " musica(s)-repertorio atualizada(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Estilos");
            } else{
                publishProgress("Músicas-Repertório já atualizadas."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Estilos");
            }

            int qtdTemposBlocosRepertorios = temposBlocosRepertorios.length();

            if(qtdTemposBlocosRepertorios > 0){
                //mostraProgressBar(progressDialog, qtdEventos, "Atualizando Eventos...");

                tempoBlocoRepertorio.atualizarDados(temposBlocosRepertorios, qtdTemposBlocosRepertorios, progressDialog, ctx);

                //escondeProgressBar(progressDialog);

                publishProgress(qtdEventos+ " musica(s)-repertorio atualizada(s)."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Estilos");
            } else{
                publishProgress("Músicas-Repertório já atualizadas."+System.getProperty("line.separator")
                        +System.getProperty("line.separator"),"Atualizando Estilos");
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
