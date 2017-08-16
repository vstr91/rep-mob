package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ArtistaList;
import br.com.vostre.repertori.adapter.EstiloList;
import br.com.vostre.repertori.listener.FiltroMusicaListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.EstiloDBHelper;

public class ModalFiltros extends android.support.v4.app.DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button btnFechar;
    Button btnFiltrar;
    Spinner spinnerEstilo;
    Spinner spinnerArtista;

    List<Estilo> estilos;
    List<Artista> artistas;

    FiltroMusicaListener listener;

    Estilo estilo;
    Artista artista;

    public FiltroMusicaListener getListener() {
        return listener;
    }

    public void setListener(FiltroMusicaListener listener) {
        this.listener = listener;
    }

    public Estilo getEstilo() {
        return estilo;
    }

    public void setEstilo(Estilo estilo) {
        this.estilo = estilo;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modal_filtros, container, false);

        view.setMinimumWidth(700);

        EstiloDBHelper estiloDBHelper = new EstiloDBHelper(getContext());
        ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(getContext());

        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnFiltrar = (Button) view.findViewById(R.id.btnFiltrar);
        spinnerEstilo = (Spinner) view.findViewById(R.id.spinnerEstilos);
        spinnerArtista = (Spinner) view.findViewById(R.id.spinnerArtistas);

        estilos = estiloDBHelper.listarTodos(getContext());
        artistas = artistaDBHelper.listarTodos(getContext());

        ArtistaList adapterArtistas = new ArtistaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, artistas, false);
        EstiloList adapterEstilos = new EstiloList(getActivity(), android.R.layout.simple_spinner_dropdown_item, estilos, false);

        Artista aTodos = new Artista();
        aTodos.setId("-1");
        aTodos.setNome("Todos");

        adapterArtistas.insert(aTodos, 0);

        Estilo eTodos = new Estilo();
        eTodos.setId("-1");
        eTodos.setNome("Todos");

        adapterEstilos.insert(eTodos, 0);

        spinnerArtista.setAdapter(adapterArtistas);
        spinnerEstilo.setAdapter(adapterEstilos);

        spinnerArtista.setOnItemSelectedListener(this);
        spinnerEstilo.setOnItemSelectedListener(this);

        btnFechar.setOnClickListener(this);
        btnFiltrar.setOnClickListener(this);

        if(estilo != null){
            spinnerEstilo.setSelection(estilos.indexOf(estilo));
        }

        if(artista != null){
            spinnerArtista.setSelection(artistas.indexOf(artista));
        }

        estilo = estilos.get(0);
        artista = artistas.get(0);

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
            case R.id.btnFechar:
                dismiss();
                break;
            case R.id.btnFiltrar:
                listener.onFiltroMusicaDismissed(estilo, artista);
                dismiss();
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(adapterView.getSelectedItem() instanceof Estilo){
            estilo = estilos.get(i);
        } else{
            artista = artistas.get(i);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
