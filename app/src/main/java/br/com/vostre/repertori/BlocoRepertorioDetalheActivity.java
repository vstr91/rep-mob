package br.com.vostre.repertori;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.adapter.BlocoRepertorioAdapter;
import br.com.vostre.repertori.adapter.MusicaRepertorioAdapter;
import br.com.vostre.repertori.adapter.TempoBlocoList;
import br.com.vostre.repertori.adapter.TempoList;
import br.com.vostre.repertori.form.ModalAdicionaBlocoRepertorio;
import br.com.vostre.repertori.form.ModalAdicionaMusicaBlocoRepertorio;
import br.com.vostre.repertori.form.ModalAdicionaMusicaRepertorio;
import br.com.vostre.repertori.form.ModalCadastroRepertorio;
import br.com.vostre.repertori.form.ModalCronometro;
import br.com.vostre.repertori.form.ModalCronometroBloco;
import br.com.vostre.repertori.form.ModalPlayer;
import br.com.vostre.repertori.form.ModalPlayerBloco;
import br.com.vostre.repertori.listener.BlocoButtonClickListener;
import br.com.vostre.repertori.listener.ButtonClickListener;
import br.com.vostre.repertori.listener.GetAudioTaskListener;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.listener.TokenTaskListener;
import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaBloco;
import br.com.vostre.repertori.model.MusicaRepertorio;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.TempoBlocoRepertorio;
import br.com.vostre.repertori.model.TempoMusicaEvento;
import br.com.vostre.repertori.model.dao.BlocoRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.MusicaBlocoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;
import br.com.vostre.repertori.model.dao.TempoBlocoRepertorioDBHelper;
import br.com.vostre.repertori.utils.BlocoDynamicListView;
import br.com.vostre.repertori.utils.Constants;
import br.com.vostre.repertori.utils.Crypt;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.DialogUtils;
import br.com.vostre.repertori.utils.DynamicListView;
import br.com.vostre.repertori.utils.GetAudioTask;
import br.com.vostre.repertori.utils.TokenTask;

public class BlocoRepertorioDetalheActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        ModalCadastroListener, ModalAdicionaListener, ButtonClickListener, DialogInterface.OnClickListener, TokenTaskListener, GetAudioTaskListener {

    TextView textViewNome;
    DynamicListView listViewMusicas;
    static MusicaRepertorioAdapter adapterMusicas;
    static TempoBlocoList adapterTempos;
    Button btnAdicionaMusica;
    Button btnCronometro;
    ListView listViewTempos;
//    Button btnRelatorio;

    List<Musica> musicas;
    List<TempoBlocoRepertorio> tempos;
    List<BlocoRepertorio> blocosRepertorio;

    MusicaBlocoDBHelper musicaBlocoDBHelper;
    BlocoRepertorioDBHelper blocoRepertorioDBHelper;

    MusicaBloco musicaBloco;
    BlocoRepertorio blocoRepertorio;
    Dialog dialogLoad;
    TextView textViewTempo;

    TempoBlocoRepertorio tbr;
    private static final int REQUEST_STORAGE_PERMISSION = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloco_repertorio_detalhe);

        musicaBlocoDBHelper = new MusicaBlocoDBHelper(getApplicationContext());
        blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        listViewMusicas = (DynamicListView) findViewById(R.id.listViewMusicas);
        btnAdicionaMusica = (Button) findViewById(R.id.btnAdicionaMusica);
        btnCronometro = (Button) findViewById(R.id.btnCronometro);
//        btnRelatorio = (Button) findViewById(R.id.btnRelatorio);
        textViewTempo = (TextView) findViewById(R.id.textViewTempo);
        listViewTempos = (ListView) findViewById(R.id.listViewTempos);

        btnAdicionaMusica.setOnClickListener(this);
