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
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.MusicaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.RepertorioDetalheActivity;
import br.com.vostre.repertori.adapter.RepertorioList;
import br.com.vostre.repertori.form.ModalCadastroMusicaProjeto;
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
public class FragmentRepertorio extends Fragment implements AdapterView.OnItemClickListener, ModalCadastroListener, AdapterView.OnItemLongClickListener {

    private ListView listaAtiva;
    private ListView listaRepertorio;

    private TextView textViewAtivas;
    private TextView textViewEspera;
    private TextView textViewAtivasObs;

    MusicaList adapterMusicasAtivas;
    RepertorioList adapterRepertorios;
    int situacao = -1;
    String idProjeto;

    List<Musica> musicasAtivas;
    List<Repertorio> repertorios;

    Projeto projeto;
    Dialog dialogLoad;

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
                Musica musica = adapterMusicasAtivas.getItem(position);
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

            repertorios = repertorioDBHelper.listarTodosPorProjeto(getContext(), projeto);


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

        for(Musica musica : musicasAtivas){

            Calendar c = musica.calcularMedia(getContext());

            if(c != null){
                tempoMedio += c.getTimeInMillis();
                cont++;
            }

        }

        Calendar tempoMedioAtivo = Calendar.getInstance();
        tempoMedioAtivo.setTimeInMillis(tempoMedio);
        return "Tempo Total: "+DataUtils.toStringSomenteHoras(tempoMedioAtivo, 1)+" ("+cont+" música(s) considerada(s))";
    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        atualizaLista();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Musica musica = null;

        switch(parent.getId()){
            case R.id.listViewMusicasAtivas:
                musica = musicasAtivas.get(position);
                break;
//            case R.id.listViewMusicasEmEspera:
//                musica = musicasEmEspera.get(position);
//                break;
        }


        ModalCadastroMusicaProjeto modalCadastroMusicaProjeto = new ModalCadastroMusicaProjeto();
        modalCadastroMusicaProjeto.setListener(this);
        modalCadastroMusicaProjeto.setMusica(musica);
        modalCadastroMusicaProjeto.setProjeto(projeto);

        modalCadastroMusicaProjeto.show(this.getFragmentManager(), "modalMusicaProjeto");

        return true;

    }

    private class CarregarItens extends AsyncTask<Void, String, String>{

        @Override
        protected String doInBackground(Void... voids) {
            musicasAtivas = carregaMusicas(0);
            repertorios = carregaRepertorios();

            adapterMusicasAtivas =
                    new MusicaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicasAtivas);

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
    }

}
