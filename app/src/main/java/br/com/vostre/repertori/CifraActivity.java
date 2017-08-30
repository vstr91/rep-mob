package br.com.vostre.repertori;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import br.com.vostre.repertori.fragment.CifraFragment;
import br.com.vostre.repertori.fragment.LetraFragment;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;

public class CifraActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cifra);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MusicaDBHelper musicaDBHelper = new MusicaDBHelper(getBaseContext());

        Musica musica = new Musica();
        musica.setId(getIntent().getStringExtra("musica"));
        musica = musicaDBHelper.carregar(getApplicationContext(), musica);

        CifraFragment f = new CifraFragment();
        Bundle args = new Bundle();
        args.putString("musica", musica.getId());
        f.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.conteudo, f).commit();

    }
}