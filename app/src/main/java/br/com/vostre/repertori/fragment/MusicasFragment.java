package br.com.vostre.repertori.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.vostre.repertori.MusicaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.form.ModalCadastroMusica;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;

public class MusicasFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, ModalCadastroListener, AdapterView.OnItemLongClickListener {

    ListView listViewMusicas;
    FloatingActionButton fabNova;
    List<Musica> musicas;
    MusicaDBHelper musicaDBHelper;
    MusicaList adapter;

    public MusicasFragment() {
        // Required empty public constructor
    }


    public static MusicasFragment newInstance() {
        MusicasFragment fragment = new MusicasFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_musicas, container, false);

        listViewMusicas = (ListView) v.findViewById(R.id.listViewMusicas);

        musicaDBHelper = new MusicaDBHelper(getContext());
        musicas = musicaDBHelper.listarTodos(getContext());

        adapter = new MusicaList(getActivity(), R.layout.listview_musicas, musicas);
        listViewMusicas.setAdapter(adapter);
        listViewMusicas.setOnItemClickListener(this);
        listViewMusicas.setOnItemLongClickListener(this);

        fabNova = (FloatingActionButton) v.findViewById(R.id.fabNova);
        fabNova.setOnClickListener(this);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Musica musica = adapter.getItem(i);

        Intent intent = new Intent(getContext(), MusicaDetalheActivity.class);
        intent.putExtra("musica", musica.getId());
        startActivity(intent);
    }

    public void atualizaLista(){
        musicas = musicaDBHelper.listarTodos(getContext());
        adapter =
                new MusicaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, musicas);

        if(listViewMusicas != null){
            listViewMusicas.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.fabNova:
                ModalCadastroMusica modalCadastroMusica = new ModalCadastroMusica();
                modalCadastroMusica.setListener(this);
                modalCadastroMusica.setStatus(0);

                modalCadastroMusica.show(getFragmentManager(), "modalMusica");

                break;
        }

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        atualizaLista();
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
