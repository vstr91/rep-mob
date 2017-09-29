package br.com.vostre.repertori.form;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import br.com.vostre.repertori.EventoDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.EventoList;
import br.com.vostre.repertori.adapter.TempoList;
import br.com.vostre.repertori.listener.ModalEventoListener;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.TempoMusicaEvento;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.TempoMusicaEventoDBHelper;
import br.com.vostre.repertori.utils.Constants;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.DialogUtils;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class ModalCronometro extends android.support.v4.app.DialogFragment implements View.OnClickListener, DialogInterface.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    Button btnFechar;
    Button btnIniciar;
    TextView textViewMusica;
    TextView textViewMedia;
    ListView listViewTempos;
    Chronometer chronometer;
    MusicaEvento musicaEvento;
    boolean isRunning = false;
    String tempo;

    TempoList adapter;
    List<TempoMusicaEvento> tmes;
    TempoMusicaEventoDBHelper tmeDBHelper;

    private MediaRecorder mRecorder = null;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String arquivoAudio = null;
    String nomeArquivo;

    public MusicaEvento getMusicaEvento() {
        return musicaEvento;
    }

    public void setMusicaEvento(MusicaEvento musicaEvento) {
        this.musicaEvento = musicaEvento;
    }

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modal_cronometro, container, false);

        int permissionAudio = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO);
        int permissionStorage = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionAudio == PackageManager.PERMISSION_GRANTED && permissionStorage == PackageManager.PERMISSION_GRANTED) {
            permissionToRecordAccepted = true;
        } else {
            requestGravacao();
        }

        view.setMinimumWidth(700);
        tmeDBHelper = new TempoMusicaEventoDBHelper(getContext());
        tmes = tmeDBHelper.listarTodosPorMusica(getContext(), musicaEvento.getMusica(), 10);

        chronometer = (Chronometer) view.findViewById(R.id.chronometer);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnIniciar = (Button) view.findViewById(R.id.btnIniciar);
        textViewMusica = (TextView) view.findViewById(R.id.textViewMusica);
        textViewMedia = (TextView) view.findViewById(R.id.textViewMedia);
        listViewTempos = (ListView) view.findViewById(R.id.listViewTempos);

        adapter = new TempoList(getActivity(), R.id.listViewTempos, tmes);
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewTempos.setAdapter(adapter);
        listViewTempos.setOnItemLongClickListener(this);
        listViewTempos.setOnItemClickListener(this);

        btnFechar.setOnClickListener(this);
        btnIniciar.setOnClickListener(this);

        textViewMusica.setText(musicaEvento.getMusica().getNome());

        atualizaMedia();

        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnIniciar:

                if(isRunning){
                    chronometer.stop();
                    btnIniciar.setText("Iniciar");
                    isRunning = false;

                    tempo = chronometer.getText().toString();
                    chronometer.setBase(SystemClock.elapsedRealtime());

                    if(permissionToRecordAccepted){
                        pararGravacao();
                    }

                    Dialog dialog = DialogUtils.criarAlertaConfirmacao(getContext(), "Cronometragem Encerrada", "Deseja salvar o tempo cronometrado ("+tempo+")?", this);
                    dialog.show();

                } else{
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    btnIniciar.setText("Parar");
                    isRunning = true;

                    arquivoAudio = Constants.CAMINHO_PADRAO_AUDIO+"/";//getActivity().getExternalFilesDir(null).getAbsolutePath()+ File.separator;
                    nomeArquivo = musicaEvento.getMusica().getSlug()+"-"+ UUID.randomUUID()+".3gp";
                    arquivoAudio += nomeArquivo;

                    System.out.println("ARQUIVO: "+arquivoAudio);

                    if(permissionToRecordAccepted){
                        iniciarGravacao();
                    }



                }

                break;
            case R.id.btnFechar:
                dismiss();

                if(permissionToRecordAccepted){
                    iniciarGravacao();
                }

                break;
        }

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        switch(i){
            case -1:
                TempoMusicaEvento tme = new TempoMusicaEvento();
                tme.setMusicaEvento(musicaEvento);
                tme.setTempo(DataUtils.horaParaData(tempo));
                tme.setStatus(0);
                tme.setEnviado(-1);
                tme.setDataCadastro(Calendar.getInstance());
                tme.setUltimaAlteracao(Calendar.getInstance());
                tme.setAudio(nomeArquivo);
                tme.setAudioEnviado(-1);

                TempoMusicaEventoDBHelper tmeDBHelper = new TempoMusicaEventoDBHelper(getContext());
                tmeDBHelper.salvarOuAtualizar(getContext(), tme);

                Toast.makeText(getContext(), "Tempo registrado com sucesso!", Toast.LENGTH_SHORT).show();
                atualizaLista();
                //dismiss();
                break;
            case -2:
                File f = new File(arquivoAudio);

                if(f.exists()){
                    f.delete();
                }

                break;
        }

    }

    private void atualizaLista(){
        tmes = tmeDBHelper.listarTodosPorMusica(getContext(), musicaEvento.getMusica(), 10);
        adapter = new TempoList(getActivity(), R.id.listViewTempos, tmes);
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewTempos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        atualizaMedia();
    }

    private void atualizaMedia(){

        if(tmes.size() > 0){
            long millis = 0;

            for(TempoMusicaEvento tme : tmes){
                millis += tme.getTempo().getTimeInMillis();
            }

            long result = millis / tmes.size();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(result);

            textViewMedia.setText("Média: "+DataUtils.toStringSomenteHoras(c, 1));
        } else{
            textViewMedia.setVisibility(View.GONE);
        }


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        final TempoMusicaEvento tme = tmes.get(i);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Confirmar Exclusão");

        builder.setMessage("Deseja realmente excluir o registro ("+DataUtils.toStringSomenteHoras(tme.getTempo(), 1)+")?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        tme.setStatus(2);
                        tme.setUltimaAlteracao(Calendar.getInstance());
                        tme.setEnviado(-1);
                        tmeDBHelper.salvarOuAtualizar(getContext(), tme);
                        Toast.makeText(getContext(), "Registro removido!", Toast.LENGTH_SHORT).show();
                        atualizaLista();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();

        return true;
    }

    private void iniciarGravacao() {

        File pastaAudio = new File(Constants.CAMINHO_PADRAO_AUDIO);

        if(!pastaAudio.exists()){
            //System.out.println("NAO EXISTE!!!!!!!!!!!!!!!!!!!!! "+pastaAudio.getAbsolutePath());
            boolean b = pastaAudio.mkdirs();
            //System.out.println("CRIOU????? "+Environment.getExternalStorageDirectory());
        }

        //System.out.println("CAMINHO >>>>>>>>>>> "+arquivoAudio);
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(arquivoAudio);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void pararGravacao() {
        mRecorder.stop();
        System.out.println("EXISTE? >>>> "+new File(arquivoAudio).exists());
        mRecorder.release();
        mRecorder = null;
    }

    private void requestGravacao() {
        Log.w("Gravacao", "Gravacao nao autorizada. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (!shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) && !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION);
            return;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final TempoMusicaEvento tme = tmes.get(i);

        if(tme.getAudio() != null && !tme.getAudio().isEmpty()){

            File f  = new File(Constants.CAMINHO_PADRAO_AUDIO+File.separator+tme.getAudio());

            if(f.exists()){
                MediaPlayer mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(f.getAbsolutePath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else{
                Toast.makeText(getContext(), "Áudio não encontrado. Ele pode ter sido movido ou excluído", Toast.LENGTH_SHORT).show();
            }

        } else{
            Toast.makeText(getContext(), "Não existe áudio disponível para esta gravação", Toast.LENGTH_SHORT).show();
        }

    }
}
