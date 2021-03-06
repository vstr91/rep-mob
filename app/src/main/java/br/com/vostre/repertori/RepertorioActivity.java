package br.com.vostre.repertori;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.com.vostre.repertori.adapter.ScreenPagerAdapter;
import br.com.vostre.repertori.form.ModalCadastroMusica;
import br.com.vostre.repertori.fragment.FragmentRepertorio;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.utils.SnackbarHelper;
import br.com.vostre.repertori.utils.ToolbarUtils;

public class RepertorioActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener, ModalCadastroListener {

    private ViewPager pager;
    private ScreenPagerAdapter pagerAdapter;

    FragmentRepertorio repAtivo;
    FragmentRepertorio repEspera;
    FragmentRepertorio repSugestao;
    FragmentRepertorio repInativo;

    FloatingActionButton fabNova;
    int tabAtual = 0;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repertorio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText("Ativo"));
        tabLayout.addTab(tabLayout.newTab().setText("Fila"));
        tabLayout.addTab(tabLayout.newTab().setText("Sugest."));
        tabLayout.addTab(tabLayout.newTab().setText("Remov."));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setOnTabSelectedListener(this);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        Bundle argAtivo = new Bundle();
        argAtivo.putInt("situacao", 0);

        repAtivo = new FragmentRepertorio();
        repAtivo.setArguments(argAtivo);

        Bundle argEspera = new Bundle();
        argEspera.putInt("situacao", 1);

        repEspera = new FragmentRepertorio();
        repEspera.setArguments(argEspera);

        Bundle argSugestao = new Bundle();
        argSugestao.putInt("situacao", 3);

        repSugestao = new FragmentRepertorio();
        repSugestao.setArguments(argSugestao);

        Bundle argInativo = new Bundle();
        argInativo.putInt("situacao", 2);

        repInativo = new FragmentRepertorio();
        repInativo.setArguments(argInativo);

        pagerAdapter.addView(repAtivo, 0);
        pagerAdapter.addView(repEspera, 1);
        pagerAdapter.addView(repSugestao, 2);
        pagerAdapter.addView(repInativo, 3);
        pagerAdapter.notifyDataSetChanged();

        fabNova = (FloatingActionButton) findViewById(R.id.fabNova);
        fabNova.setOnClickListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        pager.setCurrentItem(tab.getPosition());
        tabAtual = tab.getPosition();

        switch(tabAtual){
            case 0:
                repAtivo.atualizaLista(0);
                break;
            case 1:
                repEspera.atualizaLista(1);
                break;
            case 2:
                repSugestao.atualizaLista(3);
                break;
            case 3:
                repSugestao.atualizaLista(2);
                break;
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch(v.getId()){
            case R.id.fabNova:
                ModalCadastroMusica modalCadastroMusica = new ModalCadastroMusica();
                modalCadastroMusica.setListener(this);
                modalCadastroMusica.setStatus(tabAtual);

                modalCadastroMusica.show(getSupportFragmentManager(), "modalMusica");

                break;
        }

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        switch(tabAtual){
            case 0:
                repAtivo.atualizaLista(0);
                break;
            case 1:
                repEspera.atualizaLista(1);
                break;
            case 2:
                repSugestao.atualizaLista(3);
                break;
            case 3:
                repSugestao.atualizaLista(2);
                break;
        }
    }
}
