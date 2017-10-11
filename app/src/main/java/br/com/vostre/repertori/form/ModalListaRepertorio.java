package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.MusicaAdicionaList;
import br.com.vostre.repertori.adapter.RepertorioList;
import br.com.vostre.repertori.listener.FiltroMusicaListener;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;

public class ModalListaRepertorio extends android.support.v4.app.DialogFragment implements View.OnClickListener, AdapterView.OnItemClickListener, ModalAdicionaListener {

    Button btnFechar;
    ListView listViewRepertorios;
    List<Repertorio> repertorios;
    RepertorioDBHelper repertorioDBHelper;

    Evento evento;

    ModalAdicionaListener listener;
    RepertorioList adapterRepertorio;

    Estilo estiloFiltro;
    Artista artistaFiltro;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        repertorioDBHelper = new RepertorioDBHelper(getContext());

        View view = inflater.inflate(R.layout.modal_lista_repertorio, container, false);

        view.setMinimumWidth(700);

        listViewRepertorios = (ListView) view.findViewById(R.id.listViewRepertorios);
        btnFechar = (Button) view.findViewById(R.id.btnFechar);

        btnFechar.setOnClickListener(this);

        repertorios = repertorioDBHelper.listarTodosAtivosPorProjeto(getContext(), evento.getProjeto());

        adapterRepertorio = new RepertorioList(getActivity(), android.R.layout.simple_spinner_dropdown_item, repertorios);
        listViewRepertorios.setAdapter(adapterRepertorio);
        listViewRepertorios.setOnItemClickListener(this);

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
            case R.id.btnFechar:
                dismiss();
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ModalListaBloco modalListaBloco = new ModalListaBloco();
        modalListaBloco.setListener(this);
        modalListaBloco.setEvento(evento);
        modalListaBloco.setRepertorio(repertorios.get(position));

        modalListaBloco.show(getFragmentManager(), "modalListaBloco");
    }

    @Override
    public void onModalAdicionaDismissed(int resultado) {
        listener.onModalAdicionaDismissed(0);
        dismiss();
    }
}
