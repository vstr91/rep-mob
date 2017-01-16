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
import android.widget.Toast;

import java.util.List;

import br.com.vostre.repertori.MusicaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.RepertorioActivity;
import br.com.vostre.repertori.form.ModalCadastroMusica;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.utils.SnackbarHelper;

/**
 * Created by Almir on 17/06/2015.
 */
public class FragmentRepertorio extends Fragment implements AdapterView.OnItemClickListener, ModalCadastroListener, AdapterView.OnItemLongClickListener {

    private ListView lista;
    MusicaList adapterMusicas;
    int situacao = -1;

    List<Musica> musicas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_listview_musicas, container, false);

        lista = (ListView) rootView.findViewById(R.id.listViewMusicas);
//        editTextFiltro = (EditText) rootView.findViewById(R.id.editTextFiltro);
//        editTextFiltro.addTextChangedListener(this);

        situacao = getArguments().getInt("situacao");

        musicas = carregaMusicas(situacao);

        adapterMusicas =
                new MusicaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicas);

        adapterMusicas.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        lista.setAdapter(adapterMusicas);
        lista.setOnItemClickListener(this);
        lista.setOnItemLongClickListener(this);
        lista.setEmptyView(rootView.findViewById(R.id.textViewListaVazia));

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Musica musica = adapterMusicas.getItem(position);

        Intent intent = new Intent(getContext(), MusicaDetalheActivity.class);
        intent.putExtra("musica", musica.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizaLista(situacao);
    }

    private List<Musica> carregaMusicas(int situacao){
        MusicaDBHelper musicaDBHelper = new MusicaDBHelper(getContext());
        musicas = musicaDBHelper.listarTodosPorSituacao(getContext(), situacao);

        return musicas;

    }

    public void atualizaLista(int status){
        musicas = carregaMusicas(status);
        adapterMusicas =
                new MusicaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicas);

        if(lista != null){
            lista.setAdapter(adapterMusicas);
            adapterMusicas.notifyDataSetChanged();
        }
    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        atualizaLista(situacao);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Musica musica = musicas.get(position);
        ModalCadastroMusica modalCadastroMusica = new ModalCadastroMusica();
        modalCadastroMusica.setListener(this);
        modalCadastroMusica.setMusica(musica);

        modalCadastroMusica.show(this.getFragmentManager(), "modalMusica");

        return true;

    }
}
