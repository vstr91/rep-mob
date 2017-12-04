package br.com.vostre.repertori;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.adapter.ContatoCasaList;
import br.com.vostre.repertori.adapter.ContatoList;
import br.com.vostre.repertori.adapter.EventoList;
import br.com.vostre.repertori.adapter.MusicaList;
import br.com.vostre.repertori.form.ModalCadastroCasa;
import br.com.vostre.repertori.form.ModalCadastroContato;
import br.com.vostre.repertori.form.ModalCadastroContatoCasa;
import br.com.vostre.repertori.form.ModalOpcoesMusica;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Casa;
import br.com.vostre.repertori.model.Contato;
import br.com.vostre.repertori.model.ContatoCasa;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.CasaDBHelper;
import br.com.vostre.repertori.model.dao.ContatoCasaDBHelper;
import br.com.vostre.repertori.model.dao.ContatoDBHelper;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.DataUtils;

public class CasaDetalheActivity extends BaseActivity implements AdapterView.OnItemClickListener, ModalCadastroListener, AdapterView.OnItemLongClickListener {

    TextView textViewNome;
    ListView listViewEventos;
    ListView listViewContatos;
    EventoList adapter;
    ContatoCasaList adapterContatos;
    TextView textViewMedia;
    Button btnAdicionaContato;

    Casa casa;
    List<Evento> eventos;
    List<ContatoCasa> contatos;
    ContatoCasaDBHelper contatoCasaDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casa_detalhe);

        EventoDBHelper eventoDBHelper = new EventoDBHelper(getApplicationContext());
        CasaDBHelper casaDBHelper = new CasaDBHelper(getApplicationContext());
        contatoCasaDBHelper = new ContatoCasaDBHelper(getApplicationContext());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        listViewEventos = (ListView) findViewById(R.id.listViewEventos);
        listViewContatos = (ListView) findViewById(R.id.listViewContatos);
        textViewMedia = (TextView) findViewById(R.id.textViewMedia);
        btnAdicionaContato = (Button) findViewById(R.id.btnAdicionaContato);

        casa = new Casa();
        casa.setId(getIntent().getStringExtra("casa"));
        casa = casaDBHelper.carregar(getApplicationContext(), casa);

        textViewNome.setText(casa.getNome());

        eventos = eventoDBHelper.listarTodosPorCasa(getApplicationContext(), casa);

        adapter =
                new EventoList(this, android.R.layout.simple_spinner_dropdown_item, eventos);

        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewEventos.setAdapter(adapter);
        listViewEventos.setOnItemClickListener(this);
        listViewEventos.setEmptyView(findViewById(R.id.textViewListaVazia));

        carregaContatos();

//        Calendar c = null;
//
//        if(c != null){
//            textViewMedia.setText("Duração média: "+ DataUtils.toStringSomenteHoras(c, 1));
//        } else{
//            textViewMedia.setVisibility(View.GONE);
//        }

        textViewMedia.setText(eventos.size()+" evento(s)");
        btnAdicionaContato.setOnClickListener(this);

    }

    private void carregaContatos() {
        contatos = contatoCasaDBHelper.listarTodosPorCasa(getApplicationContext(), casa, true);
        adapterContatos =
                new ContatoCasaList(this, android.R.layout.simple_spinner_dropdown_item, contatos);

        adapterContatos.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        listViewContatos.setAdapter(adapterContatos);
        listViewContatos.setOnItemClickListener(this);
        listViewContatos.setEmptyView(findViewById(R.id.textViewListaContatoVazia));
        listViewContatos.setOnItemLongClickListener(this);
        listViewContatos.invalidate();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch(parent.getId()){
            case R.id.listViewEventos:
                Evento evento = adapter.getItem(position);

                Intent intent = new Intent(getBaseContext(), EventoDetalheActivity.class);
                intent.putExtra("evento", evento.getId());
                startActivity(intent);
                break;
            case R.id.listViewContatos:
                ContatoCasa contatoCasa = adapterContatos.getItem(position);

                Intent intent2 = new Intent(getBaseContext(), ContatoDetalheActivity.class);
                intent2.putExtra("contato", contatoCasa.getContato().getId());
                startActivity(intent2);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnAdicionaContato:
                ModalCadastroContatoCasa modalCadastroContatoCasa = new ModalCadastroContatoCasa();
                modalCadastroContatoCasa.setListener(this);

                ContatoCasa contatoCasa = new ContatoCasa();
                contatoCasa.setCasa(casa);

                modalCadastroContatoCasa.setContatoCasa(contatoCasa);
                modalCadastroContatoCasa.show(getSupportFragmentManager(), "modalContatoCasa");
                break;
        }
    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        carregaContatos();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ContatoCasa contatoCasa = contatos.get(position);
        ModalCadastroContatoCasa modalCadastroContatoCasa = new ModalCadastroContatoCasa();
        modalCadastroContatoCasa.setListener(this);
        modalCadastroContatoCasa.setContatoCasa(contatoCasa);

        modalCadastroContatoCasa.show(this.getSupportFragmentManager(), "modalContatoCasa");

        return true;
    }
}
