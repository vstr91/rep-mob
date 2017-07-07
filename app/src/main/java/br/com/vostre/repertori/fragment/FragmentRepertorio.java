package br.com.vostre.repertori.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.MainActivity;
import br.com.vostre.repertori.MusicaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.RepertorioActivity;
import br.com.vostre.repertori.form.ModalCadastroMusica;
import br.com.vostre.repertori.form.ModalCadastroMusicaProjeto;
import br.com.vostre.repertori.listener.LoadListener;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.DialogUtils;
import br.com.vostre.repertori.utils.SnackbarHelper;

/**
 * Created by Almir on 17/06/2015.
 */
public class FragmentRepertorio extends Fragment implements AdapterView.OnItemClickListener, ModalCadastroListener, AdapterView.OnItemLongClickListener {

    private ListView listaAtiva;
    private ListView listaEspera;

    private TextView textViewAtivas;
    private TextView textViewEspera;
    private TextView textViewAtivasObs;

    MusicaList adapterMusicasAtivas;
    MusicaList adapterMusicasEmEspera;
    int situacao = -1;
    String idProjeto;

    List<Musica> musicasAtivas;
    List<Musica> musicasEmEspera;

    Projeto projeto;
    Dialog dialogLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_listview_musicas, container, false);

        listaAtiva = (ListView) rootView.findViewById(R.id.listViewMusicasAtivas);
        listaEspera = (ListView) rootView.findViewById(R.id.listViewMusicasEmEspera);

        textViewAtivas = (TextView) rootView.findViewById(R.id.textViewAtivas);
        textViewEspera = (TextView) rootView.findViewById(R.id.textViewEspera);
        textViewAtivasObs = (TextView) rootView.findViewById(R.id.textViewAtivasObs);

        idProjeto = getArguments().getString("projeto");

        listaAtiva.setEmptyView(rootView.findViewById(R.id.textViewListaVazia));
        listaEspera.setEmptyView(rootView.findViewById(R.id.textViewListaVazia));

        dialogLoad = DialogUtils.criarAlertaCarregando(getContext(), "Carregando dados", "Por favor aguarde...");
        dialogLoad.show();

        CarregarItens carregarItens = new CarregarItens();
        carregarItens.execute();

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
                calcularTempoTotalRepertorio();
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

    private class CarregarItens extends AsyncTask<Void, String, String>{

        @Override
        protected String doInBackground(Void... voids) {
            musicasAtivas = carregaMusicas(0);
            musicasEmEspera = carregaMusicas(1);

            adapterMusicasAtivas =
                    new MusicaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicasAtivas);

            adapterMusicasAtivas.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

            adapterMusicasEmEspera =
                    new MusicaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicasEmEspera);

            adapterMusicasEmEspera.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

            String tempo = calcularTempoTotalRepertorio();

            return tempo;
        }

        @Override
        protected void onPostExecute(String tempo) {
            super.onPostExecute(tempo);
            listaAtiva.setAdapter(adapterMusicasAtivas);
            listaAtiva.setOnItemClickListener(FragmentRepertorio.this);
            listaAtiva.setOnItemLongClickListener(FragmentRepertorio.this);

            listaEspera.setAdapter(adapterMusicasEmEspera);
            listaEspera.setOnItemClickListener(FragmentRepertorio.this);
            listaEspera.setOnItemLongClickListener(FragmentRepertorio.this);
            textViewAtivas.setText("Músicas Ativas ("+musicasAtivas.size()+")");
            textViewEspera.setText("Músicas Em Espera ("+musicasEmEspera.size()+")");

            textViewAtivasObs.setText(tempo);
            dialogLoad.dismiss();

        }
    }

}
