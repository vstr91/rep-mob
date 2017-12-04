package br.com.vostre.repertori.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

import br.com.vostre.repertori.App;
import br.com.vostre.repertori.listener.ServerUtilsListener;
import br.com.vostre.repertori.listener.TarefaAssincronaListener;
import br.com.vostre.repertori.listener.TokenTaskListener;
import br.com.vostre.repertori.listener.UpdateSentTaskListener;
import br.com.vostre.repertori.listener.UpdateTaskListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.Casa;
import br.com.vostre.repertori.model.ComentarioEvento;
import br.com.vostre.repertori.model.Contato;
import br.com.vostre.repertori.model.ContatoCasa;
import br.com.vostre.repertori.model.Estilo;
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
import br.com.vostre.repertori.model.dao.BlocoRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.CasaDBHelper;
import br.com.vostre.repertori.model.dao.ComentarioEventoDBHelper;
import br.com.vostre.repertori.model.dao.ContatoCasaDBHelper;
import br.com.vostre.repertori.model.dao.ContatoDBHelper;
import br.com.vostre.repertori.model.dao.EstiloDBHelper;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaBlocoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.ParametroDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;
import br.com.vostre.repertori.model.dao.TempoBlocoRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.TempoMusicaEventoDBHelper;
import br.com.vostre.repertori.model.dao.TipoEventoDBHelper;
import br.com.vostre.repertori.utils.Constants;
import br.com.vostre.repertori.utils.Crypt;
import br.com.vostre.repertori.utils.ParametrosUtils;
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
    List<TempoMusicaEvento> tmes;
    List<TempoBlocoRepertorio> tbrs;
    List<TempoMusicaEvento> tmesRecebeAudio;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                serverUtils = new ServerUtils(AtualizaDadosService.this, true);
