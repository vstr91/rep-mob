package br.com.vostre.repertori;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.vostre.repertori.adapter.ScreenPagerAdapter;
import br.com.vostre.repertori.form.ModalAdicionaMusicaProjeto;
import br.com.vostre.repertori.form.ModalCadastroMusica;
import br.com.vostre.repertori.form.ModalCadastroMusicaProjeto;
import br.com.vostre.repertori.fragment.FragmentEventoProjeto;
import br.com.vostre.repertori.fragment.FragmentRepertorio;
import br.com.vostre.repertori.fragment.FragmentRepertorioEstilo;
import br.com.vostre.repertori.listener.ModalAdicionaListener;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Projeto;
import br.com.vostre.repertori.model.dao.ProjetoDBHelper;

public class MusicaProjetoActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener, ModalCadastroListener, ModalAdicionaListener {

    private ViewPager pager;
    private ScreenPagerAdapter pagerAdapter;

    FragmentRepertorio repAtivo;
    FragmentEventoProjeto eventos;
    FragmentRepertorioEstilo infos;

    FloatingActionButton fabNova;
    int tabAtual = 0;
    Projeto projeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica_projeto);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProjetoDBHelper projetoDBHelper = new ProjetoDBHelper(getBaseContext());

        String idProjeto = getIntent().getStringExtra("projeto");
        projeto = new Projeto();
        projeto.setId(idProjeto);

        projeto = projetoDBHelper.carregar(getBaseContext(), projeto);

        getSupportActionBar().setTitle("Projeto "+projeto.getNome());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText("Repertório"));
        tabLayout.addTab(tabLayout.newTab().setText("Próx. Eventos"));
        tabLayout.addTab(tabLayout.newTab().setText("Infos"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setOnTabSelectedListener(this);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        pager.setOffscreenPageLimit(2);

        Bundle argAtivo = new Bundle();
        argAtivo.putString("projeto", projeto.getId());

        repAtivo = new FragmentRepertorio();
        repAtivo.setArguments(argAtivo);

        // EVENTOS
        Bundle argEventos = new Bundle();
        argEventos.putString("projeto", projeto.getId());

        eventos = new FragmentEventoProjeto();
        eventos.setArguments(argEventos);

        // INFOS
        Bundle argInfos = new Bundle();
        argInfos.putString("projeto", projeto.getId());

        infos = new FragmentRepertorioEstilo();
        infos.setArguments(argInfos);


        pagerAdapter.addView(repAtivo, 0);
        pagerAdapter.addView(eventos, 1);
        pagerAdapter.addView(infos, 2);
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
                fabNova.setVisibility(View.VISIBLE);
                break;
            case 1:
            case 2:
                fabNova.setVisibility(View.INVISIBLE);
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
                ModalAdicionaMusicaProjeto modalAdicionaoMusicaProjeto = new ModalAdicionaMusicaProjeto();
                modalAdicionaoMusicaProjeto.setListener(this);
                modalAdicionaoMusicaProjeto.setProjeto(projeto);

                modalAdicionaoMusicaProjeto.show(getSupportFragmentManager(), "modalMusicaProjeto");

                break;
        }

    }

    @Override
    public void onModalCadastroDismissed(int resultado) {
        switch(tabAtual){
            case 0:
                repAtivo.atualizaLista();
                break;
            case 1:
                eventos.atualizaLista();
                break;
            case 2:
                infos.atualizaLista();
                infos.atualizaExecucoes();
                break;
        }
    }

    @Override
    public void onModalAdicionaDismissed(int resultado) {
        repAtivo.atualizaLista();
    }
}
