package br.com.vostre.repertori.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import br.com.vostre.repertori.App;
import br.com.vostre.repertori.MusicaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.form.ModalCadastroMusica;
import br.com.vostre.repertori.form.ModalOpcoesMusica;
import br.com.vostre.repertori.listener.ListviewComFiltroListener;
import br.com.vostre.repertori.listener.LoadListener;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.AnalyticsApplication;
import br.com.vostre.repertori.utils.DialogUtils;

public class MusicasFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, ModalCadastroListener, AdapterView.OnItemLongClickListener, TextWatcher {

    ListView listViewMusicas;
    FloatingActionButton fabNova;
    List<Musica> musicas;
    MusicaDBHelper musicaDBHelper;
    MusicaList adapter;
    Dialog dialogLoad;
    LoadListener listener;

    Tracker mTracker;

    EditText editTextFiltro;

    public LoadListener getListener() {
        return listener;
    }

    public void setListener(LoadListener listener) {
        this.listener = listener;
    }

    public MusicasFragment() {
        // Required empty public constructor
    }


    public static MusicasFragment newInstance() {
        MusicasFragment fragment = new MusicasFragment();
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
        View v = inflater.inflate(R.layout.fragment_musicas, container, false);

        listViewMusicas = (ListView) v.findViewById(R.id.listViewMusicas);
        editTextFiltro = (EditText) v.findViewById(R.id.editTextFiltro);

        fabNova = (FloatingActionButton) v.findViewById(R.id.fabNova);
        fabNova.setOnClickListener(this);
        editTextFiltro.addTextChangedListener(this);

        CarregarItens carregarItens = new CarregarItens();
        carregarItens.execute();

        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if(musicas != null){
            Musica musica = musicas.get(i);

            Intent intent = new Intent(getContext(), MusicaDetalheActivity.class);
            intent.putExtra("musica", musica.getId());
            startActivity(intent);
        }


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
    public void onResume() {
        mTracker.setScreenName("Tela Músicas");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        atualizaLista();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Musica musica = musicas.get(position);

        ModalOpcoesMusica modalOpcoesMusica = new ModalOpcoesMusica();
        modalOpcoesMusica.setMusica(musica);

        modalOpcoesMusica.show(getFragmentManager(), "modalOpcoesMusica");

        return true;

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        adapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private class CarregarItens extends AsyncTask<Void, String, String> implements ListviewComFiltroListener {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogLoad = DialogUtils.criarAlertaCarregando(getContext(), "Carregando dados", "Por favor aguarde...");
            dialogLoad.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            musicaDBHelper = new MusicaDBHelper(getContext());
            musicas = musicaDBHelper.listarTodos(getContext());

            adapter = new MusicaList(getActivity(), R.layout.listview_musicas, musicas);
            adapter.setListener(this);

            return null;
        }

        @Override
        protected void onPostExecute(String tempo) {
            super.onPostExecute(tempo);
            listViewMusicas.setAdapter(adapter);
            listViewMusicas.setOnItemClickListener(MusicasFragment.this);
            listViewMusicas.setOnItemLongClickListener(MusicasFragment.this);
            dialogLoad.dismiss();
        }

        @Override
        public void onListviewComFiltroDismissed(List<Musica> dados) {
            musicas = dados;
            adapter.notifyDataSetChanged();
            listViewMusicas.invalidate();
        }

    }

}
