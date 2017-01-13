package br.com.vostre.repertori;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.vostre.repertori.adapter.ScreenPagerAdapter;
import br.com.vostre.repertori.fragment.FragmentEvento;
import br.com.vostre.repertori.fragment.FragmentRepertorio;
import br.com.vostre.repertori.model.Evento;
import br.com.vostre.repertori.model.dao.EventoDBHelper;

public class EventosActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager pager;
    private ScreenPagerAdapter pagerAdapter;

    List<Evento> eventos;
    EventoDBHelper eventoDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventoDBHelper = new EventoDBHelper(getApplicationContext());


        eventos = eventoDBHelper.listarTodos(getApplicationContext());

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();

        CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {

                Calendar data = Calendar.getInstance();
                data.setTime(date);

                Toast.makeText(getApplicationContext(), date.toString(), Toast.LENGTH_SHORT).show();
                List<Evento> eventosData = eventoDBHelper.listarTodosPorData(getApplicationContext(), data);

                if(eventosData.size() > 0){
                    Toast.makeText(getApplicationContext(), "Eventos neste dia: "+eventosData.size(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), "Long: "+date.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        caldroidFragment.setCaldroidListener(listener);

        for(Evento evento : eventos){
            Drawable dr = ResourcesCompat.getDrawable(getResources(), R.drawable.fundo_evento, null);
            int cor = Color.parseColor("#66"+evento.getTipoEvento().getCor().replace("#", ""));
            dr.mutate().setColorFilter(cor, PorterDuff.Mode.MULTIPLY);

            caldroidFragment.setBackgroundDrawableForDate(dr, evento.getData().getTime());
        }

        caldroidFragment.refreshView();

    }

}
