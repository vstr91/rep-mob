package br.com.vostre.repertori;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.adapter.ComentarioList;
import br.com.vostre.repertori.adapter.EventoList;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.adapter.StableArrayAdapter;
import br.com.vostre.repertori.form.ModalAdicionaMusica;
import br.com.vostre.repertori.form.ModalCadastroEvento;
import br.com.vostre.repertori.form.ModalCronometro;
import br.com.vostre.repertori.form.ModalListaRepertorio;
import br.com.vostre.repertori.listener.ButtonClickListener;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.ComentarioEvento;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.TempoMusicaEvento;
import br.com.vostre.repertori.model.dao.ComentarioEventoDBHelper;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.model.dao.TempoMusicaEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.DialogUtils;
import br.com.vostre.repertori.utils.DynamicListView;
import br.com.vostre.repertori.utils.MoneyTextWatcher;
import br.com.vostre.repertori.utils.SnackbarHelper;
import br.com.vostre.repertori.utils.ToolbarUtils;

public class EventoDetalheActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        ModalCadastroListener, ModalAdicionaListener, ButtonClickListener, DialogInterface.OnClickListener, CompoundButton.OnCheckedChangeListener {

    TextView textViewNome;
    TextView textViewData;
    DynamicListView listViewMusicas;
    static StableArrayAdapter adapterMusicas;
    ListView listViewComentarios;
    EditText editTextComentario;
    Button btnComentario;
    Button btnAdicionaMusica;
    Button btnAdicionaBlocoMusica;
    Button btnPDF;
    ComentarioList adapterComentarios;
    Evento evento;

    CheckBox checkBoxCache;
    EditText editTextCache;
    Button btnSalvarCache;

    List<Musica> musicas;
    List<ComentarioEvento> comentarios;

    MusicaEventoDBHelper musicaEventoDBHelper;
    ComentarioEventoDBHelper comentarioEventoDBHelper;
    EventoDBHelper eventoDBHelper;

