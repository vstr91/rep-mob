package br.com.vostre.repertori;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import br.com.vostre.repertori.adapter.ScreenPagerAdapter;
import br.com.vostre.repertori.fragment.FragmentEvento;
import br.com.vostre.repertori.fragment.FragmentRepertorio;

public class EventosActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    private ViewPager pager;
    private ScreenPagerAdapter pagerAdapter;

    FragmentEvento eventosFuturos;
    FragmentEvento eventosPassados;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText("Futuros"));
        tabLayout.addTab(tabLayout.newTab().setText("Passados"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setOnTabSelectedListener(this);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        Bundle argPassados = new Bundle();
        argPassados.putInt("situacao", 0);

        eventosPassados = new FragmentEvento();
        eventosPassados.setArguments(argPassados);

        Bundle argFuturos = new Bundle();
        argFuturos.putInt("situacao", 1);

        eventosFuturos = new FragmentEvento();
        eventosFuturos.setArguments(argFuturos);

        pagerAdapter.addView(eventosFuturos, 0);
        pagerAdapter.addView(eventosPassados, 1);
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
