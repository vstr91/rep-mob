package br.com.vostre.repertori;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.adapter.ComentarioList;
import br.com.vostre.repertori.adapter.EventoList;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.model.ComentarioEvento;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.dao.ComentarioEventoDBHelper;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.SnackbarHelper;

public class EventoDetalheActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    TextView textViewNome;
    TextView textViewData;
    ListView listViewMusicas;
    MusicaList adapterMusicas;
    ListView listViewComentarios;
    ComentarioList adapterComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_detalhe);

        List<Musica> musicas;
        List<ComentarioEvento> comentarios;
        MusicaEventoDBHelper musicaEventoDBHelper = new MusicaEventoDBHelper(getApplicationContext());
        ComentarioEventoDBHelper comentarioEventoDBHelper = new ComentarioEventoDBHelper(getApplicationContext());
        EventoDBHelper eventoDBHelper = new EventoDBHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        textViewData = (TextView) findViewById(R.id.textViewData);
        listViewMusicas = (ListView) findViewById(R.id.listViewMusicas);
        listViewComentarios = (ListView) findViewById(R.id.listViewComentarios);

        Evento evento = new Evento();
        evento.setId(getIntent().getStringExtra("evento"));
        evento = eventoDBHelper.carregar(getApplicationContext(), evento);

        textViewNome.setText(evento.getNome());
        textViewData.setText(DataUtils.toString(evento.getData()));

        musicas = musicaEventoDBHelper.listarTodosPorEvento(getApplicationContext(), evento);
        comentarios = comentarioEventoDBHelper.listarTodosPorEvento(getApplicationContext(), evento);

        adapterMusicas =
                new MusicaList(this, android.R.layout.simple_spinner_dropdown_item, musicas);

        adapterMusicas.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewMusicas.setAdapter(adapterMusicas);
        listViewMusicas.setOnItemClickListener(this);
        listViewMusicas.setEmptyView(findViewById(R.id.textViewListaVazia));

        adapterComentarios =
                new ComentarioList(this, android.R.layout.simple_spinner_dropdown_item, comentarios);

        adapterComentarios.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewComentarios.setAdapter(adapterComentarios);
        listViewComentarios.setEmptyView(findViewById(R.id.textViewListaComentarioVazia));

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Musica musica = adapterMusicas.getItem(position);

        Intent intent = new Intent(getBaseContext(), MusicaDetalheActivity.class);
        intent.putExtra("musica", musica.getId());
        startActivity(intent);
    }
}
