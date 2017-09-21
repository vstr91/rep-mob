package br.com.vostre.repertori.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.App;
import br.com.vostre.repertori.MusicaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.RepertorioDetalheActivity;
import br.com.vostre.repertori.adapter.RepertorioList;
import br.com.vostre.repertori.form.ModalCadastroMusicaProjeto;
import br.com.vostre.repertori.form.ModalCadastroRepertorio;
import br.com.vostre.repertori.listener.ListviewComFiltroListener;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaRepertorio;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.DialogUtils;

/**
 * Created by Almir on 17/06/2015.
 */
public class FragmentRepertorio extends Fragment implements AdapterView.OnItemClickListener, ModalCadastroListener, AdapterView.OnItemLongClickListener, TextWatcher {

    private ListView listaAtiva;
    private ListView listaRepertorio;

    private TextView textViewAtivas;
    private TextView textViewEspera;
    private TextView textViewAtivasObs;
    private EditText editTextFiltro;

    MusicaList adapterMusicasAtivas;
    RepertorioList adapterRepertorios;
    int situacao = -1;
    String idProjeto;

    List<Musica> musicasAtivas;
    List<Repertorio> repertorios;

    Projeto projeto;
    Dialog dialogLoad;
    BroadcastReceiver br;

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
                R.layout.activity_listview_musicas, container, false);

        listaAtiva = (ListView) rootView.findViewById(R.id.listViewMusicasAtivas);
        listaRepertorio = (ListView) rootView.findViewById(R.id.listViewRepertorios);

        textViewAtivas = (TextView) rootView.findViewById(R.id.textViewAtivas);
        textViewEspera = (TextView) rootView.findViewById(R.id.textViewEspera);
        textViewAtivasObs = (TextView) rootView.findViewById(R.id.textViewAtivasObs);
        editTextFiltro = (EditText) rootView.findViewById(R.id.editTextFiltro);

        editTextFiltro.addTextChangedListener(this);

        idProjeto = getArguments().getString("projeto");

        listaAtiva.setEmptyView(rootView.findViewById(R.id.textViewListaVazia));
        listaRepertorio.setEmptyView(rootView.findViewById(R.id.textViewListaRepertoriosVazia));

        dialogLoad = DialogUtils.criarAlertaCarregando(getContext(), "Carregando dados", "Por favor aguarde...");
        dialogLoad.show();

        CarregarItens carregarItens = new CarregarItens();
        carregarItens.execute();

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent = null;

        switch(parent.getId()){
            case R.id.listViewMusicasAtivas:
                Musica musica = musicasAtivas.get(position);
                intent = new Intent(getContext(), MusicaDetalheActivity.class);
                intent.putExtra("musica", musica.getId());
                break;
            case R.id.listViewRepertorios:
                Repertorio repertorio = adapterRepertorios.getItem(position);
                intent = new Intent(getContext(), RepertorioDetalheActivity.class);
                intent.putExtra("repertorio", repertorio.getId());
                break;
        }


        startActivity(intent);
    }

    private List<Musica> carregaMusicas(int situacao){

        List<Musica> musicas = null;

        if(idProjeto != null){
            MusicaProjetoDBHelper musicaProjetoDBHelper = new MusicaProjetoDBHelper(getContext());
            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(getContext());
            projeto = new Projeto();
            projeto.setId(idProjeto);
            projeto = projetoDBHelper.carregar(getContext(), projeto);

            switch(situacao){
                case 0:
                    musicasAtivas = musicaProjetoDBHelper.listarTodosPorProjeto(getContext(), projeto, situacao);
                    musicas = musicasAtivas;
                    break;
//                case 1:
//                    musicasEmEspera = musicaProjetoDBHelper.listarTodosPorProjeto(getContext(), projeto, situacao);
//                    musicas = musicasEmEspera;
//                    break;
            }


            return musicas;
        } else{
            return null;
        }



    }

    private List<Repertorio> carregaRepertorios(){

        List<Repertorio> repertorios = null;

        if(idProjeto != null){
            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(getContext());
            RepertorioDBHelper repertorioDBHelper = new RepertorioDBHelper(getContext());
            projeto = new Projeto();
            projeto.setId(idProjeto);
            projeto = projetoDBHelper.carregar(getContext(), projeto);

            repertorios = repertorioDBHelper.listarTodosAtivosPorProjeto(getContext(), projeto);


            return repertorios;
        } else{
            return null;
        }



    }

    public void atualizaLista(){

        if(getActivity() != null){
            musicasAtivas = carregaMusicas(0);
            adapterMusicasAtivas =
                    new MusicaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicasAtivas);

            if(listaAtiva != null){
                listaAtiva.setAdapter(adapterMusicasAtivas);
                adapterMusicasAtivas.notifyDataSetChanged();
                textViewAtivas.setText("Músicas Ativas ("+musicasAtivas.size()+")");
                calcularTempoTotalRepertorio();
            }

            repertorios = carregaRepertorios();
            adapterRepertorios =
                    new RepertorioList(getActivity(), android.R.layout.simple_spinner_dropdown_item, repertorios);

            if(listaRepertorio != null){
                listaRepertorio.setAdapter(adapterRepertorios);
                adapterRepertorios.notifyDataSetChanged();
            }
        }


    }

    private String calcularTempoTotalRepertorio(){
        long tempoMedio = 0;
        int cont = 0;
        BigInteger total = BigInteger.ZERO;

        for(Musica musica : musicasAtivas){

            Calendar c = musica.calcularMedia(getContext());

            if(c != null){
                //System.out.println(musica.getNome()+" | "+DataUtils.toStringSomenteHoras(c, 0)+" | "+DataUtils.tempoParaSegundos(DataUtils.toStringSomenteHoras(c, 1)));
                total = total.add(BigInteger.valueOf(c.getTimeInMillis()));
                tempoMedio += DataUtils.tempoParaSegundos(DataUtils.toStringSomenteHoras(c, 1));
                cont++;
            }

        }

        Calendar tempoMedioAtivo = Calendar.getInstance();
        tempoMedioAtivo.setTimeInMillis(total.longValue());
        //System.out.println("Tempo Médio: "+DataUtils.segundosParaTempo(tempoMedio));
        return "Tempo Total: "+DataUtils.segundosParaTempo(tempoMedio)+" ("+cont+" música(s) considerada(s))";
    }

    @Override
    public void onModalCadastroDismissed(int resultado) {

        if(resultado > -1){
            atualizaLista();
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Musica musica = null;

        switch(parent.getId()){
            case R.id.listViewMusicasAtivas:
                musica = musicasAtivas.get(position);
                ModalCadastroMusicaProjeto modalCadastroMusicaProjeto = new ModalCadastroMusicaProjeto();
                modalCadastroMusicaProjeto.setListener(this);
                modalCadastroMusicaProjeto.setMusica(musica);
                modalCadastroMusicaProjeto.setProjeto(projeto);

                modalCadastroMusicaProjeto.show(this.getFragmentManager(), "modalMusicaProjeto");
                break;
            case R.id.listViewRepertorios:
                Repertorio repertorio = repertorios.get(position);
                ModalCadastroRepertorio modalCadastroRepertorio = new ModalCadastroRepertorio();
                modalCadastroRepertorio.setListener(this);
                modalCadastroRepertorio.setRepertorio(repertorio);

                modalCadastroRepertorio.show(this.getFragmentManager(), "modalRepertorio");
                break;
        }




        return true;

    }

    @Override
    public void onResume() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                CarregarItens carregarItens = new CarregarItens();
                carregarItens.execute();
            }
        };

        mTracker.setScreenName("Tela Repertório");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(br, new IntentFilter("br.com.vostre.repertori.AtualizaDadosService"));

        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(br);
        super.onPause();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        if(adapterMusicasAtivas != null){
            adapterMusicasAtivas.getFilter().filter(charSequence);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private class CarregarItens extends AsyncTask<Void, String, String> implements ListviewComFiltroListener {

        @Override
        protected String doInBackground(Void... voids) {
            musicasAtivas = carregaMusicas(0);
            repertorios = carregaRepertorios();

            adapterMusicasAtivas =
                    new MusicaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicasAtivas);
            adapterMusicasAtivas.setListener(this);

            adapterMusicasAtivas.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

            adapterRepertorios =
                    new RepertorioList(getActivity(), android.R.layout.simple_spinner_dropdown_item, repertorios);

            adapterRepertorios.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

            String tempo = calcularTempoTotalRepertorio();

            return tempo;
        }

        @Override
        protected void onPostExecute(String tempo) {
            super.onPostExecute(tempo);
            listaAtiva.setAdapter(adapterMusicasAtivas);
            listaAtiva.setOnItemClickListener(FragmentRepertorio.this);
            listaAtiva.setOnItemLongClickListener(FragmentRepertorio.this);

            listaRepertorio.setAdapter(adapterRepertorios);
            listaRepertorio.setOnItemClickListener(FragmentRepertorio.this);
            listaRepertorio.setOnItemLongClickListener(FragmentRepertorio.this);
            textViewAtivas.setText("Músicas Ativas ("+musicasAtivas.size()+")");

            textViewAtivasObs.setText(tempo);
            dialogLoad.dismiss();

        }

        @Override
        public void onListviewComFiltroDismissed(List<Musica> dados) {
            musicasAtivas = dados;
            adapterMusicasAtivas.notifyDataSetChanged();
            listaAtiva.invalidate();
        }
    }

}
