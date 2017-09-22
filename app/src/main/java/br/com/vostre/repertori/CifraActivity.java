package br.com.vostre.repertori;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.vostre.repertori.form.ModalEditaCifra;
import br.com.vostre.repertori.form.ModalEditaLetra;
import br.com.vostre.repertori.fragment.CifraFragment;
import br.com.vostre.repertori.fragment.LetraFragment;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;

public class CifraActivity extends BaseActivity implements ModalCadastroListener {

    Musica musica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cifra);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        carregarDados();

    }

    private void carregarDados() {
        MusicaDBHelper musicaDBHelper = new MusicaDBHelper(getBaseContext());

        musica = new Musica();
        musica.setId(getIntent().getStringExtra("musica"));
        musica = musicaDBHelper.carregar(getApplicationContext(), musica);

        CifraFragment f = new CifraFragment();
        Bundle args = new Bundle();
        args.putString("musica", musica.getId());
        f.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.conteudo, f).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cifra, menu);

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
            case R.id.icon_edit:

                ModalEditaCifra modalEditaCifra = new ModalEditaCifra();
                modalEditaCifra.setMusica(musica);
                modalEditaCifra.setListener(this);

                modalEditaCifra.show(getSupportFragmentManager(), "modalEditaCifra");

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        carregarDados();
    }
}