    MusicaEvento musicaEvento;
    boolean qrCode = false;
    boolean temCache = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_detalhe);

        musicaEventoDBHelper = new MusicaEventoDBHelper(getApplicationContext());
        comentarioEventoDBHelper = new ComentarioEventoDBHelper(getApplicationContext());
        eventoDBHelper = new EventoDBHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        textViewData = (TextView) findViewById(R.id.textViewData);
        listViewMusicas = (DynamicListView) findViewById(R.id.listViewMusicas);
        listViewComentarios = (ListView) findViewById(R.id.listViewComentarios);
        editTextComentario = (EditText) findViewById(R.id.editTextComentario);
        btnComentario = (Button) findViewById(R.id.btnComentario);
        btnAdicionaMusica = (Button) findViewById(R.id.btnAdicionaMusica);
        btnAdicionaBlocoMusica = (Button) findViewById(R.id.btnAdicionaBlocoMusica);
        btnPDF = (Button) findViewById(R.id.btnPDF);

        checkBoxCache = (CheckBox) findViewById(R.id.checkBoxCache);
        editTextCache = (EditText) findViewById(R.id.editTextCache);
        btnSalvarCache = (Button) findViewById(R.id.btnSalvarCache);

        btnComentario.setOnClickListener(this);
        btnAdicionaMusica.setOnClickListener(this);
        btnAdicionaBlocoMusica.setOnClickListener(this);
        btnPDF.setOnClickListener(this);
        checkBoxCache.setOnCheckedChangeListener(this);
        btnSalvarCache.setOnClickListener(this);
        evento = new Evento();

        Uri data = getIntent().getData();

        boolean  eventoExiste = false;

        if(data != null){
            List<String> params = data.getPathSegments();
            String slug = params.get(0);
            evento.setSlug(slug);
            eventoExiste = carregaInformacaoEvento(true);
            qrCode = true;
        } else{
            evento.setId(getIntent().getStringExtra("evento"));
            eventoExiste = carregaInformacaoEvento(false);
        }

        if(eventoExiste){
            String qr = getIntent().getStringExtra("qr");

            if(qr != null){
                qrCode = true;
            }

            carregaListaMusicas();

            atualizaComentarios();
        } else{
            Toast.makeText(getBaseContext(), "Evento não encontrado. Registro pode ter sido removido ou alterado.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
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

        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.icon_edit:

                ModalCadastroEvento modalCadastroEvento = new ModalCadastroEvento();
                modalCadastroEvento.setListener(this);
                modalCadastroEvento.setEvento(evento);
                modalCadastroEvento.setData(evento.getData());

                modalCadastroEvento.show(getSupportFragmentManager(), "modalEvento");

                break;
            case R.id.icon_letras:
                intent = new Intent(this, MusicaEventoActivity.class);
                intent.putExtra("repertorio", evento.getId());
                startActivity(intent);
                break;
            case R.id.icon_cifras:
                intent = new Intent(this, CifraMusicaEventoActivity.class);
                intent.putExtra("repertorio", evento.getId());
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
            case R.id.btnComentario:
                String comentario = editTextComentario.getText().toString();

                if(!comentario.isEmpty()){
                    ComentarioEvento comentarioEvento = new ComentarioEvento();
                    comentarioEvento.setTexto(comentario);
                    comentarioEvento.setStatus(0);
                    comentarioEvento.setDataCadastro(Calendar.getInstance());
                    comentarioEvento.setEnviado(-1);
                    comentarioEvento.setEvento(evento);
                    comentarioEvento.setUltimaAlteracao(Calendar.getInstance());
                    comentarioEventoDBHelper.salvarOuAtualizar(getApplicationContext(), comentarioEvento);
                    editTextComentario.setText("");
                    atualizaComentarios();

                    View view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Por favor digite o comentário.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnAdicionaMusica:
                ModalAdicionaMusica modalAdicionaMusica = new ModalAdicionaMusica();
                modalAdicionaMusica.setListener(this);
                modalAdicionaMusica.setEvento(evento);

                modalAdicionaMusica.show(getSupportFragmentManager(), "modalAdicionaMusica");
                break;
            case R.id.btnPDF:
                Intent intent = new Intent(getBaseContext(), EventoDetalhePdfActivity.class);
                intent.putExtra("evento", evento.getId());
                startActivity(intent);
                break;
            case R.id.btnAdicionaBlocoMusica:
                ModalListaRepertorio modalListaRepertorio = new ModalListaRepertorio();
                modalListaRepertorio.setListener(this);
                modalListaRepertorio.setEvento(evento);

                modalListaRepertorio.show(getSupportFragmentManager(), "modalListaRepertorio");
                break;
            case R.id.btnSalvarCache:
                String cacheString = editTextCache.getText().toString();
                cacheString = cacheString.replace(",", ".");
                cacheString = cacheString.replaceAll("[^\\d.]", "");

                Double cache = Double.parseDouble(cacheString);

                if(cache > 0){
                    evento.setCache(cache);
                    evento.setUltimaAlteracao(Calendar.getInstance());
                    evento.setEnviado(-1);
                    eventoDBHelper.salvarOuAtualizar(getApplicationContext(), evento);
                    Toast.makeText(getApplicationContext(), "Cachê Salvo!", Toast.LENGTH_SHORT).show();
                    editTextComentario.requestFocus();

                    View view = this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                } else{
                    Toast.makeText(getApplicationContext(), "Informe antes o valor do cachê!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void atualizaComentarios(){
        comentarios = comentarioEventoDBHelper.listarTodosPorEvento(getApplicationContext(), evento);
        adapterComentarios =
                new ComentarioList(this, android.R.layout.simple_spinner_dropdown_item, comentarios);

        adapterComentarios.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewComentarios.setAdapter(adapterComentarios);
        listViewComentarios.setEmptyView(findViewById(R.id.textViewListaComentarioVazia));
        listViewComentarios.invalidate();
    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        carregaInformacaoEvento(false);
    }

    @Override
    public void onModalAdicionaDismissed(int resultado) {
        carregaListaMusicas();
    }

    private void carregaListaMusicas(){
        musicas = musicaEventoDBHelper.listarTodosPorEvento(getApplicationContext(), evento);

        adapterMusicas =
                new StableArrayAdapter(this, R.id.listViewMusicas, musicas);
        adapterMusicas.setListener(this);

        adapterMusicas.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewMusicas.setAdapter(adapterMusicas);

        if (listViewMusicas.getOnItemClickListener() == null){
            listViewMusicas.setOnItemClickListener(this);
        }

        listViewMusicas.setEmptyView(findViewById(R.id.textViewListaVazia));
        listViewMusicas.setLista(musicas);
        listViewMusicas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewMusicas.setEvento(evento);

    }

    private boolean carregaInformacaoEvento(boolean usandoSlug) {

        if(usandoSlug){
            evento = eventoDBHelper.carregarPorSlug(getApplicationContext(), evento);
        } else{
            evento = eventoDBHelper.carregar(getApplicationContext(), evento);
        }

        if(evento == null){
            return false;
        } else{
            textViewNome.setText(evento.getNome());
            textViewData.setText(DataUtils.toString(evento.getData(), true));

            if(evento.getCache() > 0){
                editTextCache.setVisibility(View.VISIBLE);
                btnSalvarCache.setVisibility(View.VISIBLE);
                checkBoxCache.setChecked(true);
                checkBoxCache.invalidate();
                editTextCache.addTextChangedListener(new MoneyTextWatcher(editTextCache));
                DecimalFormat format = new DecimalFormat("###.00");
                String cache = format.format(evento.getCache());
                editTextCache.setText(cache);
            } else{
                btnSalvarCache.setVisibility(View.INVISIBLE);
                editTextCache.setVisibility(View.INVISIBLE);
            }

            return true;
        }

    }

    @Override
    public void onButtonClicked(View v, Musica musica) {

        musicaEvento = new MusicaEvento();
        musicaEvento.setMusica(musica);
        musicaEvento.setEvento(evento);
        musicaEvento = musicaEventoDBHelper.carregarPorMusicaEEvento(getApplicationContext(), musicaEvento);

        switch(v.getId()){
            case R.id.btnExcluir:

                Dialog dialog = DialogUtils.criarAlertaConfirmacao(this, "Confirmar Exclusão", "Deseja realmente excluir o registro ("+musicaEvento.getMusica().getNome()+")?", this);
                dialog.show();

                break;
            case R.id.btnTempo:
                ModalCronometro modalCronometro = new ModalCronometro();
                //modalCronometro.setListener(this);
                modalCronometro.setMusicaEvento(musicaEvento);

                modalCronometro.show(getSupportFragmentManager(), "modalCronometro");
                break;
        }


    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        switch(i){
            case -1:
                musicaEvento.setStatus(2);
                musicaEvento.setEnviado(-1);

                musicaEventoDBHelper.salvarOuAtualizar(getApplicationContext(), musicaEvento);

                musicaEventoDBHelper.corrigirOrdemPorEvento(getApplicationContext(), evento);

                carregaListaMusicas();

                Toast.makeText(getBaseContext(), "Música Removida", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        if(qrCode){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked){
            editTextCache.setVisibility(View.VISIBLE);
            btnSalvarCache.setVisibility(View.VISIBLE);
        } else{
            evento.setCache(0);
            editTextCache.setVisibility(View.INVISIBLE);
            btnSalvarCache.setVisibility(View.INVISIBLE);
        }

    }
}
