package br.com.vostre.repertori.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.vostre.repertori.EventoDetalheActivity;
import br.com.vostre.repertori.MusicaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.EventoList;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;

/**
 * Created by Almir on 17/06/2015.
 */
public class FragmentEvento extends Fragment implements TextWatcher, AdapterView.OnItemClickListener {

    private ListView lista;
    EventoList adapterEventos;
    int situacao = -1;

    List<Evento> eventos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_listview_eventos, container, false);

        lista = (ListView) rootView.findViewById(R.id.listViewEventos);
//        editTextFiltro = (EditText) rootView.findViewById(R.id.editTextFiltro);
//        editTextFiltro.addTextChangedListener(this);

        situacao = getArguments().getInt("situacao");

        eventos = carregaEventos(situacao);

        adapterEventos =
                new EventoList(getActivity(), android.R.layout.simple_spinner_dropdown_item, eventos);

        adapterEventos.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        lista.setAdapter(adapterEventos);
        lista.setOnItemClickListener(this);
        lista.setEmptyView(rootView.findViewById(R.id.textViewListaVazia));

        return rootView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        adapterEventos.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Evento evento = adapterEventos.getItem(position);

        Intent intent = new Intent(getContext(), EventoDetalheActivity.class);
        intent.putExtra("evento", evento.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizaLista();

    }

    private List<Evento> carregaEventos(int situacao){
        EventoDBHelper eventoDBHelper = new EventoDBHelper(getContext());

        if(situacao == 0){
            eventos = eventoDBHelper.listarTodosAteHoje(getContext());
        } else{
            eventos = eventoDBHelper.listarTodosAPartirDeHoje(getContext());
        }



        return eventos;

    }

    public void atualizaLista(){
        eventos = carregaEventos(situacao);
        adapterEventos =
                new EventoList(getActivity(), android.R.layout.simple_spinner_dropdown_item, eventos);
        adapterEventos.notifyDataSetChanged();

        if(lista != null){
            lista.setAdapter(adapterEventos);
        }
    }

}
