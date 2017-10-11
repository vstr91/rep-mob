package br.com.vostre.repertori.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import br.com.vostre.repertori.App;
import br.com.vostre.repertori.EventoDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.EventoList;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.utils.AnalyticsApplication;
import br.com.vostre.repertori.utils.DialogUtils;

/**
 * Created by Almir on 17/06/2015.
 */
public class FragmentEventoProjeto extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lista;
    EventoList adapterEventos;
    String idProjeto;

    List<Evento> eventos;
    Projeto projeto;
    Dialog dialogLoad;

    Tracker mTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App application = (App) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_listview_eventos, container, false);

        lista = (ListView) rootView.findViewById(R.id.listViewEventos);
//        editTextFiltro = (EditText) rootView.findViewById(R.id.editTextFiltro);
//        editTextFiltro.addTextChangedListener(this);

        idProjeto = getArguments().getString("projeto");

        lista.setEmptyView(rootView.findViewById(R.id.textViewListaVazia));

        dialogLoad = DialogUtils.criarAlertaCarregando(getContext(), "Carregando dados", "Por favor aguarde...");
        dialogLoad.show();

        CarregarItens carregarItens = new CarregarItens();
        carregarItens.execute();

        return rootView;
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
        mTracker.setScreenName("Tela Evento Projeto");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }

    private List<Evento> carregaEventos(Projeto projeto){
        EventoDBHelper eventoDBHelper = new EventoDBHelper(getContext());

        eventos = eventoDBHelper.listarTodosAPartirDeHojePorProjeto(getContext(), projeto);

        return eventos;

    }

    public void atualizaLista(){
        eventos = carregaEventos(projeto);
        adapterEventos =
                new EventoList(getActivity(), android.R.layout.simple_spinner_dropdown_item, eventos);
        adapterEventos.notifyDataSetChanged();

        if(lista != null){
            lista.setAdapter(adapterEventos);
        }
    }

    private class CarregarItens extends AsyncTask<Void, String, List<Evento>> {

        @Override
        protected List<Evento> doInBackground(Void... voids) {
            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(getContext());
            projeto = new Projeto();
            projeto.setId(idProjeto);
            projeto = projetoDBHelper.carregar(getContext(), projeto);

            eventos = carregaEventos(projeto);

            adapterEventos =
                    new EventoList(getActivity(), android.R.layout.simple_spinner_dropdown_item, eventos);

            adapterEventos.setDropDownViewResource(android.R.layout.simple_selectable_list_item);



            return null;
        }

        @Override
        protected void onPostExecute(List<Evento> eventos) {
            super.onPostExecute(eventos);
            lista.setAdapter(adapterEventos);
            lista.setOnItemClickListener(FragmentEventoProjeto.this);
            dialogLoad.dismiss();

        }
    }

}
