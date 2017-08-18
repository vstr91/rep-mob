package br.com.vostre.repertori;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.form.ModalOpcoesMusica;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

public class ArtistaDetalheActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    TextView textViewNome;
    ListView listViewMusicas;
    MusicaList adapter;
    TextView textViewMedia;
    TextView textViewObs;

    Artista artista;
    List<Musica> musicas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artista_detalhe);

        MusicaDBHelper musicaDBHelper = new MusicaDBHelper(getApplicationContext());
        ArtistaDBHelper artistaDBHelper = new ArtistaDBHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        listViewMusicas = (ListView) findViewById(R.id.listViewMusicas);
        textViewMedia = (TextView) findViewById(R.id.textViewMedia);
        textViewObs = (TextView) findViewById(R.id.textViewObs);

        artista = new Artista();
        artista.setId(getIntent().getStringExtra("artista"));
        artista = artistaDBHelper.carregar(getApplicationContext(), artista);

        textViewNome.setText(artista.getNome());

        musicas = musicaDBHelper.listarTodosPorArtista(getApplicationContext(), artista);

        adapter =
                new MusicaList(this, android.R.layout.simple_spinner_dropdown_item, musicas);

        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewMusicas.setAdapter(adapter);
        listViewMusicas.setOnItemClickListener(this);
        listViewMusicas.setOnItemLongClickListener(this);
        listViewMusicas.setEmptyView(findViewById(R.id.textViewListaVazia));

//        Calendar c = null;
//
//        if(c != null){
//            textViewMedia.setText("Duração média: "+ DataUtils.toStringSomenteHoras(c, 1));
//        } else{
//            textViewMedia.setVisibility(View.GONE);
//        }

        textViewMedia.setText(musicas.size()+" música(s)");
        calcularTempoTotalRepertorio();

    }

    private void calcularTempoTotalRepertorio(){
        long tempoMedio = 0;
        int cont = 0;

        for(Musica musica : musicas){

            Calendar c = musica.calcularMedia(getBaseContext());

            if(c != null){
                tempoMedio += c.getTimeInMillis();
                cont++;
            }


        }

        if(cont > 0){
            Calendar tempoMedioAtivo = Calendar.getInstance();
            tempoMedioAtivo.setTimeInMillis(tempoMedio);
            textViewObs.setText("Tempo Total: "+ DataUtils.toStringSomenteHoras(tempoMedioAtivo, 1)+" ("+cont+" música(s) considerada(s))");
        } else{
            textViewObs.setVisibility(View.GONE);
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Musica musica = adapter.getItem(position);

        Intent intent = new Intent(getBaseContext(), MusicaDetalheActivity.class);
        intent.putExtra("musica", musica.getId());
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Musica musica = musicas.get(position);

        ModalOpcoesMusica modalOpcoesMusica = new ModalOpcoesMusica();
        modalOpcoesMusica.setMusica(musica);

        modalOpcoesMusica.show(getSupportFragmentManager(), "modalOpcoesMusica");

        return true;

    }

}
