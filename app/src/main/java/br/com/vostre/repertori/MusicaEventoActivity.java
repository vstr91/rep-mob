package br.com.vostre.repertori;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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

import java.util.Calendar;
import java.util.List;

import br.com.vostre.repertori.adapter.ComentarioList;
import br.com.vostre.repertori.adapter.ScreenPagerAdapter;
import br.com.vostre.repertori.adapter.StableArrayAdapter;
import br.com.vostre.repertori.form.ModalAdicionaMusica;
import br.com.vostre.repertori.form.ModalCadastroEvento;
import br.com.vostre.repertori.form.ModalCronometro;
import br.com.vostre.repertori.fragment.LetraFragment;
import br.com.vostre.repertori.listener.ButtonClickListener;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.ComentarioEvento;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.MusicaEvento;
import br.com.vostre.repertori.model.dao.ComentarioEventoDBHelper;
import br.com.vostre.repertori.model.dao.EventoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaEventoDBHelper;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.DialogUtils;
import br.com.vostre.repertori.utils.DynamicListView;

public class MusicaEventoActivity extends BaseActivity implements View.OnClickListener {

    TextView textViewNome;
    TextView textViewData;
    Button btnAnterior;
    Button btnProximo;
    ViewPager pager;
    Evento evento;

    List<Musica> musicas;

    MusicaEventoDBHelper musicaEventoDBHelper;
    EventoDBHelper eventoDBHelper;

    private ScreenPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica_evento);

        musicaEventoDBHelper = new MusicaEventoDBHelper(getApplicationContext());
        eventoDBHelper = new EventoDBHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewNome = (TextView) findViewById(R.id.textViewNome);
        textViewData = (TextView) findViewById(R.id.textViewData);
        btnAnterior = (Button) findViewById(R.id.btnAnterior);
        btnProximo = (Button) findViewById(R.id.btnProximo);
        pager = (ViewPager) findViewById(R.id.pager);

        pagerAdapter = new ScreenPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(pagerAdapter);

        btnAnterior.setOnClickListener(this);
        btnProximo.setOnClickListener(this);

        evento = new Evento();
        evento.setId(getIntent().getStringExtra("repertorio"));

        carregaListaMusicas();

        int i = 0;

        for(Musica m : musicas){
            LetraFragment f = new LetraFragment();
            Bundle args = new Bundle();
            args.putString("musica", m.getId());
            f.setArguments(args);

            pagerAdapter.addView(f, i);
            i++;
        }

        pagerAdapter.notifyDataSetChanged();

        carregaInformacaoEvento();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

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
        }

        return super.onOptionsItemSelected(item);
    }

    private void carregaListaMusicas(){
        musicas = musicaEventoDBHelper.listarTodosPorEvento(getApplicationContext(), evento);
    }

    private void carregaInformacaoEvento() {
        evento = eventoDBHelper.carregar(getApplicationContext(), evento);

        textViewNome.setText(evento.getNome());
        textViewData.setText(DataUtils.toString(evento.getData(), true));
    }

    @Override
    public void onClick(View v) {

        int i = pager.getCurrentItem();

        switch(v.getId()){

            case R.id.btnAnterior:

                if(i != 0){
                    i--;
                    pager.setCurrentItem(i, true);
                } else{
                    i = pager.getAdapter().getCount()-1;
                    pager.setCurrentItem(i, true);
                }

                break;
            case R.id.btnProximo:

                if(i != pager.getAdapter().getCount()-1){
                    i++;
                    pager.setCurrentItem(i, true);
                } else{
                    i = 0;
                    pager.setCurrentItem(i, true);
                }

                break;
        }

    }
}
