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

import br.com.vostre.repertori.EstiloDetalheActivity;
import br.com.vostre.repertori.MusicaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.EstiloList;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.form.ModalCadastroEstilo;
import br.com.vostre.repertori.form.ModalCadastroMusica;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.EstiloDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.DialogUtils;

public class EstilosFragment extends Fragment implements AdapterView.OnItemClickListener, ModalCadastroListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    ListView listViewEstilos;
    EstiloList adapter;
    EstiloDBHelper estiloDBHelper;
    List<Estilo> estilos;
    FloatingActionButton fabNova;

    Dialog dialogLoad;

    public EstilosFragment() {
        // Required empty public constructor
    }


    public static EstilosFragment newInstance() {
        EstilosFragment fragment = new EstilosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_estilos, container, false);

        listViewEstilos = (ListView) v.findViewById(R.id.listViewEstilos);
        fabNova = (FloatingActionButton) v.findViewById(R.id.fabNova);

        fabNova.setOnClickListener(this);

        CarregarItens carregarItens = new CarregarItens();
        carregarItens.execute();

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Estilo estilo = estilos.get(i);

        Intent intent = new Intent(getContext(), EstiloDetalheActivity.class);
        intent.putExtra("estilo", estilo.getId());
        startActivity(intent);
    }

    public void atualizaLista(){
        estilos = estiloDBHelper.listarTodos(getContext());
        adapter = new EstiloList(getActivity(), android.R.layout.simple_spinner_dropdown_item, estilos, true);

        if(listViewEstilos != null){
            listViewEstilos.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.fabNova:
                ModalCadastroEstilo modalCadastroEstilo = new ModalCadastroEstilo();
                modalCadastroEstilo.setListener(this);

                modalCadastroEstilo.show(getFragmentManager(), "modalEstilo");

                break;
        }

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        atualizaLista();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Estilo estilo = estilos.get(position);
        ModalCadastroEstilo modalCadastroEstilo = new ModalCadastroEstilo();
        modalCadastroEstilo.setListener(this);
        modalCadastroEstilo.setEstilo(estilo);

        modalCadastroEstilo.show(this.getFragmentManager(), "modalEstilo");

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
            estiloDBHelper = new EstiloDBHelper(getContext());
            estilos = estiloDBHelper.listarTodos(getContext());

            adapter = new EstiloList(getActivity(), R.layout.listview_estilos, estilos, true);

            return null;
        }

        @Override
        protected void onPostExecute(String tempo) {
            super.onPostExecute(tempo);
            listViewEstilos.setAdapter(adapter);
            listViewEstilos.setOnItemClickListener(EstilosFragment.this);
            listViewEstilos.setOnItemLongClickListener(EstilosFragment.this);
            dialogLoad.dismiss();
        }
    }

}
