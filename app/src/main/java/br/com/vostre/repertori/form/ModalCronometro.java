package br.com.vostre.repertori.form;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
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

import java.util.Calendar;
import java.util.List;

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
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.DialogUtils;

public class ModalCronometro extends android.support.v4.app.DialogFragment implements View.OnClickListener, DialogInterface.OnClickListener, AdapterView.OnItemLongClickListener {

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

    public MusicaEvento getMusicaEvento() {
        return musicaEvento;
    }

    public void setMusicaEvento(MusicaEvento musicaEvento) {
        this.musicaEvento = musicaEvento;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modal_cronometro, container, false);

        view.setMinimumWidth(700);
        tmeDBHelper = new TempoMusicaEventoDBHelper(getContext());
        tmes = tmeDBHelper.listarTodosPorMusica(getContext(), musicaEvento.getMusica());

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

                    Dialog dialog = DialogUtils.criarAlertaConfirmacao(getContext(), "Cronometragem Encerrada", "Deseja salvar o tempo cronometrado ("+tempo+")?", this);
                    dialog.show();

                } else{
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    btnIniciar.setText("Parar");
                    isRunning = true;
                }

                break;
            case R.id.btnFechar:
                dismiss();
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

                TempoMusicaEventoDBHelper tmeDBHelper = new TempoMusicaEventoDBHelper(getContext());
                tmeDBHelper.salvarOuAtualizar(getContext(), tme);

                Toast.makeText(getContext(), "Tempo registrado com sucesso!", Toast.LENGTH_SHORT).show();
                atualizaLista();
                //dismiss();
                break;
            case -2:
                // nada ainda
                break;
        }

    }

    private void atualizaLista(){
        tmes = tmeDBHelper.listarTodosPorMusica(getContext(), musicaEvento.getMusica());
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
}