//                serverUtils.setOnResultsListener(AtualizaDadosService.this);
//                serverUtils.execute(new String[]{Constants.SERVIDOR_TESTE, String.valueOf(Constants.PORTA_SERVIDOR)});
//
//            }
//        };
//
//        timer.scheduleAtFixedRate(timerTask, 0, TIME_TO_UPDATE);

        serverUtils = new ServerUtils(AtualizaDadosService.this, true);
        serverUtils.setOnResultsListener(AtualizaDadosService.this);
        serverUtils.execute(new String[]{Constants.SERVIDOR_TESTE, String.valueOf(Constants.PORTA_SERVIDOR)});
        Toast.makeText(this, "Iniciando atualização.", Toast.LENGTH_SHORT).show();
        System.out.println("INICIOU");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(MessageService.this, "Iniciando rodada", Toast.LENGTH_LONG).show();
        return START_NOT_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onServerUtilsResultsSucceeded(boolean result) {

        if(result){

            String urlToken = Constants.URLTOKEN;

            TokenTask tokenTask = new TokenTask(urlToken, AtualizaDadosService.this, true, 1);
            tokenTask.setOnTokenTaskResultsListener(this);
            tokenTask.execute();

        } else{
            Toast.makeText(this, "Não foi possível estabelecer conexão com o servidor. Por favor verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
            enviaBroadcast();
            this.stopSelf();
            System.out.println("TERMINOU ERRO INTERNET");
        }

    }

    @Override
    public void onTokenTaskResultsSucceeded(String token, int tipo) {

        Crypt crypt = new Crypt();

        tokenCriptografado = null;

        switch(tipo){
            case 1:
                List<ComentarioEvento> comentarios;
                List<Musica> musicas;
                List<Artista> artistas;
                List<Evento> eventos;
                List<MusicaEvento> musicasEventos;

                List<Projeto> projetos;
                List<Estilo> estilos;
                List<MusicaProjeto> musicasProjetos;
                List<TipoEvento> tiposEventos;
                List<TempoMusicaEvento> temposMusicasEventos;

                List<Repertorio> repertorios;
                List<MusicaRepertorio> musicasRepertorios;

                List<BlocoRepertorio> blocosRepertorios;
                List<MusicaBloco> musicasBlocos;
                List<TempoBlocoRepertorio> temposBlocosRepertorios;

                List<Casa> casas;
                List<Contato> contatos;
                List<ContatoCasa> contatosCasas;

                ComentarioEventoDBHelper comentarioEventoDBHelper = new ComentarioEventoDBHelper(getApplicationContext());
                MusicaDBHelper musicaDBHelper = new MusicaDBHelper(getApplicationContext());
                ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(getApplicationContext());
                EventoDBHelper eventoDBHelper = new EventoDBHelper(getApplicationContext());
                MusicaEventoDBHelper musicaEventoDBHelper = new MusicaEventoDBHelper(getApplicationContext());
                TipoEventoDBHelper tipoEventoDBHelper = new TipoEventoDBHelper(getApplicationContext());

                ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(getApplicationContext());
                EstiloDBHelper estiloDBHelper = new EstiloDBHelper(getApplicationContext());
                MusicaProjetoDBHelper musicaProjetoDBHelper = new MusicaProjetoDBHelper(getApplicationContext());
                TempoMusicaEventoDBHelper tempoMusicaEventoDBHelper = new TempoMusicaEventoDBHelper(getApplicationContext());
                RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(getApplicationContext());
                MusicaRepertorioDBHelper musicaRepertorioDBHelper = new MusicaRepertorioDBHelper(getApplicationContext());

                BlocoRepertorioDBHelper blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(getApplicationContext());
                MusicaBlocoDBHelper musicaBlocoDBHelper = new MusicaBlocoDBHelper(getApplicationContext());
                TempoBlocoRepertorioDBHelper tempoBlocoRepertorioDBHelper = new TempoBlocoRepertorioDBHelper(getApplicationContext());

                CasaDBHelper casaDBHelper = new CasaDBHelper(getApplicationContext());
                ContatoDBHelper contatoDBHelper = new ContatoDBHelper(getApplicationContext());
                ContatoCasaDBHelper contatoCasaDBHelper = new ContatoCasaDBHelper(getApplicationContext());

                try {
                    tokenCriptografado = crypt.bytesToHex(crypt.encrypt(token));

                    dataUltimoAcesso = ParametrosUtils.getDataUltimoAcesso(this.getBaseContext());
                    dataUltimoAcesso = dataUltimoAcesso.equals("") ? "-" : dataUltimoAcesso;

                    String urlEnvio = Constants.URLSERVIDORENVIO+tokenCriptografado+"/"+dataUltimoAcesso;
//            String url = Constants.URLSERVIDORMSG+tokenCriptografado+"/-";

                    comentarios = comentarioEventoDBHelper.listarTodosAEnviar(getApplicationContext());
                    musicas = musicaDBHelper.listarTodosAEnviar(getApplicationContext());
                    artistas = artistaDBHelper.listarTodosAEnviar(getApplicationContext());
                    eventos = eventoDBHelper.listarTodosAEnviar(getApplicationContext());
                    musicasEventos = musicaEventoDBHelper.listarTodosAEnviar(getApplicationContext());

                    projetos = projetoDBHelper.listarTodosAEnviar(getApplicationContext());
                    estilos = estiloDBHelper.listarTodosAEnviar(getApplicationContext());
                    musicasProjetos = musicaProjetoDBHelper.listarTodosAEnviar(getApplicationContext());
                    tiposEventos = tipoEventoDBHelper.listarTodosAEnviar(getApplicationContext());

                    temposMusicasEventos = tempoMusicaEventoDBHelper.listarTodosAEnviar(getApplicationContext());

                    repertorios = repertorioDBHelper.listarTodosAEnviar(getApplicationContext());
                    musicasRepertorios = musicaRepertorioDBHelper.listarTodosAEnviar(getApplicationContext());

                    blocosRepertorios = blocoRepertorioDBHelper.listarTodosAEnviar(getApplicationContext());
                    musicasBlocos = musicaBlocoDBHelper.listarTodosAEnviar(getApplicationContext());
                    temposBlocosRepertorios = tempoBlocoRepertorioDBHelper.listarTodosAEnviar(getApplicationContext());

                    casas = casaDBHelper.listarTodosAEnviar(getApplicationContext());
                    contatos = contatoDBHelper.listarTodosAEnviar(getApplicationContext());
                    contatosCasas = contatoCasaDBHelper.listarTodosAEnviar(getApplicationContext());

                    // COMENTARIOS
                    int totalRegistros = comentarios.size() + musicas.size() + artistas.size() + eventos.size()
                            + musicasEventos.size() + projetos.size() + estilos.size() + musicasProjetos.size() + tiposEventos.size() + temposMusicasEventos.size()
                            + repertorios.size() + musicasRepertorios.size() + blocosRepertorios.size() + musicasBlocos.size() + temposBlocosRepertorios.size()
                            + casas.size() + contatos.size();

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

                        // TIPOS EVENTOS

                        json = json.concat("],");

                        json = json.concat("\"tipos_eventos\":[");
                        cont = 1;
                        int qtdTiposEventos = tiposEventos.size();

                        if(qtdTiposEventos > 0) {
                            for (TipoEvento umTipoEvento : tiposEventos) {

                                if (cont < qtdTiposEventos) {
                                    json = json.concat(umTipoEvento.toJson() + ",");
                                } else {
                                    json = json.concat(umTipoEvento.toJson());
                                }

                                cont++;

                            }
                        }

                        // ESTILOS

                        json = json.concat("],");

                        json = json.concat("\"estilos\":[");
                        cont = 1;
                        int qtdEstilos = estilos.size();

                        if(qtdEstilos > 0) {
                            for (Estilo umEstilo : estilos) {

                                if (cont < qtdEstilos) {
                                    json = json.concat(umEstilo.toJson() + ",");
                                } else {
                                    json = json.concat(umEstilo.toJson());
                                }

                                cont++;

                            }
                        }

                        // PROJETOS

                        json = json.concat("],");

                        json = json.concat("\"projetos\":[");
                        cont = 1;
                        int qtdProjetos = projetos.size();

                        if(qtdProjetos > 0) {
                            for (Projeto umProjeto : projetos) {

                                if (cont < qtdProjetos) {
                                    json = json.concat(umProjeto.toJson() + ",");
                                } else {
                                    json = json.concat(umProjeto.toJson());
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

                        // CASAS

                        json = json.concat("],");

                        json = json.concat("\"casas\":[");
                        cont = 1;
                        int qtdCasas = casas.size();

                        if(qtdCasas > 0) {
                            for (Casa umCasa : casas) {

                                if (cont < qtdCasas) {
                                    json = json.concat(umCasa.toJson() + ",");
                                } else {
                                    json = json.concat(umCasa.toJson());
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

                                if (cont < qtdEventos) {
                                    json = json.concat(umEvento.toJson() + ",");
                                } else {
                                    json = json.concat(umEvento.toJson());
                                }

                                cont++;

                            }
                        }

                        // REPERTORIOS

                        json = json.concat("],");

                        json = json.concat("\"repertorios\":[");
                        cont = 1;
                        int qtdRepertorios = repertorios.size();

                        if(qtdRepertorios > 0) {
                            for (Repertorio umRepertorio : repertorios) {

                                if (cont < qtdRepertorios) {
                                    json = json.concat(umRepertorio.toJson() + ",");
                                } else {
                                    json = json.concat(umRepertorio.toJson());
                                }

                                cont++;

                            }
                        }

                        // MUSICA EVENTO

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

                        // TEMPOS MUSICA EVENTO

                        json = json.concat("],");

                        json = json.concat("\"tempos_musicas_eventos\":[");
                        cont = 1;
                        int qtdTemposMusicasEventos = temposMusicasEventos.size();

                        if(qtdTemposMusicasEventos > 0) {
                            for (TempoMusicaEvento umTempoMusicaEvento : temposMusicasEventos) {

                                if (cont < qtdTemposMusicasEventos) {
                                    json = json.concat(umTempoMusicaEvento.toJson() + ",");
                                } else {
                                    json = json.concat(umTempoMusicaEvento.toJson());
                                }

                                cont++;

                            }
                        }

                        // MUSICA REPERTORIO

                        json = json.concat("],");

                        json = json.concat("\"musicas_repertorios\":[");
                        cont = 1;
                        int qtdMusicasRepertorios = musicasRepertorios.size();

                        if(qtdMusicasRepertorios > 0) {
                            for (MusicaRepertorio umMusicaRepertorio : musicasRepertorios) {

                                if (cont < qtdMusicasRepertorios) {
                                    json = json.concat(umMusicaRepertorio.toJson() + ",");
                                } else {
                                    json = json.concat(umMusicaRepertorio.toJson());
                                }

                                cont++;

                            }
                        }

                        // MUSICA PROJETO

                        json = json.concat("],");

                        json = json.concat("\"musicas_projetos\":[");
                        cont = 1;
                        int qtdMusicasProjetos = musicasProjetos.size();

                        if(qtdMusicasProjetos > 0) {
                            for (MusicaProjeto umaMusicaProjeto : musicasProjetos) {

                                if (cont < qtdMusicasProjetos) {
                                    json = json.concat(umaMusicaProjeto.toJson() + ",");
                                } else {
                                    json = json.concat(umaMusicaProjeto.toJson());
                                }

                                cont++;

                            }
                        }

                        // BLOCO REPERTORIO

                        json = json.concat("],");

                        json = json.concat("\"blocos_repertorios\":[");
                        cont = 1;
                        int qtdBlocosRepertorios = blocosRepertorios.size();

                        if(qtdBlocosRepertorios > 0) {
                            for (BlocoRepertorio umBlocoRepertorio : blocosRepertorios) {

                                if (cont < qtdBlocosRepertorios) {
                                    json = json.concat(umBlocoRepertorio.toJson() + ",");
                                } else {
                                    json = json.concat(umBlocoRepertorio.toJson());
                                }

                                cont++;

                            }
                        }

                        // MUSICA BLOCO REPERTORIO

                        json = json.concat("],");

                        json = json.concat("\"musicas_blocos_repertorios\":[");
                        cont = 1;
                        int qtdMusicasBlocos = musicasBlocos.size();

                        if(qtdMusicasBlocos > 0) {
                            for (MusicaBloco umMusicaBloco : musicasBlocos) {

                                if (cont < qtdMusicasBlocos) {
                                    json = json.concat(umMusicaBloco.toJson() + ",");
                                } else {
                                    json = json.concat(umMusicaBloco.toJson());
                                }

                                cont++;

                            }
                        }

                        // TEMPO BLOCO REPERTORIO

                        json = json.concat("],");

                        json = json.concat("\"tempos_blocos_repertorios\":[");
                        cont = 1;
                        int qtdTemposBlocos = temposBlocosRepertorios.size();

                        if(qtdTemposBlocos > 0) {
                            for (TempoBlocoRepertorio umTempoBlocoRepertorio : temposBlocosRepertorios) {

                                if (cont < qtdTemposBlocos) {
                                    json = json.concat(umTempoBlocoRepertorio.toJson() + ",");
                                } else {
                                    json = json.concat(umTempoBlocoRepertorio.toJson());
                                }

                                cont++;

                            }
                        }

                        // CONTATOS

                        json = json.concat("],");

                        json = json.concat("\"contatos\":[");
                        cont = 1;
                        int qtdContatos = contatos.size();

                        if(qtdContatos > 0) {
                            for (Contato umContato : contatos) {

                                if (cont < qtdContatos) {
                                    json = json.concat(umContato.toJson() + ",");
                                } else {
                                    json = json.concat(umContato.toJson());
                                }

                                cont++;

                            }
                        }

                        // CONTATOS CASAS

                        json = json.concat("],");

                        json = json.concat("\"contatos_casas\":[");
                        cont = 1;
                        int qtdContatosCasas = contatosCasas.size();

                        if(qtdContatosCasas > 0) {
                            for (ContatoCasa umContatoCasa : contatosCasas) {

                                if (cont < qtdContatosCasas) {
                                    json = json.concat(umContatoCasa.toJson() + ",");
                                } else {
                                    json = json.concat(umContatoCasa.toJson());
                                }

                                cont++;

                            }
                        }

                        json = json.concat("]");

                        json = json.concat("}");

                        Map<String, String> map = new HashMap<>();
                        map.put("dados", json);
                        map.put("total", String.valueOf(totalRegistros));

                        TarefaAssincrona utEnvio = new TarefaAssincrona(urlEnvio, "POST", AtualizaDadosService.this, map, true, 0);

                        utEnvio.setOnResultListener(this);
                        utEnvio.execute();

                    }else{
                        String url = Constants.URLSERVIDOR+tokenCriptografado+"/"+dataUltimoAcesso;
                        TarefaAssincrona ut = new TarefaAssincrona(url, "GET", AtualizaDadosService.this, null, true, 0);
                        ut.setOnResultListener(this);
                        ut.execute();
                    }

                } catch (Exception e) {
                    this.stopSelf();
                    System.out.println("TERMINOU ERRO 1");
                    Toast.makeText(this, "Houve algum problema ao sincronizar os dados... por favor tente novamente.", Toast.LENGTH_LONG).show();
                    enviaBroadcast();
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    tokenCriptografado = crypt.bytesToHex(crypt.encrypt(token));

                    for(TempoMusicaEvento tme : tmes){

                        if(tme.getAudio() != null && !tme.getAudio().isEmpty()){

                            File arquivo = new File(Constants.CAMINHO_PADRAO_AUDIO+File.separator+tme.getAudio());

                            if(arquivo.exists() && arquivo.canRead()){
                                String url = Constants.URLSERVIDORENVIOAUDIO+tokenCriptografado+"/"+tme.getAudio().replace(".", "_");
                                Map<String, String> params = new HashMap<>();
                                params.put("audio", tme.getAudio());
                                TarefaAssincrona ut = new TarefaAssincrona(url, "POST", AtualizaDadosService.this, params, true, 1);
                                ut.setOnResultListener(this);
                                ut.execute();
                            }


                        }

                    }

                    for(TempoBlocoRepertorio tbr : tbrs){

                        if(tbr.getAudio() != null && !tbr.getAudio().isEmpty()){

                            File arquivo = new File(Constants.CAMINHO_PADRAO_AUDIO+File.separator+tbr.getAudio());

                            if(arquivo.exists() && arquivo.canRead()){
                                String url = Constants.URLSERVIDORENVIOAUDIO+tokenCriptografado+"/"+tbr.getAudio().replace(".", "_");
                                Map<String, String> params = new HashMap<>();
                                params.put("audio", tbr.getAudio());
                                TarefaAssincrona ut = new TarefaAssincrona(url, "POST", AtualizaDadosService.this, params, true, 1);
                                ut.setOnResultListener(this);
                                ut.execute();
                            }


                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                // processa recebimento
                try {
                    tokenCriptografado = crypt.bytesToHex(crypt.encrypt(token));

                    for(TempoMusicaEvento tme : tmesRecebeAudio){

                        if(tme.getAudio() != null && !tme.getAudio().isEmpty()){

                            File f  = new File(Constants.CAMINHO_PADRAO_AUDIO+File.separator+tme.getAudio());

                            if(!f.exists()){
                                String url = Constants.URLSERVIDORAUDIO+tokenCriptografado+"/"+tme.getAudio();
                                Map<String, String> params = new HashMap<>();
                                params.put("audio", tme.getAudio());
                                TarefaAssincrona ut = new TarefaAssincrona(url, "GET", AtualizaDadosService.this, params, true, 2);
                                ut.setOnResultListener(this);
                                ut.execute();
                            }


                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }


    }

    @Override
    public void onTarefaAssincronaResultSucceeded(Map<String, Object> map, int acao) {

        if(acao == 0){
            if(map == null || map.size() == 0){
                Toast.makeText(this, "Houve algum problema ao sincronizar os dados... por favor tente novamente.", Toast.LENGTH_LONG).show();
                enviaBroadcast();
                this.stopSelf();
                System.out.println("TERMINOU ERRO 2");
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
                    Toast.makeText(this, "Houve algum problema ao sincronizar os dados... por favor tente novamente.", Toast.LENGTH_LONG).show();
                    enviaBroadcast();
                    this.stopSelf();
                    System.out.println("TERMINOU ERRO 3");
                }

                if(registros > 0){
                    UpdateTask updateTask = new UpdateTask(jObj, getApplicationContext());
                    updateTask.setOnResultsListener(this);
                    updateTask.execute();
                } else{

                    if(dataUltimoAcesso != null){
                        ParametrosUtils.setDataUltimoAcesso(getBaseContext(), dataUltimoAcesso);
                    }

                    Toast.makeText(this, "Seu sistema já está atualizado.", Toast.LENGTH_SHORT).show();
                    enviaBroadcast();

                    enviaAudios();
                    //recebeAudios();

                    this.stopSelf();
                    System.out.println("TERMINOU SEM REGISTROS");
                }
            } else{

                String url = Constants.URLSERVIDOR+tokenCriptografado+"/"+dataUltimoAcesso;

                TarefaAssincrona ut = new TarefaAssincrona(url, "GET", AtualizaDadosService.this, null, true, 0);
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
        } else if(acao == 1){
            JSONObject jObj = (JSONObject) map.get("json");

            if(jObj != null){
                try {
                    String audio = jObj.getString("audio");
                    System.out.println(audio);
                    TempoMusicaEventoDBHelper tempoMusicaEventoDBHelper = new TempoMusicaEventoDBHelper(getBaseContext());
                    tempoMusicaEventoDBHelper.sinalizaEnvioAudio(getBaseContext(), audio);
                    TempoBlocoRepertorioDBHelper tempoBlocoRepertorioDBHelper = new TempoBlocoRepertorioDBHelper(getBaseContext());
                    tempoBlocoRepertorioDBHelper.sinalizaEnvioAudio(getBaseContext(), audio);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        } else{
            // processa recebimento
        }

    }

    @Override
    public void onUpdateTaskResultsSucceeded(boolean result) {

        if(result){
            ParametrosUtils.setDataUltimoAcesso(getBaseContext(), dataUltimoAcesso);

            enviaBroadcast();
            Toast.makeText(this, "Atualização finalizada com sucesso.", Toast.LENGTH_LONG).show();

            // enviando audios
            enviaAudios();
            // fim envio audios

            //recebeAudios();

            this.stopSelf();
            System.out.println("TERMINOU");

        }

    }

    private void enviaAudios() {

        TempoMusicaEventoDBHelper tempoMusicaEventoDBHelper = new TempoMusicaEventoDBHelper(getBaseContext());
        TempoBlocoRepertorioDBHelper tempoBlocoRepertorioDBHelper = new TempoBlocoRepertorioDBHelper(getBaseContext());

        tmes = tempoMusicaEventoDBHelper.listarTodosAEnviarAudio(getBaseContext());
        tbrs = tempoBlocoRepertorioDBHelper.listarTodosAEnviarAudio(getBaseContext());

        if(tmes.size() > 0 || tbrs.size() > 0){
            String urlToken = Constants.URLTOKEN;

            TokenTask tokenTask = new TokenTask(urlToken, AtualizaDadosService.this, true, 2);
            tokenTask.setOnTokenTaskResultsListener(this);
            tokenTask.execute();
        }


    }

    private void recebeAudios() {

        TempoMusicaEventoDBHelper tempoMusicaEventoDBHelper = new TempoMusicaEventoDBHelper(getBaseContext());


        tmesRecebeAudio = tempoMusicaEventoDBHelper.listarTodosAReceberAudio(getBaseContext());

        if(tmesRecebeAudio.size() > 0){
            String urlToken = Constants.URLTOKEN;

            TokenTask tokenTask = new TokenTask(urlToken, AtualizaDadosService.this, true, 3);
            tokenTask.setOnTokenTaskResultsListener(this);
            tokenTask.execute();
        } else{
            enviaAudios();
        }


    }

    private void enviaBroadcast() {
        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent("br.com.vostre.repertori.AtualizaDadosService");
        intent.putExtra("registros", registros);
        broadcaster.sendBroadcast(intent);
    }

//    public static String getDataUltimoAcesso(Context context){
//        DateFormat dateFormat = new SimpleDateFormat("E dd MMM yyyy hh:mm:ss Z", Locale.ENGLISH);
//        DateFormat dateFormatWeb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date data = null;
//        ParametroDBHelper parametroDBHelper = new ParametroDBHelper(context);
//
//        String ultimaData = parametroDBHelper.carregarUltimoAcesso(context);
//
//        try {
//
//            if(!ultimaData.equals("-")){
//                data = dateFormat.parse(ultimaData.replace(",", "").replace("%20", " "));
//            }
//
//        } catch(ParseException ex){
//            ex.printStackTrace();
//        }
//
//        if(null != data){
//            return dateFormatWeb.format(data).replace(" ", "%20");
//        } else{
//            return "-";
//        }
//
//    }
//
//    public void setDataUltimoAcesso(Context context, String dataUltimoAcesso){
//
//        if(dataUltimoAcesso != null){
//            ParametroDBHelper parametroDBHelper = new ParametroDBHelper(context);
//            parametroDBHelper.gravarUltimoAcesso(context, dataUltimoAcesso);
//        }
//
//    }

}
