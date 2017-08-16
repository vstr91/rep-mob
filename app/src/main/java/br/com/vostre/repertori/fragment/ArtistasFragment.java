package br.com.vostre.repertori.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import br.com.vostre.repertori.adapter.EstiloList;
import br.com.vostre.repertori.form.ModalCadastroArtista;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.EstiloDBHelper;
import br.com.vostre.repertori.utils.DialogUtils;

public class ArtistasFragment extends Fragment implements AdapterView.OnItemClickListener, ModalCadastroListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    ListView listViewArtistas;
    ArtistaList adapter;
    ArtistaDBHelper artistaDBHelper;
    List<Artista> artistas;
    FloatingActionButton fabNova;
    Dialog dialogLoad;

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

        fabNova.setOnClickListener(this);

        CarregarItens carregarItens = new CarregarItens();
        carregarItens.execute();

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
        adapter = new ArtistaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, artistas, true);

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

    private class CarregarItens extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogLoad = DialogUtils.criarAlertaCarregando(getContext(), "Carregando dados", "Por favor aguarde...");
            dialogLoad.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            artistaDBHelper = new ArtistaDBHelper(getContext());
            artistas = artistaDBHelper.listarTodos(getContext());

            adapter = new ArtistaList(getActivity(), R.layout.listview_artistas, artistas, true);


            return null;
        }

        @Override
        protected void onPostExecute(String tempo) {
            super.onPostExecute(tempo);
            listViewArtistas.setAdapter(adapter);
            listViewArtistas.setOnItemClickListener(ArtistasFragment.this);
            listViewArtistas.setOnItemLongClickListener(ArtistasFragment.this);
            dialogLoad.dismiss();
        }
    }

}
