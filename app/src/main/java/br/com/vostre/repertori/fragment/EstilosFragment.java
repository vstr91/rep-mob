package br.com.vostre.repertori.fragment;

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

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.EstiloList;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.form.ModalCadastroEstilo;
import br.com.vostre.repertori.form.ModalCadastroMusica;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Estilo;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.EstiloDBHelper;

public class EstilosFragment extends Fragment implements AdapterView.OnItemClickListener, ModalCadastroListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    ListView listViewEstilos;
    EstiloList adapter;
    EstiloDBHelper estiloDBHelper;
    List<Estilo> estilos;
    FloatingActionButton fabNova;

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

        estiloDBHelper = new EstiloDBHelper(getContext());
        estilos = estiloDBHelper.listarTodos(getContext());

        adapter = new EstiloList(getActivity(), R.layout.listview_estilos, estilos);
        listViewEstilos.setAdapter(adapter);
        listViewEstilos.setOnItemClickListener(this);
        listViewEstilos.setOnItemLongClickListener(this);

        fabNova.setOnClickListener(this);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getContext(), "Clicou", Toast.LENGTH_LONG).show();
    }

    public void atualizaLista(){
        estilos = estiloDBHelper.listarTodos(getContext());
        adapter = new EstiloList(getActivity(), android.R.layout.simple_spinner_dropdown_item, estilos);

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

}
