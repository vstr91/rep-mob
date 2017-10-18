package br.com.vostre.repertori;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.adapter.BlocoRepertorioAdapter;
import br.com.vostre.repertori.adapter.ComentarioList;
import br.com.vostre.repertori.adapter.StableArrayAdapter;
import br.com.vostre.repertori.adapter.MusicaRepertorioAdapter;
import br.com.vostre.repertori.form.ModalAdicionaBlocoRepertorio;
import br.com.vostre.repertori.form.ModalAdicionaMusica;
import br.com.vostre.repertori.form.ModalAdicionaMusicaRepertorio;
import br.com.vostre.repertori.form.ModalCadastroEvento;
import br.com.vostre.repertori.form.ModalCadastroRepertorio;
import br.com.vostre.repertori.form.ModalCronometro;
import br.com.vostre.repertori.listener.BlocoButtonClickListener;
import br.com.vostre.repertori.listener.ButtonClickListener;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.ComentarioEvento;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.MusicaRepertorio;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.StatusMusica;
import br.com.vostre.repertori.model.dao.BlocoRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.ComentarioEventoDBHelper;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;
import br.com.vostre.repertori.utils.BlocoDynamicListView;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.DialogUtils;
import br.com.vostre.repertori.utils.DynamicListView;

import static java.security.AccessController.getContext;

public class RepertorioDetalheActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        ModalCadastroListener, ModalAdicionaListener, ButtonClickListener, DialogInterface.OnClickListener, BlocoButtonClickListener {

    TextView textViewNome;
    DynamicListView listViewMusicas;
    BlocoDynamicListView listViewBlocos;
    static MusicaRepertorioAdapter adapterMusicas;
    static BlocoRepertorioAdapter adapterBlocos;
    Button btnAdicionaMusica;
    Button btnAdicionaBloco;
    Button btnRelatorio;
    Repertorio repertorio;

    List<Musica> musicas;
    List<BlocoRepertorio> blocosRepertorio;

    MusicaRepertorioDBHelper musicaRepertorioDBHelper;
    RepertorioDBHelper repertorioDBHelper;
    BlocoRepertorioDBHelper blocoRepertorioDBHelper;

    MusicaRepertorio musicaRepertorio;
    BlocoRepertorio blocoRepertorio;
    Dialog dialogLoad;
    TextView textViewTempo;

