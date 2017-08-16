package br.com.vostre.repertori;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.vostre.repertori.adapter.ArtistaList;
import br.com.vostre.repertori.form.ModalCadastroArtista;
import br.com.vostre.repertori.form.ModalCadastroMusica;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;

public class ArtistasActivity extends BaseActivity implements ModalCadastroListener, AdapterView.OnItemLongClickListener {

    ListView listViewArtistas;
    FloatingActionButton fabNovo;
    ArtistaDBHelper artistaDBHelper;
    List<Artista> artistas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artistas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        artistaDBHelper = new ArtistaDBHelper(getBaseContext());

        listViewArtistas = (ListView) findViewById(R.id.listViewArtistas);
        fabNovo = (FloatingActionButton) findViewById(R.id.fabNovo);

        fabNovo.setOnClickListener(this);
        listViewArtistas.setOnItemLongClickListener(this);

        carregarLista();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch(v.getId()){
            case R.id.fabNovo:
                ModalCadastroArtista modalCadastroArtista = new ModalCadastroArtista();
                modalCadastroArtista.setListener(this);

                modalCadastroArtista.show(getSupportFragmentManager(), "modalArtista");

                break;
        }

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        carregarLista();
    }

    private void carregarLista(){
        artistas = artistaDBHelper.listarTodos(getBaseContext());

        ArtistaList adapterArtistas = new ArtistaList(this, android.R.layout.simple_spinner_dropdown_item, artistas, true);
        listViewArtistas.setAdapter(adapterArtistas);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Artista artista = artistas.get(position);
        ModalCadastroArtista modalCadastroArtista = new ModalCadastroArtista();
        modalCadastroArtista.setListener(this);
        modalCadastroArtista.setArtista(artista);

        modalCadastroArtista.show(getSupportFragmentManager(), "modalArtista");

        return true;
    }
}
