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

import br.com.vostre.repertori.adapter.ComentarioList;
import br.com.vostre.repertori.adapter.StableArrayAdapter;
import br.com.vostre.repertori.adapter.MusicaRepertorioAdapter;
import br.com.vostre.repertori.form.ModalAdicionaMusica;
import br.com.vostre.repertori.form.ModalAdicionaMusicaRepertorio;
import br.com.vostre.repertori.form.ModalCadastroEvento;
import br.com.vostre.repertori.form.ModalCadastroRepertorio;
import br.com.vostre.repertori.form.ModalCronometro;
import br.com.vostre.repertori.listener.ButtonClickListener;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.ComentarioEvento;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.MusicaRepertorio;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.StatusMusica;
import br.com.vostre.repertori.model.dao.ComentarioEventoDBHelper;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaProjetoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.DialogUtils;
import br.com.vostre.repertori.utils.DynamicListView;

import static java.security.AccessController.getContext;

public class RepertorioDetalheActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        ModalCadastroListener, ModalAdicionaListener, ButtonClickListener, DialogInterface.OnClickListener {

    TextView textViewNome;
    DynamicListView listViewMusicas;
    static MusicaRepertorioAdapter adapterMusicas;
    Button btnAdicionaMusica;
    Button btnRelatorio;
    Repertorio repertorio;

    List<Musica> musicas;

    MusicaRepertorioDBHelper musicaRepertorioDBHelper;
    RepertorioDBHelper repertorioDBHelper;

    MusicaRepertorio musicaRepertorio;
    Dialog dialogLoad;
    TextView textViewTempo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repertorio_detalhe);

        musicaRepertorioDBHelper = new MusicaRepertorioDBHelper(getApplicationContext());
        repertorioDBHelper = new RepertorioDBHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        listViewMusicas = (DynamicListView) findViewById(R.id.listViewMusicas);
        btnAdicionaMusica = (Button) findViewById(R.id.btnAdicionaMusica);
        btnRelatorio = (Button) findViewById(R.id.btnRelatorio);
        textViewTempo = (TextView) findViewById(R.id.textViewTempo);

        btnAdicionaMusica.setOnClickListener(this);
        btnRelatorio.setOnClickListener(this);

        repertorio = new Repertorio();
        repertorio.setId(getIntent().getStringExtra("repertorio"));

        carregaInformacaoRepertorio();

        carregaListaMusicas();

        String tempo = calcularTempoTotalRepertorio();

        textViewTempo.setText(tempo);

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
        Musica musica = adapterMusicas.getItem(position);

        Intent intent = new Intent(getBaseContext(), MusicaDetalheActivity.class);
        intent.putExtra("musica", musica.getId());
        startActivity(intent);
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
        }
    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        carregaInformacaoRepertorio();
    }

    @Override
    public void onModalAdicionaDismissed(int resultado) {
        carregaListaMusicas();
    }

    private void carregaListaMusicas(){
        musicas = musicaRepertorioDBHelper.listarTodosPorRepertorio(getApplicationContext(), repertorio, 0);

        adapterMusicas =
                new MusicaRepertorioAdapter(this, R.id.listViewMusicas, musicas);
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

                Dialog dialog = DialogUtils.criarAlertaConfirmacao(this, "Confirmar Exclusão", "Deseja realmente excluir o registro ("+musicaRepertorio.getMusica().getNome()+")?", this);
                dialog.show();

                break;
        }


    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        switch(i){
            case -1:
                ExcluirEntidade excluirEntidade = new ExcluirEntidade();
                excluirEntidade.execute();
                break;
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

    private class ExcluirEntidade extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogLoad = DialogUtils.criarAlertaCarregando(RepertorioDetalheActivity.this, "Salvando alterações", "Por favor aguarde...");
            dialogLoad.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            musicaRepertorio.setStatus(2);
            musicaRepertorio.setEnviado(-1);

            musicaRepertorioDBHelper.salvarOuAtualizar(getApplicationContext(), musicaRepertorio);

            musicaRepertorioDBHelper.corrigirOrdemPorRepertorio(getApplicationContext(), repertorio);
            String tempo = calcularTempoTotalRepertorio();

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
