package br.com.vostre.repertori.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import br.com.vostre.repertori.App;
import br.com.vostre.repertori.MainActivity;
import br.com.vostre.repertori.MusicaProjetoActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.adapter.ProjetoList;
import br.com.vostre.repertori.listener.LoadListener;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;
import br.com.vostre.repertori.utils.AnalyticsApplication;
import br.com.vostre.repertori.utils.DialogUtils;

public class ProjetosFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ListView listViewProjetos;
    List<Projeto> projetos;

    LoadListener listener;

    ProjetoDBHelper projetoDBHelper;
    ProjetoList adapter;
    Dialog dialogLoad;

    Tracker mTracker;

    public LoadListener getListener() {
        return listener;
    }

    public void setListener(LoadListener listener) {
        this.listener = listener;
    }

    public ProjetosFragment() {
        // Required empty public constructor
    }


    public static ProjetosFragment newInstance() {
        ProjetosFragment fragment = new ProjetosFragment();
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
        View v = inflater.inflate(R.layout.fragment_projetos, container, false);

        listViewProjetos = (ListView) v.findViewById(R.id.listViewProjetos);

        CarregarItens carregarItens = new CarregarItens();
        carregarItens.execute();

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Projeto projeto = projetos.get(i);
        Intent intent = new Intent(getContext(), MusicaProjetoActivity.class);
        intent.putExtra("projeto", projeto.getId());
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    @Override
    public void onResume() {
        mTracker.setScreenName("Tela Projetos");
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
            projetoDBHelper = new ProjetoDBHelper(getContext());
            projetos = projetoDBHelper.listarTodosAtivos(getContext());

            if(projetos.size() == 1){
                Projeto projeto = projetos.get(0);
                Intent intent = new Intent(getContext(), MusicaProjetoActivity.class);
                intent.putExtra("projeto", projeto.getId());
                startActivity(intent);
            }

            adapter = new ProjetoList(getActivity(), R.layout.listview_projetos, projetos, false);

            return null;
        }

        @Override
        protected void onPostExecute(String tempo) {
            super.onPostExecute(tempo);
            listViewProjetos.setAdapter(adapter);
            listViewProjetos.setOnItemClickListener(ProjetosFragment.this);
            listViewProjetos.setOnItemLongClickListener(ProjetosFragment.this);
            dialogLoad.dismiss();

        }
    }

}
