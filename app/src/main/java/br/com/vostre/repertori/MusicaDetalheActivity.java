package br.com.vostre.repertori;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.adapter.EventoList;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class MusicaDetalheActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    TextView textViewNome;
    TextView textViewArtista;
    TextView textViewTom;
    ListView listViewExecucoes;
    EventoList adapterEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica_detalhe);

        List<Evento> eventos;
        MusicaEventoDBHelper musicaEventoDBHelper = new MusicaEventoDBHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MusicaDBHelper musicaDBHelper = new MusicaDBHelper(getApplicationContext());

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        textViewArtista = (TextView) findViewById(R.id.textViewArtista);
        textViewTom = (TextView) findViewById(R.id.textViewTom);
        listViewExecucoes = (ListView) findViewById(R.id.listViewExecucoes);

        Musica musica = new Musica();
        musica.setId(getIntent().getStringExtra("musica"));
        musica = musicaDBHelper.carregar(getApplicationContext(), musica);

        textViewNome.setText(musica.getNome());
        textViewArtista.setText(musica.getArtista().getNome());
        textViewTom.setText("Tom: "+musica.getTom());

        eventos = musicaEventoDBHelper.listarTodosPorMusica(getApplicationContext(), musica);

        adapterEventos =
                new EventoList(this, android.R.layout.simple_spinner_dropdown_item, eventos);

        adapterEventos.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewExecucoes.setAdapter(adapterEventos);
        listViewExecucoes.setOnItemClickListener(this);
        listViewExecucoes.setEmptyView(findViewById(R.id.textViewListaVazia));



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Evento evento = adapterEventos.getItem(position);

        Intent intent = new Intent(getBaseContext(), EventoDetalheActivity.class);
        intent.putExtra("evento", evento.getId());
        startActivity(intent);

    }
}