//        btnRelatorio.setOnClickListener(this);
        btnCronometro.setOnClickListener(this);

        blocoRepertorio = new BlocoRepertorio();
        blocoRepertorio.setId(getIntent().getStringExtra("bloco"));

        carregaInformacaoBlocoRepertorio();

        carregaListaMusicas();
        carregaListaTempos();

        String tempo = calcularTempoTotalBloco();

        textViewTempo.setText(tempo);

        int permissionStorage = ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            requestStorage();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.evento_detalhe, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent;

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.icon_edit:

                ModalAdicionaBlocoRepertorio modalAdicionaBlocoRepertorio = new ModalAdicionaBlocoRepertorio();
                modalAdicionaBlocoRepertorio.setListener(this);
                modalAdicionaBlocoRepertorio.setBlocoRepertorio(blocoRepertorio);
                modalAdicionaBlocoRepertorio.setRepertorio(blocoRepertorio.getRepertorio());

                modalAdicionaBlocoRepertorio.show(getSupportFragmentManager(), "modalAdicionaBlocoRepertorio");

                break;
            case R.id.icon_letras:
                intent = new Intent(this, MusicaBlocoRepertorioActivity.class);
                intent.putExtra("bloco_repertorio", blocoRepertorio.getId());
                startActivity(intent);
                break;
            case R.id.icon_cifras:
                intent = new Intent(this, CifraMusicaBlocoRepertorioActivity.class);
                intent.putExtra("bloco_repertorio", blocoRepertorio.getId());
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = null;

        switch (parent.getId()) {
            case R.id.listViewMusicas:
                Musica musica = adapterMusicas.getItem(position);

                intent = new Intent(getBaseContext(), MusicaDetalheActivity.class);
                intent.putExtra("musica", musica.getId());
                startActivity(intent);
                break;
            case R.id.listViewTempos:
                tbr = tempos.get(position);

                if(tbr.getAudio() != null && !tbr.getAudio().isEmpty()){

                    File f = new File(Constants.CAMINHO_PADRAO_AUDIO+File.separator+tbr.getAudio());

                    if(f.exists()){
                        ModalPlayerBloco modalPlayer = new ModalPlayerBloco();
                        modalPlayer.setTbr(tbr);
                        modalPlayer.setCancelable(false);
                        modalPlayer.show(getSupportFragmentManager(), "modalPlayer");
                    } else{
                        Toast.makeText(getBaseContext(), "Áudio não encontrado no dispositivo. Tentando recuperar do servidor...", Toast.LENGTH_SHORT).show();

                        TokenTask tokenTask = new TokenTask(Constants.URLTOKEN, BlocoRepertorioDetalheActivity.this, false, 0);
                        tokenTask.setOnTokenTaskResultsListener(this);
                        tokenTask.execute();
                    }

                } else{
                    Toast.makeText(getBaseContext(), "Não existe áudio disponível para esta gravação", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnAdicionaMusica:
                ModalAdicionaMusicaBlocoRepertorio modalAdicionaMusicaBlocoRepertorio = new ModalAdicionaMusicaBlocoRepertorio();
                modalAdicionaMusicaBlocoRepertorio.setListener(this);
                modalAdicionaMusicaBlocoRepertorio.setBlocoRepertorio(blocoRepertorio);

                modalAdicionaMusicaBlocoRepertorio.show(getSupportFragmentManager(), "modalAdicionaMusicaBlocoRepertorio");
                break;
            case R.id.btnRelatorio:
                Intent intent = new Intent(getBaseContext(), RepertorioDetalhePdfActivity.class);
                intent.putExtra("repertorio", blocoRepertorio.getId());
                startActivity(intent);
                break;
            case R.id.btnCronometro:
                ModalCronometroBloco modalCronometro = new ModalCronometroBloco();
                //modalCronometro.setListener(this);
                modalCronometro.setBlocoRepertorio(blocoRepertorio);

                modalCronometro.show(getSupportFragmentManager(), "modalCronometro");
                break;
        }
    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        carregaInformacaoBlocoRepertorio();
    }

    @Override
    public void onModalAdicionaDismissed(int resultado) {
        carregaListaMusicas();
    }

    private void carregaListaMusicas() {
        musicas = musicaBlocoDBHelper.listarTodosPorBloco(getApplicationContext(), blocoRepertorio, 0);

        adapterMusicas =
                new MusicaRepertorioAdapter(this, R.id.listViewMusicas, musicas);
        adapterMusicas.setListener(this);

        adapterMusicas.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewMusicas.setAdapter(adapterMusicas);

        if (listViewMusicas.getOnItemClickListener() == null) {
            listViewMusicas.setOnItemClickListener(this);
        }

        listViewMusicas.setEmptyView(findViewById(R.id.textViewListaVazia));
        listViewMusicas.setLista(musicas);
        listViewMusicas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewMusicas.setBlocoRepertorio(blocoRepertorio);

    }

    private void carregaListaTempos() {
        TempoBlocoRepertorioDBHelper tempoBlocoRepertorioDBHelper = new TempoBlocoRepertorioDBHelper(getBaseContext());
        tempos = tempoBlocoRepertorioDBHelper.listarTodosPorBlocoRepertorio(getApplicationContext(), blocoRepertorio, 10);

        adapterTempos =
                new TempoBlocoList(this, R.id.listViewTempos, tempos);

        adapterTempos.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewTempos.setAdapter(adapterTempos);

        if (listViewTempos.getOnItemClickListener() == null) {
            listViewTempos.setOnItemClickListener(this);
        }

        listViewTempos.setEmptyView(findViewById(R.id.textViewListaBlocosVazia));

    }


    private void carregaInformacaoBlocoRepertorio() {
        blocoRepertorio = blocoRepertorioDBHelper.carregar(getApplicationContext(), blocoRepertorio);

        textViewNome.setText(blocoRepertorio.getNome());
    }

    @Override
    public void onButtonClicked(View v, Musica musica) {

        musicaBloco = new MusicaBloco();
        musicaBloco.setMusica(musica);
        musicaBloco.setBlocoRepertorio(blocoRepertorio);
        musicaBloco = musicaBlocoDBHelper.carregarPorMusicaEBloco(getApplicationContext(), musicaBloco);

        switch (v.getId()) {
            case R.id.btnExcluir:

                Dialog dialogMusica = DialogUtils.criarAlertaConfirmacao(this, "Confirmar Exclusão", "Deseja realmente excluir o registro (" + musicaBloco.getMusica().getNome() + ")?", this);
                dialogMusica.show();

                break;
        }


    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        switch (i) {
            case -1:
                ExcluirEntidade excluirEntidade = new ExcluirEntidade();
                excluirEntidade.execute();
                break;
        }


    }

    private String calcularTempoTotalBloco() {
        long tempoMedio = 0;
        int cont = 0;
        BigInteger total = BigInteger.ZERO;

        Calendar c = blocoRepertorio.calcularMedia(getBaseContext());

        if (c != null) {
            //System.out.println(musica.getNome()+" | "+DataUtils.toStringSomenteHoras(c, 0)+" | "+DataUtils.tempoParaSegundos(DataUtils.toStringSomenteHoras(c, 1)));
            total = total.add(BigInteger.valueOf(c.getTimeInMillis()));
            tempoMedio += DataUtils.tempoParaSegundos(DataUtils.toStringSomenteHoras(c, 1));
            cont++;
        }

        Calendar tempoMedioAtivo = Calendar.getInstance();
        tempoMedioAtivo.setTimeInMillis(total.longValue());
        //System.out.println("Tempo Médio: "+DataUtils.segundosParaTempo(tempoMedio));
        return "Tempo Total: " + DataUtils.segundosParaTempo(tempoMedio);
    }

    @Override
    public void onGetAudioTaskResultSucceeded(File result) {

        if(result != null && result.exists()){

            TempoBlocoRepertorioDBHelper tbrDBHelper = new TempoBlocoRepertorioDBHelper(getBaseContext());

            TempoBlocoRepertorio tbr = tbrDBHelper.carregarPorAudio(getBaseContext(), result.getName());

            ModalPlayerBloco modalPlayer = new ModalPlayerBloco();
            modalPlayer.setTbr(tbr);
            modalPlayer.setCancelable(false);
            modalPlayer.show(getSupportFragmentManager(), "modalPlayer");

        } else{
            Toast.makeText(getBaseContext(), "Áudio não encontrado no servidor", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onTokenTaskResultsSucceeded(String token, int tipo) {

        Crypt crypt = new Crypt();

        try {
            String  tokenCriptografado = crypt.bytesToHex(crypt.encrypt(token));
            String url = Constants.URLSERVIDORAUDIO+tokenCriptografado+"/"+tbr.getAudio();

            GetAudioTask getAudioTask = new GetAudioTask(url, BlocoRepertorioDetalheActivity.this, tbr.getAudio(), false);
            getAudioTask.setListener(this);
            getAudioTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void requestStorage() {
        Log.w("Armazenamento", "Armazenamento nao autorizado. Solicitando permissão...");

        final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestPermissions(permissions, REQUEST_STORAGE_PERMISSION);
                return;
            }
        }

    }

    private class ExcluirEntidade extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogLoad = DialogUtils.criarAlertaCarregando(BlocoRepertorioDetalheActivity.this, "Salvando alterações", "Por favor aguarde...");
            dialogLoad.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            String tempo = "";

            musicaBloco.setStatus(2);
            musicaBloco.setEnviado(-1);

            musicaBlocoDBHelper.salvarOuAtualizar(getApplicationContext(), musicaBloco);

            musicaBlocoDBHelper.corrigirOrdemPorBloco(getApplicationContext(), blocoRepertorio);
            tempo = calcularTempoTotalBloco();

            return tempo;

        }

        @Override
        protected void onPostExecute(String tempo) {
            super.onPostExecute(tempo);

            Toast.makeText(getBaseContext(), "Música Removida", Toast.LENGTH_SHORT).show();
            carregaListaMusicas();


            dialogLoad.dismiss();

        }
    }

}
