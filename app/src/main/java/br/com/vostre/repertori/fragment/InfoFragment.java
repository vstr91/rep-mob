package br.com.vostre.repertori.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.App;
import br.com.vostre.repertori.ArtistaDetalheActivity;
import br.com.vostre.repertori.MainActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ArtistaList;
import br.com.vostre.repertori.form.ModalCadastroArtista;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.ParametroDBHelper;
import br.com.vostre.repertori.model.dao.RepDBHelper;
import br.com.vostre.repertori.utils.AnalyticsApplication;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.ParametrosUtils;
import br.com.vostre.repertori.utils.ServiceUtils;

public class InfoFragment extends Fragment implements View.OnClickListener {

    TextView textViewUltimaSincronizacao;
    TextView textViewVersao;
    TextView textViewVersaoDB;
    Button btnAtualizar;
    BroadcastReceiver br;

    Tracker mTracker;

    public InfoFragment() {
        // Required empty public constructor
    }


    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                carregarDados();
            }
        };

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(br, new IntentFilter("br.com.vostre.repertori.AtualizaDadosService"));

        mTracker.setScreenName("Tela Informações Aplicativo");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(br);
        super.onPause();
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
        View v = inflater.inflate(R.layout.fragment_info, container, false);

        textViewUltimaSincronizacao = (TextView) v.findViewById(R.id.textViewUltimoAcesso);
        textViewVersao = (TextView) v.findViewById(R.id.textViewVersao);
        textViewVersaoDB = (TextView) v.findViewById(R.id.textViewVersaoDB);
        btnAtualizar = (Button) v.findViewById(R.id.btnAtualizar);

        carregarDados();

        btnAtualizar.setOnClickListener(this);

        return v;
    }

    private void carregarDados() {
        String ultimoAcesso = ParametrosUtils.getDataUltimoAcesso(getContext());

        if(!ultimoAcesso.equals("-")){
            Calendar calendar = DataUtils.bancoParaDataComEspaco(ultimoAcesso);

            textViewUltimaSincronizacao.setText(DataUtils.toString(calendar, true));
        } else{
            textViewUltimaSincronizacao.setText("-");
        }


        try {
            PackageInfo pInfo = this.getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            textViewVersao.setText(version);

            textViewVersaoDB.setText(String.valueOf(RepDBHelper.DBVERSION));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.btnAtualizar:
                ServiceUtils.iniciaServicoAtualizacao(false, getContext());
                break;
        }

    }
}
