package br.com.vostre.repertori.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import br.com.vostre.repertori.listener.ServerUtilsListener;
import br.com.vostre.repertori.listener.TarefaAssincronaListener;
import br.com.vostre.repertori.listener.TokenTaskListener;
import br.com.vostre.repertori.listener.UpdateSentTaskListener;
import br.com.vostre.repertori.listener.UpdateTaskListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.ComentarioEvento;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.ComentarioEventoDBHelper;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.model.dao.ParametroDBHelper;
import br.com.vostre.repertori.utils.Constants;
import br.com.vostre.repertori.utils.Crypt;
import br.com.vostre.repertori.utils.ServerUtils;
import br.com.vostre.repertori.utils.TarefaAssincrona;
import br.com.vostre.repertori.utils.TokenTask;
import br.com.vostre.repertori.utils.UpdateSentTask;
import br.com.vostre.repertori.utils.UpdateTask;

/**
 * Created by Almir on 30/12/2016.
 */

public class AtualizaDadosService extends Service implements ServerUtilsListener, TokenTaskListener, TarefaAssincronaListener,
        UpdateTaskListener {

    ServerUtils serverUtils;
    String dataUltimoAcesso;
    static final long TIME_TO_UPDATE = TimeUnit.MINUTES.toMillis(Constants.TEMPO_ATUALIZACAO);
    int registros = 0;
    int registrosResposta = 0;

    String tokenCriptografado;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                serverUtils = new ServerUtils(AtualizaDadosService.this, true);
                serverUtils.setOnResultsListener(AtualizaDadosService.this);
                serverUtils.execute(new String[]{Constants.SERVIDOR_TESTE, String.valueOf(Constants.PORTA_SERVIDOR)});

            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, TIME_TO_UPDATE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(MessageService.this, "Iniciando rodada", Toast.LENGTH_LONG).show();
        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onServerUtilsResultsSucceeded(boolean result) {

        if(result){

            String urlToken = Constants.URLTOKEN;

            TokenTask tokenTask = new TokenTask(urlToken, AtualizaDadosService.this, true, "1");
            tokenTask.setOnTokenTaskResultsListener(this);
            tokenTask.execute();

        }

    }

    @Override
    public void onTokenTaskResultsSucceeded(String token) {

        Crypt crypt = new Crypt();

        tokenCriptografado = null;
        List<ComentarioEvento> comentarios;
        List<Musica> musicas;
        List<Artista> artistas;
        List<Evento> eventos;
        List<MusicaEvento> musicasEventos;
        ComentarioEventoDBHelper comentarioEventoDBHelper = new ComentarioEventoDBHelper(getApplicationContext());
        MusicaDBHelper musicaDBHelper = new MusicaDBHelper(getApplicationContext());
        ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(getApplicationContext());
        EventoDBHelper eventoDBHelper = new EventoDBHelper(getApplicationContext());
        MusicaEventoDBHelper musicaEventoDBHelper = new MusicaEventoDBHelper(getApplicationContext());

        try {
            tokenCriptografado = crypt.bytesToHex(crypt.encrypt(token));

            dataUltimoAcesso = getDataUltimoAcesso(this.getBaseContext());
            dataUltimoAcesso = dataUltimoAcesso.equals("") ? "-" : dataUltimoAcesso;

            String urlEnvio = Constants.URLSERVIDORENVIO+tokenCriptografado+"/"+dataUltimoAcesso;
//            String url = Constants.URLSERVIDORMSG+tokenCriptografado+"/-";

            comentarios = comentarioEventoDBHelper.listarTodosAEnviar(getApplicationContext());
            musicas = musicaDBHelper.listarTodosAEnviar(getApplicationContext());
            artistas = artistaDBHelper.listarTodosAEnviar(getApplicationContext());
            eventos = eventoDBHelper.listarTodosAEnviar(getApplicationContext());
            musicasEventos = musicaEventoDBHelper.listarTodosAEnviar(getApplicationContext());

            // COMENTARIOS
            int totalRegistros = comentarios.size() + musicas.size() + artistas.size() + eventos.size() + musicasEventos.size();

            if(totalRegistros > 0){
                String json = "{";

                json = json.concat("\"comentarios\":[");
                int cont = 1;
                int qtdComentarios = comentarios.size();

                if(qtdComentarios > 0) {
                    for (ComentarioEvento umComentario : comentarios) {

                        if (cont < qtdComentarios) {
                            json = json.concat(umComentario.toJson() + ",");
                        } else {
                            json = json.concat(umComentario.toJson());
                        }

                        cont++;

                    }
                }

                // MUSICAS

                json = json.concat("],");

                json = json.concat("\"musicas\":[");
                cont = 1;
                int qtdMusicas = musicas.size();

                if(qtdMusicas > 0) {
                    for (Musica umMusica : musicas) {

                        if (cont < qtdMusicas) {
                            json = json.concat(umMusica.toJson() + ",");
                        } else {
                            json = json.concat(umMusica.toJson());
                        }

                        cont++;

                    }
                }

                // ARTISTAS

                json = json.concat("],");

                json = json.concat("\"artistas\":[");
                cont = 1;
                int qtdArtistas = artistas.size();

                if(qtdArtistas > 0) {
                    for (Artista umArtista : artistas) {

                        if (cont < qtdArtistas) {
                            json = json.concat(umArtista.toJson() + ",");
                        } else {
                            json = json.concat(umArtista.toJson());
                        }

                        cont++;

                    }
                }

                // EVENTOS

                json = json.concat("],");

                json = json.concat("\"eventos\":[");
                cont = 1;
                int qtdEventos = eventos.size();

                if(qtdEventos > 0) {
                    for (Evento umEvento : eventos) {

                        if (cont < qtdMusicas) {
                            json = json.concat(umEvento.toJson() + ",");
                        } else {
                            json = json.concat(umEvento.toJson());
                        }

                        cont++;

                    }
                }

                // MUSICA EVENTO

                // EVENTOS

                json = json.concat("],");

                json = json.concat("\"musicas_eventos\":[");
                cont = 1;
                int qtdMusicasEventos = musicasEventos.size();

                if(qtdMusicasEventos > 0) {
                    for (MusicaEvento umaMusicaEvento : musicasEventos) {

                        if (cont < qtdMusicasEventos) {
                            json = json.concat(umaMusicaEvento.toJson() + ",");
                        } else {
                            json = json.concat(umaMusicaEvento.toJson());
                        }

                        cont++;

                    }
                }

                json = json.concat("]");

                json = json.concat("}");

                Map<String, String> map = new HashMap<>();
                map.put("dados", json);
                map.put("total", String.valueOf(totalRegistros));

                TarefaAssincrona utEnvio = new TarefaAssincrona(urlEnvio, "POST", AtualizaDadosService.this, map, true);

                utEnvio.setOnResultListener(this);
                utEnvio.execute();

            }else{
                String url = Constants.URLSERVIDOR+tokenCriptografado+"/"+dataUltimoAcesso;
                TarefaAssincrona ut = new TarefaAssincrona(url, "GET", AtualizaDadosService.this, null, true);
                ut.setOnResultListener(this);
                ut.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onTarefaAssincronaResultSucceeded(Map<String, Object> map) {

        if(map == null || map.size() == 0){
            Toast.makeText(this, "Houve algum problema ao sincronizar os dados... uma nova tentativa será feita em breve.", Toast.LENGTH_LONG).show();
            return;
        }

        if(map.get("metodo").equals("GET")){
            JSONObject jObj = (JSONObject) map.get("json");
            dataUltimoAcesso = (String) map.get("dataUltimoAcesso");

            int status = 0;

            if(jObj != null){
                try {
                    JSONArray metadados = jObj.getJSONArray("meta");
                    JSONObject objMetadados = metadados.getJSONObject(0);

                    registros = objMetadados.getInt("registros");
                    status = objMetadados.getInt("status");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Houve algum problema ao sincronizar os dados... uma nova tentativa será feita em breve.", Toast.LENGTH_LONG).show();
            }

            if(registros > 0){
                UpdateTask updateTask = new UpdateTask(jObj, getApplicationContext());
                updateTask.setOnResultsListener(this);
                updateTask.execute();
            } else{

                if(dataUltimoAcesso != null){
                    setDataUltimoAcesso(getBaseContext(), dataUltimoAcesso);
                }

            }
        } else{

            String url = Constants.URLSERVIDOR+tokenCriptografado+"/"+dataUltimoAcesso;

            TarefaAssincrona ut = new TarefaAssincrona(url, "GET", AtualizaDadosService.this, null, true);
            ut.setOnResultListener(this);
            ut.execute();

//            JSONObject jObj = (JSONObject) map.get("json");
//
//            if(jObj != null){
//                try {
//                    JSONArray metadados = jObj.getJSONArray("meta");
//                    JSONObject objMetadados = metadados.getJSONObject(0);
//
//                    registrosResposta = objMetadados.getInt("registros");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Toast.makeText(this, "Erro ao receber dados... uma nova tentativa será feita em breve.", Toast.LENGTH_LONG).show();
//            }
//
//            if(registrosResposta > 0){
//                UpdateSentTask updateSentTask = new UpdateSentTask(jObj, getApplicationContext());
//                updateSentTask.setOnResultsSentListener(this);
//                updateSentTask.execute();
//            } else{
//
//                String url = Constants.URLSERVIDOR+tokenCriptografado+"/"+dataUltimoAcesso;
//
//                TarefaAssincrona ut = new TarefaAssincrona(url, "GET", AtualizaDadosService.this, null, true);
//                ut.setOnResultListener(this);
//                ut.execute();
//
//            }

        }

    }

    @Override
    public void onUpdateTaskResultsSucceeded(boolean result) {

        if(result){
            setDataUltimoAcesso(getBaseContext(), dataUltimoAcesso);

            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
            Intent intent = new Intent("AtualizaDadosService");
            intent.putExtra("registros", registros);
            broadcaster.sendBroadcast(intent);
        }

    }

    public static String getDataUltimoAcesso(Context context){
        DateFormat dateFormat = new SimpleDateFormat("E dd MMM yyyy hh:mm:ss Z", Locale.ENGLISH);
        DateFormat dateFormatWeb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = null;
        ParametroDBHelper parametroDBHelper = new ParametroDBHelper(context);

        String ultimaData = parametroDBHelper.carregarUltimoAcesso(context);

        try {

            if(!ultimaData.equals("-")){
                data = dateFormat.parse(ultimaData.replace(",", "").replace("%20", " "));
            }

        } catch(ParseException ex){
            ex.printStackTrace();
        }

        if(null != data){
            return dateFormatWeb.format(data).replace(" ", "%20");
        } else{
            return "-";
        }

    }

    public void setDataUltimoAcesso(Context context, String dataUltimoAcesso){

        if(dataUltimoAcesso != null){
            ParametroDBHelper parametroDBHelper = new ParametroDBHelper(context);
            parametroDBHelper.gravarUltimoAcesso(context, dataUltimoAcesso);
        }

    }

}
