package br.com.vostre.repertori;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
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
    static MusicaList adapterMusicas;
    ListView listViewComentarios;
    EditText editTextComentario;
    Button btnComentario;
    ComentarioList adapterComentarios;
    Evento evento;

    List<Musica> musicas;
    List<ComentarioEvento> comentarios;

    MusicaEventoDBHelper musicaEventoDBHelper;
    ComentarioEventoDBHelper comentarioEventoDBHelper;
    EventoDBHelper eventoDBHelper;

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
        listViewMusicas = (ListView) findViewById(R.id.listViewMusicas);
        listViewComentarios = (ListView) findViewById(R.id.listViewComentarios);
        editTextComentario = (EditText) findViewById(R.id.editTextComentario);
        btnComentario = (Button) findViewById(R.id.btnComentario);

        btnComentario.setOnClickListener(this);

        evento = new Evento();
        evento.setId(getIntent().getStringExtra("evento"));
        evento = eventoDBHelper.carregar(getApplicationContext(), evento);

        textViewNome.setText(evento.getNome());
        textViewData.setText(DataUtils.toString(evento.getData()));

        musicas = musicaEventoDBHelper.listarTodosPorEvento(getApplicationContext(), evento);

        adapterMusicas =
                new MusicaList(this, android.R.layout.simple_spinner_dropdown_item, musicas);

        adapterMusicas.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewMusicas.setAdapter(adapterMusicas);
        listViewMusicas.setOnItemClickListener(this);
        listViewMusicas.setEmptyView(findViewById(R.id.textViewListaVazia));

        atualizaComentarios();

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

}
