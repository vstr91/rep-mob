package br.com.vostre.repertori.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import br.com.vostre.repertori.MusicaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.MusicaEstiloList;
import br.com.vostre.repertori.adapter.MusicaExecucaoList;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.form.ModalCadastroMusicaProjeto;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaExecucao;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;

/**
 * Created by Almir on 17/06/2015.
 */
public class FragmentRepertorioEstilo extends Fragment implements AdapterView.OnItemClickListener, ModalCadastroListener {

    private ListView lista;
    private ListView listaExecucoes;

    MusicaEstiloList adapterMusicasEstilos;
    MusicaExecucaoList adapterMusicasExecucoes;

    int situacao = -1;
    String idProjeto;

    List<Musica> musicas;
    List<MusicaExecucao> execucoes;

    Projeto projeto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_listview_musicas_estilos, container, false);

        lista = (ListView) rootView.findViewById(R.id.listViewMusicasEstilos);
        listaExecucoes = (ListView) rootView.findViewById(R.id.listViewMusicasExecucoes);

        idProjeto = getArguments().getString("projeto");

        musicas = carregaMusicas();
        execucoes = carregaUltimaExecucao();
        Collections.sort(execucoes);

        adapterMusicasEstilos =
                new MusicaEstiloList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicas);

        adapterMusicasEstilos.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        lista.setAdapter(adapterMusicasEstilos);
        lista.setOnItemClickListener(this);
        lista.setEmptyView(rootView.findViewById(R.id.textViewListaVazia));

        // EXECUCOES
        adapterMusicasExecucoes =
                new MusicaExecucaoList(getActivity(), android.R.layout.simple_spinner_dropdown_item, execucoes);

        adapterMusicasExecucoes.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listaExecucoes.setAdapter(adapterMusicasExecucoes);
        listaExecucoes.setOnItemClickListener(this);
        listaExecucoes.setEmptyView(rootView.findViewById(R.id.textViewListaVazia));

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Musica musica = null;

        switch(parent.getId()){
            case R.id.listViewMusicasEstilos:
                musica = adapterMusicasEstilos.getItem(position);
                break;
            case R.id.listViewMusicasExecucoes:
                musica = adapterMusicasExecucoes.getItem(position).getMusica();
                break;
        }

        Intent intent = new Intent(getContext(), MusicaDetalheActivity.class);
        intent.putExtra("musica", musica.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizaLista();
        atualizaExecucoes();
    }

    private List<Musica> carregaMusicas(){

        List<Musica> musicas = null;

        if(idProjeto != null){
            MusicaProjetoDBHelper musicaProjetoDBHelper = new MusicaProjetoDBHelper(getContext());
            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(getContext());
            projeto = new Projeto();
            projeto.setId(idProjeto);
            projeto = projetoDBHelper.carregar(getContext(), projeto);

             musicas = musicaProjetoDBHelper.listarTodosPorProjetoEEstilo(getContext(), projeto, 0);

            return musicas;
        } else{
            return null;
        }

    }

    public void atualizaLista(){
        musicas = carregaMusicas();
        adapterMusicasEstilos =
                new MusicaEstiloList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicas);

        if(lista != null){
            lista.setAdapter(adapterMusicasEstilos);
            adapterMusicasEstilos.notifyDataSetChanged();
        }

    }

    public void atualizaExecucoes(){
        musicas = carregaMusicas();
        adapterMusicasExecucoes =
                new MusicaExecucaoList(getActivity(), android.R.layout.simple_spinner_dropdown_item, execucoes);

        if(listaExecucoes != null){
            listaExecucoes.setAdapter(adapterMusicasExecucoes);
            adapterMusicasExecucoes.notifyDataSetChanged();
        }

    }

    private List<MusicaExecucao> carregaUltimaExecucao(){

        List<MusicaExecucao> musicas = null;

        if(idProjeto != null){
            MusicaProjetoDBHelper musicaProjetoDBHelper = new MusicaProjetoDBHelper(getContext());
            ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(getContext());
            projeto = new Projeto();
            projeto.setId(idProjeto);
            projeto = projetoDBHelper.carregar(getContext(), projeto);

            musicas = musicaProjetoDBHelper.ultimaExecucaoPorProjeto(getContext(), projeto);

            return musicas;
        } else{
            return null;
        }

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        atualizaLista();
    }

}
