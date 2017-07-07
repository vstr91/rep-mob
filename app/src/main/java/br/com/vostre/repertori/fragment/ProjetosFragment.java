package br.com.vostre.repertori.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.vostre.repertori.MusicaProjetoActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ProjetoList;
import br.com.vostre.repertori.listener.LoadListener;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;

public class ProjetosFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView listViewProjetos;
    List<Projeto> projetos;

    LoadListener listener;

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
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_projetos, container, false);

        listViewProjetos = (ListView) v.findViewById(R.id.listViewProjetos);

        ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(getContext());
        projetos = projetoDBHelper.listarTodosAtivos(getContext());

        ProjetoList adapter = new ProjetoList(getActivity(), R.layout.listview_projetos, projetos);
        listViewProjetos.setAdapter(adapter);
        listViewProjetos.setOnItemClickListener(this);

        listener.onLoadFinished();

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Projeto projeto = projetos.get(i);
        Intent intent = new Intent(getContext(), MusicaProjetoActivity.class);
        intent.putExtra("projeto", projeto.getId());
        startActivity(intent);

    }

}
