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
        tabLayout.addTab(tabLayout.newTab().setText("Oficial"));
        tabLayout.addTab(tabLayout.newTab().setText("Em Espera"));
        tabLayout.addTab(tabLayout.newTab().setText("Sugestões"));
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

        pagerAdapter.addView(repAtivo, 0);
        pagerAdapter.addView(repEspera, 1);
        pagerAdapter.addView(repSugestao, 2);
        pagerAdapter.notifyDataSetChanged();

        fabNova = (FloatingActionButton) findViewById(R.id.fabNova);
        fabNova.setOnClickListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        pager.setCurrentItem(tab.getPosition());
        tabAtual = tab.getPosition();
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
                SnackbarHelper.notifica(v, String.valueOf(tabAtual), Snackbar.LENGTH_SHORT);

                ModalCadastroMusica modalCadastroMusica = new ModalCadastroMusica();
                modalCadastroMusica.setListener(this);
                modalCadastroMusica.setStatus(tabAtual);

                modalCadastroMusica.show(getSupportFragmentManager(), "modalMusica");

                break;
        }

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {

    }
}
