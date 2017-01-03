package br.com.vostre.repertori;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import br.com.vostre.repertori.fragment.FragmentRepertorio;

public class RepertorioActivity extends AppCompatActivity {

    private ViewPager pager;
    private ScreenPagerAdapter pagerAdapter;

    FragmentRepertorio repAtivo;
    FragmentRepertorio repEspera;
    FragmentRepertorio repSugestao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repertorio);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.addTab(tabLayout.newTab().setText("Recebidas"));
        tabLayout.addTab(tabLayout.newTab().setText("Enviadas"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setOnTabSelectedListener(this);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        repAtivo = new FragmentRepertorio(0);
        repEspera = new FragmentRepertorio(1);
        repSugestao = new FragmentRepertorio(3);

        pagerAdapter.addView(mr, 0);
        pagerAdapter.addView(me, 1);
        pagerAdapter.notifyDataSetChanged();

    }
}
