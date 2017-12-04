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
import br.com.vostre.repertori.ArtistaDetalheActivity;
import br.com.vostre.repertori.CasaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ArtistaList;
import br.com.vostre.repertori.adapter.CasaList;
import br.com.vostre.repertori.form.ModalCadastroArtista;
import br.com.vostre.repertori.form.ModalCadastroCasa;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Casa;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.CasaDBHelper;
import br.com.vostre.repertori.utils.DialogUtils;

public class CasasFragment extends Fragment implements AdapterView.OnItemClickListener, ModalCadastroListener, AdapterView.OnItemLongClickListener, View.OnClickListener {

    ListView listViewCasas;
    CasaList adapter;
    CasaDBHelper casaDBHelper;
    List<Casa> casas;
    FloatingActionButton fabNova;
    Dialog dialogLoad;
    Tracker mTracker;

    public CasasFragment() {
        // Required empty public constructor
    }


    public static CasasFragment newInstance() {
        CasasFragment fragment = new CasasFragment();
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
        View v = inflater.inflate(R.layout.fragment_casas, container, false);

        listViewCasas = (ListView) v.findViewById(R.id.listViewCasas);
        fabNova = (FloatingActionButton) v.findViewById(R.id.fabNova);

        fabNova.setOnClickListener(this);

        CarregarItens carregarItens = new CarregarItens();
        carregarItens.execute();

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Casa casa = casas.get(i);

        Intent intent = new Intent(getContext(), CasaDetalheActivity.class);
        intent.putExtra("casa", casa.getId());
        startActivity(intent);
    }

    public void atualizaLista(){
        casas = casaDBHelper.listarTodos(getContext());
        adapter = new CasaList(getActivity(), android.R.layout.simple_spinner_dropdown_item, casas);

        if(listViewCasas != null){
            listViewCasas.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.fabNova:
                ModalCadastroCasa modalCadastroCasa = new ModalCadastroCasa();
                modalCadastroCasa.setListener(this);

                modalCadastroCasa.show(getFragmentManager(), "modalCasa");

                break;
        }

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        atualizaLista();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Casa casa = casas.get(position);
        ModalCadastroCasa modalCadastroCasa = new ModalCadastroCasa();
        modalCadastroCasa.setListener(this);
        modalCadastroCasa.setCasa(casa);

        modalCadastroCasa.show(this.getFragmentManager(), "modalCasa");

        return true;

    }

    @Override
    public void onResume() {
        mTracker.setScreenName("Tela Casas");
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
            casaDBHelper = new CasaDBHelper(getContext());
            casas = casaDBHelper.listarTodos(getContext());

            adapter = new CasaList(getActivity(), R.layout.listview_casas, casas);


            return null;
        }

        @Override
        protected void onPostExecute(String tempo) {
            super.onPostExecute(tempo);
            listViewCasas.setAdapter(adapter);
            listViewCasas.setOnItemClickListener(CasasFragment.this);
            listViewCasas.setOnItemLongClickListener(CasasFragment.this);
            dialogLoad.dismiss();
        }
    }

}
