package br.com.vostre.repertori;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.adapter.ScreenPagerAdapter;
import br.com.vostre.repertori.fragment.CifraFragment;
import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.Repertorio;
import br.com.vostre.repertori.model.dao.BlocoRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.MusicaBlocoDBHelper;
import br.com.vostre.repertori.model.dao.MusicaRepertorioDBHelper;
import br.com.vostre.repertori.model.dao.RepertorioDBHelper;

public class CifraMusicaBlocoRepertorioActivity extends BaseActivity implements View.OnClickListener {

    TextView textViewNome;
    TextView textViewData;
    Button btnAnterior;
    Button btnProximo;
    ViewPager pager;
    BlocoRepertorio blocoRepertorio;

    List<Musica> musicas;

    MusicaBlocoDBHelper musicaBlocoDBHelper;
    BlocoRepertorioDBHelper blocoRepertorioDBHelper;

    private ScreenPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cifra_musica_evento);

        musicaBlocoDBHelper = new MusicaBlocoDBHelper(getApplicationContext());
        blocoRepertorioDBHelper = new BlocoRepertorioDBHelper(getApplicationContext());

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

        blocoRepertorio = new BlocoRepertorio();
        blocoRepertorio.setId(getIntent().getStringExtra("bloco_repertorio"));

        carregaListaMusicas();

        int i = 0;

        for(Musica m : musicas){
            CifraFragment f = new CifraFragment();
            Bundle args = new Bundle();
            args.putString("musica", m.getId());
            f.setArguments(args);

            pagerAdapter.addView(f, i);
            i++;
        }

        pagerAdapter.notifyDataSetChanged();

        carregaInformacaoBlocoRepertorio();

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
        musicas = musicaBlocoDBHelper.listarTodosPorBloco(getApplicationContext(), blocoRepertorio, 0);
    }

    private void carregaInformacaoBlocoRepertorio() {
        blocoRepertorio = blocoRepertorioDBHelper.carregar(getApplicationContext(), blocoRepertorio);

        textViewNome.setText(blocoRepertorio.getNome());
        textViewData.setVisibility(View.GONE);
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