package br.com.vostre.repertori;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.adapter.EventoList;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.adapter.TempoList;
import br.com.vostre.repertori.form.ModalEditaLetra;
import br.com.vostre.repertori.form.ModalLetra;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.TempoMusicaEvento;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.model.dao.TempoMusicaEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class MusicaDetalheActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    TextView textViewNome;
    TextView textViewArtista;
    TextView textViewTom;
    ListView listViewExecucoes;
    ListView listViewExecucoesCronometradas;
    EventoList adapterEventos;
    TempoList adapterExecucoes;
    Button btnBuscaVideo;
    TextView textViewMedia;
    Button btnLetra;
    Button btnEditaLetra;

    LineChart chart;

    Musica musica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica_detalhe);

        List<Evento> eventos;
        MusicaEventoDBHelper musicaEventoDBHelper = new MusicaEventoDBHelper(getApplicationContext());

        List<TempoMusicaEvento> tmes;
        TempoMusicaEventoDBHelper tempoMusicaEventoDBHelper = new TempoMusicaEventoDBHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MusicaDBHelper musicaDBHelper = new MusicaDBHelper(getApplicationContext());

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        textViewArtista = (TextView) findViewById(R.id.textViewArtista);
        textViewTom = (TextView) findViewById(R.id.textViewTom);
        listViewExecucoes = (ListView) findViewById(R.id.listViewExecucoes);
        listViewExecucoesCronometradas = (ListView) findViewById(R.id.listViewExecucoesCronometradas);
        btnBuscaVideo = (Button) findViewById(R.id.btnBuscaVideo);
        textViewMedia = (TextView) findViewById(R.id.textViewMedia);
        btnLetra = (Button) findViewById(R.id.btnLetra);
        btnEditaLetra = (Button) findViewById(R.id.btnEditaLetra);
        chart = (LineChart) findViewById(R.id.chart);

        btnBuscaVideo.setOnClickListener(this);
        btnLetra.setOnClickListener(this);
        btnEditaLetra.setOnClickListener(this);

        musica = new Musica();
        musica.setId(getIntent().getStringExtra("musica"));
        musica = musicaDBHelper.carregar(getApplicationContext(), musica);

        textViewNome.setText(musica.getNome());
        textViewArtista.setText(musica.getArtista().getNome());

        String tom = musica.getTom().equals("null") ? "-" : musica.getTom();

        textViewTom.setText(tom);

        eventos = musicaEventoDBHelper.listarTodosPorMusica(getApplicationContext(), musica);

        adapterEventos =
                new EventoList(this, android.R.layout.simple_spinner_dropdown_item, eventos);

        adapterEventos.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewExecucoes.setAdapter(adapterEventos);
        listViewExecucoes.setOnItemClickListener(this);
        listViewExecucoes.setEmptyView(findViewById(R.id.textViewListaVazia));

        tmes = tempoMusicaEventoDBHelper.listarTodosPorMusica(getApplicationContext(), musica, 10);

//        List<Entry> dados = new ArrayList<>();
//
//        long ultimoStamp = tmes.get(0).getMusicaEvento().getEvento().getData().getTimeInMillis();
//
//        for(TempoMusicaEvento tme : tmes){
//
//            long date = tme.getMusicaEvento().getEvento().getData().getTimeInMillis();
//            int dateInt =  (int) ultimoStamp - date;
//
//
//
//            dados.add(new Entry(,
//                    DataUtils.toStringSomenteHoras(tme.getTempo(), 1)));
//        }

        adapterExecucoes =
                new TempoList(this, android.R.layout.simple_spinner_dropdown_item, tmes);

        adapterEventos.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewExecucoesCronometradas.setAdapter(adapterExecucoes);
        listViewExecucoesCronometradas.setEmptyView(findViewById(R.id.textViewListaVaziaCronometro));

        Calendar c = musica.calcularMedia(getBaseContext());

        if(c != null){
            textViewMedia.setText(DataUtils.toStringSomenteHoras(c, 1));
        } else{
            textViewMedia.setText("N/D");
        }

        //btnLetra.setEnabled(!musica.getLetra().isEmpty());

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Evento evento = adapterEventos.getItem(position);

        Intent intent = new Intent(getBaseContext(), EventoDetalheActivity.class);
        intent.putExtra("evento", evento.getId());
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch(v.getId()){
            case R.id.btnBuscaVideo:

                Intent intent = new Intent(Intent.ACTION_SEARCH);
                intent.setPackage("com.google.android.youtube");
                intent.putExtra("query", musica.getNome()+" "+musica.getArtista().getNome());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
            case R.id.btnLetra:
                ModalLetra modalLetra = new ModalLetra();
                modalLetra.setMusica(musica);

                modalLetra.show(getSupportFragmentManager(), "modalLetra");
                break;
            case R.id.btnEditaLetra:
                ModalEditaLetra modalEditaLetra = new ModalEditaLetra();
                modalEditaLetra.setMusica(musica);

                modalEditaLetra.show(getSupportFragmentManager(), "modalEditaLetra");
                break;
        }

    }

}
