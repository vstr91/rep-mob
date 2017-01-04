package br.com.vostre.repertori;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.vostre.repertori.adapter.ScreenPagerAdapter;
import br.com.vostre.repertori.fragment.FragmentRepertorio;
import br.com.vostre.repertori.utils.ToolbarUtils;

public class RepertorioActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    private ViewPager pager;
    private ScreenPagerAdapter pagerAdapter;

    FragmentRepertorio repAtivo;
    FragmentRepertorio repEspera;
    FragmentRepertorio repSugestao;

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

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
