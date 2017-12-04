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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import br.com.vostre.repertori.App;
import br.com.vostre.repertori.ContatoDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ContatoList;
import br.com.vostre.repertori.form.ModalCadastroContato;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Contato;
import br.com.vostre.repertori.model.dao.ContatoDBHelper;
import br.com.vostre.repertori.utils.DialogUtils;

public class ContatosFragment extends Fragment implements AdapterView.OnItemClickListener, ModalCadastroListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    ListView listViewContatos;
    ContatoList adapter;
    ContatoDBHelper contatoDBHelper;
    List<Contato> contatos;
    FloatingActionButton fabNova;
    Dialog dialogLoad;
    Tracker mTracker;

    public ContatosFragment() {
        // Required empty public constructor
    }


    public static ContatosFragment newInstance() {
        ContatosFragment fragment = new ContatosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App application = (App) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contatos, container, false);

        listViewContatos = (ListView) v.findViewById(R.id.listViewContatos);
        fabNova = (FloatingActionButton) v.findViewById(R.id.fabNova);

        fabNova.setOnClickListener(this);

        CarregarItens carregarItens = new CarregarItens();
        carregarItens.execute();

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Contato contato = contatos.get(i);

        Intent intent = new Intent(getContext(), ContatoDetalheActivity.class);
        intent.putExtra("contato", contato.getId());
        startActivity(intent);
    }

    public void atualizaLista(){
        contatos = contatoDBHelper.listarTodos(getContext());
        adapter = new ContatoList(getActivity(), android.R.layout.simple_spinner_dropdown_item, contatos);

        if(listViewContatos != null){
            listViewContatos.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.fabNova:
                ModalCadastroContato modalCadastroContato = new ModalCadastroContato();
                modalCadastroContato.setListener(this);

                modalCadastroContato.show(getFragmentManager(), "modalContato");

                break;
        }

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        atualizaLista();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Contato contato = contatos.get(position);
        ModalCadastroContato modalCadastroContato = new ModalCadastroContato();
        modalCadastroContato.setListener(this);
        modalCadastroContato.setContato(contato);

        modalCadastroContato.show(this.getFragmentManager(), "modalContato");

        return true;

    }

    @Override
    public void onResume() {
        mTracker.setScreenName("Tela Contatos");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
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
            contatoDBHelper = new ContatoDBHelper(getContext());
            contatos = contatoDBHelper.listarTodos(getContext());

            adapter = new ContatoList(getActivity(), R.layout.listview_contatos, contatos);


            return null;
        }

        @Override
        protected void onPostExecute(String tempo) {
            super.onPostExecute(tempo);
            listViewContatos.setAdapter(adapter);
            listViewContatos.setOnItemClickListener(ContatosFragment.this);
            listViewContatos.setOnItemLongClickListener(ContatosFragment.this);
            dialogLoad.dismiss();
        }
    }

}
