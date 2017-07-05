package br.com.vostre.repertori.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.vostre.repertori.MusicaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.RepertorioActivity;
import br.com.vostre.repertori.form.ModalCadastroMusica;
import br.com.vostre.repertori.form.ModalCadastroMusicaProjeto;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.utils.SnackbarHelper;

/**
 * Created by Almir on 17/06/2015.
 */
public class FragmentRepertorio extends Fragment implements AdapterView.OnItemClickListener, ModalCadastroListener, AdapterView.OnItemLongClickListener {

    private ListView listaAtiva;
    private ListView listaEspera;

    private TextView textViewAtivas;
    private TextView textViewEspera;

    MusicaList adapterMusicasAtivas;
    MusicaList adapterMusicasEmEspera;
    int situacao = -1;
    String idProjeto;

    List<Musica> musicasAtivas;
    List<Musica> musicasEmEspera;

    Projeto projeto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_listview_musicas, container, false);

        listaAtiva = (ListView) rootView.findViewById(R.id.listViewMusicasAtivas);
        listaEspera = (ListView) rootView.findViewById(R.id.listViewMusicasEmEspera);

        textViewAtivas = (TextView) rootView.findViewById(R.id.textViewAtivas);
        textViewEspera = (TextView) rootView.findViewById(R.id.textViewEspera);

        idProjeto = getArguments().getString("projeto");

        musicasAtivas = carregaMusicas(0);
        musicasEmEspera = carregaMusicas(1);

        adapterMusicasAtivas =
                new MusicaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicasAtivas);

        adapterMusicasAtivas.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listaAtiva.setAdapter(adapterMusicasAtivas);
        listaAtiva.setOnItemClickListener(this);
        listaAtiva.setOnItemLongClickListener(this);
        listaAtiva.setEmptyView(rootView.findViewById(R.id.textViewListaVazia));

        adapterMusicasEmEspera =
                new MusicaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicasEmEspera);

        adapterMusicasEmEspera.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listaEspera.setAdapter(adapterMusicasEmEspera);
        listaEspera.setOnItemClickListener(this);
        listaEspera.setOnItemLongClickListener(this);
        listaEspera.setEmptyView(rootView.findViewById(R.id.textViewListaVazia));

        textViewAtivas.setText("Músicas Ativas ("+musicasAtivas.size()+")");
        textViewEspera.setText("Músicas Em Espera ("+musicasEmEspera.size()+")");

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Musica musica = null;

        switch(parent.getId()){
            case R.id.listViewMusicasAtivas:
                musica = adapterMusicasAtivas.getItem(position);
                break;
            case R.id.listViewMusicasEmEspera:
                musica = adapterMusicasEmEspera.getItem(position);
                break;
        }

        Intent intent = new Intent(getContext(), MusicaDetalheActivity.class);
        intent.putExtra("musica", musica.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        //atualizaLista();
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
                case 1:
                    musicasEmEspera = musicaProjetoDBHelper.listarTodosPorProjeto(getContext(), projeto, situacao);
                    musicas = musicasEmEspera;
                    break;
            }


            return musicas;
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
            }

            musicasEmEspera = carregaMusicas(1);
            adapterMusicasEmEspera =
                    new MusicaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicasEmEspera);

            if(listaEspera != null){
                listaEspera.setAdapter(adapterMusicasEmEspera);
                adapterMusicasEmEspera.notifyDataSetChanged();
                textViewEspera.setText("Músicas Em Espera ("+musicasEmEspera.size()+")");
            }
        }


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
            case R.id.listViewMusicasEmEspera:
                musica = musicasEmEspera.get(position);
                break;
        }


        ModalCadastroMusicaProjeto modalCadastroMusicaProjeto = new ModalCadastroMusicaProjeto();
        modalCadastroMusicaProjeto.setListener(this);
        modalCadastroMusicaProjeto.setMusica(musica);
        modalCadastroMusicaProjeto.setProjeto(projeto);

        modalCadastroMusicaProjeto.show(this.getFragmentManager(), "modalMusicaProjeto");

        return true;

    }
}