    Dialog dialogMusica;
    Dialog dialogBloco;
    String tempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repertorio_detalhe);

        musicaRepertorioDBHelper = new MusicaRepertorioDBHelper(getApplicationContext());
        blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(getApplicationContext());
        repertorioDBHelper = new RepertorioDBHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        listViewMusicas = (DynamicListView) findViewById(R.id.listViewMusicas);
        listViewBlocos = (BlocoDynamicListView) findViewById(R.id.listViewBlocos);
        btnAdicionaMusica = (Button) findViewById(R.id.btnAdicionaMusica);
        btnAdicionaBloco = (Button) findViewById(R.id.btnAdicionaBloco);
        btnRelatorio = (Button) findViewById(R.id.btnRelatorio);
        textViewTempo = (TextView) findViewById(R.id.textViewTempo);

        btnAdicionaMusica.setOnClickListener(this);
        btnRelatorio.setOnClickListener(this);
        btnAdicionaBloco.setOnClickListener(this);

        repertorio = new Repertorio();
        repertorio.setId(getIntent().getStringExtra("repertorio"));

        CarregarItens carregarItens = new CarregarItens();
        carregarItens.execute();

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

        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.icon_edit:

                ModalCadastroRepertorio modalCadastroRepertorio = new ModalCadastroRepertorio();
                modalCadastroRepertorio.setListener(this);
                modalCadastroRepertorio.setRepertorio(repertorio);

                modalCadastroRepertorio.show(getSupportFragmentManager(), "modalRepertorio");

                break;
            case R.id.icon_letras:
                intent = new Intent(this, MusicaRepertorioActivity.class);
                intent.putExtra("repertorio", repertorio.getId());
                startActivity(intent);
                break;
            case R.id.icon_cifras:
                intent = new Intent(this, CifraMusicaRepertorioActivity.class);
                intent.putExtra("repertorio", repertorio.getId());
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = null;

        switch(parent.getId()) {
            case R.id.listViewMusicas:
                Musica musica = adapterMusicas.getItem(position);

                intent = new Intent(getBaseContext(), MusicaDetalheActivity.class);
                intent.putExtra("musica", musica.getId());
                startActivity(intent);
                break;
            case R.id.listViewBlocos:
                BlocoRepertorio blocoRepertorio = adapterBlocos.getItem(position);

                intent = new Intent(getBaseContext(), BlocoRepertorioDetalheActivity.class);
                intent.putExtra("bloco", blocoRepertorio.getId());
                startActivity(intent);
                break;
        }


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btnAdicionaMusica:
                ModalAdicionaMusicaRepertorio modalAdicionaMusicaRepertorio = new ModalAdicionaMusicaRepertorio();
                modalAdicionaMusicaRepertorio.setListener(this);
                modalAdicionaMusicaRepertorio.setRepertorio(repertorio);

                modalAdicionaMusicaRepertorio.show(getSupportFragmentManager(), "modalAdicionaMusicaRepertorio");
                break;
            case R.id.btnRelatorio:
                Intent intent = new Intent(getBaseContext(), RepertorioDetalhePdfActivity.class);
                intent.putExtra("repertorio", repertorio.getId());
                startActivity(intent);
                break;
            case R.id.btnAdicionaBloco:
                ModalAdicionaBlocoRepertorio modalAdicionaBlocoRepertorio = new ModalAdicionaBlocoRepertorio();
                modalAdicionaBlocoRepertorio.setListener(this);
                modalAdicionaBlocoRepertorio.setRepertorio(repertorio);

                modalAdicionaBlocoRepertorio.show(getSupportFragmentManager(), "modalAdicionaBlocoRepertorio");
                break;
        }
    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        carregaInformacaoRepertorio();
    }

    @Override
    public void onModalAdicionaDismissed(int resultado) {

        if(resultado == 2){
            blocosRepertorio = blocoRepertorioDBHelper.listarTodosPorRepertorio(getApplicationContext(), repertorio, 0);
            carregaListaBlocos(blocosRepertorio);
            return;
        }

        musicas = musicaRepertorioDBHelper.listarTodosPorRepertorio(getApplicationContext(), repertorio, 0);

        carregaListaMusicas(musicas);
    }

    private void carregaListaMusicas(List<Musica> musicas){
        adapterMusicas = new MusicaRepertorioAdapter(this, R.id.listViewMusicas, musicas);
        adapterMusicas.setListener(this);

        adapterMusicas.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewMusicas.setAdapter(adapterMusicas);

        if (listViewMusicas.getOnItemClickListener() == null){
            listViewMusicas.setOnItemClickListener(this);
        }

        listViewMusicas.setEmptyView(findViewById(R.id.textViewListaVazia));
        listViewMusicas.setLista(musicas);
        listViewMusicas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewMusicas.setRepertorio(repertorio);

    }

    private void carregaListaBlocos(List<BlocoRepertorio> blocosRepertorio){
        adapterBlocos = new BlocoRepertorioAdapter(this, R.id.listViewBlocos, blocosRepertorio);
        adapterBlocos.setListener(this);

        adapterBlocos.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewBlocos.setAdapter(adapterBlocos);

        if (listViewBlocos.getOnItemClickListener() == null){
            listViewBlocos.setOnItemClickListener(this);
        }

        listViewBlocos.setEmptyView(findViewById(R.id.textViewListaBlocosVazia));
        listViewBlocos.setLista(blocosRepertorio);
        listViewBlocos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewBlocos.setRepertorio(repertorio);

    }

    private void carregaInformacaoRepertorio() {
        repertorio = repertorioDBHelper.carregar(getApplicationContext(), repertorio);

        textViewNome.setText(repertorio.getNome());
    }

    @Override
    public void onButtonClicked(View v, Musica musica) {

        musicaRepertorio = new MusicaRepertorio();
        musicaRepertorio.setMusica(musica);
        musicaRepertorio.setRepertorio(repertorio);
        musicaRepertorio = musicaRepertorioDBHelper.carregarPorMusicaERepertorio(getApplicationContext(), musicaRepertorio);

        switch(v.getId()){
            case R.id.btnExcluir:

                dialogMusica = DialogUtils.criarAlertaConfirmacao(this, "Confirmar Exclusão", "Deseja realmente excluir o registro ("+musicaRepertorio.getMusica().getNome()+")?", this);
                dialogMusica.show();

                break;
        }


    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

            switch(i){
                case -1:

                    ExcluirEntidade excluirEntidade = new ExcluirEntidade();

                    if(dialogMusica != null && dialogMusica.equals(dialogInterface)) {
                        excluirEntidade.setTipo(1);
                        excluirEntidade.execute();
                        break;
                    } else if(dialogBloco != null && dialogBloco.equals(dialogInterface)) {
                        excluirEntidade.setTipo(2);
                        excluirEntidade.execute();
                    }

            }



    }

    private String calcularTempoTotalRepertorio(){
        long tempoMedio = 0;
        int cont = 0;
        BigInteger total = BigInteger.ZERO;

        for(Musica musica : musicas){

            Calendar c = musica.calcularMedia(getBaseContext());

            if(c != null){
                //System.out.println(musica.getNome()+" | "+DataUtils.toStringSomenteHoras(c, 0)+" | "+DataUtils.tempoParaSegundos(DataUtils.toStringSomenteHoras(c, 1)));
                total = total.add(BigInteger.valueOf(c.getTimeInMillis()));
                tempoMedio += DataUtils.tempoParaSegundos(DataUtils.toStringSomenteHoras(c, 1));
                cont++;
            }

        }

        Calendar tempoMedioAtivo = Calendar.getInstance();
        tempoMedioAtivo.setTimeInMillis(total.longValue());
        //System.out.println("Tempo Médio: "+DataUtils.segundosParaTempo(tempoMedio));
        return "Tempo Total: "+DataUtils.segundosParaTempo(tempoMedio)+" ("+cont+" música(s) considerada(s))";
    }

    @Override
    public void onButtonClicked(View v, BlocoRepertorio blocoRepertorio) {
        this.blocoRepertorio = blocoRepertorioDBHelper.carregar(getApplicationContext(), blocoRepertorio);

        switch(v.getId()){
            case R.id.btnExcluirBloco:

                dialogBloco = DialogUtils.criarAlertaConfirmacao(this, "Confirmar Exclusão", "Deseja realmente excluir o registro ("+blocoRepertorio.getNome()+")?", this);
                dialogBloco.show();

                break;
        }
    }

    private class ExcluirEntidade extends AsyncTask<Void, String, String> {

        int tipo = -1;

        public int getTipo() {
            return tipo;
        }

        public void setTipo(int tipo) {
            this.tipo = tipo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogLoad = DialogUtils.criarAlertaCarregando(RepertorioDetalheActivity.this, "Salvando alterações", "Por favor aguarde...");
            dialogLoad.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            String tempo = "";

            switch(tipo){
                case 1:
                    musicaRepertorio.setStatus(2);
                    musicaRepertorio.setEnviado(-1);

                    musicaRepertorioDBHelper.salvarOuAtualizar(getApplicationContext(), musicaRepertorio);

                    musicaRepertorioDBHelper.corrigirOrdemPorRepertorio(getApplicationContext(), repertorio);
                     tempo = calcularTempoTotalRepertorio();

                    break;
                case 2:
                    blocoRepertorio.setStatus(2);
                    blocoRepertorio.setEnviado(-1);

                    blocoRepertorioDBHelper.salvarOuAtualizar(getApplicationContext(), blocoRepertorio);

                    blocoRepertorioDBHelper.corrigirOrdemPorRepertorio(getApplicationContext(), repertorio);
                    tempo = calcularTempoTotalRepertorio();

                    break;
            }

            return tempo;

        }

        @Override
        protected void onPostExecute(String tempo) {
            super.onPostExecute(tempo);

            switch(tipo) {
                case 1:
                    Toast.makeText(getBaseContext(), "Música Removida", Toast.LENGTH_SHORT).show();
                    musicas = musicaRepertorioDBHelper.listarTodosPorRepertorio(getApplicationContext(), repertorio, 0);
                    carregaListaMusicas(musicas);
                    break;
                case 2:
                    Toast.makeText(getBaseContext(), "Bloco Removido", Toast.LENGTH_SHORT).show();
                    blocosRepertorio = blocoRepertorioDBHelper.listarTodosPorRepertorio(getApplicationContext(), repertorio, 0);
                    carregaListaBlocos(blocosRepertorio);
                    break;
            }


            dialogLoad.dismiss();

        }
    }

    private class CarregarItens extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogLoad = DialogUtils.criarAlertaCarregando(RepertorioDetalheActivity.this, "Carregando dados", "Por favor aguarde...");
            dialogLoad.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            carregaInformacaoRepertorio();

            musicas = musicaRepertorioDBHelper.listarTodosPorRepertorio(getApplicationContext(), repertorio, 0);
            blocosRepertorio = blocoRepertorioDBHelper.listarTodosPorRepertorio(getApplicationContext(), repertorio, 0);

            tempo = calcularTempoTotalRepertorio();

            return null;
        }

        @Override
        protected void onPostExecute(String tempo) {
            super.onPostExecute(tempo);
            textViewTempo.setText(tempo);
            carregaListaMusicas(musicas);
            carregaListaBlocos(blocosRepertorio);
            dialogLoad.dismiss();

        }
    }

}
