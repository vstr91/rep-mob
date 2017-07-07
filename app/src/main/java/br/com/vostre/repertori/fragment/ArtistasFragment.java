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

import br.com.vostre.repertori.ArtistaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ArtistaList;
import br.com.vostre.repertori.form.ModalCadastroArtista;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;

public class ArtistasFragment extends Fragment implements AdapterView.OnItemClickListener, ModalCadastroListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    ListView listViewArtistas;
    ArtistaList adapter;
    ArtistaDBHelper artistaDBHelper;
    List<Artista> artistas;
    FloatingActionButton fabNova;

    public ArtistasFragment() {
        // Required empty public constructor
    }


    public static ArtistasFragment newInstance() {
        ArtistasFragment fragment = new ArtistasFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_artistas, container, false);

        listViewArtistas = (ListView) v.findViewById(R.id.listViewArtistas);
        fabNova = (FloatingActionButton) v.findViewById(R.id.fabNova);

        artistaDBHelper = new ArtistaDBHelper(getContext());
        artistas = artistaDBHelper.listarTodos(getContext());

        adapter = new ArtistaList(getActivity(), R.layout.listview_artistas, artistas);
        listViewArtistas.setAdapter(adapter);
        listViewArtistas.setOnItemClickListener(this);
        listViewArtistas.setOnItemLongClickListener(this);

        fabNova.setOnClickListener(this);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Artista artista = artistas.get(i);

        Intent intent = new Intent(getContext(), ArtistaDetalheActivity.class);
        intent.putExtra("artista", artista.getId());
        startActivity(intent);
    }

    public void atualizaLista(){
        artistas = artistaDBHelper.listarTodos(getContext());
        adapter = new ArtistaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, artistas);

        if(listViewArtistas != null){
            listViewArtistas.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.fabNova:
                ModalCadastroArtista modalCadastroArtista = new ModalCadastroArtista();
                modalCadastroArtista.setListener(this);

                modalCadastroArtista.show(getFragmentManager(), "modalArtista");

                break;
        }

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        atualizaLista();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Artista artista = artistas.get(position);
        ModalCadastroArtista modalCadastroArtista = new ModalCadastroArtista();
        modalCadastroArtista.setListener(this);
        modalCadastroArtista.setArtista(artista);

        modalCadastroArtista.show(this.getFragmentManager(), "modalArtista");

        return true;

    }

}
