package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.MusicaAdicionaList;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.listener.FiltroMusicaListener;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.listener.ModalHoraListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;

public class ModalAdicionaMusica extends android.support.v4.app.DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener, FiltroMusicaListener {

    Button btnSalvar;
    Button btnFechar;
    ListView listViewMusicas;
    List<Musica> musicas;
    MusicaEventoDBHelper musicaEventoDBHelper;
    Button btnFiltros;

    Evento evento;

    ModalAdicionaListener listener;
    MusicaAdicionaList adapterMusica;

    Estilo estiloFiltro;
    Artista artistaFiltro;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        musicaEventoDBHelper = new MusicaEventoDBHelper(getContext());

        View view = inflater.inflate(R.layout.modal_adiciona_musica, container, false);

        view.setMinimumWidth(700);

        listViewMusicas = (ListView) view.findViewById(R.id.listViewMusicas);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnFiltros = (Button) view.findViewById(R.id.btnFiltros);

        btnSalvar.setOnClickListener(this);
        btnFechar.setOnClickListener(this);
        btnFiltros.setOnClickListener(this);

        musicas = musicaEventoDBHelper.listarTodosAusentesEvento(getContext(), evento);

        adapterMusica = new MusicaAdicionaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicas);
        listViewMusicas.setAdapter(adapterMusica);
        listViewMusicas.setOnItemClickListener(this);

        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public ModalAdicionaListener getListener() {
        return listener;
    }

    public void setListener(ModalAdicionaListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnSalvar:

                int tamanhoLista = musicas.size();

                for(int i = 0; i < tamanhoLista; i++){
                    Musica musica = musicas.get(i);

                    if(musica.isChecked()){
                        MusicaEvento musicaEvento = new MusicaEvento();
                        musicaEvento.setMusica(musicas.get(i));
                        musicaEvento.setEvento(evento);

                        musicaEvento = musicaEventoDBHelper.carregarPorMusicaEEvento(getContext(), musicaEvento);

                        if(musicaEvento == null){
                            musicaEvento = new MusicaEvento();
                            musicaEvento.setMusica(musicas.get(i));
                            musicaEvento.setEvento(evento);
                        }

                        musicaEvento.setStatus(0);
                        musicaEvento.setUltimaAlteracao(Calendar.getInstance());
                        musicaEvento.setDataCadastro(Calendar.getInstance());
                        musicaEvento.setEnviado(-1);

                        int ordem = corrigeOrdemMusicas();

                        musicaEvento.setOrdem(ordem);

                        musicaEventoDBHelper.salvarOuAtualizar(getContext(), musicaEvento);

                    }

                }

                listener.onModalAdicionaDismissed(0);

                dismiss();
                break;
            case R.id.btnFechar:
                dismiss();
                break;
            case R.id.btnFiltros:
                ModalFiltros modalFiltros = new ModalFiltros();
                modalFiltros.setListener(this);
                modalFiltros.setArtista(artistaFiltro);
                modalFiltros.setEstilo(estiloFiltro);

                modalFiltros.show(getFragmentManager(), "modalFiltros");
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxAdicionar);
//        checkBox.setChecked(!checkBox.isChecked());

    }

    private int corrigeOrdemMusicas(){

        List<MusicaEvento> musicas = musicaEventoDBHelper.corrigirOrdemPorEvento(getContext(), evento);

        int qtdMusicas = musicas.size();

        for(int i = 0; i < qtdMusicas; i++){
            MusicaEvento musicaEvento = musicas.get(i);
            musicaEvento.setOrdem(i+1);
            musicaEvento.setEnviado(-1);
            musicaEvento.setUltimaAlteracao(Calendar.getInstance());

            musicaEventoDBHelper.salvarOuAtualizar(getContext(), musicaEvento);

        }

        return qtdMusicas+1;

    }

    private void atualizaLista(){
        adapterMusica = new MusicaAdicionaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicas);
        listViewMusicas.setAdapter(adapterMusica);
    }

    @Override
    public void onFiltroMusicaDismissed(Estilo estilo, Artista artista) {

        artistaFiltro = artista;
        estiloFiltro = estilo;

        if(estilo.getSlug() != null || artista.getSlug() != null){
            musicas = musicaEventoDBHelper.listarTodosAusentesEvento(getContext(), evento, estilo, artista);
        } else{
            musicas = musicaEventoDBHelper.listarTodosAusentesEvento(getContext(), evento);
        }

        atualizaLista();
    }
}
