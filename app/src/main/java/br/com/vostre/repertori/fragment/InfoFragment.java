package br.com.vostre.repertori.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.ArtistaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ArtistaList;
import br.com.vostre.repertori.form.ModalCadastroArtista;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.ParametroDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

public class InfoFragment extends Fragment {

    ParametroDBHelper parametroDBHelper;
    TextView textViewUltimaSincronizacao;
    TextView textViewVersao;

    public InfoFragment() {
        // Required empty public constructor
    }


    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);

        textViewUltimaSincronizacao = (TextView) v.findViewById(R.id.textViewUltimoAcesso);
        textViewVersao = (TextView) v.findViewById(R.id.textViewVersao);

        parametroDBHelper = new ParametroDBHelper(getContext());
        String ultimoAcesso = parametroDBHelper.carregarUltimoAcesso(getContext());

        Calendar calendar = DataUtils.bancoParaData(ultimoAcesso);

        textViewUltimaSincronizacao.setText(DataUtils.toString(calendar, true));

        try {
            PackageInfo pInfo = this.getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            textViewVersao.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return v;
    }

}